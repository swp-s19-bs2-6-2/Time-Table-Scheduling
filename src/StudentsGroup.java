import java.util.List;

public class StudentsGroup {
    // Group name
    public String name;
    // Parent group (For example 1st year bach)
    public StudentsGroup parentGroup;
    // Subgroups
    public List <StudentsGroup> subgroups;
    // TODO : Probably groups can intersect which can affect its workload

}
