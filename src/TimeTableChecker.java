import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class TimeTableChecker {
    static private Map <Integer, String> collisionsProblemTypes;
    static private Map <Integer, String> workloadProblemTypes;

    static Result checkForCollisions(TimeTable table){
        Result res = new Result();
        List<List<TimeSlot>> tableSlots = table.getTimeSlots();

        // Identify if there are students with more than 1 class at a time
        for (List<TimeSlot> day : tableSlots){
            HashSet <StudentsGroup> involvedGroups = new HashSet<>();
            for (TimeSlot ts : day){
                for (Lesson les : ts.getLessons()){
                    les.
                }
            }
        }

        return res;
    }

    static Result checkWorkload(TimeTable table){
        Result res = new Result();
        return res;
    }

    static public class Result {
        public int numberOfProblems;
        public int negativeScore;
        public Map <String, List <TimeSlot> > foundProblems;
    }

//    public class WorkloadCheckResult extends Result {
//
//    }
}
