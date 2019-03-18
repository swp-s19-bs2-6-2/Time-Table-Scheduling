public class Lesson implements Cloneable{
    // type of the lesson
    private CourseClassType courseClassType;
    // course of the lesson
    private Course course;
    // time slot of the lesson
    //private TimeSlot timeSlot; TODO : decide to keep or replace

    public Lesson(Course course){
        this.course = course;
    }
    public CourseClassType getCourseClassType(){
        return this.courseClassType;
    }
    public void setCourseClassType(CourseClassType courseClassType){
        this.courseClassType = courseClassType;
    }

    public Course getCourse(){
        return this.course;
    }

    public void setCourse(Course course){
        this.course = course;
    }

     // clone
     public Lesson clone() throws CloneNotSupportedException{
         Lesson clone = (Lesson)super.clone();
         clone.course = (Course) course.clone();
         //TODO: clone courseClassType
         return clone;
     }
}