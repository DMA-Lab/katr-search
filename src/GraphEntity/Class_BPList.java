package GraphEntity;

import java.util.ArrayList;

public class Class_BPList {
    public int Target;
    public int w;
    public ArrayList<Integer> path;

    public Class_BPList() {
        this.Target = 0;
        this.path = new ArrayList<>();
        this.w = 0;
    }
}
