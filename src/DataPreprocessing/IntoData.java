package DataPreprocessing;

import entity.BpPath;
import entity.Graph;
import loader.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class IntoData {

    public static void CreatData(int[] POI_Type2, int k1, double a, GraphPreprocessing GraphPreprocessing) throws IOException {

        FileReader file = new FileReader("dataset/USA-road-t.NY.txt");
        BufferedReader reader = new BufferedReader(file);

        // 跳过首行
        String line = reader.readLine();

        int lineNumber = 1;
        while ((line = reader.readLine()) != null) {//按行读取
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
        BufferedReader br1 = new BufferedReader(file1);//读取文件
        try {
            String line1;
            int count = 0;
            while ((line1 = br1.readLine()) != null) {//按行读取
                String[] sp = null;
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
        GraphPreprocessing.g = new Graph(ccc1, num1);
        GraphPreprocessing.g.createMyGraph(GraphPreprocessing.g, ccc1, num1, data);
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
        Creatsg SG1 = new Creatsg();
        //BufferedReader br3 = new BufferedReader(file1);//读取文件
        GraphPreprocessing.SG = SG1.CreatSG_NY(num2); //存储前num2个子图的骨架图中的节点
        //______________________________________________________________________________________________
        //构建边界顶点集合
        Creatbountpoint bp1 = new Creatbountpoint();
        //int[] BP = bp1.CreatBP_ca(num);

        //______________________________________________________________________________________________
        //构建POI索引POIList，存储POI的类型和数值，并给每个顶点赋予坐标
        Creatpoilist POIList1 = new Creatpoilist();
        GraphPreprocessing.POIList = POIList1.CreatPOIList_NY(ccc1, GraphPreprocessing.SG);
        //______________________________________________________________________________________________
        // //构建距离索引list
        Creatlist list1 = new Creatlist();
        GraphPreprocessing.List = list1.CreatList_NY(ccc1);
        //System.out.println("1");
        boolean flag = true;
        ArrayList<Integer> POI_Type_Num = new ArrayList<>();
        for (int i = 0; i < GraphPreprocessing.POIList.length; i++) {
            if (GraphPreprocessing.POIList[i].POI_Type != 0) {
                flag = true;
                for (Integer integer : POI_Type_Num) {
                    if (GraphPreprocessing.POIList[i].POI_Type == integer) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    POI_Type_Num.add(GraphPreprocessing.POIList[i].POI_Type);
                }
            }
        }
        //______________________________________________________________________________________________
        // //构建边界顶点索引BPList
        GraphPreprocessing.BPList = new ArrayList<>();
        for (int i = 0; i < ccc1; i++) {
            GraphPreprocessing.BPList.add(new ArrayList<BpPath>());
        }
        Creatbplist BPList1 = new Creatbplist();
        //System.out.println("111");
        BPList1.CreatBPList_NY(GraphPreprocessing.BPList, ccc1);
        //______________________________________________________________________________________________

        //计算全部点到最近的边界顶点的距离
        GraphPreprocessing.PointMinBP = CreateMinBpTable.CreatMinBP_NY();
        //______________________________________________________________________________________________
        //记录每个子图中包含哪些边界顶点
        GraphPreprocessing.BvList = new ArrayList<>();
        for (int i = 0; i < num2 + 1; i++) {
            GraphPreprocessing.BvList.add(new ArrayList<Integer>());
        }
        for (int i = 0; i < GraphPreprocessing.POIList.length; i++) {
            if (GraphPreprocessing.POIList[i].Boundary == 1) {
                GraphPreprocessing.BvList.get(GraphPreprocessing.POIList[i].SG).add(i);
            }
        }

        //______________________________________________________________________________________________


        int k = k1; //计算多少条路径
        int q = 56988; //查询点
        //double a1 = a ; //α
        int q_SG = 0;
        boolean flag1 = true;
        //int[] POI_Type = {11,12,16} ;//所求的POI的类型
        int[] POI_Type = POI_Type2;
        for (int i = 0; i < GraphPreprocessing.SG.size(); i++) {
            for (int j = 0; j < GraphPreprocessing.SG.get(i).size(); j++) {
                if (GraphPreprocessing.SG.get(i).get(j) == q) {
                    q_SG = i;
                    flag1 = true;
                    break;
                }
            }
            if (flag1) {
                break;
            }
        }
        ArrayList<ArrayList<Integer>> POI_Num2 = new ArrayList<>();
        ArrayList<Integer> path3 = new ArrayList<>();
        boolean flag4 = true;
        for (int i = 0; i < GraphPreprocessing.POIList.length; i++) {
            if (GraphPreprocessing.POIList[i].POI_Type != 0) {
                flag4 = true;
                for (ArrayList<Integer> integers : POI_Num2) {
                    if (integers.get(0) == GraphPreprocessing.POIList[i].POI_Type) {
                        flag4 = false;
                        if (GraphPreprocessing.POIList[i].POI_Num < integers.get(1)) {
                            path3.clear();
                            path3.add(integers.get(0));
                            path3.add(GraphPreprocessing.POIList[i].POI_Num);
                            path3.add(integers.get(2));
                            integers.clear();
                            integers.addAll(path3);
                        }
                        if (GraphPreprocessing.POIList[i].POI_Num > integers.get(2)) {
                            path3.clear();
                            path3.add(integers.get(0));
                            path3.add(integers.get(1));
                            path3.add(GraphPreprocessing.POIList[i].POI_Num);
                            integers.clear();
                            integers.addAll(path3);
                        }
                    }
                }
                if (flag4) {
                    POI_Num2.add(new ArrayList<Integer>());
                    POI_Num2.getLast().add(GraphPreprocessing.POIList[i].POI_Type);
                    POI_Num2.getLast().add(GraphPreprocessing.POIList[i].POI_Num);
                    POI_Num2.getLast().add(GraphPreprocessing.POIList[i].POI_Num);
                }
            }
        }
    }
}
