package child;

import interfaces.SantaVisitorInterface;
import interfaces.SantaVisitable;

/**
 * Class that extends Child class, visitable by SantaVisitor, represents
 * the teen category of child.
 */
public final class Teen extends Child implements SantaVisitable {

    public Teen(final Child child) {
        super(child);
    }

    @Override
    public void accept(final SantaVisitorInterface visitor) {
        visitor.visit(this);
    }
}
