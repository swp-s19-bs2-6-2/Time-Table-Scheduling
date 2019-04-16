package algorithms;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TimeTableCheckerTest {

    List<StudentsGroup> groups;
    List<Classroom> rooms;
    List<Teacher> teachers;
    List<TimeSlot> timeSlots;
    List<CourseClassType> classTypes;
    List<Course> courses;
//    algorithms.main.java.algorithms.TimeTable table;

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

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @org.junit.jupiter.api.Test
    void checkForCollisionsSuccess() throws CloneNotSupportedException {
        List<List<TimeSlot>> content = new ArrayList<>();
        List<TimeSlot> tempTimeslots = new ArrayList<>();
        for (TimeSlot ts : timeSlots){
            tempTimeslots.add(ts.clone());
        }
        List<Lesson> tempLessons = new ArrayList<>();
        tempLessons.add(new Lesson(classTypes.get(0), courses.get(0), teachers.get(0), groups.get(0), rooms.get(0) ,tempTimeslots.get(0)));
        tempLessons.add(new Lesson(classTypes.get(0), courses.get(1), teachers.get(1), groups.get(1), rooms.get(1) ,tempTimeslots.get(0)));
        tempLessons.add(new Lesson(classTypes.get(0), courses.get(1), teachers.get(2), groups.get(2), rooms.get(2) ,tempTimeslots.get(0)));
        tempTimeslots.get(0).setLessons(tempLessons);

//        content = new ArrayList<>();
        tempTimeslots = new ArrayList<>();
        for (TimeSlot ts : timeSlots){
            tempTimeslots.add(ts.clone());
        }
        tempLessons = new ArrayList<>();
        tempLessons.add(new Lesson(classTypes.get(1), courses.get(0), teachers.get(0), groups.get(6), rooms.get(0) ,tempTimeslots.get(1)));
        tempLessons.add(new Lesson(classTypes.get(0), courses.get(1), teachers.get(1), groups.get(1), rooms.get(1) ,tempTimeslots.get(1)));
        tempLessons.add(new Lesson(classTypes.get(0), courses.get(1), teachers.get(2), groups.get(3), rooms.get(3) ,tempTimeslots.get(1)));
        tempTimeslots.get(1).setLessons(tempLessons);

        content.add(tempTimeslots);
        TimeTable table1 = new TimeTable(content);

        TimeTableChecker.Result res = TimeTableChecker.checkForCollisions(table1);

        assertEquals(res.negativeScore, 0);
    }

    @org.junit.jupiter.api.Test
    void checkForCollisions() throws CloneNotSupportedException {
        List<List<TimeSlot>> content = new ArrayList<>();
        List<TimeSlot> tempTimeslots = new ArrayList<>();
        for (TimeSlot ts : timeSlots){
            tempTimeslots.add(ts.clone());
        }
        List<Lesson> tempLessons = new ArrayList<>();
        tempLessons.add(new Lesson(classTypes.get(0), courses.get(0), teachers.get(0), groups.get(0), rooms.get(0) ,tempTimeslots.get(0)));
        tempLessons.add(new Lesson(classTypes.get(0), courses.get(1), teachers.get(1), groups.get(1), rooms.get(1) ,tempTimeslots.get(0)));
        tempLessons.add(new Lesson(classTypes.get(0), courses.get(1), teachers.get(1), groups.get(2), rooms.get(2) ,tempTimeslots.get(0)));
        tempTimeslots.get(0).setLessons(tempLessons);

//        content = new ArrayList<>();
        tempTimeslots = new ArrayList<>();
        for (TimeSlot ts : timeSlots){
            tempTimeslots.add(ts.clone());
        }
        tempLessons = new ArrayList<>();
        tempLessons.add(new Lesson(classTypes.get(1), courses.get(0), teachers.get(0), groups.get(6), rooms.get(0) ,tempTimeslots.get(1)));
        tempLessons.add(new Lesson(classTypes.get(0), courses.get(1), teachers.get(1), groups.get(1), rooms.get(1) ,tempTimeslots.get(1)));
        tempLessons.add(new Lesson(classTypes.get(0), courses.get(1), teachers.get(2), groups.get(4), rooms.get(3) ,tempTimeslots.get(1)));
        tempTimeslots.get(1).setLessons(tempLessons);

        content.add(tempTimeslots);
        TimeTable table1 = new TimeTable(content);

        TimeTableChecker.Result res = TimeTableChecker.checkForCollisions(table1);

//        assertEquals(res.negativeScore, 0);
        assertEquals(1, res.foundProblems.get("Student has more than 1 class in one timeslot").size());
        assertEquals(0, res.foundProblems.get("Room is holding more than 1 class in one timeslot").size());
        assertEquals(1, res.foundProblems.get("algorithms.Teacher has more than 1 class in one timeslot").size());
    }

    @org.junit.jupiter.api.Test
    void checkWorkloadBest() throws CloneNotSupportedException{
        List<List<TimeSlot>> content = new ArrayList<>();
        List<TimeSlot> tempTimeslots = new ArrayList<>();
        for (TimeSlot ts : timeSlots){
            tempTimeslots.add(ts.clone());
        }
        List<Lesson> tempLessons = new ArrayList<>();
        tempLessons.add(new Lesson(classTypes.get(0), courses.get(0), teachers.get(0), groups.get(0), rooms.get(0) ,tempTimeslots.get(0)));
        tempLessons.add(new Lesson(classTypes.get(0), courses.get(1), teachers.get(1), groups.get(1), rooms.get(1) ,tempTimeslots.get(0)));
        tempLessons.add(new Lesson(classTypes.get(0), courses.get(1), teachers.get(2), groups.get(2), rooms.get(2) ,tempTimeslots.get(0)));
        tempTimeslots.get(0).setLessons(tempLessons);

//        content = new ArrayList<>();
            tempTimeslots = new ArrayList<>();
        for (TimeSlot ts : timeSlots){
            tempTimeslots.add(ts.clone());
        }
        tempLessons = new ArrayList<>();
        tempLessons.add(new Lesson(classTypes.get(1), courses.get(0), teachers.get(0), groups.get(6), rooms.get(0) ,tempTimeslots.get(1)));
        tempLessons.add(new Lesson(classTypes.get(0), courses.get(1), teachers.get(1), groups.get(1), rooms.get(1) ,tempTimeslots.get(1)));
        tempLessons.add(new Lesson(classTypes.get(0), courses.get(1), teachers.get(2), groups.get(3), rooms.get(3) ,tempTimeslots.get(1)));
        tempTimeslots.get(1).setLessons(tempLessons);

        content.add(tempTimeslots);
        TimeTable table1 = new TimeTable(content);

        TimeTableChecker.Result res = TimeTableChecker.checkWorkload(table1);

        assertEquals(res.negativeScore, 0);
    }
}