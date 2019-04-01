import java.util.ArrayList;
import java.util.List;

public class TimeSlot implements Comparable, Cloneable {
    // Starting time
    public Integer startHour;
    public Integer startMinute;
    // Finishing time
    public Integer endHour;
    public Integer endMinute;


    // Available classrooms during this time slot
    private List<Classroom> availableClassrooms;

    // Lessons during this time slot
    private List<Lesson> lessons;

    /**
     * Create new object using "other". Copies all besides lessons
     * @param other
     */
    public TimeSlot(TimeSlot other) {
        this.startHour = other.startHour;
        this.startMinute = other.startMinute;
        this.endHour = other.endHour;
        this.endMinute = other.endMinute;
        this.availableClassrooms = new ArrayList<>(other.availableClassrooms);
        this.lessons = new ArrayList<>();
    }

    public TimeSlot(int startHour, int startMinute, int endHour, int endMinute, List<Classroom> availableClassrooms) {
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;
        this.availableClassrooms = availableClassrooms;
        this.lessons = new ArrayList<>();
    }


    public TimeSlot(Integer startHour, Integer startMinute, Integer endHour, Integer endMinute, List<Classroom> availableClassrooms, List<Lesson> lessons) {
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;
        this.availableClassrooms = availableClassrooms;
        this.lessons = lessons;
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

    public List<Classroom> getAvailableClassrooms() {
        return availableClassrooms;
    }

    public void setAvailableClassrooms(List<Classroom> availableClassrooms) {
        this.availableClassrooms = availableClassrooms;
    }

    public void addClassroom(Classroom classroom){
        availableClassrooms.add(classroom);
    }

    public void removeClassroom(Classroom classroom){
        availableClassrooms.remove(classroom);
    }

    // Lessons

    public List<Lesson> getLessons(){
        return lessons;
    }
    public void setLessons(List<Lesson> lessons){
        this.lessons = lessons;
    }
    public void addLesson(Lesson lesson){
        lessons.add(lesson);
        lesson.setTimeSlot(this);
    }
    public void removeLesson(Lesson lesson){ lessons.remove(lesson); }

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

    @Override
    public String toString() {
        return "TimeSlot{" +
                "startHour=" + startHour +
                ", startMinute=" + startMinute +
                ", endHour=" + endHour +
                ", endMinute=" + endMinute +
                '}';
    }

    public TimeSlot clone() throws CloneNotSupportedException{
        TimeSlot clone = (TimeSlot)super.clone();
        clone.lessons = (List<Lesson>) cloneLessons(lessons);
        clone.availableClassrooms = (List<Classroom>)cloneClassrooms(availableClassrooms);
        return clone;
    }

    private List<Classroom> cloneClassrooms(List<Classroom> availableClassrooms) throws CloneNotSupportedException {
        List<Classroom> clone = new ArrayList<>();
        for(int i = 0; i<availableClassrooms.size(); i++){
            clone.add(availableClassrooms.get(i).clone());
        }
        return clone;
    }

    private List<Lesson> cloneLessons(List<Lesson> lessons) throws CloneNotSupportedException {
        List<Lesson> clone = new ArrayList<>();
        for(int i = 0; i<lessons.size(); i++){
            clone.add(lessons.get(i).clone());
        }
        return clone;
    }

}
