package KOSRAlgorithm;

import GraphEntity.EdegeNode;
import GraphEntity.MyGraph;
import GraphEntity.POI;
import KOSRAlgorithm._Class.*;
import KKRSAlgorithm.NavigationUtil;
//import com.carrotsearch.sizeof.RamUsageEstimator;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.MAX_VALUE;

public class Find_Topk {
    public static long startTime;
    public static long endTime;
    public static long Time1 = 0;


    public static ArrayList<ArrayList<Integer>> KOSR(MyGraph g,int PointNum, int startNum, int k, POI[] POIList, int[] POI_Type){

        ArrayList<ArrayList<Integer>> topK = new ArrayList<>();
        ArrayList<ArrayList<KOSR_Point>> KOSR_Point = new ArrayList<>();
        //构建dominance，用于剪枝
        ArrayList<Dominance> dominances = new ArrayList<>();
        //初始化顶点,初始化dominance
       // startTime1 = System.currentTimeMillis(); //开始获取时间
        for (int i = 0; i < PointNum; i++) {
            KOSR_Point.add(new ArrayList<KOSR_Point>());
            dominances.add(new Dominance(i));
        }
       // endTime1 = System.currentTimeMillis(); //开始获取时间
       // System.out.println("初始化所用时间为："+(endTime1 - startTime1));
        int nowPoint = startNum;
        //初始化头节点
        headNode headNode = new headNode();
        //将起点作为子路径加入头节点
        subPath subPath = new subPath();
        subPath.subPath.add(startNum);
        headNode.next = subPath;
        //扩展子路径
        //subPath minPath;
        //int minPathLastPoint;
        ArrayList<subPath> NowSubPaths = new ArrayList<>(); //当前的子路径
        ArrayList<subPath> NextSubPaths = new ArrayList<>(); //下一步的子路径
        //初始化子路径
        NowSubPaths.add(new subPath());
        NowSubPaths.get(NowSubPaths.size()-1).subPath.add(startNum);
        int num1= 0;


        while (topK.size() < k){
            //FindNN(KOSR_Point, minPathLastPoint, POI_Type[minPath.keyWordNum], k, g, POIList);

            for (int i = 0; i < NowSubPaths.size(); i++) {
                if (NowSubPaths.get(i).flag == true){
                    nowPoint = NowSubPaths.get(i).subPath.get(NowSubPaths.get(i).subPath.size()-1);
                    FindNN(KOSR_Point, nowPoint, POI_Type[NowSubPaths.get(i).keyWordNum], (k/1), g, POIList);
                    for (int j = 0; j < KOSR_Point.get(nowPoint).size(); j++) {
                        if (KOSR_Point.get(nowPoint).get(j).kewWordName == POI_Type[NowSubPaths.get(i).keyWordNum]){
                            for (int l = 0; l < KOSR_Point.get(nowPoint).get(j).poi.size(); l++) {
                                NextSubPaths.add(new subPath());
                                NextSubPaths.get(NextSubPaths.size()-1).subPath.addAll(NowSubPaths.get(i).subPath);
                                NextSubPaths.get(NextSubPaths.size()-1).subPath.add(KOSR_Point.get(nowPoint).get(j).poi.get(l));
                                NextSubPaths.get(NextSubPaths.size()-1).subPathWeight = NowSubPaths.get(i).subPathWeight + KOSR_Point.get(nowPoint).get(j).poiWeight.get(l);
                                NextSubPaths.get(NextSubPaths.size()-1).keyWordNum = NowSubPaths.get(i).keyWordNum+1;
                            }
                        }
                    }
                }

            }
            Dominances(dominances,NextSubPaths);
            ArrayList<Integer> needPrunePath = new ArrayList<>();
            //进行剪枝
            subPath nowPath1;
            Dominance dominance1;
//            int num111 = 0;
//            for (int i = 0; i < dominances.size(); i++) {
//                if (dominances.get(i).keyWordNum.size() > 0){
//                    num111++;
//                }
//            }
            for (int i = 0; i < NextSubPaths.size(); i++) {
                nowPath1 = NextSubPaths.get(i);
                for (int j = 0; j < dominances.get(nowPath1.subPath.get(nowPath1.subPath.size()-1)).keyWordNum.size(); j++) {
                    if (nowPath1.keyWordNum == dominances.get(nowPath1.subPath.get(nowPath1.subPath.size()-1)).keyWordNum.get(j)){
                        if (nowPath1.subPathWeight > dominances.get(nowPath1.subPath.get(nowPath1.subPath.size()-1)).minPathWegh.get(j)){
                            needPrunePath.add(i);
                        }
                    }
                }
            }
//            for (int i = 0; i < NextSubPaths.size(); i++) {
//                nowPath1 = NextSubPaths.get(i);
//                for (int j = 0; j < dominances.size(); j++) {
//                    if (nowPath1.subPath.get(nowPath1.subPath.size()-1) == dominances.get(j).pointName){
//                        dominance1 = dominances.get(j);
//                        for (int l = 0; l < dominance1.keyWordNum.size(); l++) {
//                            if (nowPath1.keyWordNum == dominance1.keyWordNum.get(l)){
//                                if (nowPath1.subPathWeight > dominance1.minPathWegh.get(l)){
//                                    needPrunePath.add(i);
//                                }
//                            }
//                        }
//                        break;
//                    }
//                }
//            }
            for (int i = 0; i < needPrunePath.size(); i++) {
                num1++;
                NextSubPaths.get(needPrunePath.get(i)).flag = false;
                //NextSubPaths.remove(needPrunePath.get(i));

            }
            needPrunePath.clear();
            NowSubPaths.clear();
            NowSubPaths.addAll(NextSubPaths);
            NextSubPaths.clear();

            if (NowSubPaths.get(0).keyWordNum == POI_Type.length){
                int minWeight;
                int Num = 0;
                for (int i = 0; i < k; i++) {
                    minWeight = MAX_VALUE;
                    for (int j = 0; j < NowSubPaths.size(); j++) {
                        if (NowSubPaths.get(j).subPathWeight < minWeight){
                            minWeight = NowSubPaths.get(j).subPathWeight;
                            Num = j;
                        }
                    }
                    topK.add(new ArrayList<Integer>());
                    topK.get(topK.size()-1).addAll(NowSubPaths.get(Num).subPath);
                    NowSubPaths.remove(Num);
                }
                System.out.println("KOSR剪枝的数量为："+num1);
                break;
            }


        }

       // System.out.println("Time="+Time1);


//        double s = (double) RamUsageEstimator.sizeOf(KOSR_Point)/(1024 * 1024 );
//        System.out.println("KOSR构建索引大小为(mb)："+s);
        return topK;
    }




    public static ArrayList<ArrayList<KOSR_Point>> FindNN(ArrayList<ArrayList<KOSR_Point>> KOSR_Point, int startIndex, int keyWordName, int k2, MyGraph g,
                                                          POI[] POIList){
        ArrayList<KOSR_Point> kosr_points = KOSR_Point.get(startIndex);
        kosr_points.add(new KOSR_Point(keyWordName));

        int[] set = new int[g.numPoint]; // 是否已并入集合，该点是否已找到最短路径
        // s到i的最短路径长度
        int[] dist = new int[g.numPoint];
        // s到i的最短路径上i的前一个节点编号
        //int[] path = new int[g.numPoint];
        List<Integer> dist1 = new ArrayList<>();
        int n = 0;
        int m = 0;
        EdegeNode a = new EdegeNode();
        EdegeNode aNext = new EdegeNode();
        // 初始化数组
        set[startIndex] = 1;
        for (int i = 0; i < g.numPoint; i++) {
            if (i == startIndex) { // 源点
                dist[i] = 0;
            } else {
                if (NavigationUtil.isConnected(g, startIndex, i)) {
                    dist[i] = (int) NavigationUtil.getEdgeWight(g, startIndex, i);
                    dist1.add(i);

                } else {
                    dist[i] = MAX_VALUE;
                }
            }
        }


        // 需进行n-2轮循环
        //long time3 = System.currentTimeMillis();//获取当前系统时间(毫秒)
       // int k1 = startIndex;
       // startTime = System.currentTimeMillis(); //开始获取时间
        EdegeNode g2;
        while (kosr_points.get(kosr_points.size()-1).poi.size() < k2){
            int k = -1;
            int min = MAX_VALUE;
            // EdegeNode g1 = g.point[k1].firstArc;
            // 找出dist[]中最小的
            for (Integer j : dist1) {
                if (dist[j] < min) {
                    min = dist[j];
                    k = j;
                }
            }
            if (k == -1) {
                // 说明从源点出发与其余节点不连通，无法再向下进行扩展
                break;
            }
            set[k] = 1; // 把节点k并入
            dist1.remove(new Integer(k));
            //dist1.remove(k);
            g2 = (EdegeNode) g.point[k].firstArc;
            // 修改dist[]、path[]
            while (g2 != null) {
                int j = g2.adjvex;
                if (dist[j] == MAX_VALUE || dist[k] + g2.value < dist[j]) {
                    if (dist[j] == MAX_VALUE) {
                        dist1.add(j);
                    }
                    dist[j] = dist[k] + g2.value;
                }
                g2 = g2.nextEdge;
            }
            if(POIList[k].POI_Type == keyWordName){
                kosr_points.get(kosr_points.size()-1).poi.add(k);
                kosr_points.get(kosr_points.size()-1).poiWeight.add(dist[k]);
            }
           // k1 = k;
        }
       // endTime = System.currentTimeMillis(); //开始获取时间
       // Time1 += endTime - startTime;
        return KOSR_Point;
    }


    public static ArrayList<Dominance> Dominances(ArrayList<Dominance> dominances,ArrayList<subPath> subPaths){
        subPath nowPath;
        int nowPoint;
        boolean flag1 ;
        boolean flag2 ;
        for (int i = 0; i < subPaths.size(); i++) {
            nowPath = subPaths.get(i);
            nowPoint = nowPath.subPath.get(nowPath.subPath.size()-1);
            flag1 = false;
            for (int j = 0; j < dominances.get(nowPoint).keyWordNum.size(); j++) {
                if (dominances.get(nowPoint).keyWordNum.get(j) == nowPath.keyWordNum ){
                    flag1 = true;
                    if (dominances.get(nowPoint).minPathWegh.get(j) > nowPath.subPathWeight){
                        dominances.get(nowPoint).minPathWegh.set(j,nowPath.subPathWeight);
                        break;
                    }
                }
            }
            if (flag1 == false){
                dominances.get(nowPoint).keyWordNum.add(nowPath.keyWordNum);
                dominances.get(nowPoint).minPathWegh.add(nowPath.subPathWeight);
            }
        }
//        for (int i = 0; i < subPaths.size(); i++) {
//            nowPath = subPaths.get(i);
//            flag1 = false;
//            for (int j = 0; j < dominances.size(); j++) {
//                if (nowPath.subPath.get(nowPath.subPath.size()-1) == dominances.get(j).pointName){
//                    flag1 = true;
//                    flag2 = false;
//                    for (int k = 0; k < dominances.get(j).keyWordNum.size(); k++) {
//                        if (dominances.get(j).keyWordNum.get(k) == nowPath.keyWordNum){
//                            flag2 = true;
//                            if (dominances.get(j).minPathWegh.get(k) > nowPath.subPathWeight){
//                                dominances.get(j).minPathWegh.set(k, nowPath.subPathWeight);
//                            }
//                        }
//                    }
//                    if (flag2 == false){
//                        dominances.get(j).keyWordNum.add(nowPath.keyWordNum);
//                        dominances.get(j).minPathWegh.add(nowPath.subPathWeight);
//                    }
//                }
//
//            }
//            if (flag1 == false){
//                dominances.add(new Dominance(nowPath.subPath.get(nowPath.subPath.size()-1)));
//                dominances.get(dominances.size()-1).keyWordNum.add(nowPath.keyWordNum);
//                dominances.get(dominances.size()-1).minPathWegh.add(nowPath.subPathWeight);
//            }
//        }


        return dominances;

    }



}
