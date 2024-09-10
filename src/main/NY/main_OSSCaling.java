package main.NY;

import baseline.ROSE.FindTopK_OSSCaling;
import entity.PoiPath;
import entity.Graph;
import entity.Poi;
import entity.Path;
import loader.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class main_OSSCaling {

    static long time2;
    static long time_OSSCaling;

    static long timeA;
    static long timeA_db;
    static long timeA_db2;
    static long timeA_db3;
    static final int num5 = 1; //循环次数

    public static void main(String[] args) throws IOException {
        int[] Poi_Type = {43, 25, 14, 28, 19, 26};//43,25,14,28,19,26,48,47时间，43,25,5,18,19,26,48,47剪枝效率
        int k1 = 6;
        double a = 0.5;//α
        int endIndex = 6000;
        int num6 = num5 - 1;
        if (num6 == 0) {
            num6 = 1;
        }

        ArrayList<FindTopK_OSSCaling.Path> Top_k_OSSCaling = OSSCaling(Poi_Type, k1, endIndex);

        System.out.println("————————————————————————————————————————————————————————————————————————————");
        System.out.println("MTDOSR算法计算所消耗的时间为:" + (time2 / num6) + "毫秒");
        System.out.println("OSSCaling算法计算所消耗的时间为:" + (time_OSSCaling / num6) + "毫秒");
        System.out.println("A的对比算法(Poi顺序固定)计算所消耗的时间为:" + (timeA_db / (num6)) + "毫秒");
        System.out.println("A的对比算算法(不进行优化)计算所消耗的时间为:" + (timeA_db3 / (num6)) + "毫秒");
        System.out.println("A的对比算算法(只进行网格剪枝)计算所消耗的时间为:" + (timeA / (num6)) + "毫秒");
        System.out.println("A算法(进行全部优化)计算所消耗的时间为:" + (timeA_db2 / (num6)) + "毫秒");
    }


    public static ArrayList<FindTopK_OSSCaling.Path> OSSCaling(int[] Poi_Type2, int k1, int endIndex) throws IOException {
        Graph g = IntoData.loadGraph("/mnt/lab/everyone/share/数据集/9th DIMACS Implementation Challenge - Shortest Paths/USA-road-topK.NY.gr");
        int vertexCount = g.vertexCount;

        //______________________________________________________________________________________________
        //将各个点放入对应的子图中
        int num2 = 200; // 存储多少个子图的骨架图中的节点
        CreateSubgraph SG1 = new CreateSubgraph();
        //BufferedReader br3 = new BufferedReader(file1);//读取文件
        ArrayList<ArrayList<Integer>> SG = SG1.load_NY(num2); //存储前num2个子图的骨架图中的节点

        //______________________________________________________________________________________________
        //构建Poi索引PoiList，存储Poi的类型和数值，并给每个顶点赋予坐标
        CreatePoiList PoiList1 = new CreatePoiList();
        Poi[] PoiList = PoiList1.loadPoiNY(vertexCount, SG);
        //______________________________________________________________________________________________
        // //构建距离索引list
        CreateList list1 = new CreateList();
        ArrayList<ArrayList<Path>> List = list1.CreatList_NY(vertexCount);
        //System.out.println("1");
        boolean flag;
        ArrayList<Integer> Poi_Type_Num = new ArrayList<>();
        for (Poi poi : PoiList) {
            if (poi.poiType != 0) {
                flag = true;
                for (Integer integer : Poi_Type_Num) {
                    if (poi.poiType == integer) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    Poi_Type_Num.add(poi.poiType);
                }
            }
        }
        //______________________________________________________________________________________________
        // //构建边界顶点索引BPList
        ArrayList<ArrayList<PoiPath>> BPList = new ArrayList<>();
        for (int i = 0; i < vertexCount; i++) {
            BPList.add(new ArrayList<>());
        }
        CreateBpList BPList1 = new CreateBpList();
        //System.out.println("111");
        BPList1.CreatBPList_NY(BPList, vertexCount);
        //______________________________________________________________________________________________

        //计算全部点到最近的边界顶点的距离
        ArrayList<ArrayList<Integer>> PointMinBP = CreateMinBpTable.CreatMinBP_NY();


        //查找top_k
        int k = k1; //计算多少条路径
        int q = 56988; //查询点
        //int endIndex1 = 2000;
        double a = 0.2; //α
        int q_SG = 0;
        boolean flag1 = true;
        //int[] Poi_Type = {11,12,16} ;//所求的Poi的类型
        int[] Poi_Type = Poi_Type2;//36,54,50,1,6,3,9多，49,48,33,38,23,58,11少
        ArrayList<FindTopK_OSSCaling.Path> topK = new ArrayList<>();
        for (int ii = 0; ii < num5; ii++) {
            FindTopK_OSSCaling topk = new FindTopK_OSSCaling();
            long startTime1 = System.currentTimeMillis(); //开始获取时间
            topK = topk.Find_Path_OSSCaling(g, q, Poi_Type, vertexCount, PoiList, List, BPList, PointMinBP, k, endIndex);
            long endTime1 = System.currentTimeMillis(); //开始获取时间
//         if (ii != 0 || num5 == 1){
//             time_OSSCaling += endTime1 - startTime1;
//         }
            time_OSSCaling += endTime1 - startTime1;
        }
//        long startTime2 = System.currentTimeMillis(); //开始获取时间
//        ArrayList<Lower_bound> Top_k_NoOpt = topk_No.Top_k_NOOPT(g,q,q_SG,k,Poi_Type,SG,List,PoiList,a,BPList,PointMinBP);
//        long endTime2 = System.currentTimeMillis(); //开始获取时间
//        long time2 = endTime2 - startTime2;

        //测试图中各个兴趣点的数量以及分布情况
//        ArrayList<Integer> Poi_Type1 = new ArrayList<>();
//        ArrayList<ArrayList<Integer>> Poi_SGNum = new ArrayList<>();
//        for (int i = 0; i < SG.size(); i++) {
//            for (int j = 0; j < SG.get(i).size(); j++) {
//               if (PoiList[SG.get(i).get(j)].Poi_Type != 0){
//                   if (isnum(PoiList[SG.get(i).get(j)].Poi_Type,Poi_Type1) == false){
//                       Poi_Type1.add(PoiList[SG.get(i).get(j)].Poi_Type);
//                   }
//               }
//            }
//        }
//        for (int i = 0; i < Poi_Type1.size(); i++) {
//            Poi_SGNum.add(new ArrayList<>());
//        }
//
//        for (int ii = 0; ii < Poi_Type1.size(); ii++) {
//            for (int i = 0; i < SG.size(); i++) {
//                for (int j = 0; j < SG.get(i).size(); j++) {
//                    if (PoiList[SG.get(i).get(j)].Poi_Type == Poi_Type1.get(ii)){
//                        Poi_SGNum.get(ii).add(i);
//                        break;
//                    }
//                }
//            }
//        }
//        for (int i = 0; i < Poi_Type1.size(); i++) {
//            System.out.println(Poi_Type1.get(i)+"所在子图的数量为："+Poi_SGNum.get(i).size());
//        }

        return topK;
    }
}
