package algorithms;

import java.util.Map;

public class CourseClassType {  // TODO : refactor, class is not needed anymore, probably should be reaplaced with enum

    public String name;    // Lecture, tutorial, e.t.c.
    public Boolean allStudents;


    public CourseClassType(String name, Boolean allStudents) {
        this.name = name;
        this.allStudents = allStudents;
    }

    public CourseClassType(String name, Boolean allStudents, Map<ClassroomEquipment, Integer> requiredEquipment) {
        this.name = name;
        this.allStudents = allStudents;
    }

    public CourseClassType clone() throws CloneNotSupportedException {  // TODO : Do we really need to clone this?
//        CourseClassType clone = (CourseClassType) super.clone();
//        return clone;
        return new CourseClassType(this.name, this.allStudents);
    }

    @Override
    public String toString() {
        return "algorithms.CourseClassType{" +
                "name='" + name + '\'' +
                ", allStudents=" + allStudents +
                '}';
    }
}
