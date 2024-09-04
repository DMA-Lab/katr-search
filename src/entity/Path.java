package entity;

import java.util.ArrayList;

public class Path {
    // 起始点
    public int start;
    // 终点
    public int end;
    // 路径权重
    public int weight;
    // 路径上的顶点
    public ArrayList<Integer> vertices;

    public Path() {
        this.start = 0;
        this.end = 0;
        this.vertices = new ArrayList<>();
        this.weight = 0;
    }

    public Path(int start, int end, ArrayList<Integer> vertices) {
        this.start = start;
        this.end = end;
        this.vertices = vertices;
    }
}
