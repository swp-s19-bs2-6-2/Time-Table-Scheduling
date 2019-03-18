import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class TimeTable {
    private List<List<TimeSlot>> timeSlots;


    // creates appropriate time table
    public TimeTable(List<TimeSlot> availableDayTimeSlots, int workingDays, List<Lesson> availableLessons) throws CloneNotSupportedException {
        List<List<TimeSlot>> availableTimeSlots = new ArrayList<>();
        for (int day = 0; day < workingDays; day++) {
            availableTimeSlots.add(new ArrayList<>(cloneTimeSlots(availableDayTimeSlots)));
        }
        timeSlots = go(availableTimeSlots, availableLessons, 0, 0);
    }

    private List<TimeSlot> cloneTimeSlots(List<TimeSlot> availableDayTimeSlots) throws CloneNotSupportedException {
        List<TimeSlot> result = new ArrayList<>();
        for(int i = 0; i<availableDayTimeSlots.size(); i++){
            result.add(availableDayTimeSlots.get(i).clone());
        }
        return result;
    }


    private List<List<TimeSlot>> go(List<List<TimeSlot>> currentState, List<Lesson> availableLessons, int currentDay, int currentTimeSlotNumber) { // doing recursion
        /*System.out.println("here iteration is " + currentDay + " " + currentTimeSlotNumber);
        for (int i = 0; i < currentDay; i++) {
            for (int j = 0; j < currentTimeSlotNumber; j++) {
                TimeSlot timeSlot = currentState.get(i).get(j);
                System.out.println("day: " + i + " time: " + timeSlot.startHour + ":" + timeSlot.startMinute + " - " + timeSlot.endHour + ":" + timeSlot.endMinute);
                System.out.print("lessons: ");
                for (int k = 0; k < timeSlot.getLessons().size(); k++) {
                    System.out.print(timeSlot.getLessons().get(k).getCourse().courseName + " ");
                }
                System.out.println();
            }
        }*/
        if (availableLessons.isEmpty()) {
            return currentState;
        }
        if (currentDay == currentState.size()) {
            return null;
        }
        // next lesson in queue
        int nextLessonId = availableLessons.size() - 1;
        Lesson lesson = availableLessons.get(nextLessonId);
        TimeSlot timeSlot = currentState.get(currentDay).get(currentTimeSlotNumber);

        if (canAddLesson(timeSlot, lesson)) {
            addLesson(timeSlot, lesson);
            availableLessons.remove(nextLessonId);
            List<List<TimeSlot>> result = go(currentState, availableLessons, currentDay, currentTimeSlotNumber);
            if (result != null) return result;
            availableLessons.add(lesson);
            removeLesson(timeSlot, lesson);
        }
        currentTimeSlotNumber = (currentTimeSlotNumber + 1) % currentState.get(currentDay).size();
        currentDay = (currentTimeSlotNumber == 0 ? currentDay + 1 : currentDay);
        return go(currentState, availableLessons, currentDay, currentTimeSlotNumber);
    }

    private boolean canAddLesson(TimeSlot timeSlot, Lesson lesson) {
        return true;
    }

    private void addLesson(TimeSlot timeSlot, Lesson lesson) {
        timeSlot.addLesson(lesson);
    }

    private void removeLesson(TimeSlot timeSlot, Lesson lesson) {
        timeSlot.removeLesson(lesson);
    }

    public void printTimeTable() {
        for (int i = 0; i < timeSlots.size(); i++) {
            for (int j = 0; j < timeSlots.get(i).size(); j++) {
                TimeSlot timeSlot = timeSlots.get(i).get(j);
                System.out.println("day: " + i + " time: " + timeSlot.startHour + ":" + timeSlot.startMinute + " - " + timeSlot.endHour + ":" + timeSlot.endMinute);
                System.out.print("lessons: ");
                for (int k = 0; k < timeSlot.getLessons().size(); k++) {
                    System.out.print(timeSlot.getLessons().get(k).getCourse().courseName + " ");
                }
                System.out.println();
            }
        }
    }
}
