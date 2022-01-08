package data;

import child.Child;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that stores all changes from 1 year. Used for output.
 */
public final class AnnualChildren {
    private List<Child> children;

    public AnnualChildren(final List<Child> children) {
        this.setChildren(new ArrayList<>());
        for (Child child : children) {
            Child newChild = new Child(child);
            this.getChildren().add(newChild);
        }

    }

    public List<Child> getChildren() {
        return children;
    }

    public void setChildren(final List<Child> children) {
        this.children = children;
    }
}
