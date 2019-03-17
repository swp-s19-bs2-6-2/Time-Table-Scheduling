import java.util.List;

public class StudentsGroup {
    // TODO : Probably groups can intersect which can affect its workload
    // Group name
    public String name;
    // Parent group (For example 1st year bach)
    public StudentsGroup parentGroup;
    // Subgroups
    public List <StudentsGroup> subgroups;

    public StudentsGroup(String name, StudentsGroup parentGroup) {
        this.name = name;
        this.parentGroup = parentGroup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StudentsGroup getParentGroup() {
        return parentGroup;
    }

    public void setParentGroup(StudentsGroup parentGroup) {
        this.parentGroup = parentGroup;
    }

    public void addSubGroup(String name){
        subgroups.add(new StudentsGroup(name, this));
    }

    public void removeSubGroup(String name){
        for (StudentsGroup group : subgroups){
            if (group.name.equals(name)){
                subgroups.remove(group);
            }
        }
    }

}
