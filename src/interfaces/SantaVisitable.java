package interfaces;

public interface SantaVisitable {
    /**
     * Visitable method for classes who are visited to implement
     */
    void accept(SantaVisitorInterface v);
}
