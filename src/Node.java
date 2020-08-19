
public abstract class Node {
    protected final SeersRooftop main;

    public Node(SeersRooftop main) {
        this.main = main;
    }

    public abstract boolean validate();

    public abstract int execute();
}
