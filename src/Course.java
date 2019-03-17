import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Course {
    // Types of classes (lecture, tutorial, e.t.c.) and amount
    private Map<CourseClassType,  Integer> classTypes;
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

    public Map<CourseClassType, Integer> getClassTypes() {
        return classTypes;
    }

    public void setClassTypes(Map<CourseClassType, Integer> classTypes) {
        this.classTypes = classTypes;
    }

    public void putClassType(CourseClassType classType, Integer quantity) {
        this.classTypes.put(classType, quantity);
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
