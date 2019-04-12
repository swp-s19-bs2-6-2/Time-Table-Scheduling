import java.util.ArrayList;
import java.util.List;

public class TimeTable {
    private List<List<TimeSlot>> timeSlots;

    public List<List<TimeSlot>> getTimeSlots() {
        return timeSlots;
    }

    // creates appropriate time table
    public TimeTable(List<TimeSlot> availableDayTimeSlots, int workingDays, List<Lesson> availableLessons) throws CloneNotSupportedException {
        List<List<TimeSlot>> availableTimeSlots = new ArrayList<>();
        for (int day = 0; day < workingDays; day++) {
            availableTimeSlots.add(cloneTimeSlots(availableDayTimeSlots));
        }
        List<List<List<TimeSlot>>> possibleSchedules = go(availableTimeSlots, availableLessons, 0, 0, 1000);
        timeSlots = chooseBest(possibleSchedules);
    }

    /**
     * Constructor needed for testing purposes
     * @param timeSlots
     */
    public TimeTable(List<List<TimeSlot>> timeSlots) {
        this.timeSlots = timeSlots;
    }

    public List<List<TimeSlot>> getTimeSlots() {
        return timeSlots;
    }

    public int kek = 11;

    /**
     * finds possible schedules
     *
     * @param currentState          current state of the time table (time slots and lessons inside it)
     * @param availableLessons      shows the lessons which is not put to the schedule
     * @param currentDay
     * @param currentTimeSlotNumber
     * @param maxResults            maximum amount of results to find
     * @return
     * @throws CloneNotSupportedException
     */
    private List<List<List<TimeSlot>>> go(List<List<TimeSlot>> currentState, List<Lesson> availableLessons, int currentDay, int currentTimeSlotNumber, int maxResults) throws CloneNotSupportedException { // doing recursion
        if (currentDay == currentState.size()) {
            return null;
        }
        List<List<List<TimeSlot>>> result = new ArrayList<>();
        if (availableLessons.isEmpty()) {

            result.add(currentState);

            /*System.err.println("HERE:    " + kek++);
            for (int i = 0; i < currentState.size(); i++) {
                for (int j = 0; j < currentState.get(i).size(); j++) {
                    TimeSlot timeSlot = currentState.get(i).get(j);
                    System.err.println("day: " + i + " time: " + timeSlot.startHour + ":" + timeSlot.startMinute + " - " + timeSlot.endHour + ":" + timeSlot.endMinute);
                    System.err.print("lessons: ");
                    for (int k = 0; k < timeSlot.getLessons().size(); k++) {
                        System.err.print(timeSlot.getLessons().get(k).getCourse().courseName + " ");
                    }
                    System.err.println();
                }
            }*/
            return result;
        }
        // next lesson in queue
        List<List<TimeSlot>> nextState_Added = new ArrayList<>(cloneState(currentState));
        List<Lesson> nextAvailableLessons_Added = new ArrayList<>(cloneLessons(availableLessons));
        int nextLessonId = nextAvailableLessons_Added.size() - 1;
        Lesson lesson = nextAvailableLessons_Added.get(nextLessonId);
        TimeSlot timeSlot = nextState_Added.get(currentDay).get(currentTimeSlotNumber);
        // adds lesson to the current time slot
        if (canAddLesson(timeSlot, lesson)) {
            addLesson(timeSlot, lesson);
            nextAvailableLessons_Added.remove(nextLessonId);
            List<List<List<TimeSlot>>> nextPossibleSchedules = go(nextState_Added, nextAvailableLessons_Added, currentDay, currentTimeSlotNumber, maxResults);
            if (nextPossibleSchedules != null) {
                for (int i = 0; i < nextPossibleSchedules.size(); i++) {
                    if (result.size() < maxResults) result.add(nextPossibleSchedules.get(i));
                }
            }
            if (result.size() == maxResults) return result;
        }
        // goes forward without adding the lesson to the current time slot
        List<List<TimeSlot>> nextState_Continued = new ArrayList<>(currentState);
        List<Lesson> nextAvailableLessons_Continued = new ArrayList<>(availableLessons);
        currentTimeSlotNumber = (currentTimeSlotNumber + 1) % nextState_Continued.get(currentDay).size();
        currentDay = (currentTimeSlotNumber == 0 ? currentDay + 1 : currentDay);
        List<List<List<TimeSlot>>> nextPossibleSchedules = go(nextState_Continued, nextAvailableLessons_Continued, currentDay, currentTimeSlotNumber, maxResults);
        if (nextPossibleSchedules != null) {
            for (int i = 0; i < nextPossibleSchedules.size(); i++) {
                if (result.size() < maxResults) result.add(nextPossibleSchedules.get(i));
            }
        }
        return result;
    }


    private boolean canAddLesson(TimeSlot timeSlot, Lesson lesson) { // TODO: all possible conditions for adding the lesson to the current time slot
        if (timeSlot.getAvailableClassrooms().size() <= timeSlot.getLessons().size()) {
            return false;
        }
        for (int i = 0; i < timeSlot.getLessons().size(); i++) {
            if (intersectTeacherOrStudents(lesson, timeSlot.getLessons().get(i))) {
                return false;
            }
        }
        return true;
    }

    private boolean intersectTeacherOrStudents(Lesson lesson_A, Lesson lesson_B) {
        if (lesson_A.getAssignedGroup().intersect(lesson_B.getAssignedGroup())
                || lesson_A.getAssignedTeacher().getTeacherID() == lesson_B.getAssignedTeacher().getTeacherID()) {
            return true;
        }
        return false;
    }

    private void addLesson(TimeSlot timeSlot, Lesson lesson) {
        timeSlot.addLesson(lesson);
    }

    private void removeLesson(TimeSlot timeSlot, Lesson lesson) {
        timeSlot.removeLesson(lesson);
    }

    // chooses the best schedule from the provided list
    public List<List<TimeSlot>> chooseBest(List<List<List<TimeSlot>>> possilbeSchedules) {
        if(possilbeSchedules==null || possilbeSchedules.size() == 0){
            return null;
        }
        int best = 0;
        for (int i = 1; i < possilbeSchedules.size(); i++) {
            if (betterSchedule(possilbeSchedules.get(i), possilbeSchedules.get(best))) {
                best = i;
            }
        }
        return possilbeSchedules.get(best);
    }

    /**
     * compares two schedules
     *
     * @param A first schedule to compare
     * @param B second schedule to compare
     * @return true if a is better than b, and false otherwise
     */
    public boolean betterSchedule(List<List<TimeSlot>> A, List<List<TimeSlot>> B) {
        int days = A.size(), timeslots = B.get(0).size();
        int totalLessons = 0;
        for (int day = 0; day < days; day++) {
            for (int id = 0; id < timeslots; id++) {
                totalLessons += A.get(day).get(id).getLessons().size();
            }
        }

        double averageLessonsPerDay = (double) totalLessons / A.size();
        double difference_A = 0, difference_B = 0;
        for (int day = 0; day < days; day++) {
            int lessonsCurrentDay_A = 0, lessonsCurrentDay_B = 0;
            for (int id = 0; id < timeslots; id++) {
                lessonsCurrentDay_A += A.get(day).get(id).getLessons().size();
                lessonsCurrentDay_B += B.get(day).get(id).getLessons().size();
            }
            difference_A += Math.abs(lessonsCurrentDay_A - averageLessonsPerDay);
            difference_B += Math.abs(lessonsCurrentDay_B - averageLessonsPerDay);
        }

        if (difference_A < difference_B) return true;
        else return false;
    }

    public void printTimeTable() {
        for (int i = 0; i < timeSlots.size(); i++) {
            for (int j = 0; j < timeSlots.get(i).size(); j++) {
                TimeSlot timeSlot = timeSlots.get(i).get(j);
                System.out.println("day: " + i + " time: " + timeSlot.startHour + ":" + timeSlot.startMinute + " - " + timeSlot.endHour + ":" + timeSlot.endMinute);
                System.out.println("lessons: ");
                for (int k = 0; k < timeSlot.getLessons().size(); k++) {
                    System.out.print(timeSlot.getLessons().get(k).getCourse().courseName + " "
                            + timeSlot.getLessons().get(k).getCourseClassType().name + " " + timeSlot.getLessons().get(k).getAssignedGroup().name + "\n");
                }
                System.out.println();
            }
        }
    }

    private List<TimeSlot> cloneTimeSlots(List<TimeSlot> availableDayTimeSlots) throws CloneNotSupportedException {
        List<TimeSlot> clone = new ArrayList<>();
        for (int i = 0; i < availableDayTimeSlots.size(); i++) {
            clone.add(availableDayTimeSlots.get(i).clone());
        }
        return clone;
    }

    private List<List<TimeSlot>> cloneState(List<List<TimeSlot>> currentState) throws CloneNotSupportedException {
        List<List<TimeSlot>> clone = new ArrayList<>();
        for (int i = 0; i < currentState.size(); i++) {
            clone.add(cloneTimeSlots(currentState.get(i)));
        }
        return clone;
    }

    private List<Lesson> cloneLessons(List<Lesson> availableLessons) throws CloneNotSupportedException {
        List<Lesson> clone = new ArrayList<>();
        for (int i = 0; i < availableLessons.size(); i++) {
            clone.add(availableLessons.get(i).clone());
        }
        return clone;
    }

}
