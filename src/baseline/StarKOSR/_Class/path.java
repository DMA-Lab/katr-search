package StarKOSR._Class;

import java.util.ArrayList;

public class path {
    public int keyWordNum; //当前路线包含多少个兴趣点
    public ArrayList<Integer> path; //当前路线
    public int weight; //路线权值

    public path() {
        this.keyWordNum = 0;
        this.path = new ArrayList<>();
        this.weight = 0;
    }
}
