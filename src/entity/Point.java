package entity;

// 这个类用于邻接表，因为每一个顶点在邻接表中都存在一个指向其它顶点的指针域所以要将指针域和数据域封装成一个具体的类
public class Point {
    // 顶点的编号
    public int id;
    // 该点第一条边
    public EdgeNode firstArc;

    public Point(int id) {
        this.id = id;
        this.firstArc = null;
    }
}