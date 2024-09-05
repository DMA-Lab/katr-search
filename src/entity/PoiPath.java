package entity;

import java.util.ArrayList;

// 两个 Poi 之间的路径
public class PoiPath {
    public int target;
    public int distance;

    public PoiPath() {
        this.target = 0;
        this.distance = 0;
    }
}
