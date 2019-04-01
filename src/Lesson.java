public class Lesson implements Cloneable {
    // type of the lesson
    private CourseClassType courseClassType;
    // course of the lesson
    private Course course;

    //time slot of the lesson
    private TimeSlot timeSlot;// TODO : decide to keep or replace

    public Lesson(Course course) {
        this.course = course;
    }

    public CourseClassType getCourseClassType() {
        return this.courseClassType;
    }

    public void setCourseClassType(CourseClassType courseClassType) {
        this.courseClassType = courseClassType;
    }

    public Course getCourse() {
        return this.course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
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
                ", timeSlot=" + timeSlot +
                '}';
    }
}