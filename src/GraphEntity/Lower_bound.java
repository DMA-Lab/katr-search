package GraphEntity;

import java.util.ArrayList;

public class Lower_bound {
    public ArrayList<Integer> path; // 路径
    public int dis; // 路径距离
    public int w_poi; // 路径上兴趣点的兴趣值之和
    public double score; // 路径的评分

    public Lower_bound(){
        this.path = new ArrayList<Integer>();
        this.dis = 0;
        this.w_poi = 0;
        this.score = 0;
    }

    public Lower_bound(Lower_bound b){
        this.path = b.path;
        this.dis = b.dis;
        this.w_poi = b.w_poi;
        this.score = b.score;
    }
}
