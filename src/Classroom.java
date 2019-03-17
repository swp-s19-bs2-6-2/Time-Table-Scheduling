import java.util.Map;

public class Classroom {
    public int capacity;
    Map <ClassroomEquipment, Integer> equipment;    // <Equipment : Quantity>


    public Classroom(int capacity) {
        this.capacity = capacity;
    }

    public boolean addNewEquipment(ClassroomEquipment equipment_type, int quantity){
        if (this.equipment.containsKey(equipment_type)){
            return false;
        }
        this.equipment.put(equipment_type, quantity);
        return true;
    }

    public boolean updateEquipment(ClassroomEquipment equipment_type, int quantity){
        if (!this.equipment.containsKey(equipment_type)){
            return false;
        }
        this.equipment.put(equipment_type, quantity);
        return true;
    }

    public boolean removeEquipment(ClassroomEquipment equipment_type){
        if (this.equipment.containsKey(equipment_type)){
            return false;
        }
        this.equipment.remove(equipment_type);
        return true;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
