import java.util.*;

public class Course {

    // Lessons of the course
//    private List<Lesson> lessons;   // TODO : decide to keep or replace
    private  Map<CourseClassType, Integer> classTypes;
    // Order of classes
    private List<CourseClassType> classesOrder;
    // Groups of enrolled students
    private List<StudentsGroup> enrolledStudents;

    // Class order

    boolean setClassesOrder(List<CourseClassType> order){
        Map<CourseClassType,  Integer> checkClassTypes = new HashMap<>();
        for (CourseClassType class_type : order){
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

//    boolean setClassesOrder(List<CourseClassType> order){
//        // Getting class types
//        Set<CourseClassType> classTypes = new HashSet<>();
//        for (Lesson lesson :lessons) {
//            classTypes.add(lesson.getCourseClassType());
//        }
//        // Comparing correspondence
//        for (CourseClassType classType : order){
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
//    public List<Lesson> getClassTypes() {
//        return classTypes;
//    }
//
//    public void setClassTypes(List<Lesson> classTypes) {
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

    // Lessons

//    public List<Lesson> getLessons() {
//        return lessons;
//    }
//
//    public boolean setLessons(List<Lesson> lessons) {
//        // Checking course and order
//        if (this.classesOrder.size() > 0) {
//            Iterator<CourseClassType> orderIterator = this.classesOrder.iterator();
//            CourseClassType currentClassType = orderIterator.next();
//            int groupsAmount = this.enrolledStudents.size();
//            int groupsHadClass = 0;
//
//            for (Lesson lesson : lessons){
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
}
