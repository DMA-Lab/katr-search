package baseline.ROSE;

import entity.EdegeNode;
import entity.Graph;
import entity.POI;

import java.util.*;
import java.util.concurrent.CountDownLatch;

public class Dijkstra {
    static Set<MyPath> candidatePaths = new HashSet<>();
    static List<MyPath> result = new ArrayList<>();

    public static List<MyPath> ShortestPath(Graph g, int startIndex, int endIndex, int K) throws InterruptedException {
        // 结果列表
        // 候选路径列表
        // 候选路径列表中权值最小的路径，及其对应的节点个数
        // 第一条最短路径
        // long time5= System.currentTimeMillis();//获取当前系统时间(毫秒)
        long startTime1 = System.currentTimeMillis(); //开始获取时间
        Dijkstra_1_1 pi = new Dijkstra_1_1(g, startIndex, endIndex, null);
        pi.run();
        long endTime1 = System.currentTimeMillis(); //开始获取时间
        long time2 = endTime1 - startTime1;
        // System.out.println("DJ总共花费的时间为：" + time2);
        /*System.out.print("获得p1路径的执行时间为：");
        System.out.println(System.currentTimeMillis()-time5+"毫秒");*/
        result.add(pi.getP1());
        //int k = 1;
        //List < Integer > pk = pi.getP1().path;
        /*int sum = pk.size() - 1 ;
        CountDownLatch cdl = new CountDownLatch(sum);*/
        //ExecutorService executorService = Executors.newFixedThreadPool(32);
//        while (k < K) {
//            // int sum = pk.size() - 1 ;
//            final CountDownLatch cdl = new CountDownLatch(pk.size() - 1);
//            for (int i = 0; i < pk.size() - 1; i++){
//                double w1 = 0;
//                for (int j = 0; j <= i - 1; j++) {
//                    w1 += NavigationUtil.getEdgeWight(g, pk.get(j), pk.get(j + 1));
//                }
//                Dijkstra_1 path1 =  new Dijkstra_1(g, pk.get(i), endIndex, pk.subList(i, i + 2),w1,cdl,startIndex,pk,i);
//                path1.Dijkstra();
//            }
//            if (candidatePaths == null || candidatePaths.size() == 0) {
//                // 没有候选路径，则无需再继续向下求解
//                break;
//            } else {
//                // 从候选路径中选出最合适的一条，移除并加入到结果列表
//                MyPath fitPath = getFitPathFromCandidate(candidatePaths);
//                candidatePaths.remove(fitPath);
//                result.add(fitPath);
//                k++;
//                pk = fitPath.path;
//            }
//        }
        return result;
    }

    public static MyPath Dijkstra(Graph g, int startIndex, int endIndex) {
        MyPath path1 = new MyPath();
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
        // 需进行n-2轮循环
        //long time3 = System.currentTimeMillis();//获取当前系统时间(毫秒)
        int k1 = startIndex;
        while (k1 != endIndex) {
            int k = -1;
            int min = Integer.MAX_VALUE;
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
            //dist1.remove(k);
            EdegeNode g2 = (EdegeNode) Graph.point[k].firstArc;
            // 修改dist[]、path[]
            while (g2 != null) {
                int j = g2.adj_vertex;
                if (dist[j] == Integer.MAX_VALUE || dist[k] + g2.weight < dist[j]) {
                    if (dist[j] == Integer.MAX_VALUE) {
                        dist1.add(j);
                    }
                    dist[j] = dist[k] + g2.weight;
                    path[j] = k;
                }
                g2 = g2.nextEdge;
            }
            k1 = k;
        }

        if (dist[endIndex] == Double.MAX_VALUE) {

        } else {
            //MyPath result = new MyPath();
            //long startTime1 = System.currentTimeMillis(); //开始获取时间
            path1.path = getMinimumPath(g, startIndex, endIndex, path);
            //long endTime1 = System.currentTimeMillis(); //开始获取时间
            //long time2 = endTime1 - startTime1;
            //System.out.println("添加路径花费的时间为：" + time2);
            path1.weight = dist[endIndex];
            //return p1;


        }
        return path1;
    }

    public static List<Integer> getMinimumPath(Graph g, int sIndex, int tIndex, int[] path) {
        List<Integer> result = new ArrayList<>();
        Stack<Integer> stack = new Stack<>();
        stack.push(tIndex);
        int i = path[tIndex];
        while (i != -1) {
            stack.push(i);
            i = path[i];
        }
        while (!stack.isEmpty()) {
            result.add(Graph.point[stack.pop()].id);
        }
        //System.out.println();
        return result;
    }

    public static int ORCSK_Num(int[] POI_Type, POI[] POIList) {
        int num = 0;
        if (POI_Type.length == 2) {
            num = 1;
        } else if (POI_Type.length == 4) {
            num = 2;
        } else if (POI_Type.length == 6) {
            num = POI_Type.length;
        } else {
            num = (POI_Type.length);
        }

        return num;
    }

    public static int ORCSK_POINum(int[] POI_Type, POI[] POIList) {
        int num = 0;
        if (POI_Type.length == 2) {
            num = (int) (0.01 * POIList.length);
        } else if (POI_Type.length == 4) {
            num = (int) (0.3 * POIList.length);
        } else if (POI_Type.length == 6) {
            num = (int) (1.3 * POIList.length);
        } else {
            num = (int) (3.5 * POIList.length);
        }

        return num;
    }

    public static class Dijkstra_1_1 extends Thread {
        public Graph g;
        public int startIndex;
        public int startIndex0;
        public int endIndex;
        public List<Integer> unavailableNodeIndexs;
        public MyPath p1;
        public double w1;
        public CountDownLatch cdl;
        public List<Integer> pk;
        public int i;

        public Dijkstra_1_1(Graph g, int startIndex, int endIndex, List<Integer> unavailableNodeIndexs,
                            double w1, CountDownLatch cdl, int startIndex0, List<Integer> pk, int i) {
            this.g = g;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.unavailableNodeIndexs = unavailableNodeIndexs;
            this.cdl = cdl;
            this.w1 = w1;
            this.startIndex0 = startIndex0;
            this.pk = pk;
            this.i = i;
        }

        public Dijkstra_1_1(Graph g, int startIndex, int endIndex, List<Integer> unavailableNodeIndexs) {
            this.g = g;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.unavailableNodeIndexs = unavailableNodeIndexs;
        }

        public Dijkstra_1_1(Graph g, int startIndex, int endIndex) {
            this.g = g;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.unavailableNodeIndexs = null;
        }

        public MyPath getP1() {

            return p1;
        }

        public void run() {
            //synchronized (candidatePaths){
            // System.out.println(Thread.currentThread().getName() + "线程开始执行");
            int[] set = new int[g.numPoint]; // 是否已并入集合，该点是否已找到最短路径
            // s到i的最短路径长度
            int[] dist = new int[g.numPoint];
            // s到i的最短路径上i的前一个节点编号
            int[] path = new int[g.numPoint];
            //辅助存放需要改变的点的数组
            List<Integer> dist1 = new ArrayList<>();
            int n = 0;
            int m = 0;
            EdegeNode a = new EdegeNode();
            EdegeNode aNext = new EdegeNode();
            if (unavailableNodeIndexs != null && unavailableNodeIndexs.size() != 0) {
                Integer b1 = unavailableNodeIndexs.get(0);
                Integer b2 = unavailableNodeIndexs.get(1);
                a = (EdegeNode) Graph.point[b1].firstArc;

                while (a != null) {
                    if (a.adj_vertex == b2)
                    //a.adjvex == unavailableNodeIndexs.get(endIndex))
                    {
                        //a.value = (int) Double.MAX_VALUE;
                        n = 1;
                        break;
                    } else {
                        a = a.nextEdge;
                    }
                }
                if (n == 1) {
                    m = a.weight;
                    a.weight = Integer.MAX_VALUE;
                }
            }
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
            // 需进行n-2轮循环
            //long time3 = System.currentTimeMillis();//获取当前系统时间(毫秒)
            int k1 = startIndex;
            while (k1 != endIndex) {
                int k = -1;
                int min = Integer.MAX_VALUE;
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
                //dist1.remove(k);
                EdegeNode g2 = (EdegeNode) Graph.point[k].firstArc;
                // 修改dist[]、path[]
                while (g2 != null) {
                    int j = g2.adj_vertex;
                    if (dist[j] == Integer.MAX_VALUE || dist[k] + g2.weight < dist[j]) {
                        if (dist[j] == Integer.MAX_VALUE) {
                            dist1.add(j);
                        }
                        dist[j] = dist[k] + g2.weight;
                        path[j] = k;
                    }
                    g2 = g2.nextEdge;
                }
                k1 = k;
            }
               /* System.out.print("p1循环的执行时间为：");
                System.out.println(System.currentTimeMillis()-time3+"毫秒");*/
            if (unavailableNodeIndexs != null && unavailableNodeIndexs.size() != 0) {
                if (n == 1) {

                    a.weight = m;
                }
            }
            if (dist[endIndex] == Double.MAX_VALUE) {
                // 说明没有最短路径，两点不连通
                //System.out.println("两点之间不连通");
                // return null;
            } else {
                MyPath result = new MyPath();
                //long startTime1 = System.currentTimeMillis(); //开始获取时间
                result.path = getMinimumPath(g, startIndex, endIndex, path);
                //long endTime1 = System.currentTimeMillis(); //开始获取时间
                //long time2 = endTime1 - startTime1;
                //System.out.println("添加路径花费的时间为：" + time2);
                result.weight = dist[endIndex];
                //return p1;

                p1 = result;
            }
            //System.out.println("p1 = "+p1);
            if (unavailableNodeIndexs != null && unavailableNodeIndexs.size() != 0) {
                MyPath path1 = new MyPath();

                List<Integer> tempPath = new ArrayList<>(pk.subList(0, i));

                tempPath.addAll(p1.path);
                path1.path = tempPath;
                path1.weight = p1.weight + w1;
                p1 = path1;
                if (!candidatePaths.contains(path1) && !result.contains(path1)) {
                    addCandidatePaths(path1);
                    //candidatePaths.add( path1 );
                }
            }
            if (unavailableNodeIndexs != null && unavailableNodeIndexs.size() != 0) {
                //System.out.println("第"+i+"个子程序运行完成");
                cdl.countDown();

                //System.out.println(Thread.currentThread().getName() + "线程结束执行");
            }
            //}
        }

        private void addCandidatePaths(MyPath p1) {
            candidatePaths.add(p1);
        }

        public List<Integer> getMinimumPath(Graph g, int sIndex, int tIndex, int[] path) {
            List<Integer> result = new ArrayList<>();
            Stack<Integer> stack = new Stack<>();
            stack.push(tIndex);
            int i = path[tIndex];
            while (i != -1) {
                stack.push(i);
                i = path[i];
            }
            while (!stack.isEmpty()) {
                result.add(Graph.point[stack.pop()].id);
            }
            //System.out.println();
            return result;
        }
    }

    public static class MyPath {
        // 路径上的各个节点对应的数组下标（从起点到终点）
        public List<Integer> path;
        // 路径总权值
        public double weight;

        // 路径上节点个数：通过path.size()得到
        public MyPath() {
            this.path = new ArrayList<>();
            this.weight = 0;
        }

        public MyPath(List<Integer> path, double weight) {
            this.path = path;
            this.weight = weight;
        }

        public MyPath(MyPath path1) {
            this.path = path1.path;
            this.weight = path1.weight;
        }

        @Override
        public String toString() {
            return "MyPath{" +
                    "path=" + path +
                    ", weight=" + weight +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MyPath path1 = (MyPath) o;
            return Objects.equals(path, path1.path);
        }

        @Override
        public int hashCode() {
            int result;
            long temp;
            result = path != null ? path.hashCode() : 0;
            temp = Double.doubleToLongBits(weight);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            return result;
        }
    }

}
