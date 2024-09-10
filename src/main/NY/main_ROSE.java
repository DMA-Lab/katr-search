// Top k Optimal Sequenced Route Query with Poi Preferences
// https://link.springer.com/article/10.1007/s41019-022-00177-5

package main.NY;

import baseline.ROSE.FindTopk_NoOpt;
import baseline.ROSE.FindPathMTDOSR;
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

public class main_ROSE {

    static long time2;
    static final int num5 = 1; //循环次数

    public static void main(String[] args) throws IOException {

        int[] Poi_Type = {43, 25};//43,25,14,28,19,26,48,47时间，43,25,5,18,19,26,48,47剪枝效率
        int k = 20;
        double a = 0.5;//α
        int endIndex = 6000;
        ArrayList<FindPathMTDOSR.Path> Top_k_MTDOSR = MTDOSR(Poi_Type, k);
    }

    public static ArrayList<FindPathMTDOSR.Path> MTDOSR(int[] poiType, int k) throws IOException {
        Graph g = IntoData.loadGraph("/mnt/lab/everyone/share/数据集/9th DIMACS Implementation Challenge - Shortest Paths/USA-road-d.NY.gr");
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
        //计算多少条路径
        int q = 56988; //查询点
        double a = 0.2; //α
        int q_SG = 0;
        boolean flag1 = true;
        //int[] Poi_Type = {11,12,16} ;//所求的Poi的类型
        //36,54,50,1,6,3,9多，49,48,33,38,23,58,11少
        ArrayList<FindPathMTDOSR.Path> topK = new ArrayList<>();
        for (int ii = 0; ii < num5; ii++) {
            FindPathMTDOSR topk = new FindPathMTDOSR();
            FindTopk_NoOpt topk_No = new FindTopk_NoOpt();
            long startTime1 = System.currentTimeMillis(); //开始获取时间
            topK = topk.FindPath(g, q, poiType, PoiList);
            long endTime1 = System.currentTimeMillis(); //开始获取时间
//         if (ii != 0 || num5 == 1){
//             time2 += endTime1 - startTime1;
//         }
            time2 += endTime1 - startTime1;
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
