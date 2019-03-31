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
    public final String[] FILES = {STUDENTS_FILE_NAME, COURSES_FILE_NAME};

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
            System.out.println(c.courseName);
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




    public void validate(final File WD) throws NotDirectoryException, FileNotFoundException{
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





    //ToDo: Rewrite
    /*public List<Classroom> getClassRooms(Parser p) throws IncorrectFileStructureException{
        List<String[]> lines = parse();
        List<Classroom> classes = new ArrayList<>();

        int l = 0;//Line number for the exception
        for (String[] line: lines){
            l++;

            //ToDo: line[1::-1] - equipment
            Classroom c;
            try {
                c = new Classroom(Integer.parseInt(line[0]));
            }catch(NumberFormatException e){
                throw new IncorrectFileStructureException(l, "Expected integers, got: "+Arrays.toString(line));
            }
            classes.add(c);
        }
        return classes;
    }

    public List<TimeSlot> getTimeSlots(Parser p, List<Classroom> availableClassrooms) throws IncorrectFileStructureException {
        List<String[]> lines = parse();
        List<TimeSlot> slots = new ArrayList<>();

        int l = 0;//line of error
        for(String[] line: lines){
            if (line.length != 4) throw new IncorrectFileStructureException(l, "Expected be 4 elements, got: "+line.length);
            l++;

            List<Classroom> cp = availableClassrooms
                    .stream()
                    .map( c -> new Classroom(c.getCapacity(), c.getEquipment()) )
                    .collect(Collectors.toList());

            try {
                slots.add(new TimeSlot(Integer.parseInt(line[0]), Integer.parseInt(line[1]), Integer.parseInt(line[2]), Integer.parseInt(line[3]), cp));
            }catch(NumberFormatException e){
                throw new IncorrectFileStructureException(l, "Expected integers, got: "+Arrays.toString(line));
            }
        }
        return slots;
    }

    public List<Lesson> getLessons() throws IncorrectFileStructureException {
        List<String[]> lines = parse();
        List<Lesson> lessons = new ArrayList<>();
        for (String[] line: lines){
            Course c = new Course(line[0]); // Course name
            for (int i = 1; i < line.length; i++){
                //ToDo: line[i] - course type which is now not very clear within internal structure
                Lesson l = new Lesson(c);
                lessons.add(l);
            }
        }
        return lessons;
    }*/



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



    static class IncorrectFileStructureException extends Exception{
        IncorrectFileStructureException(String message){super(message);}
        IncorrectFileStructureException(int line, String message){
            super("Incorrect file format: "+message+"\nline: "+line);
        }
    }
}