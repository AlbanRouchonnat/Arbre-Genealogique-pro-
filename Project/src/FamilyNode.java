import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class FamilyNode {
    private String name;
    private Visibility visibility;
    private List<FamilyNode> children = new ArrayList<>();
    private FamilyNode parent;

    public FamilyNode(String name, Visibility visibility) {
        this.name = name;
        this.visibility = visibility;
    }

    public String getName() {
        return name;
    }

    public void addChild(FamilyNode child) {
        this.children.add(child);
        child.setParent(this);
    }

    public void setParent(FamilyNode parent) {
        this.parent = parent;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public boolean canAccess(FamilyNode requester) {
        switch (this.visibility) {
            case PUBLIC:
                return true;
            case PRIVATE:
                return this == requester;
            case PROTECTED:
                return this == requester || this.parent == requester || this.children.contains(requester) || (this.parent != null && this.parent.children.contains(requester));
            default:
                return false;
        }
    }

    @Override
    public String toString() {
        return "FamilyNode{" +
                "name='" + name + '\'' +
                ", visibility=" + visibility +
                '}';
    }
}
