package entity;

// 这个类用于邻接表，因为每一个顶点在邻接表中都存在一个指向其它顶点的指针域所以要将指针域和数据域封装成一个具体的类
public class Point<EdegeNode> {

    public int id;
    // 该点第一条边
    public EdegeNode firstArc;
    // 共有几个点
    public int num = 0;

    public Point() {}

    public Point(int id) {
        this.id = id;
        this.firstArc = null;
    }
}