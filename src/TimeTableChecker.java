import java.util.List;
import java.util.Map;

public class TimeTableChecker {
    static private Map <Integer, String> collisionsProblemTypes;
    static private Map <Integer, String> workloadProblemTypes;

    static Result checkForCollisions(TimeTable table){
        Result res = new Result();
        return res;
    }

    static Result checkWorkload(TimeTable table){
        Result res = new Result();
        return res;
    }

    static public class Result {
        int numberOfProblems;
        int negativeScore;
        Map <String, List <TimeSlot> > foundProblems;
    }

//    public class WorkloadCheckResult extends Result {
//
//    }
}
