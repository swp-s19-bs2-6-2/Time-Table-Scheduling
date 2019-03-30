public class Lesson implements Cloneable {
    // type of the lesson
    private CourseClassType courseClassType;
    // course of the lesson
    private Course course;

    // assigned teacher
    private Teacher assignedTeacher;

    // time slot of the lesson
    //private TimeSlot timeSlot; TODO : decide to keep or replace

    // Constructors

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
        clone.course = (Course) course.clone();
        clone.courseClassType = (CourseClassType) courseClassType.clone();
        return clone;
    }
}