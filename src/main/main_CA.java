package main;

import KKRSAlgorithm.*;
import entity.*;
import loader.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class main_CA {
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

    public static void main(String[] args) throws InterruptedException, IOException {
        int[] POI_Type = {16, 12, 23, 9, 33, 58};//16,12,23,9,33,58,11,21少,16,12,23,9,33,58,4,55剪枝效率
        int k1 = 10;
        double a = 0.999999;//α
        int endIndex = 6000;
        int num6 = num5 - 1;
        if (num6 == 0) {
            num6 = 1;
        }
        //ArrayList<Find_Topk_OSSCaling.Path> Top_k_OSSCaling = OSSCaling(POI_Type,k1,endIndex);
        // ArrayList<Find_path_MTDOSR.Path> Top_k_MTDOSR = MTDOSR(POI_Type,k1);
        ArrayList<LowerBound> Top_k_A = A(POI_Type, k1, a);

        System.out.println("————————————————————————————————————————————————————————————————————————————");
        System.out.println("KSRG算法计算所消耗的时间为:" + (time2 / num6) + "毫秒");
        System.out.println("OSSCaling算法计算所消耗的时间为:" + (time_OSSCaling / num6) + "毫秒");
        System.out.println("A的对比算法(POI顺序固定)计算所消耗的时间为:" + (timeA_db / (num6)) + "毫秒");
        System.out.println("A的对比算算法(不进行优化)计算所消耗的时间为:" + (timeA_db3 / (num6)) + "毫秒");
        System.out.println("A的对比算算法(只进行网格剪枝)计算所消耗的时间为:" + (timeA / (num6)) + "毫秒");
        System.out.println("A算法(进行全部优化)计算所消耗的时间为:" + (timeA_db2 / (num6)) + "毫秒");
    }


    public static ArrayList<Find_Topk_OSSCaling.Path> OSSCaling(int[] POI_Type2, int k1, int endIndex) throws InterruptedException, IOException {

        FileReader file = null;
        FileReader file1 = null;
        ArrayList<ArrayList<Double>> c1 = new ArrayList<>();
        try {
            file1 = new FileReader("D://IDEA//POI_shuju//calcedge//calcedge1.txt");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br1 = new BufferedReader(file1);//读取文件
        try {
            String line1;
            while ((line1 = br1.readLine()) != null) {//按行读取
                String[] sp = null;
                sp = line1.split(" ");//按空格进行分割
                c1.add(new ArrayList<Double>());
                for (int i = 1; i < 4; i++) {
                    c1.get(c1.size() - 1).add(Double.valueOf(sp[i]));
                }
                c1.add(new ArrayList<Double>());
                for (int i = 1; i < 4; i++) {
                    //c[count][i] = sp[i];
                    if (i == 1) {
                        c1.get(c1.size() - 1).add(Double.valueOf(sp[2]));
                    } else if (i == 2) {
                        c1.get(c1.size() - 1).add(Double.valueOf(sp[1]));
                    } else if (i == 3) {
                        c1.get(c1.size() - 1).add(Double.valueOf(sp[3]));
                    }

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        double[][] data = new double[c1.size()][4];
        for (int i = 0; i < c1.size(); i++) {
            for (int j = 1; j < 4; j++) {
                data[i][j] = Double.parseDouble(String.valueOf(c1.get(i).get(j - 1)));
            }
        }
        int[][] data1 = new int[data.length + 1][4];
        int n1 = 0;
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < 4; j++) {
                if (j == 3) {
                    n1 = (int) (data[i][3] * 10000);
                    data1[i][3] = n1;
                } else {
                    data1[i][j] = (int) data[i][j];
                }
            }
        }
        data1[data.length][0] = 0;
        data1[data.length][1] = 0;
        data1[data.length][2] = 1;
        data1[data.length][3] = 100;
        int num1 = c1.size() + 1;
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

        int num = c1.size() + 1;
        //划分子图
        //System.out.println("1");
        try {
            file1 = new FileReader("D://IDEA//POI_shuju//calcedge//calcedge_100.txt");

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
        ArrayList<ArrayList<Integer>> SG = SG1.CreatSG_CA(num2); //存储前num2个子图的骨架图中的节点
        //______________________________________________________________________________________________
        //______________________________________________________________________________________________
        //构建POI索引POIList，存储POI的类型和数值
        Creatpoilist POIList1 = new Creatpoilist();
        POI[] POIList = POIList1.CreatPOIList_ca(ccc1, SG);
        //______________________________________________________________________________________________
        // //构建距离索引list
        Creatlist list1 = new Creatlist();
        ArrayList<ArrayList<Path>> List = list1.CreatList_CA(ccc1);
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
        //构建边界顶点索引BPList
        ArrayList<ArrayList<BpPath>> BPList = new ArrayList<>();
        for (int i = 0; i < ccc1; i++) {
            BPList.add(new ArrayList<BpPath>());
        }
        Creatbplist BPList1 = new Creatbplist();
        //System.out.println("111");
        BPList1.CreatBPList_CA(BPList, ccc1);
        //______________________________________________________________________________________________

        //计算全部点到最近的边界顶点的距离
        ArrayList<ArrayList<Integer>> PointMinBP = CreateMinBpTable.CreateMinBP_CA();


        //查找top_k
        int k = k1; //计算多少条路径
        int q = 1842; //查询点
        //int endIndex1 = 2000;
        double a = 0.2; //α
        int q_SG = 0;
        boolean flag1 = true;
        //int[] POI_Type = {11,12,16} ;//所求的POI的类型
        int[] POI_Type = POI_Type2;//36,54,50,1,6,3,9多，49,48,33,38,23,58,11少
        ArrayList<Find_Topk_OSSCaling.Path> Top_k = new ArrayList<>();
        for (int ii = 0; ii < num5; ii++) {
            Find_Topk_OSSCaling topk = new Find_Topk_OSSCaling();
            long startTime1 = System.currentTimeMillis(); //开始获取时间
            Top_k = topk.Find_Path_OSSCaling(g, q, POI_Type, ccc1, POIList, List, BPList, PointMinBP, k, endIndex);
            long endTime1 = System.currentTimeMillis(); //开始获取时间
//         if (ii != 0 || num5 == 1){
//             time_OSSCaling += endTime1 - startTime1;
//         }
            time_OSSCaling += endTime1 - startTime1;
        }
//        long startTime2 = System.currentTimeMillis(); //开始获取时间
//        ArrayList<Lower_bound> Top_k_NoOpt = topk_No.Top_k_NOOPT(g,q,q_SG,k,POI_Type,SG,List,POIList,a,BPList,PointMinBP);
//        long endTime2 = System.currentTimeMillis(); //开始获取时间
//        long time2 = endTime2 - startTime2;

        //测试图中各个兴趣点的数量以及分布情况
//        ArrayList<Integer> POI_Type1 = new ArrayList<>();
//        ArrayList<ArrayList<Integer>> POI_SGNum = new ArrayList<>();
//        for (int i = 0; i < SG.size(); i++) {
//            for (int j = 0; j < SG.get(i).size(); j++) {
//               if (POIList[SG.get(i).get(j)].POI_Type != 0){
//                   if (isnum(POIList[SG.get(i).get(j)].POI_Type,POI_Type1) == false){
//                       POI_Type1.add(POIList[SG.get(i).get(j)].POI_Type);
//                   }
//               }
//            }
//        }
//        for (int i = 0; i < POI_Type1.size(); i++) {
//            POI_SGNum.add(new ArrayList<>());
//        }
//
//        for (int ii = 0; ii < POI_Type1.size(); ii++) {
//            for (int i = 0; i < SG.size(); i++) {
//                for (int j = 0; j < SG.get(i).size(); j++) {
//                    if (POIList[SG.get(i).get(j)].POI_Type == POI_Type1.get(ii)){
//                        POI_SGNum.get(ii).add(i);
//                        break;
//                    }
//                }
//            }
//        }
//        for (int i = 0; i < POI_Type1.size(); i++) {
//            System.out.println(POI_Type1.get(i)+"所在子图的数量为："+POI_SGNum.get(i).size());
//        }


        return Top_k;

    }


    public static ArrayList<Find_path_MTDOSR.Path> MTDOSR(int[] POI_Type2, int k1) throws InterruptedException, IOException {

        FileReader file = null;
        FileReader file1 = null;
        ArrayList<ArrayList<Double>> c1 = new ArrayList<>();
        try {
            file1 = new FileReader("D://IDEA//POI_shuju//calcedge//calcedge1.txt");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br1 = new BufferedReader(file1);//读取文件
        try {
            String line1;
            while ((line1 = br1.readLine()) != null) {//按行读取
                String[] sp = null;
                sp = line1.split(" ");//按空格进行分割
                c1.add(new ArrayList<Double>());
                for (int i = 1; i < 4; i++) {
                    c1.get(c1.size() - 1).add(Double.valueOf(sp[i]));
                }
                c1.add(new ArrayList<Double>());
                for (int i = 1; i < 4; i++) {
                    //c[count][i] = sp[i];
                    if (i == 1) {
                        c1.get(c1.size() - 1).add(Double.valueOf(sp[2]));
                    } else if (i == 2) {
                        c1.get(c1.size() - 1).add(Double.valueOf(sp[1]));
                    } else if (i == 3) {
                        c1.get(c1.size() - 1).add(Double.valueOf(sp[3]));
                    }

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        double[][] data = new double[c1.size()][4];
        for (int i = 0; i < c1.size(); i++) {
            for (int j = 1; j < 4; j++) {
                data[i][j] = Double.parseDouble(String.valueOf(c1.get(i).get(j - 1)));
            }
        }
        int[][] data1 = new int[data.length + 1][4];
        int n1 = 0;
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < 4; j++) {
                if (j == 3) {
                    n1 = (int) (data[i][3] * 10000);
                    data1[i][3] = n1;
                } else {
                    data1[i][j] = (int) data[i][j];
                }
            }
        }
        data1[data.length][0] = 0;
        data1[data.length][1] = 0;
        data1[data.length][2] = 1;
        data1[data.length][3] = 100;
        int num1 = c1.size() + 1;
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

        int num = c1.size() + 1;
        //划分子图
        //System.out.println("1");
        try {
            file1 = new FileReader("D://IDEA//POI_shuju//calcedge//calcedge_100.txt");

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
        ArrayList<ArrayList<Integer>> SG = SG1.CreatSG_CA(num2); //存储前num2个子图的骨架图中的节点
        //______________________________________________________________________________________________
        //______________________________________________________________________________________________
        //构建POI索引POIList，存储POI的类型和数值
        Creatpoilist POIList1 = new Creatpoilist();
        POI[] POIList = POIList1.CreatPOIList_ca(ccc1, SG);
        //______________________________________________________________________________________________
        // //构建距离索引list
        Creatlist list1 = new Creatlist();
        ArrayList<ArrayList<Path>> List = list1.CreatList_CA(ccc1);
        //System.out.println("1");
        boolean flag = true;
        ArrayList<Integer> POI_Type_Num = new ArrayList<>();
        for (POI poi : POIList) {
            if (poi.POI_Type != 0) {
                flag = true;
                for (Integer integer : POI_Type_Num) {
                    if (poi.POI_Type == integer) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    POI_Type_Num.add(poi.POI_Type);
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
        BPList1.CreatBPList_CA(BPList, ccc1);
        //______________________________________________________________________________________________

        //计算全部点到最近的边界顶点的距离
        ArrayList<ArrayList<Integer>> PointMinBP = CreateMinBpTable.CreateMinBP_CA();


        //查找top_k
        int k = k1; //计算多少条路径
        int q = 1842; //查询点
        double a = 0.2; //α
        int q_SG = 0;
        boolean flag1 = true;
        //int[] POI_Type = {11,12,16} ;//所求的POI的类型
        int[] POI_Type = POI_Type2;//36,54,50,1,6,3,9多，49,48,33,38,23,58,11少
        ArrayList<Find_path_MTDOSR.Path> Top_k = new ArrayList<>();
        for (int ii = 0; ii < num5; ii++) {
            Find_path_MTDOSR topk = new Find_path_MTDOSR();
            Find_Topk_NoOpt topk_No = new Find_Topk_NoOpt();
            long startTime1 = System.currentTimeMillis(); //开始获取时间
            Top_k = topk.Find_Path(g, q, POI_Type, ccc1, POIList, List, BPList, PointMinBP, k);
            long endTime1 = System.currentTimeMillis(); //开始获取时间
//         if (ii != 0 || num5 == 1){
//             time2 += endTime1 - startTime1;
//         }
            time2 += endTime1 - startTime1;
        }


        return Top_k;

    }


    public static ArrayList<LowerBound> A(int[] POI_Type2, int k1, double a) throws InterruptedException, IOException {

        FileReader file1 = null;
        ArrayList<ArrayList<Double>> c1 = new ArrayList<>();
        try {
            file1 = new FileReader("D://IDEA//POI_shuju//calcedge//calcedge1.txt");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br1 = new BufferedReader(file1);//读取文件
        try {
            String line1;
            while ((line1 = br1.readLine()) != null) {//按行读取
                String[] sp = null;
                sp = line1.split(" ");//按空格进行分割
                c1.add(new ArrayList<Double>());
                for (int i = 1; i < 4; i++) {
                    c1.get(c1.size() - 1).add(Double.valueOf(sp[i]));
                }
                c1.add(new ArrayList<Double>());
                for (int i = 1; i < 4; i++) {
                    //c[count][i] = sp[i];
                    if (i == 1) {
                        c1.get(c1.size() - 1).add(Double.valueOf(sp[2]));
                    } else if (i == 2) {
                        c1.get(c1.size() - 1).add(Double.valueOf(sp[1]));
                    } else if (i == 3) {
                        c1.get(c1.size() - 1).add(Double.valueOf(sp[3]));
                    }

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        double[][] data = new double[c1.size()][4];
        for (int i = 0; i < c1.size(); i++) {
            for (int j = 1; j < 4; j++) {
                data[i][j] = Double.parseDouble(String.valueOf(c1.get(i).get(j - 1)));
            }
        }
        int[][] data1 = new int[data.length + 1][4];
        int n1 = 0;
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < 4; j++) {
                if (j == 3) {
                    n1 = (int) (data[i][3] * 10000);
                    data1[i][3] = n1;
                } else {
                    data1[i][j] = (int) data[i][j];
                }
            }
        }
        data1[data.length][0] = 0;
        data1[data.length][1] = 0;
        data1[data.length][2] = 1;
        data1[data.length][3] = 100;
        int num1 = c1.size() + 1;
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

        int num = c1.size() + 1;
        //划分子图
        //System.out.println("1");
        try {
            file1 = new FileReader("D://IDEA//POI_shuju//calcedge//calcedge_100.txt");

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
        ArrayList<ArrayList<Integer>> SG = SG1.CreatSG_CA(num2); //存储前num2个子图的骨架图中的节点
        //______________________________________________________________________________________________
        //构建边界顶点集合
        Creatbountpoint bp1 = new Creatbountpoint();
        //int[] BP = bp1.CreatBP_ca(num);

        //______________________________________________________________________________________________
        //构建POI索引POIList，存储POI的类型和数值，并给每个顶点赋予坐标
        Creatpoilist POIList1 = new Creatpoilist();
        POI[] POIList = POIList1.CreatPOIList_ca(ccc1, SG);
        //______________________________________________________________________________________________
        // //构建距离索引list
        Creatlist list1 = new Creatlist();
        ArrayList<ArrayList<Path>> List = list1.CreatList_CA(ccc1);
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
        BPList1.CreatBPList_CA(BPList, ccc1);
        //______________________________________________________________________________________________

        //计算全部点到最近的边界顶点的距离
        ArrayList<ArrayList<Integer>> PointMinBP = CreateMinBpTable.CreateMinBP_CA();

//测试数据集中的各个POI的数量
//        ArrayList<ArrayList<Integer>> POI_Num = new ArrayList<>();
//        boolean flag17 = false;
//        for (int i = 0; i < POIList.length; i++) {
//            if (POIList[i].POI_Type != 0){
//                flag17 = false;
//                for (int j = 0; j < POI_Num.size(); j++) {
//                    if (POI_Num.get(j).get(0) == POIList[i].POI_Type){
//                        POI_Num.get(j).add(i);
//                        flag17 = true;
//                    }
//                }
//                if (flag17 == false){
//                    POI_Num.add(new ArrayList<Integer>());
//                    POI_Num.get(POI_Num.size()-1).add(POIList[i].POI_Type);
//                }
//            }
//        }
//        for (int i = 0; i < POI_Num.size(); i++) {
//            System.out.println("poi类型为："+POI_Num.get(i).get(0)+",数量为："+(POI_Num.get(i).size()-1));
//        }

//     Random random = new Random();
//     for (int i = 0; i < List.size(); i++) {
//         for (int j = 0; j < List.get(i).size(); j++) {
//             for (int k = 0; k <40 ; k++) {
//                 List.get(i).get(j).path.add(random.nextInt(ccc));
//             }
//         }
//     }
//     Double num4 = (((double) RamUsageEstimator.sizeOf(List))) / (1 * 1024 * 1024 );
//     System.out.println("索引所占空间为（mb）："+num4);

        //查找top_k
        int k = k1; //计算多少条路径
        int q = 1842; //查询点
        //double a1 = a ; //α
        int q_SG = 0;
        boolean flag1 = true;
        //int[] POI_Type = {11,12,16} ;//所求的POI的类型
        int[] POI_Type = POI_Type2;
        for (int i = 0; i < SG.size(); i++) {
            for (int j = 0; j < SG.get(i).size(); j++) {
                if (SG.get(i).get(j) == q) {
                    q_SG = i;
                    flag1 = true;
                    break;
                }
            }
            if (flag1) {
                break;
            }
        }
        time2 = time2 / 2;
        ArrayList<ArrayList<Integer>> POI_Num2 = new ArrayList<>();
        ArrayList<Integer> path3 = new ArrayList<>();
        boolean flag4 = true;
        for (int i = 0; i < POIList.length; i++) {
            if (POIList[i].POI_Type != 0) {
                flag4 = true;
                for (int j = 0; j < POI_Num2.size(); j++) {
                    if (POI_Num2.get(j).get(0) == POIList[i].POI_Type) {
                        flag4 = false;
                        if (POIList[i].POI_Num < POI_Num2.get(j).get(1)) {
                            path3.clear();
                            path3.add(POI_Num2.get(j).get(0));
                            path3.add(POIList[i].POI_Num);
                            path3.add(POI_Num2.get(j).get(2));
                            POI_Num2.get(j).clear();
                            POI_Num2.get(j).addAll(path3);
                        }
                        if (POIList[i].POI_Num > POI_Num2.get(j).get(2)) {
                            path3.clear();
                            path3.add(POI_Num2.get(j).get(0));
                            path3.add(POI_Num2.get(j).get(1));
                            path3.add(POIList[i].POI_Num);
                            POI_Num2.get(j).clear();
                            POI_Num2.get(j).addAll(path3);
                        }
                    }
                }
                if (flag4) {
                    POI_Num2.add(new ArrayList<Integer>());
                    POI_Num2.get(POI_Num2.size() - 1).add(POIList[i].POI_Type);
                    POI_Num2.get(POI_Num2.size() - 1).add(POIList[i].POI_Num);
                    POI_Num2.get(POI_Num2.size() - 1).add(POIList[i].POI_Num);
                }
            }
        }
        long startTime1 = System.currentTimeMillis(); //开始获取时间
        long endTime1 = System.currentTimeMillis(); //开始获取时间
        Find_Topk_A topk = new Find_Topk_A();
        Find_Topk_A_db topk_db = new Find_Topk_A_db();
        Find_Topk_A_db2 topk_db2 = new Find_Topk_A_db2();
        Find_TopK_A_db3 topk_db3 = new Find_TopK_A_db3();
        Find_Topk_NoOpt topk_No = new Find_Topk_NoOpt();
        ArrayList<LowerBound> Top_k = new ArrayList<>();
        ArrayList<LowerBound> Top_k_db = new ArrayList<>();
        ArrayList<LowerBound> Top_k_db2 = new ArrayList<>();
        ArrayList<LowerBound> Top_k_db3 = new ArrayList<>();

        for (int ii = 0; ii < num5; ii++) {


            startTime1 = System.currentTimeMillis(); //开始获取时间
            Top_k_db3 = topk_db3.Top_k_db3(g, q, q_SG, k, POI_Type, SG, List, POIList, a, BPList, PointMinBP); //不进行优化
            endTime1 = System.currentTimeMillis(); //开始获取时间
            time5 = endTime1 - startTime1;

            startTime1 = System.currentTimeMillis(); //开始获取时间
            Top_k = topk.Top_k(g, q, q_SG, k, POI_Type, SG, List, POIList, a, BPList, PointMinBP);//只进行网格剪枝优化
            endTime1 = System.currentTimeMillis(); //开始获取时间
            time1 = endTime1 - startTime1;

            startTime1 = System.currentTimeMillis(); //开始获取时间
            Top_k_db2 = topk_db2.Top_k_db2(g, q, q_SG, k, POI_Type, SG, List, POIList, a, BPList, PointMinBP); //进行全部优化的算法
            endTime1 = System.currentTimeMillis(); //开始获取时间
            time4 = endTime1 - startTime1;

            startTime1 = System.currentTimeMillis(); //开始获取时间
            Top_k_db = topk_db.Top_k_db(g, q, q_SG, k, POI_Type, SG, List, POIList, a, BPList, PointMinBP); //POI有顺序
            endTime1 = System.currentTimeMillis(); //开始获取时间
            time3 = endTime1 - startTime1;

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

        }

        //测试数据集中的各个POI的数量
//        ArrayList<ArrayList<Integer>> POI_Num = new ArrayList<>();
//        boolean flag17 = false;
//     for (int i = 0; i < POIList.length; i++) {
//         if (POIList[i].POI_Type != 0){
//             flag17 = false;
//             for (int j = 0; j < POI_Num.size(); j++) {
//                 if (POI_Num.get(j).get(0) == POIList[i].POI_Type){
//                     POI_Num.get(j).add(i);
//                     flag17 = true;
//                 }
//             }
//             if (flag17 == false){
//                 POI_Num.add(new ArrayList<>());
//                 POI_Num.get(POI_Num.size()-1).add(POIList[i].POI_Type);
//             }
//         }
//     }
//     for (int i = 0; i < POI_Num.size(); i++) {
//         System.out.println("poi类型为："+POI_Num.get(i).get(0)+",数量为："+(POI_Num.get(i).size()-1));
//     }


        return Top_k;

    }

}