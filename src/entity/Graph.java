package entity;


public class Graph {
    public final Vertex[] vertices;
    public final int vertexCount;
    public final int edgeCount;

    public Graph(int vertexCount, int edgeCount) {
        this.vertexCount = vertexCount;
        this.edgeCount = edgeCount;
        this.vertices = new Vertex[vertexCount + 1];
        for (int i = 0; i < vertexCount + 1; i++) {
            vertices[i] = new Vertex(i);
        }
    }

    public Graph(int vertexCount, int edgeCount, int[][] edgeParams) {
        this(vertexCount, edgeCount);
        for (int[] edgeParam : edgeParams) {
            // TODO: 根据 main_OSSCaling.java 中的数据来看，edgeParams 似乎仅仅是一个 vertex_count * 4 的数组
            // 而不是一个临接矩阵.
            addEdge(edgeParam[0], edgeParam[1], edgeParam[2]);
        }
    }

    public void addEdge(int v1, int v2, int weight) {
        EdgeNode edge = new EdgeNode(v2, weight);
        edge.next = vertices[v1].firstArc;
        vertices[v1].firstArc = edge;
    }
}
