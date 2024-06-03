package entity;

import java.util.ArrayList;

public class LowerBound {
    /// 路径
    public ArrayList<Integer> path;
    // 路径距离
    public int dis;
    // 路径上兴趣点的兴趣值之和
    public int w_poi;
    // 路径的评分
    public double score;

    public LowerBound() {
        this.path = new ArrayList<>();
        this.dis = 0;
        this.w_poi = 0;
        this.score = 0;
    }
}
