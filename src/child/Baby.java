package child;

import interfaces.SantaVisitorInterface;
import interfaces.SantaVisitable;

/**
 * Class that extends Child class, visitable by SantaVisitor, represents
 * the baby category of child.
 */
public final class Baby extends Child implements SantaVisitable {

    public Baby(final Child child) {
        super(child);
    }

    @Override
    public void accept(final SantaVisitorInterface visitor) {
        visitor.visit(this);
    }
}
