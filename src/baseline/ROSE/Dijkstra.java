package baseline.ROSE;

import entity.EdgeNode;
import entity.Graph;
import entity.Poi;

import java.util.*;
import java.util.concurrent.CountDownLatch;

public class Dijkstra {
    Set<Path> candidatePaths = new HashSet<>();
    List<Path> result = new ArrayList<>();

    public List<Path> ShortestPath(Graph g, int startIndex, int endIndex) {
        // 结果列表
        // 候选路径列表
        // 候选路径列表中权值最小的路径，及其对应的节点个数
        // 第一条最短路径
        DijkstraProcess pi = new DijkstraProcess(g, startIndex, endIndex, null);
        pi.run();

        result.add(pi.getPath());
        return result;
    }

    public static Path DoDijkstra(Graph g, int startIndex, int endIndex) {
        Path path1 = new Path();
        boolean[] visited = new boolean[g.vertexCount]; // 是否已并入集合，该点是否已找到最短路径
        // s到i的最短路径长度
        int[] dist = new int[g.vertexCount];
        // s到i的最短路径上i的前一个节点编号
        int[] path = new int[g.vertexCount];
        //辅助存放需要改变的点的数组
        List<Integer> dist1 = new ArrayList<>();

        // 初始化数组
        visited[startIndex] = true;
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
        // 需进行n-2轮循环
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
            visited[k] = true; // 把节点k并入
            dist1.remove(Integer.valueOf(k));

            EdgeNode g2 = (EdgeNode) g.vertices[k].firstArc;
            // 修改dist[]、path[]
            while (g2 != null) {
                int j = g2.adjVertex;
                if (dist[j] == Integer.MAX_VALUE || dist[k] + g2.weight < dist[j]) {
                    if (dist[j] == Integer.MAX_VALUE) {
                        dist1.add(j);
                    }
                    dist[j] = dist[k] + g2.weight;
                    path[j] = k;
                }
                g2 = g2.next;
            }
            k1 = k;
        }

        path1.path = getMinimumPath(g, startIndex, endIndex, path);
        path1.length = dist[endIndex];

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
            result.add(g.vertices[stack.pop()].id);
        }
        return result;
    }

    public static int ORCSK_Num(int[] Poi_Type, Poi[] PoiList) {
        int length = Poi_Type.length;
        return switch (length) {
            case 2 -> 1;
            case 4 -> 2;
            case 6 -> length;
            default -> length;
        };
    }

    public static int ORCSK_PoiNum(int[] Poi_Type, Poi[] PoiList) {
        int num;
        if (Poi_Type.length == 2) {
            num = (int) (0.01 * PoiList.length);
        } else if (Poi_Type.length == 4) {
            num = (int) (0.3 * PoiList.length);
        } else if (Poi_Type.length == 6) {
            num = (int) (1.3 * PoiList.length);
        } else {
            num = (int) (3.5 * PoiList.length);
        }

        return num;
    }

    public class DijkstraProcess {
        public Graph g;
        public int startIndex;
        public int endIndex;
        public List<Integer> unavailableNodeIndexs;
        public Path path;
        public double w1;
        public CountDownLatch cdl;
        public List<Integer> pk;
        public int i;

        public DijkstraProcess(Graph g, int startIndex, int endIndex, List<Integer> unavailableNodeIndexs) {
            this.g = g;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.unavailableNodeIndexs = unavailableNodeIndexs;
        }

        public Path getPath() {
            return path;
        }

        public void run() {
            int[] set = new int[g.vertexCount]; // 是否已并入集合，该点是否已找到最短路径
            // s到i的最短路径长度
            int[] dist = new int[g.vertexCount];
            // s到i的最短路径上i的前一个节点编号
            int[] previous = new int[g.vertexCount];
            //辅助存放需要改变的点的数组
            List<Integer> dist1 = new ArrayList<>();
            boolean isEdgeFound = false;
            int originalWeight = 0;
            EdgeNode currentEdge = new EdgeNode();
            if (unavailableNodeIndexs != null && !unavailableNodeIndexs.isEmpty()) {
                Integer b1 = unavailableNodeIndexs.get(0);
                Integer b2 = unavailableNodeIndexs.get(1);
                currentEdge = g.vertices[b1].firstArc;

                while (currentEdge != null) {
                    if (currentEdge.adjVertex == b2) {
                        isEdgeFound = true;
                        break;
                    } else {
                        currentEdge = currentEdge.next;
                    }
                }
                if (isEdgeFound) {
                    originalWeight = currentEdge.weight;
                    currentEdge.weight = Integer.MAX_VALUE;
                }
            }
            // 初始化数组
            set[startIndex] = 1;
            for (int i = 0; i < g.vertexCount; i++) {
                if (i == startIndex) { // 源点
                    dist[i] = 0;
                    previous[i] = -1;
                } else {
                    if (NavigationUtil.isConnected(g, startIndex, i)) {
                        dist[i] = (int) NavigationUtil.getWeight(g, startIndex, i);
                        previous[i] = startIndex;
                        dist1.add(i);

                    } else {
                        dist[i] = Integer.MAX_VALUE;
                        previous[i] = -1;
                    }
                }
            }
            // 需进行n-2轮循环
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
                dist1.remove(k);
                EdgeNode g2 = g.vertices[k].firstArc;
                // 修改dist[]、previous[]
                while (g2 != null) {
                    int j = g2.adjVertex;
                    if (dist[j] == Integer.MAX_VALUE || dist[k] + g2.weight < dist[j]) {
                        if (dist[j] == Integer.MAX_VALUE) {
                            dist1.add(j);
                        }
                        dist[j] = dist[k] + g2.weight;
                        previous[j] = k;
                    }
                    g2 = g2.next;
                }
                k1 = k;
            }

            if (unavailableNodeIndexs != null && !unavailableNodeIndexs.isEmpty()) {
                if (isEdgeFound) {
                    currentEdge.weight = originalWeight;
                }
            }
            {
                Path result = new Path();
                result.path = getMinimumPath(g, startIndex, endIndex, previous);
                result.length = dist[endIndex];
                path = result;
            }
            if (unavailableNodeIndexs != null && !unavailableNodeIndexs.isEmpty()) {
                Path path1 = new Path();

                List<Integer> tempPath = new ArrayList<>(pk.subList(0, i));

                tempPath.addAll(path.path);
                path1.path = tempPath;
                path1.length = path.length + w1;
                path = path1;
                if (!candidatePaths.contains(path1) && !result.contains(path1)) {
                    addCandidatePaths(path1);
                }
            }
            if (unavailableNodeIndexs != null && !unavailableNodeIndexs.isEmpty()) {
                cdl.countDown();
            }
        }

        private void addCandidatePaths(Path path) {
            candidatePaths.add(path);
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
                result.add(g.vertices[stack.pop()].id);
            }
            return result;
        }
    }

    public static class Path {
        // 路径上的各个节点对应的数组下标（从起点到终点）
        public List<Integer> path;
        // 路径总权值
        public double length;

        // 路径上节点个数：通过path.size()得到
        public Path() {
            this.path = new ArrayList<>();
            this.length = 0;
        }

        public Path(Path other) {
            this.path = other.path;
            this.length = other.length;
        }

        @Override
        public String toString() {
            return String.format("Path{path=%s, weight=%s}", path, length);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Path otherPath = (Path) o;
            return Objects.equals(path, otherPath.path);
        }

        @Override
        public int hashCode() {
            int result;
            result = path != null ? path.hashCode() : 0;
            result = 31 * result + Double.hashCode(length);
            return result;
        }
    }
}
