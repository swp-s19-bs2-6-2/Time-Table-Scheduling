package algorithms;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class TimeTableTest {
    List<StudentsGroup> groups;
    List<Classroom> rooms;
    List<Teacher> teachers;
    List<TimeSlot> timeSlots;
    List<CourseClassType> classTypes;
    List<Course> courses;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        // Groups initialization

        BitSet students = new BitSet(12);
        groups = new ArrayList<>();
        // Groups distributions (GroupName : start_from-end_with):
        // G1 : 1-3, G2 : 4-6, G3 : 7-9, G4 : 10-12, G5 : 1-6, G6 : 7-12, G7 : 4-9
        students.set(0,3);
        groups.add(new StudentsGroup("G1", students));

        students = new BitSet(12);
        students.set(3,6);
        groups.add(new StudentsGroup("G2", students));

        students = new BitSet(12);
        students.set(6,9);
        groups.add(new StudentsGroup("G3", students));

        students = new BitSet(12);
        students.set(9,12);
        groups.add(new StudentsGroup("G4", students));

        students = new BitSet(12);
        students.set(0,6);
        groups.add(new StudentsGroup("G5", students));

        students = new BitSet(12);
        students.set(6,12);
        groups.add(new StudentsGroup("G6", students));

        students = new BitSet(12);
        students.set(3,9);
        groups.add(new StudentsGroup("G7", students));

        // Rooms initialization

        rooms = new ArrayList<>();
        // 4 rooms : 101,102,103,104
        rooms.add(new Classroom("101", 12));
        rooms.add(new Classroom("102", 12));
        rooms.add(new Classroom("103", 12));
        rooms.add(new Classroom("104", 5));

        // Teachers initialization

        teachers = new ArrayList<>();
        teachers.add(new Teacher(1, "T1"));
        teachers.add(new Teacher(2, "T2"));
        teachers.add(new Teacher(3, "T3"));
        teachers.add(new Teacher(4, "T4"));
        teachers.add(new Teacher(5, "T5"));

        // Timeslots initialization

        timeSlots = new ArrayList<>();
        timeSlots.add(new TimeSlot(12, 0, 14, 0, rooms));
        timeSlots.add(new TimeSlot(14, 0, 16, 0, rooms));
        timeSlots.add(new TimeSlot(16, 0, 18, 0, rooms));

        // Class types initialization

        classTypes = new ArrayList<>();
        classTypes.add(new CourseClassType("lecture", true));
        classTypes.add(new CourseClassType("lab", false));

        // Courses initialization (some fictive courses)

        courses = new ArrayList<>();
        courses.add(new Course("C1"));
        courses.add(new Course("C2"));
        courses.add(new Course("C3"));

    }
}
