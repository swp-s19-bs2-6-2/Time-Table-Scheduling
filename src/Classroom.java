import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Classroom implements Cloneable{
    private String name;
    public int capacity;
    private Map <ClassroomEquipment, Integer> equipment;    // <Equipment : Quantity>


    public Classroom(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
        this.equipment = new HashMap<>();
    }

    public Classroom(String name, int capacity, Map<ClassroomEquipment, Integer> equipment) {
        this.name = name;
        this.capacity = capacity;
        this.equipment = equipment;
    }

    // Equipment manipulations

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

    public Map<ClassroomEquipment, Integer> getEquipment() {
        return equipment;
    }

    public void setEquipment(Map<ClassroomEquipment, Integer> equipment) {
        this.equipment = equipment;
    }

    // Capacity manipulations

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    public Classroom clone() throws CloneNotSupportedException {
        Classroom clone = (Classroom)super.clone();
        clone.equipment = (Map<ClassroomEquipment, Integer>) cloneEquipment(equipment);
        return clone;
    }

    private Map<ClassroomEquipment, Integer> cloneEquipment(Map<ClassroomEquipment, Integer> equipment) throws CloneNotSupportedException {
        Map<ClassroomEquipment, Integer> clone = new HashMap<ClassroomEquipment, Integer>();
        Iterator<Map.Entry<ClassroomEquipment, Integer>> iterator = equipment.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<ClassroomEquipment, Integer> pair = iterator.next();
            clone.put(pair.getKey().clone(), pair.getValue());
        }
        return clone;
    }

    // Name operations

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Classroom{" +
                "name='" + name + '\'' +
                ", capacity=" + capacity +
                ", equipment=" + equipment +
                '}';
    }
}
