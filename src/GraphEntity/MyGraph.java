package GraphEntity;

import java.util.ArrayList;

//图邻接表的表示法
public class MyGraph {
    public static Point[] point;
    public int[] visted;
    public int numPoint;
    public int numEdeges;
    public MyGraph(int numPoint,int numEdeges)
    {
        this.numPoint=numPoint;
        this.numEdeges=numEdeges;
        point=new Point[numPoint];  //初始化点集数组
        visted=new int[numPoint];
    }

    public int getWeight(int v1, int v2) {
        EdegeNode p = (EdegeNode) point[v1].firstArc;
        while (p != null) {
            if (p.adjvex == v2) {
                return p.value;
            }
            p = p.nextEdge;
        }
        return 0;
    }

    public int getPathDistance(ArrayList<Integer> path) {
        int distance = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            int w = getWeight(path.get(i), path.get(i + 1));
            if (w == 0) {
//                return Integer.MAX_VALUE;
            }
            distance += w;
        }
        return distance;
    }

    public void createMyGraph(MyGraph MyGraph,int numPoint,int numEdeges,int EdegesPoint[][])  //创建图
    {
        long time3= System.currentTimeMillis();//获取当前系统时间(毫秒)
        for(int i=0;i<numPoint;i++)
        {
            MyGraph.visted[i]=0;
            //System.out.println("请输入第"+(i+1)+"顶点的数据:");
            MyGraph.point[i]=new Point(i);   //录入顶点的数据域
        }
        for(int i=0;i<numEdeges;i++)   //初始化边表,这里使用到了链表中间的头插法
        {
            EdegeNode a=new EdegeNode(EdegesPoint[i][2],EdegesPoint[i][3]);         //记录出度
            a.nextEdge= (EdegeNode) MyGraph.point[EdegesPoint[i][1]].firstArc;  //头插法
            MyGraph.point[EdegesPoint[i][1]].firstArc=a;
            MyGraph.point[EdegesPoint[i][1]].num++;

        }
        System.out.print("创建图的执行时间为：");
        System.out.println(System.currentTimeMillis()-time3+"毫秒");
    } public void createMyGraph1(MyGraph MyGraph,int numPoint,int numEdeges,int EdegesPoint[][])  //创建图
    {
        long time3= System.currentTimeMillis();//获取当前系统时间(毫秒)
        for(int i=0;i<numPoint;i++)
        {
            MyGraph.visted[i]=0;
            //System.out.println("请输入第"+(i+1)+"顶点的数据:");
            MyGraph.point[i]=new Point(i);   //录入顶点的数据域
        }
        for(int i=1;i<numEdeges+1;i++)   //初始化边表,这里使用到了链表中间的头插法
        {
            EdegeNode a=new EdegeNode(EdegesPoint[i][2],EdegesPoint[i][3]);         //记录出度
            a.nextEdge= (EdegeNode) MyGraph.point[EdegesPoint[i][1]].firstArc;  //头插法
            MyGraph.point[EdegesPoint[i][1]].firstArc=a;
            MyGraph.point[EdegesPoint[i][1]].num++;

        }
        System.out.print("创建图的执行时间为：");
        System.out.println(System.currentTimeMillis()-time3+"毫秒");
    }

//    public void createMyGraph1(MyGraph MyGraph,int numPoint,int numEdeges,float EdegesPoint[][])  //创建图
//    {
//        long time3= System.currentTimeMillis();//获取当前系统时间(毫秒)
//        for(int i=0;i<numPoint;i++)
//        {
//            MyGraph.visted[i]=0;
//            //System.out.println("请输入第"+(i+1)+"顶点的数据:");
//            MyGraph.point[i]=new Point(i);   //录入顶点的数据域
//        }
//        for(int i=0;i<numEdeges;i++)   //初始化边表,这里使用到了链表中间的头插法
//        {
//            EdegeNode a=new EdegeNode(EdegesPoint[i][2],EdegesPoint[i][3]);         //记录出度
//            a.nextEdge= (EdegeNode) MyGraph.point[EdegesPoint[i][1]].firstArc;  //头插法
//            MyGraph.point[EdegesPoint[i][1]].firstArc=a;
//            MyGraph.point[EdegesPoint[i][1]].num++;
//
//        }
//        System.out.print("创建图的执行时间为：");
//        System.out.println(System.currentTimeMillis()-time3+"毫秒");
//    }
}
