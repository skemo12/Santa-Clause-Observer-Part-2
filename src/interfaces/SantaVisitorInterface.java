package interfaces;

import child.Baby;
import child.Kid;
import child.Teen;

public interface SantaVisitorInterface {
    /**
     * Visit method for Baby type of child
     */
    void visit(Baby child);
    /**
     * Visit method for Kid type of child
     */
    void visit(Kid child);
    /**
     * Visit method for Teen type of child
     */
    void visit(Teen child);

}
