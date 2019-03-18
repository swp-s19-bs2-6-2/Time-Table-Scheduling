public class Lesson{
    // type of the lesson
    private CourseClassType courseClassType;
    // course of the lesson
    private Course course;
    // time slot of the lesson
    private TimeSlot timeSlot;


    public CourseClassType getCourseClassType(){
        return this.courseClassType;
    }
    public void setCourseClassType(CourseClassType courseClassType){
        this.CourseClassType = courseClassType;
    }

    public Course getCourse(){
        return this.course;
    }

    public void setCourse(Course course){
        this.course = course;
    }

    public TimeSlot getTimeSlot(){
        return this.timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot){
        this.timeSlot = timeSlot;
    }
}