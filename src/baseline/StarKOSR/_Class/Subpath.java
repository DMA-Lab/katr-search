package baseline.StarKOSR._Class;

import java.util.ArrayList;

public class Subpath {
    public int keyworkCount; //该子路线中已经保存了多少个兴趣点
    public ArrayList<Integer> subpath;
    public int subPathWeight;
    public boolean flag;

    public Subpath() {
        this.keyworkCount = 0;
        this.subpath = new ArrayList<>();
        this.subPathWeight = 0;
        this.flag = true;
    }
}
