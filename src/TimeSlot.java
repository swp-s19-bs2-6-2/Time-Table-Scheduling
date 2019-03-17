import java.util.List;

public class TimeSlot {
    // Starting time
    public int startHour;
    public int startMinute;
    // Finishing time
    public int endHour;
    public int endMinute;
    // Available classrooms during this time slot
    public List<Classroom> availableClassrooms;
}
