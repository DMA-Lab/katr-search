package main;

import loader.DataSuite;
import loader.IntoData;
import katr.FindFirstTopK;
import katr.FindTopK;
import katr.FindTopK_SToE;
import entity.LowerBound;

import java.io.IOException;
import java.util.ArrayList;

public class KATR {
    static long timeKATR = 0;
    static long timeAllHeatTo1 = 0;
    static long timeSToE = 0;

    static final ArrayList<Long> timeKATR1 = new ArrayList<>();
    static final ArrayList<Long> timeSToE1 = new ArrayList<>();
    static final ArrayList<Long> timeAllHeatTo11 = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        // 43,25,14,28,19,26,48,47时间，43,25,5,18,19,26,48,47剪枝效率
        int[] Poi_Type = {43, 25};
        // 想取的结果数
        int k = 20;
        double alpha = 0.5;
        int times = 1; //循环次数

        DataSuite dataSuite = new DataSuite();
        IntoData.CreatData(Poi_Type, dataSuite);

        ArrayList<LowerBound> Top_k_KATR_SToE = KATR_SToE(Poi_Type, k, alpha, dataSuite, times);
        ArrayList<LowerBound> Top_k_KATR = KATR(Poi_Type, k, alpha, dataSuite, times);
        ArrayList<LowerBound> Top_k_KATRAllHeatTo1 = KATR_AllHeatTo1(Poi_Type, k, alpha, dataSuite, times);

        timeKATR = timeKATR1.getFirst();
        timeSToE = timeSToE1.getFirst();
        timeAllHeatTo1 = timeAllHeatTo11.getFirst();

        System.out.println("————————————————————————————————————————————————————————————————————————————");
        System.out.println("KATR查询所消耗的时间为:" + timeKATR + "毫秒");
        System.out.println("KATRAllHeatTo1查询所消耗的时间为:" + timeAllHeatTo1 + "毫秒");
        System.out.println("KATRSToE查询所消耗的时间为:" + timeSToE + "毫秒");
    }

    public static ArrayList<LowerBound> KATR(int[] Poi_Type2, int k, double alpha, DataSuite dataSuite, int times) {
        int vQ = 56988; // 查询点
        int q_SG = 0;
        //int[] Poi_Type = {11,12,16} ;//所求的Poi的类型
        for (int i = 0; i < dataSuite.subgraphs.size(); i++) {
            for (int j = 0; j < dataSuite.subgraphs.get(i).size(); j++) {
                if (dataSuite.subgraphs.get(i).get(j) == vQ) {
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

        ArrayList<LowerBound> topK = new ArrayList<>();
        FindTopK topk = new FindTopK();

        for (int i = 0; i < times; i++) {
            long startTime1 = System.currentTimeMillis(); //开始获取时间
            topK = topk.KATRFindTopk(dataSuite.graph, vQ, q_SG, k, Poi_Type2, dataSuite.subgraphs, dataSuite.paths, dataSuite.poiList, alpha,
                    dataSuite.BPList, dataSuite.PointMinBP, dataSuite.BvList); //进行全部优化的算法
            long endTime1 = System.currentTimeMillis(); //开始获取时间
            timeKATR1.add((endTime1 - startTime1));

            // 输出所有路径的得分、距离、兴趣值
            for (LowerBound lowerBound : topK) {
                System.out.println("score: " + lowerBound.score + ", dis: " + lowerBound.dis + ", totalInterest: " + lowerBound.totalInterest);
            }
            topK.clear();
        }
        return topK;
    }

    public static ArrayList<LowerBound> KATR_AllHeatTo1(int[] Poi_Type2, int k, double a, DataSuite dataSuite, int num) {
        int q = 56988; //查询点
        int q_SG = 0;
        boolean flag1 = true;
        //int[] Poi_Type = {11,12,16} ;//所求的Poi的类型
        int[] Poi_Type = Poi_Type2;
        for (int i = 0; i < dataSuite.subgraphs.size(); i++) {
            for (int j = 0; j < dataSuite.subgraphs.get(i).size(); j++) {
                if (dataSuite.subgraphs.get(i).get(j) == q) {
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

        for (int i = 0; i < dataSuite.poiList.length; i++) {
            if (dataSuite.poiList[i].poiType != 0) {
                dataSuite.poiList[i].poiNum = 1;
            }
        }

        ArrayList<LowerBound> topK = new ArrayList<>();
        FindFirstTopK topk = new FindFirstTopK();
        //ArrayList<Lower_bound> Top_k = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            long startTime1 = System.currentTimeMillis(); //开始获取时间
            topK = topk.KATRFindTopk(dataSuite.graph, q, q_SG, k, Poi_Type, dataSuite.subgraphs, dataSuite.paths, dataSuite.poiList, a,
                    dataSuite.BPList, dataSuite.PointMinBP, dataSuite.BvList); //进行全部优化的算法
            long endTime1 = System.currentTimeMillis(); //开始获取时间
            timeAllHeatTo11.add(endTime1 - startTime1);
            topK.clear();
        }
        return topK;
    }

    public static ArrayList<LowerBound> KATR_SToE(int[] Poi_Type2, int k, double a, DataSuite dataSuite, int num) {
        int q = 56988; //查询点
        //double a1 = a ; //α
        int q_SG = 0;
        boolean flag1 = true;
        //int[] Poi_Type = {11,12,16} ;//所求的Poi的类型
        int[] Poi_Type = Poi_Type2;
        for (int i = 0; i < dataSuite.subgraphs.size(); i++) {
            for (int j = 0; j < dataSuite.subgraphs.get(i).size(); j++) {
                if (dataSuite.subgraphs.get(i).get(j) == q) {
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

        int endIndex = 0;
        boolean flag;
        for (int i = 0; i < dataSuite.poiList.length; i++) {
            if (dataSuite.poiList[i].poiType != 0) {
                flag = false;
                for (int value : Poi_Type) {
                    if (dataSuite.poiList[i].poiType == value) {
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

        ArrayList<LowerBound> topK = new ArrayList<>();
        FindTopK_SToE topk = new FindTopK_SToE();
        for (int i = 0; i < num; i++) {
            long startTime1 = System.currentTimeMillis(); //开始获取时间
            topK = topk.KATRFindTopK(dataSuite.graph, q, endIndex, q_SG, k, Poi_Type, dataSuite.subgraphs, dataSuite.paths, dataSuite.poiList, a,
                    dataSuite.BPList, dataSuite.PointMinBP, dataSuite.BvList); //进行全部优化的算法
            long endTime1 = System.currentTimeMillis(); //开始获取时间
            timeSToE1.add((endTime1 - startTime1));
            topK.clear();
        }
        return topK;
    }
}
