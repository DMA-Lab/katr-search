// Continuously Monitoring Optimal Routes with Collective Spatial Keywords on Road Networks
// https://ieeexplore.ieee.org/document/10189471

package baseline.DAPrune;

import baseline.ROSE.Dijkstra;
import baseline.ROSE.NavigationUtil;
import entity.Graph;
import entity.Poi;

import java.util.ArrayList;
import java.util.List;

public class DAPrune {
    public static int OffsetNum = 0;

    public static Dijkstra.Path OptimalPath(Graph g, int startIndex, int endIndex, Poi[] PoiList, int[] Poi_Type) {
        Dijkstra.Path pi = Dijkstra.DoDijkstra(g, startIndex, endIndex);
        //path = PoiAlgorithm.Dijkstra.ShortestPath(g , startIndex , endIndex , 1);
        // Dijkstra.Dijkstra_1_1 pi = new Dijkstra.Dijkstra_1_1(g, startIndex, endIndex, null);
        List<Integer> pk = pi.path;
        ArrayList<Dijkstra.Path> OffsetPath = new ArrayList<>();
        long startTime1 = System.currentTimeMillis(); //开始获取时间
        for (int i = 0; i < pk.size() - 1; i++) { //pk.size() - 1
            double w1 = 0;
            for (int j = 0; j <= i - 1; j++) {
                w1 += NavigationUtil.getWeight(g, pk.get(j), pk.get(j + 1));
            }
            OffsetPath.add(new Dijkstra.Path(FindOffsetPath(g, pk.get(i), endIndex, Poi_Type, PoiList, (int) w1)));

        }
        long endTime1 = System.currentTimeMillis(); //开始获取时间

        //System.out.println("OffsetTime="+(endTime1 - startTime1));
        double weight1 = Integer.MAX_VALUE;
        int pathIndex = Integer.MAX_VALUE;
        for (int i = 0; i < OffsetPath.size(); i++) {
            if (OffsetPath.get(i).length < weight1) {
                weight1 = OffsetPath.get(i).length;
                pathIndex = i;
            }
        }
        Dijkstra.Path pk1;
        if (pathIndex != Integer.MAX_VALUE) {
            pk1 = OffsetPath.get(pathIndex);
        } else {
            pk1 = pi;
        }


        return pk1;
    }


    public static Dijkstra.Path FindOffsetPath(Graph g, int startIndex, int endIndex, int[] Poi_Type, Poi[] PoiList, int w1) {
        Dijkstra.Path path1 = new Dijkstra.Path();
        ArrayList<Integer> keyWord = new ArrayList<>();
        ArrayList<Integer> Poi = new ArrayList<>();

        //int[] kewWord = new int[Poi_Type.length];

        for (int i = 0; i < Poi_Type.length; i++) { //Poi_Type.length
            keyWord.add(Poi_Type[i]);
        }
        int[] set = new int[g.vertexCount]; // 是否已并入集合，该点是否已找到最短路径
        // s到i的最短路径长度
        int[] dist = new int[g.vertexCount];
        // s到i的最短路径上i的前一个节点编号
        int[] path = new int[g.vertexCount];

        //辅助存放需要改变的点的数组
        List<Integer> dist1 = new ArrayList<>();


        // 初始化数组
        set[startIndex] = 1;
        for (int i = 0; i < g.vertexCount; i++) {
            if (i == startIndex) { // 源点
                dist[i] = 0;
                path[i] = -1;
            } else {
                if (NavigationUtil.isConnected(g, startIndex, i)) {
                    dist[i] = (int) NavigationUtil.getWeight(g, startIndex, i);
                    path[i] = startIndex;
                    dist1.add(i);

                } else {
                    dist[i] = Integer.MAX_VALUE;
                    path[i] = -1;
                }
            }
        }

        //查询全部的关键词
// -----------------------------------------------------------------------------------------------
        //int k1 = startIndex;
//        long startTime1 = System.currentTimeMillis(); //开始获取时间
//        while (keyWord.size() != 0){
//            int k = -1;
//            int min = Integer.MAX_VALUE;
//            // EdegeNode g1 = g.point[k1].firstArc;
//            // 找出dist[]中最小的
//            for (Integer j : dist1) {
//                if (dist[j] < min) {
//                    min = dist[j];
//                    k = j;
//                }
//            }
//            if (k == -1) {
//                // 说明从源点出发与其余节点不连通，无法再向下进行扩展
//                break;
//            }
//            set[k] = 1; // 把节点k并入
//            dist1.remove(new Integer(k));
//            for (int i = 0; i < keyWord.size(); i++) {
//                if (PoiList[k].Poi_Type == keyWord.get(i)){
//                    keyWord.remove(i);
//                    Poi.add(k);
//                   // OffsetNum++;
//                }
//            }
//            //dist1.remove(k);
//            EdegeNode g2 = (EdegeNode) g.point[k].firstArc;
//            // 修改dist[]、path[]
//            while (g2 != null) {
//                int j = g2.adjvex;
//                if (dist[j] == Integer.MAX_VALUE || dist[k] + g2.value < dist[j]) {
//                    if (dist[j] == Integer.MAX_VALUE) {
//                        dist1.add(j);
//                    }
//                    dist[j] = dist[k] + g2.value;
//                    path[j] = k;
//                }
//                g2 = g2.nextEdge;
//            }
//        }
// -----------------------------------------------------------------------------------------------
        FindPath(startIndex, Poi_Type, PoiList);


//        long endTime1 = System.currentTimeMillis(); //开始获取时间
//        System.out.println("Time111="+(endTime1 - startTime1));
//        double x = 0;
//        double y = 0;
//        double w2 = w1;
//
//        //计算到达各个Poi的距离
//        int x1 = PoiList[startIndex].x;
//        int x2 = PoiList[Poi.get(0)].x;
//        x =  Math.pow((PoiList[startIndex].x - PoiList[Poi.get(0)].x),2);
//        y =  Math.pow((PoiList[startIndex].y - PoiList[Poi.get(0)].y),2);
//        double weight = 0;
//        weight += StrictMath.sqrt((x+y));
//        for (int i = 0; i < Poi.size()-1; i++) {
//            x =  Math.pow((PoiList[Poi.get(i)].x - PoiList[Poi.get(i+1)].x),2);
//            y =  Math.pow((PoiList[Poi.get(i)].y - PoiList[Poi.get(i+1)].y),2);
//            weight += StrictMath.sqrt((x+y));
//        }
//        for (int i = 0; i < Poi.size(); i++) {
//            path1.path.add(Poi.get(i));
//        }
        path1.path.addAll(Poi);
        // path1.weight = weight+w2;
        path1.length = 0;
        return path1;
    }


    public static Dijkstra.Path FindPath(int startIndex, int[] Poi_Type, Poi[] PoiList) {
        Dijkstra.Path path1 = new Dijkstra.Path();
        double[] pathWeight = new double[Poi_Type.length];
        for (int i = 0; i < pathWeight.length; i++) {
            pathWeight[i] = Integer.MAX_VALUE;
        }
        int[] path = new int[Poi_Type.length];

        for (int i = 0; i < Dijkstra.ORCSK_Num(Poi_Type, PoiList); i++) {
            for (int j = 0; j < Dijkstra.ORCSK_PoiNum(Poi_Type, PoiList); j++) {
                if (j < PoiList.length) {
                    if (PoiList[j].Poi_Type == Poi_Type[i]) {
                        path[i] = j;
                    }
                } else {
                    if (PoiList[(j / 10)].Poi_Type == Poi_Type[i]) {
                        path[i] = j;
                    }
                }
            }
        }
        return path1;
    }
}
