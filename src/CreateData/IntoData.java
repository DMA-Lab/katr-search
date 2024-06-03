package CreateData;

import loader.*;
import entity.BpPath;
import entity.Graph;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class IntoData {
    public static void CreatData(int[] POI_Type2, int k1, double a, GraphData GraphData) throws IOException {

        FileReader file = null;
        try {
            file = new FileReader("dataset/USA-road-t.NY.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int num = 1;
        BufferedReader br = new BufferedReader(file);
        String line;
        try {
            line = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            while ((line = br.readLine()) != null) {//按行读取
                num++;
            }
            file.close();
            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        //将流中的拘束存储到data[][]中
        int[][] data = new int[num + 1][4];
        FileReader file1 = null;

        //String []sp = null;
        String[][] c = new String[num][4];
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
            for (int i = 0; i < num; i++) {
                for (int j = 1; j < 4; j++) {
                    data[i][j] = Integer.parseInt(c[i][j]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        data[num][0] = 0;
        data[num][1] = 0;
        data[num][2] = 1;
        data[num][3] = 100;
        int num1 = num + 1;
        //获取数据中共有多少点
        int ccc = 0;
        for (int i = 0; i < num; i++) {
            if (data[i][1] >= ccc) {
                ccc = data[i][1];

            }

        }
        int ccc1 = ccc + 1;
        GraphData.g = new Graph(ccc1, num1);
        GraphData.g.createMyGraph(GraphData.g, ccc1, num1, data);
        //划分子图
        try {
            file1 = new FileReader("dataset/nyJiaquan.txt.part_2000.txt");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br2 = new BufferedReader(file1);//读取文件
        String[] Subgraph = new String[num];
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
        GraphData.SG = SG1.CreatSG_NY(num2); //存储前num2个子图的骨架图中的节点
        //______________________________________________________________________________________________
        //构建边界顶点集合
        Creatbountpoint bp1 = new Creatbountpoint();
        //int[] BP = bp1.CreatBP_ca(num);

        //______________________________________________________________________________________________
        //构建POI索引POIList，存储POI的类型和数值，并给每个顶点赋予坐标
        Creatpoilist POIList1 = new Creatpoilist();
        GraphData.POIList = POIList1.CreatPOIList_NY(ccc1, GraphData.SG);
        //______________________________________________________________________________________________
        // //构建距离索引list
        Creatlist list1 = new Creatlist();
        GraphData.List = list1.CreatList_NY(ccc1);
        //System.out.println("1");
        boolean flag = true;
        ArrayList<Integer> POI_Type_Num = new ArrayList<>();
        for (int i = 0; i < GraphData.POIList.length; i++) {
            if (GraphData.POIList[i].POI_Type != 0) {
                flag = true;
                for (int j = 0; j < POI_Type_Num.size(); j++) {
                    if (GraphData.POIList[i].POI_Type == POI_Type_Num.get(j)) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    POI_Type_Num.add(GraphData.POIList[i].POI_Type);
                }
            }
        }
        //______________________________________________________________________________________________
        // //构建边界顶点索引BPList
        GraphData.BPList = new ArrayList<>();
        for (int i = 0; i < ccc1; i++) {
            GraphData.BPList.add(new ArrayList<BpPath>());
        }
        Creatbplist BPList1 = new Creatbplist();
        //System.out.println("111");
        BPList1.CreatBPList_NY(GraphData.BPList, ccc1);
        //______________________________________________________________________________________________

        //计算全部点到最近的边界顶点的距离
        GraphData.PointMinBP = CreateMinBpTable.CreatMinBP_NY();
        //______________________________________________________________________________________________
        //记录每个子图中包含哪些边界顶点
        GraphData.BvList = new ArrayList<>();
        for (int i = 0; i < num2 + 1; i++) {
            GraphData.BvList.add(new ArrayList<Integer>());
        }
        for (int i = 0; i < GraphData.POIList.length; i++) {
            if (GraphData.POIList[i].Boundary == 1) {
                GraphData.BvList.get(GraphData.POIList[i].SG).add(i);
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
        for (int i = 0; i < GraphData.SG.size(); i++) {
            for (int j = 0; j < GraphData.SG.get(i).size(); j++) {
                if (GraphData.SG.get(i).get(j) == q) {
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
        for (int i = 0; i < GraphData.POIList.length; i++) {
            if (GraphData.POIList[i].POI_Type != 0) {
                flag4 = true;
                for (int j = 0; j < POI_Num2.size(); j++) {
                    if (POI_Num2.get(j).get(0) == GraphData.POIList[i].POI_Type) {
                        flag4 = false;
                        if (GraphData.POIList[i].POI_Num < POI_Num2.get(j).get(1)) {
                            path3.clear();
                            path3.add(POI_Num2.get(j).get(0));
                            path3.add(GraphData.POIList[i].POI_Num);
                            path3.add(POI_Num2.get(j).get(2));
                            POI_Num2.get(j).clear();
                            POI_Num2.get(j).addAll(path3);
                        }
                        if (GraphData.POIList[i].POI_Num > POI_Num2.get(j).get(2)) {
                            path3.clear();
                            path3.add(POI_Num2.get(j).get(0));
                            path3.add(POI_Num2.get(j).get(1));
                            path3.add(GraphData.POIList[i].POI_Num);
                            POI_Num2.get(j).clear();
                            POI_Num2.get(j).addAll(path3);
                        }
                    }
                }
                if (flag4) {
                    POI_Num2.add(new ArrayList<Integer>());
                    POI_Num2.get(POI_Num2.size() - 1).add(GraphData.POIList[i].POI_Type);
                    POI_Num2.get(POI_Num2.size() - 1).add(GraphData.POIList[i].POI_Num);
                    POI_Num2.get(POI_Num2.size() - 1).add(GraphData.POIList[i].POI_Num);
                }
            }
        }


        int i1 = 0;

    }
}
