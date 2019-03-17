import java.util.HashSet;
import java.util.Set;

public class TimeSlot implements Comparable{
    // Starting time
    public Integer startHour;
    public Integer startMinute;
    // Finishing time
    public Integer endHour;
    public Integer endMinute;


    // Available classrooms during this time slot
    private Set<Classroom> availableClassrooms;

    public TimeSlot(Integer startHour, Integer startMinute, Integer endHour, Integer endMinute, Set<Classroom> availableClassrooms) {
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;
        this.availableClassrooms = availableClassrooms;
    }

    public TimeSlot(Integer startHour, Integer startMinute, Integer endHour, Integer endMinute) {
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;
        this.availableClassrooms = new HashSet<>();
    }

    public TimeSlot(TimeSlot other) {
        this.startHour = other.startHour;
        this.startMinute = other.startMinute;
        this.endHour = other.endHour;
        this.endMinute = other.endMinute;
        this.availableClassrooms = new HashSet<>(other.availableClassrooms);
    }

    // Time

    public void setStartTime(int startHour, int startMinute){
        this.startHour = startHour;
        this.startMinute = startMinute;
    }

    public void setEndTime(int endHour, int endMinute){
        this.endHour = endHour;
        this.endMinute = endMinute;
    }

    // Classrooms

    public Set<Classroom> getAvailableClassrooms() {
        return availableClassrooms;
    }

    public void setAvailableClassrooms(Set<Classroom> availableClassrooms) {
        this.availableClassrooms = availableClassrooms;
    }

    public void addClassroom(Classroom classroom){
        availableClassrooms.add(classroom);
    }

    public void removeClassroom(Classroom classroom){
        availableClassrooms.remove(classroom);
    }

    /**
     * Compares to other time slot using start hour and start minute
     * @param o
     * @return
     */
    @Override
    public int compareTo(Object o) {
        TimeSlot other = (TimeSlot)o;
        if (this.startHour.equals(other.startHour)){
            return this.startMinute.compareTo(other.startMinute);
        }
        return this.startHour.compareTo(other.startHour);
    }

}
