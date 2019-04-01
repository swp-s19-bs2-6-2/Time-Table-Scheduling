public class ClassroomEquipment implements Cloneable{
    public String name;

    public ClassroomEquipment(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Equals when names are equal
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ClassroomEquipment)){
            return false;
        }
        ClassroomEquipment other = (ClassroomEquipment)obj;
        return this.name.equals(other.name);
    }

    public ClassroomEquipment clone() throws CloneNotSupportedException {
        ClassroomEquipment clone = (ClassroomEquipment)super.clone();
        return clone;
    }

    @Override
    public String toString() {
        return "ClassroomEquipment{" +
                "name='" + name + '\'' +
                '}';
    }
}
