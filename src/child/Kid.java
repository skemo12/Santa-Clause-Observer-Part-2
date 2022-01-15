package child;

import interfaces.SantaVisitorInterface;
import interfaces.SantaVisitable;

/**
 * Class that extends Child class, visitable by SantaVisitor, represents
 * the kid category of child.
 */
public final class Kid extends Child implements SantaVisitable {

    public Kid(final Child child) {
        super(child);
    }

    @Override
    public void accept(final SantaVisitorInterface visitor) {
        visitor.visit(this);
    }
}
