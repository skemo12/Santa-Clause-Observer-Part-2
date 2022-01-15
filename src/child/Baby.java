package child;

import interfaces.SantaVisitorInterface;
import interfaces.Visitable;

/**
 * Class that extends Child class, visitable by SantaVisitor, represents
 * the baby category of child.
 */
public final class Baby extends Child implements Visitable {

    public Baby(final Child child) {
        super(child);
    }

    @Override
    public void accept(final SantaVisitorInterface visitor) {
        visitor.visit(this);
    }
}
