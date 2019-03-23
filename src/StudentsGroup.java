import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StudentsGroup {
    // TODO : Probably groups can intersect which can affect its workload
    // TODO : Should we identify group uniquely by its name?

    // Group name
    public String name;
    // Parent group (For example 1st year bach)
    private List<StudentsGroup> parentGroups;
    // Subgroups
    private List<StudentsGroup> subgroups;

    public StudentsGroup(String name) {
        this.name = name;
        this.parentGroups = new ArrayList<StudentsGroup>();
        this.subgroups = new ArrayList<StudentsGroup>();
    }

    public StudentsGroup(String name, List<StudentsGroup> parentGroups, List<StudentsGroup> subgroups) {
        this.name = name;
        this.parentGroups = parentGroups;
        this.subgroups = subgroups;
    }

    // Name

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<StudentsGroup> getParentGroups() {
        return parentGroups;
    }

    // Parent groups

    public void setParentGroups(List<StudentsGroup> parentGroups) {
        this.parentGroups = parentGroups;
    }

    public void addParentGroup(StudentsGroup parent) {
        this.parentGroups.add(parent);
        parent.subgroups.add(this);
    }

    public void removeParentGroup(String name) {
        for (StudentsGroup group : subgroups) {
            if (group.name.equals(name)) {
                subgroups.remove(group);
            }
        }
    }

    // Sub groups

    public List<StudentsGroup> getSubgroups() {
        return subgroups;
    }

    public void setSubgroups(List<StudentsGroup> subgroups) {
        this.subgroups = subgroups;
    }

    public void createSubGroup(String name) {
        subgroups.add(new StudentsGroup(name, new ArrayList<StudentsGroup>(Arrays.asList(this)), new ArrayList<>()));
    }

    public void removeSubGroup(String name) {
        for (StudentsGroup group : subgroups) {
            if (group.name.equals(name)) {
                subgroups.remove(group);
            }
        }
    }
    /*public StudentsGroup clone() throws CloneNotSupportedException {
        StudentsGroup clone = (StudentsGroup) super.clone();

    }*/
}
