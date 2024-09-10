package baseline.StarKOSR;

import baseline.ROSE.NavigationUtil;
import baseline.StarKOSR._Class.Dominance;
import baseline.StarKOSR._Class.KOSR_Point;
import baseline.StarKOSR._Class.HeadNode;
import baseline.StarKOSR._Class.Subpath;
import entity.EdgeNode;
import entity.Graph;
import entity.Poi;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.MAX_VALUE;

public class FindTopK {
    public static ArrayList<ArrayList<Integer>> KOSR(Graph g, int PointNum, int startNum, int k, Poi[] PoiList, int[] Poi_Type) {

        ArrayList<ArrayList<Integer>> topK = new ArrayList<>();
        ArrayList<ArrayList<KOSR_Point>> KOSR_Point = new ArrayList<>();
        //构建dominance，用于剪枝
        ArrayList<Dominance> dominances = new ArrayList<>();
        //初始化顶点,初始化dominance
        for (int i = 0; i < PointNum; i++) {
            KOSR_Point.add(new ArrayList<>());
            dominances.add(new Dominance(i));
        }
        int nowPoint;
        //初始化头节点
        HeadNode headnode = new HeadNode();
        //将起点作为子路径加入头节点
        Subpath subPath = new Subpath();
        subPath.subpath.add(startNum);
        headnode.next = subPath;
        //扩展子路径
        //subPath minPath;
        //int minPathLastPoint;
        ArrayList<Subpath> nowSubpaths = new ArrayList<>(); //当前的子路径
        ArrayList<Subpath> nextSubpaths = new ArrayList<>(); //下一步的子路径
        //初始化子路径
        nowSubpaths.add(new Subpath());
        nowSubpaths.getFirst().subpath.add(startNum);
        int num1 = 0;

        while (true) {
            //FindNN(KOSR_Point, minPathLastPoint, Poi_Type[minPath.keyWordNum], k, g, PoiList);

            for (Subpath nowSubpath : nowSubpaths) {
                if (nowSubpath.flag) {
                    nowPoint = nowSubpath.subpath.getLast();
                    FindNN(KOSR_Point, nowPoint, Poi_Type[nowSubpath.keyworkCount], (k), g, PoiList);
                    for (int j = 0; j < KOSR_Point.get(nowPoint).size(); j++) {
                        if (KOSR_Point.get(nowPoint).get(j).kewWordName == Poi_Type[nowSubpath.keyworkCount]) {
                            for (int l = 0; l < KOSR_Point.get(nowPoint).get(j).poi.size(); l++) {
                                nextSubpaths.add(new Subpath());
                                nextSubpaths.getLast().subpath.addAll(nowSubpath.subpath);
                                nextSubpaths.getLast().subpath.add(KOSR_Point.get(nowPoint).get(j).poi.get(l));
                                nextSubpaths.getLast().subPathWeight = nowSubpath.subPathWeight + KOSR_Point.get(nowPoint).get(j).poiWeight.get(l);
                                nextSubpaths.getLast().keyworkCount = nowSubpath.keyworkCount + 1;
                            }
                        }
                    }
                }

            }
            Dominances(dominances, nextSubpaths);
            ArrayList<Integer> needPrunePath = new ArrayList<>();
            //进行剪枝
            Subpath nowPath1;

            for (int i = 0; i < nextSubpaths.size(); i++) {
                nowPath1 = nextSubpaths.get(i);
                for (int j = 0; j < dominances.get(nowPath1.subpath.getLast()).keyWordNum.size(); j++) {
                    if (nowPath1.keyworkCount == dominances.get(nowPath1.subpath.getLast()).keyWordNum.get(j)) {
                        if (nowPath1.subPathWeight > dominances.get(nowPath1.subpath.getLast()).minPathWegh.get(j)) {
                            needPrunePath.add(i);
                        }
                    }
                }
            }

            for (Integer integer : needPrunePath) {
                num1++;
                nextSubpaths.get(integer).flag = false;
            }
            needPrunePath.clear();
            nowSubpaths.clear();
            nowSubpaths.addAll(nextSubpaths);
            nextSubpaths.clear();

            if (nowSubpaths.getFirst().keyworkCount == Poi_Type.length) {
                int minWeight;
                int Num = 0;
                for (int i = 0; i < k; i++) {
                    minWeight = MAX_VALUE;
                    for (int j = 0; j < nowSubpaths.size(); j++) {
                        if (nowSubpaths.get(j).subPathWeight < minWeight) {
                            minWeight = nowSubpaths.get(j).subPathWeight;
                            Num = j;
                        }
                    }
                    topK.add(new ArrayList<>());
                    topK.getLast().addAll(nowSubpaths.get(Num).subpath);
                    nowSubpaths.remove(Num);
                }
                System.out.println("KOSR剪枝的数量为：" + num1);
                break;
            }
        }
        return topK;
    }


    public static void FindNN(ArrayList<ArrayList<KOSR_Point>> KOSR_Point, int startIndex, int keyWordName, int k2, Graph g,
                              Poi[] PoiList) {
        ArrayList<KOSR_Point> kosr_points = KOSR_Point.get(startIndex);
        kosr_points.add(new KOSR_Point(keyWordName));

        int[] set = new int[g.vertexCount]; // 是否已并入集合，该点是否已找到最短路径
        // s到i的最短路径长度
        int[] dist = new int[g.vertexCount];
        // s到i的最短路径上i的前一个节点编号
        //int[] path = new int[g.numPoint];
        List<Integer> dist1 = new ArrayList<>();
        int n = 0;
        int m = 0;
        EdgeNode a = new EdgeNode();
        EdgeNode aNext = new EdgeNode();
        // 初始化数组
        set[startIndex] = 1;
        for (int i = 0; i < g.vertexCount; i++) {
            if (i == startIndex) { // 源点
                dist[i] = 0;
            } else {
                if (NavigationUtil.isConnected(g, startIndex, i)) {
                    dist[i] = (int) NavigationUtil.getWeight(g, startIndex, i);
                    dist1.add(i);

                } else {
                    dist[i] = MAX_VALUE;
                }
            }
        }


        // 需进行n-2轮循环
        //long time3 = System.currentTimeMillis();//获取当前系统时间(毫秒)
        // int k1 = startIndex;
        // startTime = System.currentTimeMillis(); //开始获取时间
        EdgeNode g2;
        while (kosr_points.getLast().poi.size() < k2) {
            int k = -1;
            int min = MAX_VALUE;
            // EdegeNode g1 = g.point[k1].firstArc;
            // 找出dist[]中最小的
            for (Integer j : dist1) {
                if (dist[j] < min) {
                    min = dist[j];
                    k = j;
                }
            }
            if (k == -1) {
                // 说明从源点出发与其余节点不连通，无法再向下进行扩展
                break;
            }
            set[k] = 1; // 把节点k并入
            dist1.remove(Integer.valueOf(k));
            g2 = g.vertices[k].firstArc;
            // 修改dist[]、path[]
            while (g2 != null) {
                int j = g2.adjVertex;
                if (dist[j] == MAX_VALUE || dist[k] + g2.weight < dist[j]) {
                    if (dist[j] == MAX_VALUE) {
                        dist1.add(j);
                    }
                    dist[j] = dist[k] + g2.weight;
                }
                g2 = g2.next;
            }
            if (PoiList[k].poiType == keyWordName) {
                kosr_points.getLast().poi.add(k);
                kosr_points.getLast().poiWeight.add(dist[k]);
            }
            // k1 = k;
        }
        // endTime = System.currentTimeMillis(); //开始获取时间
        // Time1 += endTime - startTime;
    }


    public static void Dominances(ArrayList<Dominance> dominances, ArrayList<Subpath> subpaths) {
        Subpath nowPath;
        int nowPoint;
        boolean flag1;
        for (Subpath subpath : subpaths) {
            nowPath = subpath;
            nowPoint = nowPath.subpath.getLast();
            flag1 = false;
            for (int j = 0; j < dominances.get(nowPoint).keyWordNum.size(); j++) {
                if (dominances.get(nowPoint).keyWordNum.get(j) == nowPath.keyworkCount) {
                    flag1 = true;
                    if (dominances.get(nowPoint).minPathWegh.get(j) > nowPath.subPathWeight) {
                        dominances.get(nowPoint).minPathWegh.set(j, nowPath.subPathWeight);
                        break;
                    }
                }
            }
            if (!flag1) {
                dominances.get(nowPoint).keyWordNum.add(nowPath.keyworkCount);
                dominances.get(nowPoint).minPathWegh.add(nowPath.subPathWeight);
            }
        }
    }
}
