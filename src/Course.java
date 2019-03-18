import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Course {
    // Lessons of the course
    private List<Lesson> lessons;
    // Order of classes
    private List<CourseClassType> classesOrder;
    // Groups of enrolled students
    private List<StudentsGroup> enrolledStudents;

    // Class order

    boolean setClassesOrder(List<CourseClassType> order){
        Map<CourseClassType,  Integer> checkClassTypes = new HashMap<>();
        for (CourseClassType class_type : order){
            checkClassTypes.put(class_type, checkClassTypes.get(class_type) + 1);
        }
        for (CourseClassType class_type : this.classTypes.keySet()){
            if (!checkClassTypes.get(class_type).equals(this.classTypes.get(class_type))){
                return false;
            }
        }
        this.classesOrder = order;
        return true;
    }

    public List<CourseClassType> getClassesOrder() {
        return classesOrder;
    }

    // Class types

    public List<Lesson> getClassTypes() {
        return classTypes;
    }

    public void setClassTypes(List<Lesson> classTypes) {
        this.classTypes = classTypes;
    }

    // Enrolled students

    public List<StudentsGroup> getEnrolledStudents() {
        return enrolledStudents;
    }

    public void setEnrolledStudents(List<StudentsGroup> enrolledStudents) {
        this.enrolledStudents = enrolledStudents;
    }

    public void addEnrolledStudents(StudentsGroup enrolledStudents) {
        this.enrolledStudents.add(enrolledStudents);
    }

}
