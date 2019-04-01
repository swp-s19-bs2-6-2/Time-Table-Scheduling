public class Lesson implements Cloneable {
    // type of the lesson
    private CourseClassType courseClassType;
    // course of the lesson
    private Course course;
    // assigned teacher
    private Teacher assignedTeacher;
    // group for this lesson
    private StudentsGroup assignedGroup;

    // time slot of the lesson
    private TimeSlot timeSlot;

    // Constructors TODO : Too much constructors, need to decide which of them to keep

    public Lesson(CourseClassType courseClassType, Course course) {
        this.courseClassType = courseClassType;
        this.course = course;
        this.assignedTeacher = null;
    }

    public Lesson(CourseClassType courseClassType, Course course, Teacher assignedTeacher) {
        this.courseClassType = courseClassType;
        this.course = course;
        this.assignedTeacher = assignedTeacher;
    }

    public Lesson(CourseClassType courseClassType, Course course, Teacher assignedTeacher, TimeSlot timeSlot) {
        this.courseClassType = courseClassType;
        this.course = course;
        this.assignedTeacher = assignedTeacher;
        this.timeSlot = timeSlot;
    }

    public Lesson(CourseClassType courseClassType, Course course, Teacher assignedTeacher, StudentsGroup assignedGroup, TimeSlot timeSlot) {
        this.courseClassType = courseClassType;
        this.course = course;
        this.assignedTeacher = assignedTeacher;
        this.assignedGroup = assignedGroup;
        this.timeSlot = timeSlot;
    }

    public Lesson(Course course) {
        this.course = course;
    }

    // Class types operations

    public CourseClassType getCourseClassType() {
        return this.courseClassType;
    }

    public void setCourseClassType(CourseClassType courseClassType) {
        this.courseClassType = courseClassType;
    }

    // Course operations

    public Course getCourse() {
        return this.course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    // Time slot operations

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    // Assigned group

    public StudentsGroup getAssignedGroup() {
        return assignedGroup;
    }

    public void setAssignedGroup(StudentsGroup assignedGroup) {
        this.assignedGroup = assignedGroup;
    }

    // Assigned teacher operations

    public Teacher getAssignedTeacher() {
        return assignedTeacher;
    }

    public void setAssignedTeacher(Teacher assignedTeacher) {
        this.assignedTeacher = assignedTeacher;
    }

    // clone
    public Lesson clone() throws CloneNotSupportedException {
        Lesson clone = (Lesson) super.clone();
        if (course != null) clone.course = (Course) course.clone();
        if (courseClassType != null) clone.courseClassType = (CourseClassType) courseClassType.clone(); // TODO: should not be null
        return clone;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "courseClassType=" + courseClassType +
                ", course=" + course +
                ", assignedTeacher=" + assignedTeacher +
                ", assignedGroup=" + assignedGroup +
                ", timeSlot=" + timeSlot +
                '}';
    }
}