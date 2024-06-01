package main_KATR;

import CreatData.GraphData;
import CreatData.IntoData;
import CreatGraph.*;
import GraphEntity.*;
import KATRAlgorithm.FindInitialTopk;
import KATRAlgorithm.FindTopk;
import KATRAlgorithm.FindTopk_SToE;
import KKRSAlgorithm.Find_Topk_A_db2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class main_KATR {

    static long time1;
    static long time2;
    static long time3;
    static long time4;
    static long time5;
    static long time_OSSCaling;

    static long timeKATR = 0;
    static long timeAllHeatTo1 = 0;
    static long timeSToE = 0;
    static long timeA_db3;
    static ArrayList<Long> timeKATR1 = new ArrayList<>();
    static ArrayList<Long> timeSToE1 = new ArrayList<>();
    static ArrayList<Long> timeAllHeatTo11 = new ArrayList<>();
    public static void main(String[] args) throws InterruptedException {
        int[] POI_Type = {43,25};//43,25,14,28,19,26,48,47时间，43,25,5,18,19,26,48,47剪枝效率
        int k1 = 20;
        double a = 0.5;//α
        int num = 1; //循环次数
//        int endIndex = 6000;


        GraphData GraphData = new GraphData();
        IntoData.CreatData(POI_Type,k1,a,GraphData);

        ArrayList<Lower_bound> Top_k_KATR_SToE = KATR_SToE(POI_Type,k1,a,GraphData,num);
        ArrayList<Lower_bound> Top_k_KATR = KATR(POI_Type,k1,a,GraphData,num);
        ArrayList<Lower_bound> Top_k_KATRAllHeatTo1 = KATR_AllHeatTo1(POI_Type,k1,a,GraphData,num);

        if (num > 1){
            for (int i = 1; i < timeKATR1.size(); i++) {
                timeKATR +=  timeKATR1.get(i);
                timeSToE += timeSToE1.get(i);
                timeAllHeatTo1 += timeAllHeatTo11.get(i);
            }
            timeKATR = timeKATR/(num-1);
            timeSToE = timeSToE/(num-1);
            timeAllHeatTo1 = timeAllHeatTo1/(num-1);

        }else {
            timeKATR = timeKATR1.get(0);
            timeSToE = timeSToE1.get(0);
            timeAllHeatTo1 = timeAllHeatTo11.get(0);
        }


          System.out.println("————————————————————————————————————————————————————————————————————————————");
//        System.out.println("MTDOSR算法计算所消耗的时间为:"+(time2/num6)+"毫秒");
//        System.out.println("OSSCaling算法计算所消耗的时间为:"+(time_OSSCaling/num6)+"毫秒");
//        System.out.println("A的对比算法(POI顺序固定)计算所消耗的时间为:"+(timeA_db/(num6))+"毫秒");
//        System.out.println("A的对比算算法(不进行优化)计算所消耗的时间为:"+(timeA_db3/(num6))+"毫秒");
//        System.out.println("A的对比算算法(只进行网格剪枝)计算所消耗的时间为:"+(timeA/(num6))+"毫秒");
          System.out.println("KATR查询所消耗的时间为:"+timeKATR+"毫秒");
          System.out.println("KATRAllHeatTo1查询所消耗的时间为:"+timeAllHeatTo1+"毫秒");
          System.out.println("KATRSToE查询所消耗的时间为:"+timeSToE+"毫秒");
    }

    public static ArrayList<Lower_bound> KATR(int[] POI_Type2,int k1,double a,GraphData GraphData,int num) throws InterruptedException {

        int k = k1; //计算多少条路径
        int q = 56988; //查询点
        int q_SG = 0;
        boolean flag1 = true;
        //int[] POI_Type = {11,12,16} ;//所求的POI的类型
        int[] POI_Type = POI_Type2;
        for (int i = 0; i < GraphData.SG.size(); i++) {
            for (int j = 0; j < GraphData.SG.get(i).size(); j++) {
                if (GraphData.SG.get(i).get(j) == q){
                    q_SG = i;
                    flag1 = true;
                    break;
                }
            }
            if (flag1 == true){
                break;
            }
        }
        ArrayList<ArrayList<Integer>> POI_Num2 = new ArrayList<>();
        ArrayList<Integer> path3 = new ArrayList<>();
        boolean flag4 = true;
        for (int i = 0; i < GraphData.POIList.length; i++) {
            if (GraphData.POIList[i].POI_Type != 0){
                flag4 = true;
                for (int j = 0; j < POI_Num2.size(); j++) {
                    if (POI_Num2.get(j).get(0) == GraphData.POIList[i].POI_Type){
                        flag4 = false;
                        if (GraphData.POIList[i].POI_Num < POI_Num2.get(j).get(1)){
                            path3.clear();
                            path3.add(POI_Num2.get(j).get(0));
                            path3.add(GraphData.POIList[i].POI_Num);
                            path3.add(POI_Num2.get(j).get(2));
                            POI_Num2.get(j).clear();
                            POI_Num2.get(j).addAll(path3);
                        }
                        if (GraphData.POIList[i].POI_Num > POI_Num2.get(j).get(2)){
                            path3.clear();
                            path3.add(POI_Num2.get(j).get(0));
                            path3.add(POI_Num2.get(j).get(1));
                            path3.add(GraphData.POIList[i].POI_Num);
                            POI_Num2.get(j).clear();
                            POI_Num2.get(j).addAll(path3);
                        }
                    }
                }
                if (flag4 == true){
                    POI_Num2.add(new ArrayList<Integer>());
                    POI_Num2.get(POI_Num2.size()-1).add(GraphData.POIList[i].POI_Type);
                    POI_Num2.get(POI_Num2.size()-1).add(GraphData.POIList[i].POI_Num);
                    POI_Num2.get(POI_Num2.size()-1).add(GraphData.POIList[i].POI_Num);
                }
            }
        }


        ArrayList<Lower_bound> Top_k = new ArrayList<>();
        FindTopk topk = new FindTopk();
        //ArrayList<Lower_bound> Top_k = new ArrayList<>();

        int v_num = GraphData.POIList.length;
        for (int i = 0; i < num; i++) {
            long startTime1 = System.currentTimeMillis(); //开始获取时间
            Top_k = topk.KATRFindTopk(GraphData.g,q,q_SG,k,POI_Type,GraphData.SG,GraphData.List,GraphData.POIList,a,
                    GraphData.BPList,GraphData.PointMinBP,GraphData.BvList); //进行全部优化的算法
            long endTime1 = System.currentTimeMillis(); //开始获取时间
            timeKATR1.add((endTime1 - startTime1));
            Top_k.clear();
        }
//        long startTime1 = System.currentTimeMillis(); //开始获取时间
//        Top_k = topk.KATRFindTopk(GraphData.g,q,q_SG,k,POI_Type,GraphData.SG,GraphData.List,GraphData.POIList,a,
//                                  GraphData.BPList,GraphData.PointMinBP,GraphData.BvList); //进行全部优化的算法
////        System.out.println("KATR算法完成");
//        long endTime1 = System.currentTimeMillis(); //开始获取时间
//        timeKATR = endTime1 - startTime1;
        return Top_k;
    }

    public static ArrayList<Lower_bound> KATR_AllHeatTo1(int[] POI_Type2,int k1,double a,GraphData GraphData,int num) throws InterruptedException {

        int k = k1; //计算多少条路径
        int q = 56988; //查询点
        //double a1 = a ; //α
        int q_SG = 0;
        boolean flag1 = true;
        //int[] POI_Type = {11,12,16} ;//所求的POI的类型
        int[] POI_Type = POI_Type2;
        for (int i = 0; i < GraphData.SG.size(); i++) {
            for (int j = 0; j < GraphData.SG.get(i).size(); j++) {
                if (GraphData.SG.get(i).get(j) == q){
                    q_SG = i;
                    flag1 = true;
                    break;
                }
            }
            if (flag1 == true){
                break;
            }
        }
        ArrayList<ArrayList<Integer>> POI_Num2 = new ArrayList<>();
        ArrayList<Integer> path3 = new ArrayList<>();
        boolean flag4 = true;
        for (int i = 0; i < GraphData.POIList.length; i++) {
            if (GraphData.POIList[i].POI_Type != 0){
                flag4 = true;
                for (int j = 0; j < POI_Num2.size(); j++) {
                    if (POI_Num2.get(j).get(0) == GraphData.POIList[i].POI_Type){
                        flag4 = false;
                        if (GraphData.POIList[i].POI_Num < POI_Num2.get(j).get(1)){
                            path3.clear();
                            path3.add(POI_Num2.get(j).get(0));
                            path3.add(GraphData.POIList[i].POI_Num);
                            path3.add(POI_Num2.get(j).get(2));
                            POI_Num2.get(j).clear();
                            POI_Num2.get(j).addAll(path3);
                        }
                        if (GraphData.POIList[i].POI_Num > POI_Num2.get(j).get(2)){
                            path3.clear();
                            path3.add(POI_Num2.get(j).get(0));
                            path3.add(POI_Num2.get(j).get(1));
                            path3.add(GraphData.POIList[i].POI_Num);
                            POI_Num2.get(j).clear();
                            POI_Num2.get(j).addAll(path3);
                        }
                    }
                }
                if (flag4 == true){
                    POI_Num2.add(new ArrayList<Integer>());
                    POI_Num2.get(POI_Num2.size()-1).add(GraphData.POIList[i].POI_Type);
                    POI_Num2.get(POI_Num2.size()-1).add(GraphData.POIList[i].POI_Num);
                    POI_Num2.get(POI_Num2.size()-1).add(GraphData.POIList[i].POI_Num);
                }
            }
        }
        for (int i = 0; i < GraphData.POIList.length; i++) {
            if (GraphData.POIList[i].POI_Type != 0){
                GraphData.POIList[i].POI_Num = 1;
            }
        }


        ArrayList<Lower_bound> Top_k = new ArrayList<>();
        FindInitialTopk topk = new FindInitialTopk();
        //ArrayList<Lower_bound> Top_k = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            long startTime1 = System.currentTimeMillis(); //开始获取时间
            Top_k = topk.KATRFindTopk(GraphData.g,q,q_SG,k,POI_Type,GraphData.SG,GraphData.List,GraphData.POIList,a,
                    GraphData.BPList,GraphData.PointMinBP,GraphData.BvList); //进行全部优化的算法
            long endTime1 = System.currentTimeMillis(); //开始获取时间
            timeAllHeatTo11.add(endTime1 - startTime1);
            Top_k.clear();
        }
        return Top_k;
    }

    public static ArrayList<Lower_bound> KATR_SToE(int[] POI_Type2,int k1,double a,GraphData GraphData,int num) throws InterruptedException{
        int k = k1; //计算多少条路径
        int q = 56988; //查询点
        //double a1 = a ; //α
        int q_SG = 0;
        boolean flag1 = true;
        //int[] POI_Type = {11,12,16} ;//所求的POI的类型
        int[] POI_Type = POI_Type2;
        for (int i = 0; i < GraphData.SG.size(); i++) {
            for (int j = 0; j < GraphData.SG.get(i).size(); j++) {
                if (GraphData.SG.get(i).get(j) == q){
                    q_SG = i;
                    flag1 = true;
                    break;
                }
            }
            if (flag1 == true){
                break;
            }
        }
        ArrayList<ArrayList<Integer>> POI_Num2 = new ArrayList<>();
        ArrayList<Integer> path3 = new ArrayList<>();
        boolean flag4 = true;
        for (int i = 0; i < GraphData.POIList.length; i++) {
            if (GraphData.POIList[i].POI_Type != 0){
                flag4 = true;
                for (int j = 0; j < POI_Num2.size(); j++) {
                    if (POI_Num2.get(j).get(0) == GraphData.POIList[i].POI_Type){
                        flag4 = false;
                        if (GraphData.POIList[i].POI_Num < POI_Num2.get(j).get(1)){
                            path3.clear();
                            path3.add(POI_Num2.get(j).get(0));
                            path3.add(GraphData.POIList[i].POI_Num);
                            path3.add(POI_Num2.get(j).get(2));
                            POI_Num2.get(j).clear();
                            POI_Num2.get(j).addAll(path3);
                        }
                        if (GraphData.POIList[i].POI_Num > POI_Num2.get(j).get(2)){
                            path3.clear();
                            path3.add(POI_Num2.get(j).get(0));
                            path3.add(POI_Num2.get(j).get(1));
                            path3.add(GraphData.POIList[i].POI_Num);
                            POI_Num2.get(j).clear();
                            POI_Num2.get(j).addAll(path3);
                        }
                    }
                }
                if (flag4 == true){
                    POI_Num2.add(new ArrayList<Integer>());
                    POI_Num2.get(POI_Num2.size()-1).add(GraphData.POIList[i].POI_Type);
                    POI_Num2.get(POI_Num2.size()-1).add(GraphData.POIList[i].POI_Num);
                    POI_Num2.get(POI_Num2.size()-1).add(GraphData.POIList[i].POI_Num);
                }
            }
        }

        int endIndex = 0;
        boolean flag = false;
        for (int i = 0; i < GraphData.POIList.length; i++) {
            if (GraphData.POIList[i].POI_Type != 0){
                flag = false;
                for (int j = 0; j < POI_Type.length; j++) {
                    if (GraphData.POIList[i].POI_Type == POI_Type[j]){
                        flag = true;
                    }
                    if (flag == true){
                        break;
                    }
                }
                if (flag == false){
                    endIndex = i;
                }
            }
        }
        System.out.println("endIndex="+endIndex);

        ArrayList<Lower_bound> Top_k = new ArrayList<>();
        FindTopk_SToE topk = new FindTopk_SToE();
        //ArrayList<Lower_bound> Top_k = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            long startTime1 = System.currentTimeMillis(); //开始获取时间
            Top_k = topk.KATRFindTopk(GraphData.g,q,endIndex,q_SG,k,POI_Type,GraphData.SG,GraphData.List,GraphData.POIList,a,
                    GraphData.BPList,GraphData.PointMinBP,GraphData.BvList); //进行全部优化的算法
            long endTime1 = System.currentTimeMillis(); //开始获取时间
            timeSToE1.add((endTime1 - startTime1));
            Top_k.clear();
        }


//        long startTime1 = System.currentTimeMillis(); //开始获取时间
//        Top_k = topk.KATRFindTopk(GraphData.g,q,endIndex,q_SG,k,POI_Type,GraphData.SG,GraphData.List,GraphData.POIList,a,
//                GraphData.BPList,GraphData.PointMinBP,GraphData.BvList); //进行全部优化的算法
//        long endTime1 = System.currentTimeMillis(); //开始获取时间
//        timeSToE = endTime1 - startTime1;
        return Top_k;
    }


}
