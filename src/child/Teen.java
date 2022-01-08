package child;

import enums.Category;
import enums.Cities;
import enums.ElvesType;
import interfaces.SantaVisitorInterface;
import interfaces.Visitable;

import java.util.List;

/**
 * Class that extends Child class, visitable by SantaVisitor, represents
 * the teen category of child.
 */
public final class Teen extends Child implements Visitable {

    public Teen(final Integer id, final Integer age, final Double niceScore,
                final String firstName, final String lastName,
                final Cities city, final List<Category> giftsPreferences,
                final ElvesType elf, final Double niceScoreBonus) {
        super(id, age, niceScore, firstName, lastName, city, giftsPreferences,
                elf, niceScoreBonus);
    }

    public Teen(final Child child) {
        super(child);
    }

    @Override
    public void accept(final SantaVisitorInterface visitor) {
        visitor.visit(this);
    }
}
