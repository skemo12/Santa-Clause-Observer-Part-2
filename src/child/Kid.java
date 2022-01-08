package child;

import enums.Category;
import enums.Cities;
import interfaces.SantaVisitorInterface;
import interfaces.Visitable;

import java.util.List;

/**
 * Class that extends Child class, visitable by SantaVisitor, represents
 * the kid category of child.
 */
public final class Kid extends Child implements Visitable {

    public Kid(final Integer id, final Integer age, final Double niceScore,
               final String firstName, final String lastName, final Cities city,
               final List<Category> giftsPreferences) {
        super(id, age, niceScore, firstName, lastName, city, giftsPreferences);
    }

    public Kid(final Child child) {
        super(child);
    }

    @Override
    public void accept(final SantaVisitorInterface visitor) {
        visitor.visit(this);
    }
}
