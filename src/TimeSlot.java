import java.util.Set;

public class TimeSlot implements Comparable{
    // Starting time
    public int startHour;
    public int startMinute;
    // Finishing time
    public int endHour;
    public int endMinute;
    // Available classrooms during this time slot
    private Set<Classroom> availableClassrooms;

    public void setStartTime(int startHour, int startMinute){
        this.startHour = startHour;
        this.startMinute = startMinute;
    }

    public void setEndTime(int endHour, int endMinute){
        this.endHour = endHour;
        this.endMinute = endMinute;
    }

    public void addClassroom(Classroom classroom){
        availableClassrooms.add(classroom);
    }

    public void removeClassroom(Classroom classroom){
        availableClassrooms.remove(classroom);
    }

    @Override
    public int compareTo(Object o) {    //TODO : complete
        return 0;
    }
}
