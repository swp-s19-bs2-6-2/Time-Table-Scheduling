import java.util.Map;

public class CourseClassType implements Cloneable{
    public String name;    // Lecture, tutorial, e.t.course.
    public Boolean allStudents;


    public CourseClassType(String name, Boolean allStudents) {
        this.name = name;
        this.allStudents = allStudents;
    }

    public CourseClassType(String name, Boolean allStudents, Map<ClassroomEquipment, Integer> requiredEquipment) {
        this.name = name;
        this.allStudents = allStudents;
    }

    public CourseClassType clone() throws CloneNotSupportedException {
        CourseClassType clone = (CourseClassType) super.clone();
        return clone;
    }

    @Override
    public String toString() {
        return "CourseClassType{" +
                "name='" + name + '\'' +
                ", allStudents=" + allStudents +
                '}';
    }
}
