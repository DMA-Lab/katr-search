package entity;

//图邻接表的表示法
public class Graph {
    public static Point[] point;
    public int[] visited;
    public int numPoint;
    public int numbEdges;

    public Graph(int numPoint, int numbEdges) {
        this.numPoint = numPoint;
        this.numbEdges = numbEdges;
        point = new Point[numPoint];  //初始化点集数组
        visited = new int[numPoint];
    }

    public void createMyGraph(Graph MyGraph, int numPoint, int numEdeges, int[][] edgesPoints)  //创建图
    {
        long time3 = System.currentTimeMillis();//获取当前系统时间(毫秒)
        for (int i = 0; i < numPoint; i++) {
            MyGraph.visited[i] = 0;
            point[i] = new Point(i);   //录入顶点的数据域
        }
        for (int i = 0; i < numEdeges; i++)   //初始化边表,这里使用到了链表中间的头插法
        {
            EdegeNode a = new EdegeNode(edgesPoints[i][2], edgesPoints[i][3]);         //记录出度
            a.nextEdge = (EdegeNode) point[edgesPoints[i][1]].firstArc;  //头插法
            point[edgesPoints[i][1]].firstArc = a;
            point[edgesPoints[i][1]].num++;

        }
        System.out.print("创建图的执行时间为：");
        System.out.println(System.currentTimeMillis() - time3 + "毫秒");
    }
}
