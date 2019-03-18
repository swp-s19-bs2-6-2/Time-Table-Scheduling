import java.util.ArrayList;
import java.util.List;

public class TimeTable {
    private List<List<TimeSlot>> availableTimeSlots;
    private List<Course> courses;

    public TimeTable(List<List<TimeSlot>> availableTimeSlots) {
        this.availableTimeSlots = availableTimeSlots;
    }

    public TimeTable(List<TimeSlot> availableDayTimeSlots, int workingDays) {
        this.availableTimeSlots = new ArrayList<>();
        for (int day = 0; day < workingDays; day++) {
            this.availableTimeSlots.add(new ArrayList<>(availableDayTimeSlots));
        }
    }

    // finds the appropriate time table
    public List<List<TimeSlot>> createTimeTable() { // doing

    }

    private List<List<List<TimeSlot>>> go(List<List<TimeSlot>> currentState, List<Lesson> availableLessons) { // doing recursion
        List<List<List<TimeSlot>>> possible = new List<>();
        if(availableLessons.isEmpty()){
            possible.add(currentState);
            return possible;
        }
        // next lesson in queue
        Lesson current = availableLessons.get(availableLessons.size() - 1);
        availableLessons.remove(availableLessons.size() - 1);
        for(List<TimeSlot> day : currentState){
            for(TimeSlot timeSlot : day){
                if(canAddLesson(timeSlot, current)){
                    AddLesson(timeSlot, )
                }
            }
        }

    }
    private boolean canAddLesson(TimeSlot timeSlot, Lesson lesson){

    }

    public List<TimeSlot> getDayTimeslots(int day) { // TODO : days start from 0 or from 1? :D
        return this.availableTimeSlots.get(day);
    }

    public List<TimeSlot> setDayTimeslots(int day, List<TimeSlot> timeSlots) {
        return this.availableTimeSlots.set(day, timeSlots);
    }
}
