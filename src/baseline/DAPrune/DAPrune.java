// Continuously Monitoring Optimal Routes with Collective Spatial Keywords on Road Networks
// https://ieeexplore.ieee.org/document/10189471

package DAPrune;

import KKRSAlgorithm.NavigationUtil;
import entity.Graph;
import entity.POI;

import java.util.ArrayList;
import java.util.List;

public class DAPrune {
    public static int OffsetNum = 0;

    public static KKRSAlgorithm.Dijkstra.MyPath OptimalPath(Graph g, int startIndex, int endIndex, POI[] POIList, int[] POI_Type) {
        KKRSAlgorithm.Dijkstra.MyPath pi = KKRSAlgorithm.Dijkstra.Dijkstra(g, startIndex, endIndex);
        //path = POIAlgorithm.Dijkstia.ShortestPath(g , startIndex , endIndex , 1);
        // Dijkstia.Dijkstra_1_1 pi = new Dijkstia.Dijkstra_1_1(g, startIndex, endIndex, null);
        List<Integer> pk = pi.path;
        ArrayList<KKRSAlgorithm.Dijkstra.MyPath> OffsetPath = new ArrayList<>();
        long startTime1 = System.currentTimeMillis(); //开始获取时间
        for (int i = 0; i < pk.size() - 1; i++) { //pk.size() - 1
            double w1 = 0;
            for (int j = 0; j <= i - 1; j++) {
                w1 += NavigationUtil.getEdgeWight(g, pk.get(j), pk.get(j + 1));
            }
            OffsetPath.add(new KKRSAlgorithm.Dijkstra.MyPath(FindOffsetPath(g, pk.get(i), endIndex, POI_Type, POIList, (int) w1)));

        }
        long endTime1 = System.currentTimeMillis(); //开始获取时间

        //System.out.println("OffsetTime="+(endTime1 - startTime1));
        double weight1 = Integer.MAX_VALUE;
        int pathIndex = Integer.MAX_VALUE;
        for (int i = 0; i < OffsetPath.size(); i++) {
            if (OffsetPath.get(i).weight < weight1) {
                weight1 = OffsetPath.get(i).weight;
                pathIndex = i;
            }
        }
        KKRSAlgorithm.Dijkstra.MyPath pk1;
        if (pathIndex != Integer.MAX_VALUE) {
            pk1 = OffsetPath.get(pathIndex);
        } else {
            pk1 = pi;
        }


        return pk1;
    }


    public static KKRSAlgorithm.Dijkstra.MyPath FindOffsetPath(Graph g, int startIndex, int endIndex, int[] POI_Type, POI[] POIList, int w1) {
        KKRSAlgorithm.Dijkstra.MyPath path1 = new KKRSAlgorithm.Dijkstra.MyPath();
        ArrayList<Integer> keyWord = new ArrayList<>();
        ArrayList<Integer> POI = new ArrayList<>();

        //int[] kewWord = new int[POI_Type.length];

        for (int i = 0; i < POI_Type.length; i++) { //POI_Type.length
            keyWord.add(POI_Type[i]);
        }
        int[] set = new int[g.numPoint]; // 是否已并入集合，该点是否已找到最短路径
        // s到i的最短路径长度
        int[] dist = new int[g.numPoint];
        // s到i的最短路径上i的前一个节点编号
        int[] path = new int[g.numPoint];

        //辅助存放需要改变的点的数组
        List<Integer> dist1 = new ArrayList<>();


        // 初始化数组
        set[startIndex] = 1;
        for (int i = 0; i < g.numPoint; i++) {
            if (i == startIndex) { // 源点
                dist[i] = 0;
                path[i] = -1;
            } else {
                if (NavigationUtil.isConnected(g, startIndex, i)) {
                    dist[i] = (int) NavigationUtil.getEdgeWight(g, startIndex, i);
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
//                if (POIList[k].POI_Type == keyWord.get(i)){
//                    keyWord.remove(i);
//                    POI.add(k);
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
        FindPath(startIndex, POI_Type, POIList);


//        long endTime1 = System.currentTimeMillis(); //开始获取时间
//        System.out.println("Time111="+(endTime1 - startTime1));
//        double x = 0;
//        double y = 0;
//        double w2 = w1;
//
//        //计算到达各个POI的距离
//        int x1 = POIList[startIndex].x;
//        int x2 = POIList[POI.get(0)].x;
//        x =  Math.pow((POIList[startIndex].x - POIList[POI.get(0)].x),2);
//        y =  Math.pow((POIList[startIndex].y - POIList[POI.get(0)].y),2);
//        double weight = 0;
//        weight += StrictMath.sqrt((x+y));
//        for (int i = 0; i < POI.size()-1; i++) {
//            x =  Math.pow((POIList[POI.get(i)].x - POIList[POI.get(i+1)].x),2);
//            y =  Math.pow((POIList[POI.get(i)].y - POIList[POI.get(i+1)].y),2);
//            weight += StrictMath.sqrt((x+y));
//        }
//        for (int i = 0; i < POI.size(); i++) {
//            path1.path.add(POI.get(i));
//        }
        path1.path.addAll(POI);
        // path1.weight = weight+w2;
        path1.weight = 0;
        return path1;
    }


    public static KKRSAlgorithm.Dijkstra.MyPath FindPath(int startIndex, int[] POI_Type, POI[] POIList) {
        KKRSAlgorithm.Dijkstra.MyPath path1 = new KKRSAlgorithm.Dijkstra.MyPath();
        double[] pathWeight = new double[POI_Type.length];
        for (int i = 0; i < pathWeight.length; i++) {
            pathWeight[i] = Integer.MAX_VALUE;
        }
        int[] path = new int[POI_Type.length];

        for (int i = 0; i < KKRSAlgorithm.Dijkstra.ORCSK_Num(POI_Type, POIList); i++) {
            for (int j = 0; j < KKRSAlgorithm.Dijkstra.ORCSK_POINum(POI_Type, POIList); j++) {
                if (j < POIList.length) {
                    if (POIList[j].POI_Type == POI_Type[i]) {
                        path[i] = j;
                    }
                } else {
                    if (POIList[(j / 10)].POI_Type == POI_Type[i]) {
                        path[i] = j;
                    }
                }

            }
        }

        return path1;
    }


}
