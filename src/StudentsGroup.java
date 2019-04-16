import java.lang.reflect.Array;
import java.util.*;

/**
 * Represents group of students.
 * It is possible to set which of N students will be in the group. However need to assign to each student ID from 0 to N-1
 */
public class StudentsGroup implements Cloneable{

    // Group name
    public String name;
    // Amount of students in the group
    private int studentsNumber;
    // BitSet (bitmap) containing information about students
    private BitSet students;



    // Constructors
    public StudentsGroup(int studentsNumber){
        this.studentsNumber = studentsNumber;
        this.students = new BitSet(studentsNumber);
    }

    public StudentsGroup(BitSet students){
        this.studentsNumber = students.size();
        this.students = students;
    }

    // Getters and setters

    /**
     * Get maximum number of students (number of all students in the university)
     * @return
     */
    public int getMaxStudentsNumber() {
        return this.studentsNumber;
    }

    /**
     * Get number of students in the current group
     * @return
     */
    public int getStudentsNumber() {
        return this.students.cardinality();
    }

    // Set operations

    /**
     * Checks if groups are intersecting
     * @param other
     * @return True if intersects and false otherwise.
     */
    public boolean intersect(StudentsGroup other){
        return students.intersects(other.students);
    }

    // Students operations

    /**
     * Add student with specified ID to this group
     * @param studentID
     * @return True if operations is succeed and false otherwise
     */
    public boolean addStudent(int studentID){
        if (studentID >= this.studentsNumber || studentID < 0){
            return false;
        }
        students.set(studentID);
        return true;
    }

    /**
     * Add all students with specified IDs from iterable to this group
     * @param studentIDs
     * @return True if operations is succeed and false otherwise
     */
    public boolean addStudents(Iterable <Integer> studentIDs){
        Iterator <Integer> iter = studentIDs.iterator();
        while (iter.hasNext()){
            int curr = iter.next();
            if (curr >= this.studentsNumber || curr < 0){
                return false;
            }
        }
        iter = studentIDs.iterator();
        while (iter.hasNext()){
            students.set(iter.next());
        }
        return true;
    }

    /**
     * Updates values for all students using BitSet
     * @param students
     * @return True if operations is succeed and false otherwise
     */
    public boolean updateStudents(BitSet students){
        if (students.size() != studentsNumber){
            return false;
        }
        this.students = students;
        return true;
    }

    // Other methods (interfaces and utilities)

    @Override
    public Object clone() throws CloneNotSupportedException {
        return new StudentsGroup((BitSet) this.students.clone());
    }
}
