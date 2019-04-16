import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class Main {


    public static void main(String[] args) throws CloneNotSupportedException, IOException, Parser.IncorrectFileStructureException {
        List<Lesson> l = Parser.getLessons(new Parser(new FileInputStream("courses.csv")));

        List<Classroom> c = Parser.getClassRooms(new Parser(new FileInputStream("classrooms.csv")));

        List<TimeSlot> s = Parser.getTimeSlots(new Parser(new FileInputStream("timeSlots.csv")), c);

        TimeTable timeTable = new TimeTable(s, 5, l);
        timeTable.printTimeTable();

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