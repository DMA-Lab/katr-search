package main;

import preprocessing.GraphPreprocessing;
import preprocessing.IntoData;
import baseline.KATR.FindFirstTopK;
import baseline.KATR.FindTopK;
import baseline.KATR.FindTopK_SToE;
import entity.LowerBound;

import java.io.IOException;
import java.util.ArrayList;

public class KATR {
    static long timeKATR = 0;
    static long timeAllHeatTo1 = 0;
    static long timeSToE = 0;

    static ArrayList<Long> timeKATR1 = new ArrayList<>();
    static ArrayList<Long> timeSToE1 = new ArrayList<>();
    static ArrayList<Long> timeAllHeatTo11 = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException, IOException {
        // 43,25,14,28,19,26,48,47时间，43,25,5,18,19,26,48,47剪枝效率
        int[] Poi_Type = {43, 25};
        // 想取的结果数
        int k = 20;
        double alpha = 0.5;
        int num = 1; //循环次数

        GraphPreprocessing GraphPreprocessing = new GraphPreprocessing();
        IntoData.CreatData(Poi_Type, k, GraphPreprocessing);

        ArrayList<LowerBound> Top_k_KATR_SToE = KATR_SToE(Poi_Type, k, alpha, GraphPreprocessing, num);
        ArrayList<LowerBound> Top_k_KATR = KATR(Poi_Type, k, alpha, GraphPreprocessing, num);
        ArrayList<LowerBound> Top_k_KATRAllHeatTo1 = KATR_AllHeatTo1(Poi_Type, k, alpha, GraphPreprocessing, num);

        timeKATR = timeKATR1.getFirst();
        timeSToE = timeSToE1.getFirst();
        timeAllHeatTo1 = timeAllHeatTo11.getFirst();

        System.out.println("————————————————————————————————————————————————————————————————————————————");
        System.out.println("KATR查询所消耗的时间为:" + timeKATR + "毫秒");
        System.out.println("KATRAllHeatTo1查询所消耗的时间为:" + timeAllHeatTo1 + "毫秒");
        System.out.println("KATRSToE查询所消耗的时间为:" + timeSToE + "毫秒");
    }

    public static ArrayList<LowerBound> KATR(int[] Poi_Type2, int k, double a, GraphPreprocessing GraphPreprocessing, int num) {
        int vQ = 56988; // 查询点
        int q_SG = 0;
        boolean flag1 = true;
        //int[] Poi_Type = {11,12,16} ;//所求的Poi的类型
        for (int i = 0; i < GraphPreprocessing.SG.size(); i++) {
            for (int j = 0; j < GraphPreprocessing.SG.get(i).size(); j++) {
                if (GraphPreprocessing.SG.get(i).get(j) == vQ) {
                    q_SG = i;
                    flag1 = true;
                    break;
                }
            }
            if (flag1) {
                break;
            }
        }
        ArrayList<ArrayList<Integer>> Poi_Num2 = new ArrayList<>();
        ArrayList<Integer> path3 = new ArrayList<>();
        boolean flag4 = true;
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
                    Poi_Num2.add(new ArrayList<Integer>());
                    Poi_Num2.getLast().add(GraphPreprocessing.PoiList[i].Poi_Type);
                    Poi_Num2.getLast().add(GraphPreprocessing.PoiList[i].Poi_Num);
                    Poi_Num2.getLast().add(GraphPreprocessing.PoiList[i].Poi_Num);
                }
            }
        }

        ArrayList<LowerBound> topK = new ArrayList<>();
        FindTopK topk = new FindTopK();

        for (int i = 0; i < num; i++) {
            long startTime1 = System.currentTimeMillis(); //开始获取时间
            topK = topk.KATRFindTopk(GraphPreprocessing.graph, vQ, q_SG, k, Poi_Type2, GraphPreprocessing.SG, GraphPreprocessing.List, GraphPreprocessing.PoiList, a,
                    GraphPreprocessing.BPList, GraphPreprocessing.PointMinBP, GraphPreprocessing.BvList); //进行全部优化的算法
            long endTime1 = System.currentTimeMillis(); //开始获取时间
            timeKATR1.add((endTime1 - startTime1));
            topK.clear();
        }
//        long startTime1 = System.currentTimeMillis(); //开始获取时间
//        topK = topk.KATRFindTopk(GraphData.g,vQ,q_SG,k,Poi_Type,GraphData.SG,GraphData.List,GraphData.PoiList,a,
//                                  GraphData.BPList,GraphData.PointMinBP,GraphData.BvList); //进行全部优化的算法
////        System.out.println("KATR算法完成");
//        long endTime1 = System.currentTimeMillis(); //开始获取时间
//        timeKATR = endTime1 - startTime1;
        return topK;
    }

    public static ArrayList<LowerBound> KATR_AllHeatTo1(int[] Poi_Type2, int k, double a, GraphPreprocessing GraphPreprocessing, int num) {
        int q = 56988; //查询点
        int q_SG = 0;
        boolean flag1 = true;
        //int[] Poi_Type = {11,12,16} ;//所求的Poi的类型
        int[] Poi_Type = Poi_Type2;
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
        ArrayList<ArrayList<Integer>> Poi_Num2 = new ArrayList<>();
        ArrayList<Integer> path3 = new ArrayList<>();
        boolean flag4 = true;
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
                    Poi_Num2.add(new ArrayList<Integer>());
                    Poi_Num2.getLast().add(GraphPreprocessing.PoiList[i].Poi_Type);
                    Poi_Num2.getLast().add(GraphPreprocessing.PoiList[i].Poi_Num);
                    Poi_Num2.getLast().add(GraphPreprocessing.PoiList[i].Poi_Num);
                }
            }
        }

        for (int i = 0; i < GraphPreprocessing.PoiList.length; i++) {
            if (GraphPreprocessing.PoiList[i].Poi_Type != 0) {
                GraphPreprocessing.PoiList[i].Poi_Num = 1;
            }
        }

        ArrayList<LowerBound> Top_k = new ArrayList<>();
        FindFirstTopK topk = new FindFirstTopK();
        //ArrayList<Lower_bound> Top_k = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            long startTime1 = System.currentTimeMillis(); //开始获取时间
            Top_k = topk.KATRFindTopk(GraphPreprocessing.graph, q, q_SG, k, Poi_Type, GraphPreprocessing.SG, GraphPreprocessing.List, GraphPreprocessing.PoiList, a,
                    GraphPreprocessing.BPList, GraphPreprocessing.PointMinBP, GraphPreprocessing.BvList); //进行全部优化的算法
            long endTime1 = System.currentTimeMillis(); //开始获取时间
            timeAllHeatTo11.add(endTime1 - startTime1);
            Top_k.clear();
        }
        return Top_k;
    }

    public static ArrayList<LowerBound> KATR_SToE(int[] Poi_Type2, int k, double a, GraphPreprocessing GraphPreprocessing, int num) {
        int q = 56988; //查询点
        //double a1 = a ; //α
        int q_SG = 0;
        boolean flag1 = true;
        //int[] Poi_Type = {11,12,16} ;//所求的Poi的类型
        int[] Poi_Type = Poi_Type2;
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
        ArrayList<ArrayList<Integer>> Poi_Num2 = new ArrayList<>();
        ArrayList<Integer> path3 = new ArrayList<>();
        boolean flag4 = true;
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
                    Poi_Num2.add(new ArrayList<Integer>());
                    Poi_Num2.getLast().add(GraphPreprocessing.PoiList[i].Poi_Type);
                    Poi_Num2.getLast().add(GraphPreprocessing.PoiList[i].Poi_Num);
                    Poi_Num2.getLast().add(GraphPreprocessing.PoiList[i].Poi_Num);
                }
            }
        }

        int endIndex = 0;
        boolean flag = false;
        for (int i = 0; i < GraphPreprocessing.PoiList.length; i++) {
            if (GraphPreprocessing.PoiList[i].Poi_Type != 0) {
                flag = false;
                for (int value : Poi_Type) {
                    if (GraphPreprocessing.PoiList[i].Poi_Type == value) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    endIndex = i;
                }
            }
        }
        System.out.println("endIndex=" + endIndex);

        ArrayList<LowerBound> Top_k = new ArrayList<>();
        FindTopK_SToE topk = new FindTopK_SToE();
        for (int i = 0; i < num; i++) {
            long startTime1 = System.currentTimeMillis(); //开始获取时间
            Top_k = topk.KATRFindTopK(GraphPreprocessing.graph, q, endIndex, q_SG, k, Poi_Type, GraphPreprocessing.SG, GraphPreprocessing.List, GraphPreprocessing.PoiList, a,
                    GraphPreprocessing.BPList, GraphPreprocessing.PointMinBP, GraphPreprocessing.BvList); //进行全部优化的算法
            long endTime1 = System.currentTimeMillis(); //开始获取时间
            timeSToE1.add((endTime1 - startTime1));
            Top_k.clear();
        }
        return Top_k;
    }
}
