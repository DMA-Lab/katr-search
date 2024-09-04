package baseline.ROSE;

import entity.Graph;
import entity.Poi;

import java.util.ArrayList;
import java.util.List;

public class Find_path_MTDOSR {
    public ArrayList<Path> FindPath(Graph g, int q, int[] Poi_Type, Poi[] PoiList) {
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

        //查找全部类型的Poi各自有那些顶顶点
        for (int i = 0; i < PoiList.length; i++) {
            for (int j = 0; j < Poi_List.size(); j++) {
                if (PoiList[i].Poi_Type == Poi_Type[j]) {
                    Poi_List.get(j).add(i);
                }
            }
        }

        int index1 = q;
        int index2;
        double w1;
        int j1;
        List<Dijkstra.Path> allPath = new ArrayList<>();
        double w_Max = Double.MAX_VALUE;
        double w_index;

        Dijkstra dijkstra = new Dijkstra();
        for (int i = 0; i < Poi_List.size() - 1; i++) {
            for (int j = 0; j < Poi_List.get(i).size(); j++) {
                w_index = 0;
                index2 = Poi_List.get(i).get(j);

                allPath = dijkstra.ShortestPath(g, index1, index2);
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
            index1 = Poi_List.get(i).get(j1);
        }
        return path1;
    }

    public static class Path {
        public ArrayList<Integer> Poi_Type; // 兴趣点类型
        public ArrayList<Integer> path;
        public int OS; // 顶点所在子图
        public int BS; // 顶点所在x轴子图

        public Path() {
            this.BS = 0;
            this.OS = 0;
            this.Poi_Type = new ArrayList<>();
            this.path = new ArrayList<>();
        }
    }
}
