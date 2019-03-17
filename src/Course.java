import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Course {
    // Types of classes and amount
    Map<CourseClassType,  Integer> classTypes;
    // Order of classes
    List<CourseClassType> classesOrder;
    // Groups of enrolled students
    List<StudentsGroup> enrolledStudents;

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
}
