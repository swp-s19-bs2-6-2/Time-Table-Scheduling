package algorithms;

import java.util.*;

public class Course implements Cloneable{

    // Lessons of the course
//    private List<algorithms.Lesson> lessons;   // TODO : decide to keep or replace
    private  Map<CourseClassType, Integer> classTypes;
    // Order of classes
    private List<CourseClassType> classesOrder;
    // Groups of enrolled students
    private List<StudentsGroup> enrolledStudents;
    // Teachers for this course with taught classes and amount
    private Map<Teacher, Map<CourseClassType, Integer>> teachersMap;
    public String courseName;

    // Constructors

    public Course(Map<CourseClassType, Integer> classTypes, List<CourseClassType> classesOrder, List<StudentsGroup> enrolledStudents, Map<Teacher, Map<CourseClassType, Integer>> teachersMap, String courseName) {
        this.classTypes = classTypes;
        this.classesOrder = classesOrder;
        this.enrolledStudents = enrolledStudents;
        this.teachersMap = teachersMap;
        this.courseName = courseName;
    }

    public Course(String courseName){
        this.classTypes = new HashMap<>();
        this.classesOrder = new ArrayList<>();
        this.enrolledStudents = new ArrayList<>();
        this.teachersMap = new HashMap<>();
        this.courseName = courseName;

    }

    // Teachers

    private boolean isTeachersMapConsistent(Map<Teacher, Map<CourseClassType, Integer>> teachersMap){
        Map<CourseClassType, Integer> assignedClassTypes = new HashMap<>();
        for (Map.Entry<Teacher, Map<CourseClassType, Integer>> teacher : teachersMap.entrySet()){
            assignedClassTypes.putAll(teacher.getValue());
        }
        // Comparing values in assignedClassTypes and this.classTypes TODO : Implement and test with .equals()
        for (CourseClassType key : this.classTypes.keySet()){
            if (!assignedClassTypes.containsKey(key)){
                return false;
            }
            if (!assignedClassTypes.get(key).equals(this.classTypes.get(key))){
                return false;
            }
        }
        return true;
    }

    public Map<Teacher, Map<CourseClassType, Integer>> getTeachersMap() {
        return teachersMap;
    }

    public boolean setTeachersMap(Map<Teacher, Map<CourseClassType, Integer>> teachersMap) {
        if (!isTeachersMapConsistent(teachersMap)){
            return false;
        }
        this.teachersMap = teachersMap;
        return true;
    }

    public void addTeacher(Teacher teacher, Map<CourseClassType, Integer> assignedClasses){
        this.teachersMap.put(teacher, assignedClasses);
    }

    public void assignClassToTeacher(Teacher teacher, CourseClassType classType, int amount){
        // TODO : add validation
        if (!this.teachersMap.containsKey(teacher)){
            this.teachersMap.put(teacher, new HashMap<>());
        }
        this.teachersMap.get(teacher).put(classType, amount);
    }

    // Class order

    public boolean setClassesOrder(List<CourseClassType> order){
        Map<CourseClassType,  Integer> checkClassTypes = new HashMap<>();
        for (CourseClassType class_type : order){
            if (!checkClassTypes.containsKey(class_type)) checkClassTypes.put(class_type, 0);
            checkClassTypes.put(class_type, checkClassTypes.get(class_type) + 1);
        }
        for (CourseClassType class_type : this.classTypes.keySet()){
            if (!checkClassTypes.get(class_type).equals(this.classTypes.get(class_type))){
                return false;
            }
        }
        this.classesOrder = order;
        return true;
    }

//    boolean setClassesOrder(List<algorithms.CourseClassType> order){
//        // Getting class types
//        Set<algorithms.CourseClassType> classTypes = new HashSet<>();
//        for (algorithms.Lesson lesson :lessons) {
//            classTypes.add(lesson.getCourseClassType());
//        }
//        // Comparing correspondence
//        for (algorithms.CourseClassType classType : order){
//            if (!classTypes.contains(classType)){
//                return false;
//            }
//        }
//        this.classesOrder = order;
//        return true;
//    }

    public List<CourseClassType> getClassesOrder() {
        return classesOrder;
    }

//    // Class types
//
//    public List<algorithms.Lesson> getClassTypes() {
//        return classTypes;
//    }
//
//    public void setClassTypes(List<algorithms.Lesson> classTypes) {
//        this.classTypes = classTypes;
//    }



    // Enrolled students

    public List<StudentsGroup> getEnrolledStudents() {
        return enrolledStudents;
    }

    public void setEnrolledStudents(List<StudentsGroup> enrolledStudents) {
        this.enrolledStudents = enrolledStudents;
    }

    public void addEnrolledStudents(StudentsGroup enrolledStudents) {
        this.enrolledStudents.add(enrolledStudents);
    }

    public Course clone() throws CloneNotSupportedException {
        Course clone = (Course) super.clone();
        clone.classTypes = (Map<CourseClassType, Integer>) cloneClassTypes(classTypes);
        clone.classesOrder = cloneClassesOrder(classesOrder);
        //clone.enrolledStudents = cloneEnrolledStudents(enrolledStudents);
        //TODO: need to clone other attributes
        return clone;
    }

    private List<StudentsGroup> cloneEnrolledStudents(List<StudentsGroup> enrolledStudents) throws CloneNotSupportedException {
        List<StudentsGroup> clone = new ArrayList<>();
        for(int i = 0; i<enrolledStudents.size(); i++){
            clone.add((StudentsGroup) enrolledStudents.get(i).clone());
        }
        return clone;
    }

    private List<CourseClassType> cloneClassesOrder(List<CourseClassType> classesOrder) throws CloneNotSupportedException {
        if(classesOrder == null) return null;
        List<CourseClassType> clone = new ArrayList<>();
        for(int i = 0; i<classesOrder.size(); i++){
            clone.add(classesOrder.get(i).clone());
        }
        return clone;
    }

    private Map<CourseClassType, Integer> cloneClassTypes(Map<CourseClassType, Integer> classTypes) throws CloneNotSupportedException {
        if(classTypes == null) return null;
        Map<CourseClassType, Integer> clone = new HashMap<>();
        Iterator<Map.Entry<CourseClassType, Integer>> iterator = classTypes.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<CourseClassType, Integer> pair = iterator.next();
            clone.put(pair.getKey().clone(), pair.getValue());
        }
        return clone;
    }

    // Lessons

//    public List<algorithms.Lesson> getLessons() {
//        return lessons;
//    }
//
//    public boolean setLessons(List<algorithms.Lesson> lessons) {
//        // Checking course and order
//        if (this.classesOrder.size() > 0) {
//            Iterator<algorithms.CourseClassType> orderIterator = this.classesOrder.iterator();
//            algorithms.CourseClassType currentClassType = orderIterator.next();
//            int groupsAmount = this.enrolledStudents.size();
//            int groupsHadClass = 0;
//
//            for (algorithms.Lesson lesson : lessons){
//                if (lesson.getCourse() != this){
//                    return false;
//                }
//                // checking if number of groups corresponds to number of classes if no "allStudents"
//                if (!currentClassType.allStudents){
//                    groupsHadClass++;
//                }
//                if (currentClassType != lesson.getCourseClassType()){
//                    if (!orderIterator.hasNext()){
//                        return false;
//                    } else {
//                        // If number of labs doesn't correspond to number of groups
//                        if (!currentClassType.allStudents){
//                            if (groupsHadClass % groupsAmount != 0){
//                                return false;
//                            }
//                        }
//
//                        currentClassType = orderIterator.next();
//                        if (currentClassType != lesson.getCourseClassType()){
//                            return false;
//                        }
//                        groupsHadClass = 0;
//                    }
//                }
//            }
//
//            if (!currentClassType.allStudents){
//                if (groupsHadClass % groupsAmount != 0){
//                    return false;
//                }
//            }
//        }
//        // If order is ok
//        this.lessons = lessons;
//        return true;
//    }

    @Override
    public String toString() {
        return "algorithms.Course{" +
                "classTypes=" + classTypes +
                ", classesOrder=" + classesOrder +
                ", enrolledStudents=" + enrolledStudents +
                ", teachersMap=" + teachersMap +
                ", courseName='" + courseName + '\'' +
                '}';
    }
}
