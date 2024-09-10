package loader;

import entity.Graph;

import java.io.*;
import java.util.ArrayList;

public class IntoData {

    public static Graph loadGraph(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        int numVertices;
        int numEdges;

        Graph graph = null;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("c")) {
                continue;
            } else if (line.startsWith("p")) {
                // Problem line
                String[] parts = line.split(" ");
                numVertices = Integer.parseInt(parts[2]);
                numEdges = Integer.parseInt(parts[3]);
                graph = new Graph(numVertices, numEdges);
            } else if (line.startsWith("a")) {
                // Edge line
                String[] parts = line.split(" ");
                int vertex1 = Integer.parseInt(parts[1]);
                int vertex2 = Integer.parseInt(parts[2]);
                int weight = Integer.parseInt(parts[3]);
                assert graph != null;
                graph.addEdge(vertex1, vertex2, weight);
            }
        }
        reader.close();
        return graph;
    }

    public static void CreatData(int[] selectedPoiType, DataSuite dataSuite) throws IOException {
        Graph graph = IntoData.loadGraph("/mnt/lab/everyone/share/数据集/9th DIMACS Implementation Challenge - Shortest Paths/USA-road-t.NY.gr");
        dataSuite.graph = graph;

        int vertexCount = graph.vertexCount;
        int edgeCount = graph.edgeCount;

//        //划分子图
//        FileReader file = new FileReader("/mnt/lab/everyone/share/DATA/USA-road-t.NY.gr/AHP/nyJiaquan.txt.part_2000.txt");
//        BufferedReader br2 = new BufferedReader(file);//读取文件
//        String[] Subgraph = new String[vertexCount];
//
//        String line;
//        int count = 0;
//        while ((line = br2.readLine()) != null) {//按行读取
//            Subgraph[count++] = line;
//        }

        //______________________________________________________________________________________________
        //将各个点放入对应的子图中
        int subgraphLimit = 200; // 存储多少个子图的骨架图中的节点
        CreateSubgraph subgraph = new CreateSubgraph();
        dataSuite.subgraphs = subgraph.load_NY(subgraphLimit); //存储前num2个子图的骨架图中的节点

        //______________________________________________________________________________________________
        //构建Poi索引PoiList，存储Poi的类型和数值，并给每个顶点赋予坐标
        CreatePoiList poiList = new CreatePoiList();
        dataSuite.poiList = poiList.loadPoiNY(vertexCount, dataSuite.subgraphs);
        //______________________________________________________________________________________________
        // //构建距离索引list
        CreateList list1 = new CreateList();
        dataSuite.paths = list1.CreatList_NY(vertexCount);

        boolean flag;
        ArrayList<Integer> Poi_Type_Num = new ArrayList<>();
        for (int i = 0; i < dataSuite.poiList.length; i++) {
            if (dataSuite.poiList[i].poiType != 0) {
                flag = true;
                for (Integer integer : Poi_Type_Num) {
                    if (dataSuite.poiList[i].poiType == integer) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    Poi_Type_Num.add(dataSuite.poiList[i].poiType);
                }
            }
        }
        //______________________________________________________________________________________________
        // //构建边界顶点索引BPList
        dataSuite.BPList = new ArrayList<>();
        for (int i = 0; i < vertexCount; i++) {
            dataSuite.BPList.add(new ArrayList<>());
        }
        CreateBpList BPList1 = new CreateBpList();

        BPList1.CreatBPList_NY(dataSuite.BPList, vertexCount);
        //______________________________________________________________________________________________

        //计算全部点到最近的边界顶点的距离
        dataSuite.PointMinBP = CreateMinBpTable.CreatMinBP_NY();
        //______________________________________________________________________________________________
        //记录每个子图中包含哪些边界顶点
        dataSuite.BvList = new ArrayList<>();
        for (int i = 0; i < subgraphLimit + 1; i++) {
            dataSuite.BvList.add(new ArrayList<>());
        }
        for (int i = 0; i < dataSuite.poiList.length; i++) {
            if (dataSuite.poiList[i].boundary == 1) {
                dataSuite.BvList.get(dataSuite.poiList[i].subgraphId).add(i);
            }
        }

        //______________________________________________________________________________________________

        int q = 56988; //查询点
        //int[] Poi_Type = {11,12,16} ;//所求的Poi的类型
        int[] Poi_Type = selectedPoiType;
        for (int i = 0; i < dataSuite.subgraphs.size(); i++) {
            for (int j = 0; j < dataSuite.subgraphs.get(i).size(); j++) {
                if (dataSuite.subgraphs.get(i).get(j) == q) {
                    break;
                }
            }
        }
        ArrayList<ArrayList<Integer>> Poi_Num2 = new ArrayList<>();
        ArrayList<Integer> path3 = new ArrayList<>();
        boolean flag4;
        for (int i = 0; i < dataSuite.poiList.length; i++) {
            if (dataSuite.poiList[i].poiType != 0) {
                flag4 = true;
                for (ArrayList<Integer> integers : Poi_Num2) {
                    if (integers.get(0) == dataSuite.poiList[i].poiType) {
                        flag4 = false;
                        if (dataSuite.poiList[i].poiNum < integers.get(1)) {
                            path3.clear();
                            path3.add(integers.get(0));
                            path3.add(dataSuite.poiList[i].poiNum);
                            path3.add(integers.get(2));
                            integers.clear();
                            integers.addAll(path3);
                        }
                        if (dataSuite.poiList[i].poiNum > integers.get(2)) {
                            path3.clear();
                            path3.add(integers.get(0));
                            path3.add(integers.get(1));
                            path3.add(dataSuite.poiList[i].poiNum);
                            integers.clear();
                            integers.addAll(path3);
                        }
                    }
                }
                if (flag4) {
                    Poi_Num2.add(new ArrayList<>());
                    Poi_Num2.getLast().add(dataSuite.poiList[i].poiType);
                    Poi_Num2.getLast().add(dataSuite.poiList[i].poiNum);
                    Poi_Num2.getLast().add(dataSuite.poiList[i].poiNum);
                }
            }
        }
    }
}
