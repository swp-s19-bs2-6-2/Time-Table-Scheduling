import java.io.File;
import java.io.IOException;

public class Main {


    private final static String DIR = "data";
    private static void test(){
        try {
            Parser p = new Parser();
            Parser.TableResult r = p.parseAll(new File(DIR));
            new TimeTable(r.getTimeSlots(), 5, r.getLessons()).printTimeTable();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    public static void main(String[] args) throws CloneNotSupportedException, IOException, Parser.IncorrectFileStructureException {
        test();

        /*List<Lesson> availableLessons = new ArrayList<>();
        for(int i = 0; i<3; i++){
            Course course = new Course(Integer.toString(i));
            availableLessons.add(new Lesson(course));
        }
        List<Classroom> availableClassrooms = new ArrayList<>();
        for(int i = 0; i<2; i++){
            availableClassrooms.add(new Classroom(10));
        }
        List<TimeSlot> availableTimeSlots = new ArrayList<>();
        for(int i = 0; i<2; i++){
            availableTimeSlots.add(new TimeSlot(i, 0, i + 1, 0, availableClassrooms));
        }
        TimeTable timeTable = new TimeTable(availableTimeSlots, 5, availableLessons);
        timeTable.printTimeTable();*/
    }
}