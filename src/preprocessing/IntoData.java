package preprocessing;

import entity.Graph;
import loader.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class IntoData {

    public static void CreatData(int[] Poi_Type2, int k1, GraphPreprocessing GraphPreprocessing) throws IOException {

        FileReader file = new FileReader("dataset/USA-road-t.NY.txt");
        BufferedReader reader = new BufferedReader(file);

        // 跳过首行
        String line = reader.readLine();

        int lineNumber = 1;
        while (reader.readLine() != null) {//按行读取
            lineNumber++;
        }
        file.close();

        //将流中的拘束存储到data[][]中
        int[][] data = new int[lineNumber + 1][4];
        FileReader file1 = null;

        //String []sp = null;
        String[][] c = new String[lineNumber][4];
        try {
            file1 = new FileReader("dataset/USA-road-t.NY.txt");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert file1 != null;
        BufferedReader br1 = new BufferedReader(file1);//读取文件
        try {
            String line1;
            int count = 0;
            while ((line1 = br1.readLine()) != null) {//按行读取
                String[] sp;
                sp = line1.split(" ");//按空格进行分割
                System.arraycopy(sp, 1, c[count], 1, 3);
                count++;
            }
            for (int i = 0; i < lineNumber; i++) {
                for (int j = 1; j < 4; j++) {
                    data[i][j] = Integer.parseInt(c[i][j]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        data[lineNumber][0] = 0;
        data[lineNumber][1] = 0;
        data[lineNumber][2] = 1;
        data[lineNumber][3] = 100;
        int num1 = lineNumber + 1;
        //获取数据中共有多少点
        int ccc = 0;
        for (int i = 0; i < lineNumber; i++) {
            if (data[i][1] >= ccc) {
                ccc = data[i][1];
            }
        }
        int ccc1 = ccc + 1;
        GraphPreprocessing.graph = new Graph(ccc1, num1);
        GraphPreprocessing.graph.init(ccc1, num1, data);
        //划分子图
        try {
            file1 = new FileReader("dataset/nyJiaquan.txt.part_2000.txt");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br2 = new BufferedReader(file1);//读取文件
        String[] Subgraph = new String[lineNumber];
        try {
            String line2;
            int count = 0;
            while ((line2 = br2.readLine()) != null) {//按行读取
                Subgraph[count] = line2;
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //______________________________________________________________________________________________
        //将各个点放入对应的子图中
        int num2 = 200; // 存储多少个子图的骨架图中的节点
        CreateSubgraph SG1 = new CreateSubgraph();
        //BufferedReader br3 = new BufferedReader(file1);//读取文件
        GraphPreprocessing.SG = SG1.CreatSG_NY(num2); //存储前num2个子图的骨架图中的节点

        //______________________________________________________________________________________________
        //构建Poi索引PoiList，存储Poi的类型和数值，并给每个顶点赋予坐标
        CreatePoiList PoiList1 = new CreatePoiList();
        GraphPreprocessing.PoiList = PoiList1.CreatPoiList_NY(ccc1, GraphPreprocessing.SG);
        //______________________________________________________________________________________________
        // //构建距离索引list
        CreateList list1 = new CreateList();
        GraphPreprocessing.List = list1.CreatList_NY(ccc1);
        //System.out.println("1");
        boolean flag;
        ArrayList<Integer> Poi_Type_Num = new ArrayList<>();
        for (int i = 0; i < GraphPreprocessing.PoiList.length; i++) {
            if (GraphPreprocessing.PoiList[i].Poi_Type != 0) {
                flag = true;
                for (Integer integer : Poi_Type_Num) {
                    if (GraphPreprocessing.PoiList[i].Poi_Type == integer) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    Poi_Type_Num.add(GraphPreprocessing.PoiList[i].Poi_Type);
                }
            }
        }
        //______________________________________________________________________________________________
        // //构建边界顶点索引BPList
        GraphPreprocessing.BPList = new ArrayList<>();
        for (int i = 0; i < ccc1; i++) {
            GraphPreprocessing.BPList.add(new ArrayList<>());
        }
        CreateBpList BPList1 = new CreateBpList();
        //System.out.println("111");
        BPList1.CreatBPList_NY(GraphPreprocessing.BPList, ccc1);
        //______________________________________________________________________________________________

        //计算全部点到最近的边界顶点的距离
        GraphPreprocessing.PointMinBP = CreateMinBpTable.CreatMinBP_NY();
        //______________________________________________________________________________________________
        //记录每个子图中包含哪些边界顶点
        GraphPreprocessing.BvList = new ArrayList<>();
        for (int i = 0; i < num2 + 1; i++) {
            GraphPreprocessing.BvList.add(new ArrayList<>());
        }
        for (int i = 0; i < GraphPreprocessing.PoiList.length; i++) {
            if (GraphPreprocessing.PoiList[i].Boundary == 1) {
                GraphPreprocessing.BvList.get(GraphPreprocessing.PoiList[i].SG).add(i);
            }
        }

        //______________________________________________________________________________________________

        int q = 56988; //查询点
        //double a1 = a ; //α
        int q_SG = 0;
        boolean flag1 = true;
        //int[] Poi_Type = {11,12,16} ;//所求的Poi的类型
        int[] Poi_Type = Poi_Type2;
        for (int i = 0; i < GraphPreprocessing.SG.size(); i++) {
            for (int j = 0; j < GraphPreprocessing.SG.get(i).size(); j++) {
                if (GraphPreprocessing.SG.get(i).get(j) == q) {
                    break;
                }
            }
            if (flag1) {
                break;
            }
        }
        ArrayList<ArrayList<Integer>> Poi_Num2 = new ArrayList<>();
        ArrayList<Integer> path3 = new ArrayList<>();
        boolean flag4;
        for (int i = 0; i < GraphPreprocessing.PoiList.length; i++) {
            if (GraphPreprocessing.PoiList[i].Poi_Type != 0) {
                flag4 = true;
                for (ArrayList<Integer> integers : Poi_Num2) {
                    if (integers.get(0) == GraphPreprocessing.PoiList[i].Poi_Type) {
                        flag4 = false;
                        if (GraphPreprocessing.PoiList[i].Poi_Num < integers.get(1)) {
                            path3.clear();
                            path3.add(integers.get(0));
                            path3.add(GraphPreprocessing.PoiList[i].Poi_Num);
                            path3.add(integers.get(2));
                            integers.clear();
                            integers.addAll(path3);
                        }
                        if (GraphPreprocessing.PoiList[i].Poi_Num > integers.get(2)) {
                            path3.clear();
                            path3.add(integers.get(0));
                            path3.add(integers.get(1));
                            path3.add(GraphPreprocessing.PoiList[i].Poi_Num);
                            integers.clear();
                            integers.addAll(path3);
                        }
                    }
                }
                if (flag4) {
                    Poi_Num2.add(new ArrayList<>());
                    Poi_Num2.getLast().add(GraphPreprocessing.PoiList[i].Poi_Type);
                    Poi_Num2.getLast().add(GraphPreprocessing.PoiList[i].Poi_Num);
                    Poi_Num2.getLast().add(GraphPreprocessing.PoiList[i].Poi_Num);
                }
            }
        }
    }
}
