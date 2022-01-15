package child;

import interfaces.SantaVisitorInterface;
import interfaces.Visitable;

/**
 * Class that extends Child class, visitable by SantaVisitor, represents
 * the teen category of child.
 */
public final class Teen extends Child implements Visitable {

    public Teen(final Child child) {
        super(child);
    }

    @Override
    public void accept(final SantaVisitorInterface visitor) {
        visitor.visit(this);
    }
}
