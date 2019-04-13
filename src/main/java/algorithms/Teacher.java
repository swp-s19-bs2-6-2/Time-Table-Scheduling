package algorithms;

import java.util.*;

public class Teacher {
    private int teacherID;

    private String name;
//    private List <TimeSlot> preferredTimeslots; // There is a problem with such representation. During schedule generation time slots are being cloned several times and thi info is not relevant anymore
    private  Map<Integer, List<Integer> > preferredTimeslots;

    // Constructors

    public Teacher(int teacherID, String name){
        this.teacherID = teacherID;
        this.name = name;
        this.preferredTimeslots = new HashMap<>();
    }

    public Teacher(int teacherID, String name, Map<Integer, List<Integer> > preferredTimeslots) {
        this.teacherID = teacherID;
        this.name = name;
        this.preferredTimeslots = preferredTimeslots;
        this.sortTimesSlots();
    }

    // Getters and setters

    public int getTeacherID() {
        return teacherID;
    }

    public String getName() {
        return name;
    }

    public Map<Integer, List<Integer> > getPreferredTimeslots() {
        return preferredTimeslots;
    }

    public boolean setPreferredTimeslots(Map<Integer, List<Integer> > preferredTimeslots) {
        if (!checkTimeslots(preferredTimeslots)){
            return false;
        }
        this.preferredTimeslots = preferredTimeslots;
        this.sortTimesSlots();
        return true;
    }

    // Time slots operations

    private void sortTimesSlots(){
        for (Map.Entry<Integer, List <Integer > > day: this.preferredTimeslots.entrySet()){
            Collections.sort(day.getValue());
        }
    }

    private boolean checkTimeslots(Map<Integer, List<Integer> > timeSlots){
        for (Integer day : timeSlots.keySet()) {
            Set<Integer> timeSlotsSet = new HashSet<>();
            for (Integer t : timeSlots.get(day)) {
                if (timeSlotsSet.contains(t)) {
                    return false;
                }
                timeSlotsSet.add(t);
            }
        }
        return true;
    }

    public boolean addTimeslots(Map<Integer, List<Integer> > timeSlots){
        if (!checkTimeslots(timeSlots)){
            return false;
        }

        // Checking if time slots are already chosen.
        for (Integer day : timeSlots.keySet()){
            if (!this.preferredTimeslots.containsKey(day)){
                continue;
            }
            for (Integer timeslotIdx : timeSlots.get(day)) {
                if (this.preferredTimeslots.get(day).contains(timeslotIdx)) {
                    return false;
                }
            }
        }

        for (Integer day : timeSlots.keySet()){
            if (!this.preferredTimeslots.containsKey(day)){
                this.preferredTimeslots.put(day, new ArrayList<>(timeSlots.get(day)));
            }
            this.preferredTimeslots.get(day).addAll(timeSlots.get(day));
        }

        this.sortTimesSlots();

        return true;
    }

    public boolean addSingleTimeslot(Integer day, Integer timeSlotIdx){
        if (!this.preferredTimeslots.containsKey(day)){
            this.preferredTimeslots.put(day, new ArrayList<>());
            this.preferredTimeslots.get(day).add(timeSlotIdx);
            return true;
        }
        if (this.preferredTimeslots.get(day).contains(timeSlotIdx)){
            return false;
        }
        preferredTimeslots.get(day).add(timeSlotIdx);
        this.sortTimesSlots();  // TODO : Probably should be replaced with something more efficient
        return true;
    }

    @Override
    public String toString() {
        return "algorithms.Teacher{" +
                "teacherID=" + teacherID +
                ", name='" + name + '\'' +
                ", preferredTimeslots=" + preferredTimeslots +
                '}';
    }
}
