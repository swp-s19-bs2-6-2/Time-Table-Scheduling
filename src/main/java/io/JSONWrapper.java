package io;

import algorithms.Lesson;
import algorithms.TimeSlot;
import algorithms.TimeTable;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;


public class JSONWrapper {
    public String wrap(TimeTable tt){
        JSONObject tr = new JSONObject();
        tr.put(ANALYSIS_TAG, new JSONObject()); // for future analysis

        List<List<JSONObject>> days = new LinkedList<>(); // For storing schedules for each day

        // Going through all the time slots in the day
        for (int i = 0; i < tt.getTimeSlots().size(); i++) {
            List<JSONObject> slots = new LinkedList<>();

            //Going through all the lessons of the time slot
            for (int j = 0; j < tt.getTimeSlots().get(i).size(); j++) {
                //An Object for each of the lesson with time
                JSONObject tso = new JSONObject();
                TimeSlot timeSlot = tt.getTimeSlots().get(i).get(j);
                tso.put(START_HOUR_TAG, timeSlot.startHour);
                tso.put(END_HOUR_TAG, timeSlot.endHour);
                tso.put(START_MINUTE_TAG, timeSlot.startMinute);
                tso.put(END_MINUTE_TAG, timeSlot.endMinute);

                //And all the classes of the time slot
                List<JSONObject> classes = new LinkedList<>();
                for (int k = 0; k < timeSlot.getLessons().size(); k++) {
                    Lesson l = timeSlot.getLessons().get(k);
                    String course = l.getCourse().courseName;
                    String type = l.getCourseClassType().name;
                    String group = l.getAssignedGroup().name;
                    String teacher = l.getAssignedTeacher().getName();
                    String room = "room_holder";//l.getClassroom().getName();//ToDo fix when getClassroom is done

                    //An object for each lesson with all the information
                    JSONObject o = new JSONObject();
                    o.put(CLASS_TYPE_TAG, type);
                    o.put(COURSE_NAME_TAG, course);
                    o.put(TEACHER_NAME_TAG, teacher);
                    o.put(GROUP_NAME_TAG, group);
                    o.put(ROOM_TAG, room);
                    classes.add(o);
                }
                //Adding info about classes into the time slot
                tso.put(LESSONS_LIST_TAG, classes);
                //Adding the slot into slots list
                slots.add(tso);
            }
            //Adding all the day slots into the day
            days.add(slots);
        }
        JSONObject daysObject = new JSONObject();
        //Putting the days info into an object
        daysObject.put(DAYS_LIST_TAG, days);
        //And adding the object into the table part of the JSON
        tr.put(TABLE_OBJECT_TAG, daysObject);

        return tr.toString();
    }

    private final static String ANALYSIS_TAG = "analysis",
                                START_HOUR_TAG = "startHour",
                                END_HOUR_TAG = "endHour",
                                START_MINUTE_TAG = "startMinute",
                                END_MINUTE_TAG = "endMinute",
                                CLASS_TYPE_TAG = "type",
                                COURSE_NAME_TAG = "course",
                                TEACHER_NAME_TAG = "teacher",
                                GROUP_NAME_TAG = "group",
                                ROOM_TAG = "room",
                                LESSONS_LIST_TAG = "lessons",
                                DAYS_LIST_TAG = "days",
                                TABLE_OBJECT_TAG = "table";
}
