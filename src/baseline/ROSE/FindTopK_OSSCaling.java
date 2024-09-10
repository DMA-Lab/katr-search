package baseline.ROSE;

import entity.PoiPath;
import entity.Graph;
import entity.Poi;

import java.util.ArrayList;
import java.util.List;

public class FindTopK_OSSCaling {
    public static ArrayList<ArrayList<Integer>> Finf_Path1(ArrayList<ArrayList<Integer>> all,
                                                           ArrayList<ArrayList<Integer>> path3, int[] Poi_Type, int q) {
        ArrayList<ArrayList<Integer>> path = new ArrayList<>();
        for (ArrayList<Integer> integers : all) {
            //找到q所在子图中所有的符合要求的poi
            ArrayList<ArrayList<Integer>> path1 = new ArrayList<>();
            for (int i = 0; i < Poi_Type.length; i++) {
                path1.add(new ArrayList<Integer>());
                // path1.get(i).add(q);
            }
            for (int i = 0; i < Poi_Type.length; i++) {
                path1.get(i).addAll(path3.get(integers.get(i)));
            }
            //找到所有的路线
            ArrayList<ArrayList<Integer>> path2 = new ArrayList<>();
            for (int i = 0; i < path1.getFirst().size(); i++) {
                path2.add(new ArrayList<>());
                path2.get(i).add(path1.getFirst().get(i));
            }
            path.addAll(Find_Path(path1, path2, 0));
        }
        return path;

    }

    public static ArrayList<ArrayList<Integer>> Find_Path(ArrayList<ArrayList<Integer>> path1,
                                                          ArrayList<ArrayList<Integer>> path2, int k) {
        ArrayList<ArrayList<Integer>> path = new ArrayList<>();
        if (k <= path1.size() - 2) {
            int num = 0;
            for (ArrayList<Integer> integers : path2) {
                for (int j = 0; j < path1.get(k + 1).size(); j++) {
                    path.add(new ArrayList<>());
                    for (Integer integer : integers) {
                        path.get(num).add(integer);
                    }
                    path.get(num).add(path1.get(k + 1).get(j));

                    num++;
                }
            }
            path2 = Find_Path(path1, path, k + 1);
        } else if (k > path1.size() - 2) {
            return path2;
        }

        return path2;
    }

    public ArrayList<Path> Find_Path_OSSCaling(Graph g, int q, int[] Poi_Type, int ccc1, Poi[] PoiList, ArrayList<ArrayList<entity.Path>> List,
                                               ArrayList<ArrayList<PoiPath>> BPList, ArrayList<ArrayList<Integer>> PointMinBP, int k3, int endIndex) {
        int nn1 = 0;//当只需要找一条路径的时候使用这个数来代替get(0)
        ArrayList<Path> path1 = new ArrayList<>();
        path1.add(new Path());
        //创建顶点标签
        ArrayList<ArrayList<Integer>> Poi_List = new ArrayList<>();
        ArrayList<Integer> Poi_Type1 = new ArrayList<>();
        for (int k : Poi_Type) {
            Poi_Type1.add(k);
        }
        for (int i = 0; i < Poi_Type.length; i++) {
            Poi_List.add(new ArrayList<>());
        }
        boolean flag = true;
        boolean flag1 = true;
        int q_Index = q;
        int startIndex = q;

        //查找全部类型的Poi各自有那些顶顶点
        for (int i = 0; i < PoiList.length; i++) {
            for (int j = 0; j < Poi_List.size(); j++) {
                if (PoiList[i].poiType == Poi_Type[j]) {
                    Poi_List.get(j).add(i);
                }
            }
        }
        //计算这些兴趣点的平均值
        ArrayList<Dijkstra.Path> path2 = new ArrayList<>();
        int k1 = q;
        double MinWeight;
        int w = 0;
        int src = q;
        int dst;
        double w1;
        int j1 = Integer.MAX_VALUE;
        Dijkstra dijkstra = new Dijkstra();
        List<Dijkstra.Path> allPath = new ArrayList<>();
        double w_Max = Double.MAX_VALUE;
        double w_index;

        for (int i = 0; i < Poi_List.size() - 1; i++) {
            for (int j = 0; j < Poi_List.get(i).size(); j++) {
                w_index = 0;
                dst = Poi_List.get(i).get(j);
                allPath = dijkstra.ShortestPath(g, src, dst);
                w_index += allPath.getLast().length;
                if (w_index >= w_Max) {
                    allPath.removeLast();
                } else {
                    w_Max = w_index;
                }
            }
            j1 = Integer.MAX_VALUE;
            w1 = Double.MAX_VALUE;
            for (int j = 0; j < allPath.size(); j++) {
                if (allPath.get(j).length < w1) {
                    w1 = allPath.get(j).length;
                    j1 = j;
                }
            }

            src = Poi_List.get(i).get(j1);
        }

        List<Dijkstra.Path> allPath1 = new ArrayList<>();
        dijkstra.ShortestPath(g, j1, endIndex);

        return path1;
    }

    public boolean isnum(int n, ArrayList<Integer> path) {
        boolean flag = false;
        for (int j : path) {
            if (j == n) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    public List<Integer> getMinimumPath(Graph g, int sIndex, int tIndex, int[] path) {
        return Dijkstra.getMinimumPath(g, 0, tIndex, path);
    }

    public static class Path {
        public final ArrayList<Integer> Poi_Type; //兴趣点类型
        public final ArrayList<Integer> path;
        public final int OS;//顶点所在子图
        public final int BS; //顶点所在x轴子图

        public Path() {
            this.BS = 0;
            this.OS = 0;
            this.Poi_Type = new ArrayList<>();
            this.path = new ArrayList<>();

        }
    }
}
