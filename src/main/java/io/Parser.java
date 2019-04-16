package io;

import algorithms.*;
import com.opencsv.CSVReader;

import java.io.*;
import java.nio.file.NotDirectoryException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Parser
{
    private static final String STUDENTS_FILE_NAME = "Students.csv";
    private static final String COURSES_FILE_NAME = "Courses.csv";
    private static final String CLASSROOMS_FILE_NAME = "ClassRooms.csv";
    private static final String TIME_SLOTS_FILE_NAME = "TimeSlots.csv";
    private static final String TEACHERS_FILE_NAME = "Teachers.csv";
    private static final String LESSONS_FILE_NAME = "Lessons.csv";
    private static final String WORKING_DAYS_FILE_NAME = "WorkingDays.csv";

    private static final String[] FILES = {STUDENTS_FILE_NAME, COURSES_FILE_NAME, CLASSROOMS_FILE_NAME,
            TIME_SLOTS_FILE_NAME, TEACHERS_FILE_NAME, LESSONS_FILE_NAME, WORKING_DAYS_FILE_NAME};

    //A bit of HARDCODING
    private static final Map<String, CourseClassType> TYPES = new HashMap<>();
    static {
        TYPES.put("Lecture", new CourseClassType("Lecture", false));
        TYPES.put("Tutorial", new CourseClassType("Tutorial", false));
        TYPES.put("Lab", new CourseClassType("Lab", true));
    }


    public TableResult parseAll(File WD) throws NotDirectoryException, FileNotFoundException, IncorrectFileStructureException {
        validate(WD);
        int workingDays = parseWorkingDays(new FileInputStream(WD.getPath() + File.separator + WORKING_DAYS_FILE_NAME));

        List<Classroom> classRooms = parseClassRooms(new FileInputStream(WD.getPath() + File.separator + CLASSROOMS_FILE_NAME));
        List<TimeSlot> ts = parseTimeSlots(new FileInputStream(WD.getPath() + File.separator + TIME_SLOTS_FILE_NAME), classRooms);

        Map<String, YearGroup> groups = parseGroups(new FileInputStream(WD.getPath() + File.separator + STUDENTS_FILE_NAME));
        Map<String, NCourse> courses = parseCourses(new FileInputStream(WD.getPath() + File.separator + COURSES_FILE_NAME), groups);
        Map<Integer, Teacher> teachers = parseTeacher(new FileInputStream(WD.getPath() + File.separator + TEACHERS_FILE_NAME), workingDays, ts);
        List<Lesson> lessons = parseLessons(new FileInputStream(WD.getPath() + File.separator + LESSONS_FILE_NAME),
                courses, teachers);

        return new TableResult(ts, lessons, workingDays);
    }


    /*All the parsing methods
    They take an Input stream and already parsed data and convert them into collection of needed objects
    */
    private List<Lesson> parseLessons(final InputStream file, final Map<String, NCourse> courses,
                              final Map<Integer, Teacher> teachers)
            throws IncorrectFileStructureException
    {
        List<Lesson> toRet = new ArrayList<>();
        List<String[]> lines = parse(file);

        int l = 0;
        for(String[] line: lines){
            if (line.length < 2) throw new IncorrectFileStructureException(l, "Should be Name of course, and at least one pair of Type:TeacherID");
            if (!courses.containsKey(line[0])) throw new IncorrectFileStructureException(l, "There's no such a course named "+line[0]);
            NCourse course = courses.get(line[0]);
            Iterator<StudentsGroup> groupIterator = course.group.child.values().iterator();

            for (int i = 1; i < line.length; i++) {
                String[] st = line[i].split(":");
                if (st.length != 2) throw new IncorrectFileStructureException(l, "algorithms.Lesson type - algorithms.Teacher pair should be in form of [type:teacher_id]");
                if (!TYPES.containsKey(st[0])) throw new IncorrectFileStructureException(l, "There's no such course type as "+st[0]);
                CourseClassType classType = TYPES.get(st[0]);
                int teacherId;
                try{
                    teacherId = Integer.parseInt(st[1]);
                }catch (NumberFormatException e){
                    throw new IncorrectFileStructureException(l, "algorithms.Teacher id should be an integer");
                }
                if (!teachers.containsKey(teacherId)) throw new IncorrectFileStructureException(l, "There's no teacher with id = "+teacherId);
                Teacher teacher = teachers.get(teacherId);
                Lesson lesson = new Lesson(classType, course.course, teacher);
                //A little bit of HARDCODING
                //ToDo: Waits resolution of ClassTypes
                if (classType.name.equals("Lab")){
                    if (!groupIterator.hasNext()) throw new IncorrectFileStructureException(l, "Too many lab sessions. Not enough groups");
                    StudentsGroup g = groupIterator.next();
                    lesson.setAssignedGroup(g);
                }else{
                    lesson.setAssignedGroup(course.group.parent);
                }
                toRet.add(lesson);
            }
            l++;
        }

        return toRet;
    }



    private Map<String, CourseClassType> parseClassTypes(final InputStream file) throws IncorrectFileStructureException{
        final Map<String, CourseClassType> toRet = new HashMap<>();
        final List<String[]> lines = parse(file);

        int l = 0;
        for (String[] line: lines){
            if (line.length != 1) throw new IncorrectFileStructureException(l, "Should be in format of [Type]");
            if (toRet.containsKey(line[0])) throw new IncorrectFileStructureException(l, "Type "+line[0]+" already exists");
            toRet.put(line[0], new CourseClassType(line[0], true));//ToDo: remove after fix
            l++;
        }
        return toRet;
    }

    private Map<Integer, Teacher> parseTeacher(final InputStream file, final int workingDays, final List<TimeSlot> slots) throws IncorrectFileStructureException{
        final Map<Integer, Teacher> toRet = new HashMap<>();
        final List<String[]> lines = parse(file);

        int l = 0;//for error
        for (String[] line: lines){
            if (line.length < 2) throw new IncorrectFileStructureException(l, "Should be in form of [ID, Name{, preferred_day:preferred_slot, -\"\"-}]");
            int id;
            try {
                id = Integer.parseInt(line[0]);
            }catch (NumberFormatException e){
                throw new IncorrectFileStructureException(l, "Id should be integer, but got "+line[0]);
            }
            if (toRet.containsKey(id)) throw new IncorrectFileStructureException(l, "There is already a teacher with id "+line[0]+".");
            Teacher teacher = new Teacher(id, line[1]);
            toRet.put(id, teacher);

            for (int i = 2; i < line.length; i++) {
                String[] st = line[i].split(":");
                if (st.length != 2) throw new IncorrectFileStructureException(l, "(Preferred day - Preferred time slot) pair should be in form of [day:slot]");
                int day, slot;
                try{
                    day = Integer.parseInt(st[0])-1;
                    slot = Integer.parseInt(st[1])-1;
                }catch (NumberFormatException e){
                    throw new IncorrectFileStructureException(l, "Day and slot should be an integer");
                }

                if (day < 0 || day >= workingDays) throw new IncorrectFileStructureException(l, "Day is out of range");
                if (slot < 0 || slot >= slots.size()) throw new IncorrectFileStructureException(l, "Slot is out of range");

                teacher.addSingleTimeslot(day, slot);
            }

            l++;
        }
        return toRet;
    }


    private List<TimeSlot> parseTimeSlots(final InputStream file, final List<Classroom> availableClassrooms) throws IncorrectFileStructureException {
        final List<String[]> lines = parse(file);
        final List<TimeSlot> slots = new ArrayList<>();

        int l = 0;//line of error
        for(String[] line: lines){
            if (line.length != 4) throw new IncorrectFileStructureException(l, "Expected be 4 elements, got: "+line.length);
            l++;

            List<Classroom> cp = availableClassrooms
                    .stream()
                    .map( c -> new Classroom(c.getName(), c.getCapacity(), c.getEquipment()) )
                    .collect(Collectors.toList());
            try {
                slots.add(new TimeSlot(Integer.parseInt(line[0]), Integer.parseInt(line[1]), Integer.parseInt(line[2]), Integer.parseInt(line[3]), cp));
            }catch(NumberFormatException e){
                throw new IncorrectFileStructureException(l, "Expected integers, got: "+Arrays.toString(line));
            }
        }
        return slots;
    }

    private List<Classroom> parseClassRooms(final InputStream classRoomsFile) throws IncorrectFileStructureException{
        final List<String[]> lines = parse(classRoomsFile);
        final List<Classroom> classes = new ArrayList<>();

        int l = 0;//Line number for the exception
        for (String[] line: lines){
            l++;
            if (line.length < 2) throw new IncorrectFileStructureException(l, "Should be in form of [name, capacity{, equipment:number of equipment, -\"-\"-}]");

            Classroom c;
            try {
                c = new Classroom(line[0], Integer.parseInt(line[1]));
            }catch(NumberFormatException e){
                throw new IncorrectFileStructureException(l, "Expected integers, got: "+line[1]);
            }
            for (int i = 2; i < line.length; i++){
                final String[] st = line[i].split(":");
                if (st.length < 2) throw new IncorrectFileStructureException(l, "Equipment should be in form of [name:number], got: "+line[i]);
                try{
                    c.addNewEquipment(new ClassroomEquipment(st[0]), Integer.parseInt(st[1]));
                }catch(NumberFormatException e){
                    throw new IncorrectFileStructureException(l, "Expected integers, got: "+st[1]);
                }
            }

            classes.add(c);
        }
        return classes;
    }

    private Map<String, NCourse> parseCourses(final InputStream files, final Map<String, YearGroup> groups) throws IncorrectFileStructureException{
        final List<String[]> lines = parse(files);

        final Map<String, NCourse> toReturn = new HashMap<>();
        int ln = 0;//lines for error
        for (String[] line: lines){
            if (line.length != 2 && line.length != 5) throw new IncorrectFileStructureException(ln, "Should be in form of [name, group] or [name, group, lesson_type_1, lesson_type_2, lesson_type_3] for order");
            if (!groups.containsKey(line[1])) throw new IncorrectFileStructureException(ln, "Grade group "+line[1]+" does not exist.");
            Course course = new Course(line[0]);
            toReturn.put(line[0], new NCourse(course, groups.get(line[1])));


            if (line.length == 5){
                List<CourseClassType> order = new ArrayList<>(3);
                for (int i = 2; i < 5; i++){
                    if (!TYPES.containsKey(line[i])) throw new IncorrectFileStructureException(ln, "There's no such type as "+line[i]);
                    if (order.contains(TYPES.get(line[i]))) throw new IncorrectFileStructureException(ln, "There's already a "+line[i]+" in lessons order");
                    order.add(TYPES.get(line[i]));
                }
                boolean result = course.setClassesOrder(order);
                if (!result) throw new IncorrectFileStructureException(ln, "Course order doesn't correspond to Course lessons.");
            }


            ln++;
        }
        return toReturn;
    }

    private Map<String, YearGroup> parseGroups(final InputStream studentsFile) throws IncorrectFileStructureException{
        final List<String[]> lines = parse(studentsFile);
        final int studentsNumber = lines.size();

        final Map<String, YearGroup> tr = new HashMap<>();

        int id = 0;//current student ID
        for (String[] line: lines){
            if (line.length != 3) throw new IncorrectFileStructureException(id, "Should be in form of [Name, BS, Group]");

            //final String name = line[0];
            final String grade = line[1];
            final String studentGroup = line[1]+"-"+line[2];

            if (!tr.containsKey(grade)){
                tr.put(grade, new YearGroup(new StudentsGroup(grade, studentsNumber)));
            }
            YearGroup curr = tr.get(grade);
            curr.parent.addStudent(id);

            if (!curr.child.containsKey(studentGroup))
                curr.child.put(studentGroup, new StudentsGroup(studentGroup, studentsNumber));
            curr.child.get(studentGroup).addStudent(id);

            id++;
        }

        return tr;
    }

    private int parseWorkingDays(final InputStream file) throws IncorrectFileStructureException{
        List<String[]> lines = parse(file);
        if (lines.size() != 1) throw new IncorrectFileStructureException("Should be only one line containing only working days number");
        if (lines.get(0).length != 1) throw new IncorrectFileStructureException("Should be only one line containing only working days number");
        try{
            int tr = Integer.parseInt(lines.get(0)[0]);
            if (tr <= 0) throw new NumberFormatException();
            return tr;
        }catch (NumberFormatException e){
            throw new IncorrectFileStructureException("Working days number should be a positive integer.");
        }
    }


    //Checks if the working directory us valid, throws Exceptions otherwise
    private void validate(final File workingDirectory) throws NotDirectoryException, FileNotFoundException{
        if (!workingDirectory.exists()) throw new FileNotFoundException("Directory not found: "+workingDirectory.getAbsolutePath());
        if (workingDirectory.isFile()) throw new NotDirectoryException(workingDirectory.getAbsolutePath()+" is not a directory.");

        String[] fls = workingDirectory.list();
        fls = fls == null? new String[]{} : fls;
        final List<String> files = Arrays.asList(fls);

        final List<String> notFoundFiles = new LinkedList<>();
        for (String f: FILES) {
            if (!files.contains(f))
                notFoundFiles.add(f);
        }
        if(!notFoundFiles.isEmpty()) throw new FileNotFoundException("There are no files "+notFoundFiles+" in working directory "+workingDirectory.getAbsolutePath());
    }


    //Internal method which converts CSV into List of String arrays.
    private List<String[]> parse(InputStream f){
        try(CSVReader cr = new CSVReader(new BufferedReader(new InputStreamReader(f)))) {
            return cr.readAll()
                    .stream()
                    .map( s -> Stream.of(s)
                            .map(String::trim)
                            .toArray(String[]::new) )
                    .collect(Collectors.toList());

        }catch (IOException e){
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }

    private class YearGroup{
        StudentsGroup parent;
        HashMap<String, StudentsGroup> child = new HashMap<>();
        YearGroup(StudentsGroup parent) { this.parent = parent; }
        @Override
        public String toString() {
            return "YearGroup{" +
                    "parent=" + parent +
                    ", child=" + child +
                    '}';
        }
    }

    //Class for returning parsing result
    public class TableResult{
        private List<TimeSlot> timeSlots;
        private List<Lesson> lessons;
        private int workingDays;

        public TableResult(List<TimeSlot> timeSlots, List<Lesson> lessons, int workingDays) { this.timeSlots = timeSlots;this.lessons = lessons; this.workingDays = workingDays;}
        public List<TimeSlot> getTimeSlots() { return timeSlots; }
        public List<Lesson> getLessons() { return lessons; }
        public int getWorkingDays() { return workingDays; }
    }

    //algorithms.Course with group number
    private class NCourse{
        Course course;
        YearGroup group;
        NCourse(Course course, YearGroup group){ this.course = course; this.group = group; }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            NCourse nCourse = (NCourse) o;
            return course.equals(nCourse.course) &&
                    group.equals(nCourse.group);
        }
        @Override
        public int hashCode() {
            return Objects.hash(course, group);
        }
        @Override
        public String toString() {
            return "NCourse{" +
                    "course=" + course +
                    ", group=" + group +
                    '}';
        }
    }

    //Is thrown when files are incorrect, the message should contain information about what was wrong in file
    public static class IncorrectFileStructureException extends Exception{
        IncorrectFileStructureException(String message){super(message);}
        IncorrectFileStructureException(int line, String message){
            super("Incorrect file format: "+message+"\nline: "+line);
        }
    }
}