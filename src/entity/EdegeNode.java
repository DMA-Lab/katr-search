package entity;

/// 某顶点的边使用链表存储.
/// 该结构便是一个链表的节点
public class EdegeNode {
    // 边指向的点
    public int adj_vertex;
    // 边权值
    public int weight;
    // 下一个节点对象
    public EdegeNode nextEdge;

    public EdegeNode() {}

    public EdegeNode(int adjVertex, int weight)
    {
        this.adj_vertex = adjVertex;
        this.weight = weight;
    }
}