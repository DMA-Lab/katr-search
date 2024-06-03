package CreateTxt;

import loader.Creatbplist;
import loader.Creatlist;
import loader.Creatpoilist;
import loader.Creatsg;
import entity.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CreatMinBP {

    public static void main(String[] args) throws InterruptedException, IOException {
        FileReader file1 = null;
        ArrayList<ArrayList<String>> cc = new ArrayList<>();
        try {
            file1 = new FileReader("D://IDEA//POI_shuju//COL/USA-road-d.COL.txt");

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
                cc.add(new ArrayList<String>());
                for (int i = 1; i < 4; i++) {
                    //c[count][i] = sp[i];
                    cc.get(cc.size() - 1).add(sp[i]);
                }
                //System.out.println("已经写入了第"+count+"条数据");
                count++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        int[][] data1 = new int[cc.size() + 1][4];
        for (int i = 1; i < cc.size() + 1; i++) {
            for (int j = 1; j < 4; j++) {
                data1[i][j] = Integer.parseInt(String.valueOf(cc.get(i - 1).get(j - 1)));
            }
        }

        data1[0][0] = 0;
        data1[0][1] = 0;
        data1[0][2] = 1;
        data1[0][3] = 100;
        int num1 = cc.size() + 1;
        //获取数据中共有多少点
        int ccc = 0;
        for (int i = 0; i < data1.length; i++) {
            if (data1[i][1] >= ccc) {
                ccc = data1[i][1];
            }
        }
        int ccc1 = ccc + 1;
        Graph g = new Graph(ccc1, num1);
        g.createMyGraph(g, ccc1, num1, data1);

        int num = cc.size() + 1;
        //划分子图
        //System.out.println("1");
        try {
            file1 = new FileReader("D://IDEA//POI_shuju//COL//COL_4000.txt");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br2 = new BufferedReader(file1);//读取文件
        String[] Subgraph = new String[ccc1];
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
        int num2 = 100; // 存储多少个子图的骨架图中的节点
        Creatsg SG1 = new Creatsg();
        //BufferedReader br3 = new BufferedReader(file1);//读取文件
        ArrayList<ArrayList<Integer>> SG = SG1.CreatSG_COL(num2); //存储前num2个子图的骨架图中的节点

        //______________________________________________________________________________________________
        //构建POI索引POIList，存储POI的类型和数值
        Creatpoilist POIList1 = new Creatpoilist();
        POI[] POIList = POIList1.CreatPOIList_COL(ccc1, SG);
        //______________________________________________________________________________________________
        // //构建距离索引list
        Creatlist list1 = new Creatlist();
        ArrayList<ArrayList<Path>> List = list1.CreatList_COL(num2);
        //System.out.println("1");
        boolean flag = true;
        ArrayList<Integer> POI_Type_Num = new ArrayList<>();
        for (int i = 0; i < POIList.length; i++) {
            if (POIList[i].POI_Type != 0) {
                flag = true;
                for (int j = 0; j < POI_Type_Num.size(); j++) {
                    if (POIList[i].POI_Type == POI_Type_Num.get(j)) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    POI_Type_Num.add(POIList[i].POI_Type);
                }
            }
        }
        //______________________________________________________________________________________________
        // //构建边界顶点索引BPList
        ArrayList<ArrayList<BpPath>> BPList = new ArrayList<>();
        for (int i = 0; i < ccc1; i++) {
            BPList.add(new ArrayList<BpPath>());
        }
        Creatbplist BPList1 = new Creatbplist();
        //System.out.println("111");
        BPList1.CreatBPList_COL(BPList, ccc1);
        //______________________________________________________________________________________________
        //将图中的点按照位置分组
//        ArrayList<ArrayList<Integer>> Point_BF = Creatbfpoint.Creatbfpoint();
//        //将这个分组计入POIList
//        for (int i = 0; i < Point_BF.size(); i++) {
//            for (int j = 2; j < Point_BF.get(i).size(); j++) {
//                POIList[Point_BF.get(i).get(j)].x = Point_BF.get(i).get(0);
//                POIList[Point_BF.get(i).get(j)].y = Point_BF.get(i).get(1);
//            }
//        }

        //将子图内的路径按照顶点来分类
        ArrayList<ArrayList<Path>> PointList = new ArrayList<>();
        for (int i = 0; i < BPList.size(); i++) {
            PointList.add(new ArrayList<Path>());
        }
        for (int i = 0; i < List.size(); i++) {
            for (int j = 0; j < List.get(i).size(); j++) {
                PointList.get(List.get(i).get(j).start).add(new Path());
                PointList.get(List.get(i).get(j).start).get(PointList.get(List.get(i).get(j).start).size() - 1).start = List.get(i).get(j).start;
                PointList.get(List.get(i).get(j).start).get(PointList.get(List.get(i).get(j).start).size() - 1).end = List.get(i).get(j).end;
                PointList.get(List.get(i).get(j).start).get(PointList.get(List.get(i).get(j).start).size() - 1).weight = List.get(i).get(j).weight;
                PointList.get(List.get(i).get(j).start).get(PointList.get(List.get(i).get(j).start).size() - 1).vertices.addAll(List.get(i).get(j).vertices);
            }
        }
//        int num11 = 0;
//        for (int i = 0; i < POIList.length; i++) {
//            if (POIList[i].Boundary != 0){
//                num11++;
//            }
//        }

        ArrayList<ArrayList<Integer>> MinBP = new ArrayList<>(); //存放所有点到它最近边界顶点的点编号和距离
        int min = 0;
        int minPoint = 0;
        for (int i = 0; i < PointList.size(); i++) {
            if (PointList.get(i).size() != 0) {
                min = Integer.MAX_VALUE;
                minPoint = Integer.MAX_VALUE;
                for (int k = 0; k < PointList.get(i).size(); k++) {
                    if (PointList.get(i).get(k).weight < min && BPList.get(PointList.get(i).get(k).end).size() != 0) {//BPList.get(PointList.get(i).get(k).ePoint).size()!= 0 //POIList[PointList.get(i).get(k).ePoint].Boundary != 0
                        min = PointList.get(i).get(k).weight;
                        minPoint = PointList.get(i).get(k).end;
                    }
                }
            } else {
                min = Integer.MAX_VALUE;
                minPoint = Integer.MAX_VALUE;
            }
            MinBP.add(new ArrayList<Integer>());
            MinBP.get(MinBP.size() - 1).add(minPoint);
            MinBP.get(MinBP.size() - 1).add(min);
            //System.out.println("一共需要完成的数量："+PointList.size()+",已经完成的数量："+i);
        }

        int num22 = 0;
//        for (int i = 0; i < MinBP.size(); i++) {
//            if (MinBP.get(i).get(1) < 2000000000 && MinBP.get(i).get(0) < 2000000000){
//                num22++;
//            }
//        }

        TextWriter.WriteTxt10(MinBP);
        System.out.println("1");


    }
}
