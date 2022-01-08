package interfaces;

public interface Visitable {
    /**
     * Visitable method for classes who are visited to implement
     */
    void accept(SantaVisitorInterface v);
}
