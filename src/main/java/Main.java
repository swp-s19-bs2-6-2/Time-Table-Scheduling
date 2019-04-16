import algorithms.TimeTable;
import io.JSONWrapper;
import io.Parser;

import java.io.File;


public class Main {
    private final static String DIR = "data";
    private static void test(){
        try {
            Parser p = new Parser();
            Parser.TableResult r = p.parseAll(new File(DIR));
            TimeTable tt = new TimeTable(r.getTimeSlots(), 5, r.getLessons());
            tt.printTimeTable();
            System.out.println(new JSONWrapper().wrap(tt));

        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    public static void main(String[] args){
        test();

        /*List<algorithms.Lesson> availableLessons = new ArrayList<>();
        for(int i = 0; i<3; i++){
            algorithms.Course course = new algorithms.Course(Integer.toString(i));
            availableLessons.add(new algorithms.Lesson(course));
        }
        List<algorithms.Classroom> availableClassrooms = new ArrayList<>();
        for(int i = 0; i<2; i++){
            availableClassrooms.add(new algorithms.Classroom(10));
        }
        List<algorithms.TimeSlot> availableTimeSlots = new ArrayList<>();
        for(int i = 0; i<2; i++){
            availableTimeSlots.add(new algorithms.TimeSlot(i, 0, i + 1, 0, availableClassrooms));
        }
        algorithms.TimeTable timeTable = new algorithms.TimeTable(availableTimeSlots, 5, availableLessons);
        timeTable.printTimeTable();*/
    }
}