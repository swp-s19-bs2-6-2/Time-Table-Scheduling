package algorithms;

import java.util.*;

public class Teacher {
    private int teacherID;

    private String name;
    private List <TimeSlot> preferredTimeslots;

    // Constructors

    public Teacher(int teacherID, String name){
        this.teacherID = teacherID;
        this.name = name;
        this.preferredTimeslots = new ArrayList<>();
    }

    public Teacher(int teacherID, String name, List<TimeSlot> preferredTimeslots) {
        this.teacherID = teacherID;
        this.name = name;
        this.preferredTimeslots = preferredTimeslots;
    }

    // Getters and setters

    public int getTeacherID() {
        return teacherID;
    }

    public String getName() {
        return name;
    }

    public List<TimeSlot> getPreferredTimeslots() {
        return preferredTimeslots;
    }

    public boolean setPreferredTimeslots(List<TimeSlot> preferredTimeslots) {
        if (!checkTimeslots(preferredTimeslots)){
            return false;
        }
        this.preferredTimeslots = preferredTimeslots;
        return true;
    }

    // Time slots operations

    private boolean checkTimeslots(Collection <TimeSlot> timeSlots){
        Set<TimeSlot> timeSlotsSet = new HashSet<>();
        for (TimeSlot t : timeSlots){
            if (timeSlotsSet.contains(t)){
                return false;
            }
            timeSlotsSet.add(t);
        }
        return true;
    }

    public boolean addTimeslots(Collection <TimeSlot> timeSlots){
        if (!checkTimeslots(timeSlots)){
            return false;
        }
        for (TimeSlot t : timeSlots){
            if (this.preferredTimeslots.contains(t)){
                return false;
            }
        }
//        for (algorithms.TimeSlot t : timeSlots){
//            this.preferredTimeslots.add(t);
//        }
        this.preferredTimeslots.addAll(timeSlots);
        return true;
    }

    public boolean addSingleTimeslot(TimeSlot timeSlot){
        if (this.preferredTimeslots.contains(timeSlot)){
            return false;
        }
        preferredTimeslots.add(timeSlot);
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
