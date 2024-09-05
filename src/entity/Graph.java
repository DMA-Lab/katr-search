package entity;


public class Graph {
    public final boolean[] visited;
    public final Vertex[] vertices;
    public final int vertexCount;
    public final int edgeCount;

    public Graph(int vertexCount, int edgeCount) {
        this.vertexCount = vertexCount;
        this.edgeCount = edgeCount;
        vertices = new Vertex[vertexCount];
        visited = new boolean[vertexCount];
    }

    public void init(int vertexCount, int edgeCount, int[][] edgeMatrix)
    {
        for (int i = 0; i < vertexCount; i++) {
            visited[i] = false;
            vertices[i] = new Vertex(i);   //录入顶点的数据域
        }

        // edgeMatrix 就是从 txt 文件中读取的原始数据.
        // 第二列是边的起点，第三列是边的终点，第四列是边的权值
        // 初始化边表，链表头插法
        for (int i = 0; i < edgeCount; i++)
        {
            int v1 = edgeMatrix[i][1];
            int v2 = edgeMatrix[i][2];
            int weight = edgeMatrix[i][3];
            EdgeNode edge = new EdgeNode(v2, weight);
            edge.next = vertices[v1].firstArc;
            vertices[v1].firstArc = edge;
        }
    }
}
