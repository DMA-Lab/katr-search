package entity;

import java.util.ArrayList;

// 两个 Poi 之间的路径
public class PoiPath {
    public int target;
    public int weight;
    public ArrayList<Integer> points;

    public PoiPath() {
        this.target = 0;
        this.points = new ArrayList<>();
        this.weight = 0;
    }
}
