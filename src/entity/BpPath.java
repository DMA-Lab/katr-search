package entity;

import java.util.ArrayList;

// 两个 POI 之间的路径
public class BpPath {
    public int target;
    public int weight;
    public ArrayList<Integer> points;

    public BpPath() {
        this.target = 0;
        this.points = new ArrayList<>();
        this.weight = 0;
    }
}
