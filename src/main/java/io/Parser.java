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

    static final String[] FILES = {STUDENTS_FILE_NAME, COURSES_FILE_NAME, CLASSROOMS_FILE_NAME,
            TIME_SLOTS_FILE_NAME, TEACHERS_FILE_NAME, LESSONS_FILE_NAME};

    //A bit of HARDCODING
    private static Map<String, CourseClassType> TYPES = new HashMap<>();
    static {
        TYPES.put("Lecture", new CourseClassType("Lecture", false));
        TYPES.put("Tutorial", new CourseClassType("Tutorial", false));
        TYPES.put("Lab", new CourseClassType("Lab", true));
    }


    //ToDo: remove output
    public TableResult parseAll(File WD) throws NotDirectoryException, FileNotFoundException, IncorrectFileStructureException {
        validate(WD);
        Map<String, YearGroup> groups = parseGroups(new FileInputStream(WD.getPath() + File.separator + STUDENTS_FILE_NAME));
        for (YearGroup g: groups.values())
            System.out.println(g);
        Map<String, NCourse> courses = parseCourses(new FileInputStream(WD.getPath() + File.separator + COURSES_FILE_NAME), groups);
        for (NCourse c: courses.values())
            System.out.println(c);
        Map<Integer, Teacher> teachers = parseTeacher(new FileInputStream(WD.getPath() + File.separator + TEACHERS_FILE_NAME));
        for (Teacher t: teachers.values())
            System.out.println(t);
        List<Lesson> lessons = parseLessons(new FileInputStream(WD.getPath() + File.separator + LESSONS_FILE_NAME),
                courses, teachers);
        for (Lesson l: lessons)
            System.out.println(l);

        List<Classroom> classRooms = parseClassRooms(new FileInputStream(WD.getPath() + File.separator + CLASSROOMS_FILE_NAME));
        for (Classroom c: classRooms)
            System.out.println(c);
        List<TimeSlot> ts = parseTimeSlots(new FileInputStream(WD.getPath() + File.separator + TIME_SLOTS_FILE_NAME), classRooms);
        for (TimeSlot s: ts)
            System.out.println(s);

        return new TableResult(ts, lessons);
    }


    /*All the parsing methods
    They take an Input stream and already parsed data and convert them into collection of needed objects
    */
    List<Lesson> parseLessons(final InputStream file, final Map<String, NCourse> courses,
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
                if (st.length != 2) throw new IncorrectFileStructureException(l, "algorithms.Lesson type - algorithms.Teacher pair shold be in form of [type:teacher_id]");
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



    Map<String, CourseClassType> parseClassTypes(final InputStream file) throws IncorrectFileStructureException{
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

    Map<Integer, Teacher> parseTeacher(final InputStream file) throws IncorrectFileStructureException{
        final Map<Integer, Teacher> toRet = new HashMap<>();
        final List<String[]> lines = parse(file);

        int l = 0;//for error
        for (String[] line: lines){
            if (line.length != 2) throw new IncorrectFileStructureException(l, "Should be in form of [ID, Name]");
            int id;
            try {
                id = Integer.parseInt(line[0]);
            }catch (NumberFormatException e){
                throw new IncorrectFileStructureException(l, "Id should be integer, but got "+line[0]);
            }
            if (toRet.containsKey(id)) throw new IncorrectFileStructureException(l, "There is already a teacher with id "+line[0]+".");
            toRet.put(id, new Teacher(id, line[1]));

            //ToDo: preferences

            l++;
        }
        return toRet;
    }


    List<TimeSlot> parseTimeSlots(final InputStream file, final List<Classroom> availableClassrooms) throws IncorrectFileStructureException {
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

    List<Classroom> parseClassRooms(final InputStream ClassRoomsFile) throws IncorrectFileStructureException{
        final List<String[]> lines = parse(ClassRoomsFile);
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

    Map<String, NCourse> parseCourses(final InputStream files, final Map<String, YearGroup> groups) throws IncorrectFileStructureException{
        final List<String[]> lines = parse(files);

        final Map<String, NCourse> toReturn = new HashMap<>();
        int ln = 0;//lines for error
        for (String[] line: lines){
            if (line.length != 2) throw new IncorrectFileStructureException(ln, "Should be in form of [name, group]");
            if (!groups.containsKey(line[1])) throw new IncorrectFileStructureException(ln, "Grade group "+line[1]+" does not exist.");
            toReturn.put(line[0], new NCourse(new Course(line[0]), groups.get(line[1])));
            ln++;
        }
        return toReturn;
    }

    Map<String, YearGroup> parseGroups(final InputStream studentsFile) throws IncorrectFileStructureException{
        //ToDo: electives?
        final List<String[]> lines = parse(studentsFile);
        final int studentsNumber = lines.size();

        final Map<String, YearGroup> tr = new HashMap<>();

        int id = 0;//current student ID
        for (String[] line: lines){
            if (line.length != 3) throw new IncorrectFileStructureException(id, "Should be in form of [Name, BS, Group]");

            final String name = line[0];
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


    //Checks if the working directory us valid, throws Exceptions otherwise
    void validate(final File WD) throws NotDirectoryException, FileNotFoundException{
        if (!WD.exists()) throw new FileNotFoundException("Directory not found: "+WD.getAbsolutePath());
        if (WD.isFile()) throw new NotDirectoryException(WD.getAbsolutePath()+" is not a directory.");

        String[] fls = WD.list();
        fls = fls == null? new String[]{} : fls;
        final List<String> files = Arrays.asList(fls);

        final List<String> notFoundFiles = new LinkedList<>();
        for (String f: FILES) {
            if (!files.contains(f))
                notFoundFiles.add(f);
        }
        if(!notFoundFiles.isEmpty()) throw new FileNotFoundException("There are no files "+notFoundFiles+" in working directory "+WD.getAbsolutePath());
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
        public TableResult(List<TimeSlot> timeSlots, List<Lesson> lessons) { this.timeSlots = timeSlots;this.lessons = lessons; }
        public List<TimeSlot> getTimeSlots() { return timeSlots; }
        public List<Lesson> getLessons() { return lessons; }
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