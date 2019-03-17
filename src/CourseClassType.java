import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class CourseClassType {
    String name;    // Lecture, tutorial, e.t.c.
    Boolean allStudents;
    Map <ClassroomEquipment, Integer> requiredEquipment;


    public CourseClassType(String name, Boolean allStudents) {
        this.name = name;
        this.allStudents = allStudents;
        this.requiredEquipment = new HashMap<>();
    }

    public boolean addNewEquipment(ClassroomEquipment equipment_type, int quantity){
        if (this.requiredEquipment.containsKey(equipment_type)){
            return false;
        }
        this.requiredEquipment.put(equipment_type, quantity);
        return true;
    }

    public boolean updateEquipment(ClassroomEquipment equipment_type, int quantity){
        if (!this.requiredEquipment.containsKey(equipment_type)){
            return false;
        }
        this.requiredEquipment.put(equipment_type, quantity);
        return true;
    }

    public boolean removeEquipment(ClassroomEquipment equipment_type){
        if (this.requiredEquipment.containsKey(equipment_type)){
            return false;
        }
        this.requiredEquipment.remove(equipment_type);
        return true;
    }
}
