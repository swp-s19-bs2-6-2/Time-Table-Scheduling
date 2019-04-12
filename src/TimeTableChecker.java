import java.util.*;

public class TimeTableChecker {
    static private Map <Integer, String> collisionsProblemTypes;
    static private Map <Integer, String> workloadProblemTypes;

    static Result checkForCollisions(TimeTable table){
        Result res = new Result();
        List<List<TimeSlot>> tableSlots = table.getTimeSlots();
        List<String> problemDescription = new ArrayList<>();
        int currentProblem = 0;

        // Identify if there are students with more than 1 class at a time

        problemDescription.add("Student has more than 1 class in one timeslot");
        res.foundProblems.put(problemDescription.get(currentProblem), new ArrayList<>());

        for (List<TimeSlot> day : tableSlots){
            for (TimeSlot ts : day){
                BitSet involvedStudents = null;
                for (Lesson les : ts.getLessons()){
                    StudentsGroup currentGroup = les.getAssignedGroup();
                    if (involvedStudents == null){
                        involvedStudents = new BitSet(currentGroup.getMaxStudentsNumber());
                    }
                    if (involvedStudents.intersects(currentGroup.getStudents())){
                        res.foundProblems.get(problemDescription.get(currentProblem)).add(ts);
                        break;
                    } else {
                        involvedStudents.or(currentGroup.getStudents());
                    }
                }
            }
        }

        // Identify if there is a teacher having 2 classes at a time
        currentProblem++;
        problemDescription.add("Teacher has more than 1 class in one timeslot");
        res.foundProblems.put(problemDescription.get(currentProblem), new ArrayList<>());

        for (List<TimeSlot> day : tableSlots){
            for (TimeSlot ts : day){
                Set <Teacher> involvedTeachers = new HashSet<>();
                for (Lesson les : ts.getLessons()){
//                    System.out.println(les.toString());
                    Teacher currentTeacher = les.getAssignedTeacher();
                    if (involvedTeachers.contains(currentTeacher)){
                        System.out.println("FOUND INTERSECTING TEACHER!");
                        res.foundProblems.get(problemDescription.get(currentProblem)).add(ts);
                        break;
                    }
                    involvedTeachers.add(currentTeacher);
                }
            }
        }

        // Identify if there is a room holding 2 classes at a time
        currentProblem++;
        problemDescription.add("Room is holding more than 1 class in one timeslot");
        res.foundProblems.put(problemDescription.get(currentProblem), new ArrayList<>());

        for (List<TimeSlot> day : tableSlots){
            for (TimeSlot ts : day){
                Set <Classroom> involvedClassrooms = new HashSet<>();
                for (Lesson les : ts.getLessons()){
                    Classroom currentClassroom = les.getClassroom();
                    if (involvedClassrooms.contains(currentClassroom)){
                        res.foundProblems.get(problemDescription.get(currentProblem)).add(ts);
                        break;
                    }
                    involvedClassrooms.add(currentClassroom);
                }
            }
        }

        return res;
    }

    static Result checkWorkload(TimeTable table){
        Result res = new Result();
        List<List<TimeSlot>> tableSlots = table.getTimeSlots();
        List<String> problemDescription = new ArrayList<>();
        int currentProblem = 0;

        // Checking amount of classes in a day.
        // If student has >=4 classes, it adds +1pt, >=5 adds +2 pts for each

        problemDescription.add("4 or more classes in a day");
        res.foundProblems.put(problemDescription.get(0), new ArrayList<>());
        problemDescription.add("3 or more lectures in a day");
        res.foundProblems.put(problemDescription.get(1), new ArrayList<>());


        for (List<TimeSlot> day : tableSlots){
            long[] involvedStudents = null;
            long[] lecturesNumber = null;
            for (TimeSlot ts : day){
                boolean[] problems = new boolean[problemDescription.size()];
                for (Lesson les : ts.getLessons()){
                    StudentsGroup currentGroup = les.getAssignedGroup();
                    if (involvedStudents == null){
                        involvedStudents = new long[currentGroup.getMaxStudentsNumber()];
                        lecturesNumber = new long[currentGroup.getMaxStudentsNumber()];
                    }
                    for (int i = 0; i < currentGroup.getMaxStudentsNumber(); i++){
//                        involvedStudents[i] = involvedStudents[i] + (currentGroup.getStudents().get(i) ? 1 : 0);
                        if (currentGroup.getStudents().get(i)) {
                            involvedStudents[i] = involvedStudents[i] + 1;
                            if (involvedStudents[i] == 4) {
                                if (!problems[0]) {
                                    res.foundProblems.get(problemDescription.get(0)).add(ts);
                                    problems[0] = true;
                                }
                                res.negativeScore += 1;
                            }
                            if (involvedStudents[i] > 4) {
                                res.negativeScore += 2;
                            }
                        }
                        // If lecture -> add 1
                        if (les.getCourseClassType().name.equals("lecture")){
                            lecturesNumber[i] += 1;
                            if (lecturesNumber[i] == 3){
                                res.negativeScore += 3;
                                if (!problems[1]) {
                                    res.foundProblems.get(problemDescription.get(1)).add(ts);
                                    problems[1] = true;
                                }
                            }
                            if (lecturesNumber[i] > 3){
                                res.negativeScore += 5;
                            }
                        }
                    }
                }
            }
        }


        return res;
    }

    static public class Result {
        public int numberOfProblems;
        public int negativeScore;
        public Map <String, List <TimeSlot> > foundProblems;

        public Result() {
            this.foundProblems = new HashMap<>();
        }
    }

//    public class WorkloadCheckResult extends Result {
//
//    }
}
