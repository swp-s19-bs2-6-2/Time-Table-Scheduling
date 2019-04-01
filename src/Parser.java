import com.opencsv.CSVReader;

import java.io.*;
import java.nio.file.NotDirectoryException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Parser
{
    //ToDo: Check if data is duplicates
    private static final String STUDENTS_FILE_NAME = "Students.csv";
    private static final String COURSES_FILE_NAME = "Courses.csv";
    private static final String CLASSROOMS_FILE_NAME = "ClassRooms.csv";
    private static final String TIME_SLOTS_FILE_NAME = "TimeSlots.csv";
    public final String[] FILES = {STUDENTS_FILE_NAME, COURSES_FILE_NAME, CLASSROOMS_FILE_NAME, TIME_SLOTS_FILE_NAME};

    private final static String DIR = "data";
    public static void test(){
        try {
            Parser p = new Parser();
            p.parseAll(new File(DIR));
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }



    //ToDo: return type
    //ToDO: rework
    public void parseAll(File WD) throws NotDirectoryException, FileNotFoundException, IncorrectFileStructureException {
        validate(WD);
        List<StudentsGroup> groups = parseGroups(new FileInputStream(WD.getPath() + File.separator + STUDENTS_FILE_NAME));
        for (StudentsGroup g: groups)
            System.out.println(g);
        List<Course> courses = parseCourses(new FileInputStream(WD.getPath() + File.separator + COURSES_FILE_NAME));
        for (Course c: courses)
            System.out.println(c);
        List<Classroom> crms = parseClassRooms(new FileInputStream(WD.getPath() + File.separator + CLASSROOMS_FILE_NAME));
        for (Classroom c: crms)
            System.out.println(c);
        List<TimeSlot> ts = parseTimeSlots(new FileInputStream(WD.getPath() + File.separator + TIME_SLOTS_FILE_NAME), crms);
        for (TimeSlot s: ts)
            System.out.println(s);
    }


    /*All the parsing methods
    They take an Input stream and already parsed data and convert them into collection of needed objects
    */



    public List<TimeSlot> parseTimeSlots(final InputStream file, final List<Classroom> availableClassrooms) throws IncorrectFileStructureException {
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

    public List<Classroom> parseClassRooms(final InputStream ClassRoomsFile) throws IncorrectFileStructureException{
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

    public List<Course> parseCourses(final InputStream groupFiles) throws IncorrectFileStructureException{
        final List<String[]> lines = parse(groupFiles);

        final List<Course> toReturn = new ArrayList<>();
        int ln = 0;//lines for error
        for (String[] line: lines){
            if (line.length >1) throw new IncorrectFileStructureException(ln, "Should contain only course name");
            toReturn.add(new Course(line[0]));
            ln++;
        }
        return toReturn;
    }

    public List<StudentsGroup> parseGroups(final InputStream studentsFile) throws IncorrectFileStructureException{
        final List<String[]> lines = parse(studentsFile);
        final int studentsNumber = lines.size();

        final Map<String, StudentsGroup> years = new HashMap<>();
        final Map<String, StudentsGroup> groups = new HashMap<>();

        int id = 0;//current student ID
        for (String[] line: lines){
            if (line.length != 3) throw new IncorrectFileStructureException(id, "Should be in form of [Name, BS, Group]");

            final String name = line[0];
            final String grade = line[1];
            final String studentGroup = line[1]+"-"+line[2];

            if (!years.containsKey(grade)){
                years.put(grade, new StudentsGroup(grade, studentsNumber));
            }
            years.get(grade).addStudent(id);

            if (!groups.containsKey(studentGroup)){
                groups.put(studentGroup, new StudentsGroup(studentGroup, studentsNumber));
            }
            groups.get(studentGroup).addStudent(id);

            id++;
        }
        List<StudentsGroup> toReturn = new ArrayList<>();
        toReturn.addAll(years.values());
        toReturn.addAll(groups.values());
        return toReturn;
    }



    //Checks if the working directory us valid, throws Exceptions otherwise
    private void validate(final File WD) throws NotDirectoryException, FileNotFoundException{
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



    //Is thrown when files are incorrect, the message should contain information about what was wrong in file
    static class IncorrectFileStructureException extends Exception{
        IncorrectFileStructureException(String message){super(message);}
        IncorrectFileStructureException(int line, String message){
            super("Incorrect file format: "+message+"\nline: "+line);
        }
    }
}