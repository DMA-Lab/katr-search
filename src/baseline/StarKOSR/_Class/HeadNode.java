package baseline.StarKOSR._Class;

public class HeadNode {
    public Subpath next;

    public HeadNode() {
        this.next = null;
    }

    public HeadNode(Subpath n) {
        this.next = n;
    }
}
