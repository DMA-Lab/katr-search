package main.NY;

import loader.Creatbountpoint;
import loader.Creatpoilist;
import loader.Creatsg;
import GraphEntity.MyGraph;
import GraphEntity.POI;
import GraphEntity.keyWordList;
import KOSRAlgorithm.Find_Topk;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class main_KOSR {
    static long time1;
    static long time2;
    static long time3;
    static long time4;
    static long time5;
    static long time_OSSCaling;

    static long timeA;
    static long timeA_db;
    static long timeA_db2;
    static long timeA_db3;
    static int num5 = 1; //循环次数

    public static void main(String[] args) throws InterruptedException {

        int[] POI_Type = {43, 25, 5, 18, 19, 26, 48, 47};//43,25,14,28,19,26,48,47时间，43,25,5,18,19,26,48,47剪枝效率
        int k1 = 10;
        double a = 0.5;//α
        int endIndex = 6000;
        int num6 = num5 - 1;
        if (num6 == 0) {
            num6 = 1;
        }
        ArrayList<ArrayList<Integer>> Top_k_KOSR = KOSR(POI_Type, k1, a);

        System.out.println("KOSR算法消耗时间为：" + time1 + "ms");
    }


    public static ArrayList<ArrayList<Integer>> KOSR(int[] POI_Type2, int k1, double a) throws InterruptedException {

        FileReader file = null;
        try {
            file = new FileReader("D://IDEA//USA-road-t.NY.gr//USA-road-t.NY.txt");
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
            file1 = new FileReader("D://IDEA//USA-road-t.NY.gr//USA-road-t.NY.txt");

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
        MyGraph g = new MyGraph(ccc1, num1);
        g.createMyGraph(g, ccc1, num1, data);
        //划分子图
        try {
            file1 = new FileReader("D://IDEA//USA-road-t.NY.gr//AHP//nyJiaquan.txt.part_2000.txt");

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
        ArrayList<ArrayList<Integer>> SG = SG1.CreatSG_NY(num2); //存储前num2个子图的骨架图中的节点
        //______________________________________________________________________________________________
        //构建边界顶点集合
        Creatbountpoint bp1 = new Creatbountpoint();
        //int[] BP = bp1.CreatBP_ca(num);

        //______________________________________________________________________________________________
        //构建POI索引POIList，存储POI的类型和数值，并给每个顶点赋予坐标
        Creatpoilist POIList1 = new Creatpoilist();
        POI[] POIList = POIList1.CreatPOIList_NY(ccc1, SG);
        //计算全部点到最近的边界顶点的距离
//        ArrayList<ArrayList<Integer>> PointMinBP = Creat_MinBP.CreatMinBP_NY();
        ArrayList<keyWordList> keyWordList = new ArrayList<>();
        boolean flag = false;
        for (int i = 0; i < POIList.length; i++) {
            if (POIList[i].POI_Type != 0) {
                flag = false;
                for (int j = 0; j < keyWordList.size(); j++) {
                    if (keyWordList.get(j).name == POIList[i].POI_Type) {
                        keyWordList.get(j).Node.add(i);
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    keyWordList.add(new keyWordList(POIList[i].POI_Type));
                    keyWordList.get(keyWordList.size() - 1).Node.add(i);

                }
            }
        }


        //查找top_k
        int k = k1; //计算多少条路径
        int q = 56988; //查询点

        //ArrayList<Lower_bound> Top_k = new ArrayList<>();
        long startTime1 = System.currentTimeMillis(); //开始获取时间
        ArrayList<ArrayList<Integer>> topK = Find_Topk.KOSR(g, ccc1, q, k, POIList, POI_Type2);
        long endTime1 = System.currentTimeMillis(); //开始获取时间
        time1 = endTime1 - startTime1;
        //double a1 = a ; //α
//        int q_SG = 0;
//        boolean flag1 = true;
//        //int[] POI_Type = {11,12,16} ;//所求的POI的类型
//        int[] POI_Type = POI_Type2;
//        for (int i = 0; i < SG.size(); i++) {
//            for (int j = 0; j < SG.get(i).size(); j++) {
//                if (SG.get(i).get(j) == q){
//                    q_SG = i;
//                    flag1 = true;
//                    break;
//                }
//            }
//            if (flag1 == true){
//                break;
//            }
//        }
//        ArrayList<ArrayList<Integer>> POI_Num2 = new ArrayList<>();
//        ArrayList<Integer> path3 = new ArrayList<>();
//        boolean flag4 = true;
//        for (int i = 0; i < POIList.length; i++) {
//            if (POIList[i].POI_Type != 0){
//                flag4 = true;
//                for (int j = 0; j < POI_Num2.size(); j++) {
//                    if (POI_Num2.get(j).get(0) == POIList[i].POI_Type){
//                        flag4 = false;
//                        if (POIList[i].POI_Num < POI_Num2.get(j).get(1)){
//                            path3.clear();
//                            path3.add(POI_Num2.get(j).get(0));
//                            path3.add(POIList[i].POI_Num);
//                            path3.add(POI_Num2.get(j).get(2));
//                            POI_Num2.get(j).clear();
//                            POI_Num2.get(j).addAll(path3);
//                        }
//                        if (POIList[i].POI_Num > POI_Num2.get(j).get(2)){
//                            path3.clear();
//                            path3.add(POI_Num2.get(j).get(0));
//                            path3.add(POI_Num2.get(j).get(1));
//                            path3.add(POIList[i].POI_Num);
//                            POI_Num2.get(j).clear();
//                            POI_Num2.get(j).addAll(path3);
//                        }
//                    }
//                }
//                if (flag4 == true){
//                    POI_Num2.add(new ArrayList<Integer>());
//                    POI_Num2.get(POI_Num2.size()-1).add(POIList[i].POI_Type);
//                    POI_Num2.get(POI_Num2.size()-1).add(POIList[i].POI_Num);
//                    POI_Num2.get(POI_Num2.size()-1).add(POIList[i].POI_Num);
//                }
//            }
//        }


        for (int ii = 0; ii < num5; ii++) {

//
//            startTime1 = System.currentTimeMillis(); //开始获取时间
//            Top_k_db3 = topk_db3.Top_k_db3(g,q,q_SG,k,POI_Type,SG,List,POIList,a,BPList,PointMinBP); //不进行优化
//            endTime1 = System.currentTimeMillis(); //开始获取时间
//            time5 = endTime1 - startTime1;
//
//            startTime1 = System.currentTimeMillis(); //开始获取时间
//            Top_k = topk.Top_k(g,q,q_SG,k,POI_Type,SG,List,POIList,a,BPList,PointMinBP);//只进行网格剪枝优化
//            endTime1 = System.currentTimeMillis(); //开始获取时间
//            time1 = endTime1 - startTime1;
//
//            startTime1 = System.currentTimeMillis(); //开始获取时间
//            Top_k_db2 = topk_db2.Top_k_db2(g,q,q_SG,k,POI_Type,SG,List,POIList,a,BPList,PointMinBP); //进行全部优化的算法
//            endTime1 = System.currentTimeMillis(); //开始获取时间
//            time4 = endTime1 - startTime1;
//
//            startTime1 = System.currentTimeMillis(); //开始获取时间
//            //Top_k_db = topk_db.Top_k_db(g,q,q_SG,k,POI_Type,SG,List,POIList,a,BPList,PointMinBP); //POI有顺序
//            endTime1 = System.currentTimeMillis(); //开始获取时间
//            time3 = endTime1 - startTime1;

//           if (ii >= 0 || num5 == 1){
//               timeA +=  time1;
//               timeA_db += time3;
//               timeA_db2 += time4;
//               timeA_db3 += time5;
//
//           }
            timeA += time1;
            timeA_db += time3;
            timeA_db2 += time4;
            timeA_db3 += time5;
            time_OSSCaling = time4 * 23;

        }


        return topK;

    }
}
