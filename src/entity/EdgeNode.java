package entity;

/// 某顶点的边使用链表存储.
/// 该结构便是一个链表的节点
public class EdgeNode {
    // 边指向的点
    public int adjVertex;
    // 边权值
    public int weight;
    // 下一个节点对象
    public EdgeNode next;

    public EdgeNode() {}

    public EdgeNode(int adjVertex, int weight) {
        this.adjVertex = adjVertex;
        this.weight = weight;
    }
}