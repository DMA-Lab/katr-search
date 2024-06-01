package KATRAlgorithm;

import GraphEntity.*;

import java.util.ArrayList;

import static KATRAlgorithm.FindTopk.allsort;
import static java.lang.Math.sqrt;

public class FindInitialTopk {

    public static int num_LJ1 = 0;//路线剪枝1
    public static int num_LJ2 = 0;//路线剪枝2
    public static int num_LJ3 = 0;
    public static int num_LJ4 = 0;
    public static int num_safeR = 0;
    public static int num_kzSG = 0;
    public static int num_jzSG = 0;
    public static int num_hxP = 0;
    public static int num_jzP = 0;
    //public static int num_LJ5 = 0;//判断每次总的组合里面有多少条组合形式
    long startTime1 = System.currentTimeMillis(); //开始获取时间
    long endTime1 = System.currentTimeMillis(); //开始获取时间
    long time11 = 0;
    long time22 = 0;
    long time33 = 0;

    public ArrayList<Lower_bound> KATRFindTopk (MyGraph g,  int q, int q_SG, int k, int[] POI_Type, ArrayList<ArrayList<Integer>> SG, ArrayList<ArrayList<list>> List , POI[] POIList,
                                                double a, ArrayList<ArrayList<Class_BPList>> BPList, ArrayList<ArrayList<Integer>> PointMinBP, ArrayList<ArrayList<Integer>> BvList) throws InterruptedException {

        //找到查询点q所在的子图
        boolean flag1 = false;
        //int q_SG = 0;
        int[] SG_num = new int[SG.size()]; //判断这个子图是否已经被搜索过

        int[] a2 = new int[POI_Type.length];
        for (int i = 0; i < POI_Type.length; i++) {
            a2[i] = i;
        }

        //获得这些数字的全排列
        ArrayList<ArrayList<Integer>> order1 = new ArrayList<>();
        ArrayList<ArrayList<Integer>> all = allsort(a2, order1, 0);
        ArrayList<ArrayList<Integer>> all1 = new ArrayList<>();
        ArrayList<ArrayList<ArrayList<Integer>>> path = new ArrayList<>();
        ArrayList<ArrayList<Integer>> path2 = new ArrayList<>();
        ArrayList<ArrayList<Integer>> path3 = new ArrayList<>();
        boolean flag12 = true;
        all1.add(new ArrayList<Integer>());
        for (int i = 0; i < POI_Type.length; i++) {
            all1.get(0).add(i);
        }
        //将q_SG中的POI加入path3
        for (int i = 0; i < POI_Type.length; i++) {
            path3.add(new ArrayList<Integer>());
        }
        Add_POIAsSG(path3, POI_Type, SG, POIList, all, q_SG);
        boolean flag4 = true;
        ArrayList<Integer> NoType = new ArrayList<>();
        int num44 = 0;
        for (int l = 0; l < path3.size(); l++) {//判断这个子图是否有全部的兴趣点
            if (path3.get(l).size() == 0) {
                NoType.add(l);//
                num44++;
                flag4 = false;
                //break;
            }
        }

        if (flag4 == false) {
            //System.out.println("初始子图中不包括全部的POI类型");
            boolean flag6 = false;
            int num55 = 0;
            while (num44 > 0) {
                for (int i = 0; i < SG.size(); i++) {
                    flag6 = false;
                    for (int j = 0; j < SG.get(i).size(); j++) {
                        if (POIList[SG.get(i).get(j)].POI_Type == POI_Type[NoType.get(num55)]) {
                            path3.get(NoType.get(num55)).add(SG.get(i).get(j));
                            num55++;
                            num44--;
                            flag6 = true;
                            break;
                        }
                    }
                    if (flag6 == true) {
                        break;
                    }
                }
            }
        }
        double R;
        //寻找距离查询点q最近的一个边界顶点
        int num45 = 1;
        for (int i = 0; i < path3.size(); i++) {
            if (path3.get(i).size() != 0) {
                num45 = num45 * path3.get(i).size();
            }
        }
        num_LJ1 += num45 * all.size();
        num_LJ3 = num45 * all.size();
        int q_BP = PointMinBP.get(q).get(0);
        int Min_w = PointMinBP.get(q).get(1);
        ArrayList<Integer> q_BP_Path = new ArrayList<>();
        path2 = Finf_Path1(all1, path3, POI_Type, q);
//        Find_Path2(ArrayList<ArrayList<Integer>> path,int[] POI_Type,POI[] POIList,ArrayList<ArrayList<Integer>> all,
//        int q){
        // startTime1 = System.currentTimeMillis();
        path = Find_Path2(path2, POI_Type, POIList, all, q, 0, 0, a);
        //从这些路径中选出k条q到第一节点最短的路径

        //ArrayList<ArrayList<Integer>> path_k = Find_path_k(q,path, k, q_SG,POIList, List,PointMinBP,BPList);
        //endTime1 = System.currentTimeMillis();
        // time11 = time11 + endTime1 - startTime1;
        // startTime1 = System.currentTimeMillis();
        //startTime1 = System.currentTimeMillis();
        ArrayList<Lower_bound> allPath = Find_allPath(path, List, POIList, a, q_BP, Min_w, BPList, PointMinBP, k);
        // endTime1 = System.currentTimeMillis();
        // time33 = time33 + endTime1 - startTime1;
        // ArrayList<Lower_bound> allPath = new ArrayList<>();
        //endTime1 = System.currentTimeMillis();
        //time1 = endTime1 - startTime1;
        // System.out.println("路线剪枝测试成功,所用的时间为："+time1);

        ArrayList<Lower_bound> Top_k = Find_Top_k(allPath, k);
        double LB = Find_LB(Top_k);


        return Top_k;
    }



    public static ArrayList<ArrayList<Integer>> Find_Path(ArrayList<ArrayList<Integer>> path1,
                                                          ArrayList<ArrayList<Integer>> path2, int k) {
        ArrayList<ArrayList<Integer>> path = new ArrayList<>();
        if (k <= path1.size()-2) {
            int num = 0;
            for (int i = 0; i < path2.size(); i++) {
                for (int j = 0; j < path1.get(k+1).size(); j++) {
                    path.add(new ArrayList<Integer>());
//                    path.get(num).addAll(path2.get(i));
//                    path.get(num).addAll(path1.get(j));
                    for (int l = 0; l < path2.get(i).size(); l++) {
                        path.get(num).add(path2.get(i).get(l));
                    }
                    path.get(num).add(path1.get(k+1).get(j));
//                    if (path1.get(k+1).size() > 1){
//
//                    }else {
//                        path.get(num).add(path1.get(k+1).get(0));
//                    }

                    num++;
                }
            }
            path2 = Find_Path(path1,path,k+1);
        }else if (k > path1.size()-2){
            return path2;
        }

        return path2;
    }

    public ArrayList<Lower_bound> Find_allPath (ArrayList<ArrayList<ArrayList<Integer>>> path, ArrayList<ArrayList<list>> List, POI[] POIList,double a,
                                                int q_BP,int w_BP,ArrayList<ArrayList<Class_BPList>> BPList,ArrayList<ArrayList<Integer>> PointList,int k) throws InterruptedException {
        ArrayList<Lower_bound> LB = new ArrayList<>();
        int w = 0;
        int num = 0;
        double score_Min = 0;
        double score1 = 0;
        int q_BP1 = q_BP;
        int w_BP1 = w_BP;
        long startTime2 = System.currentTimeMillis(); //开始获取时间
        long endTime2 = System.currentTimeMillis(); //开始获取时间
        long time2 = 0;
        long time3 = 0;
        long time4 = 0;
        long time5 = 0;
        long time6 = 0;
        ArrayList<Integer> path_q2 = new ArrayList<>();
        ArrayList <Integer> path1 = new ArrayList<>(); //存放路径
        //num_LJ1 += path.size();
        int POI_NUM ;
        int q_index;
        int q_index1;
        for (int i = 0; i < path.size(); i++) {
            //num_LJ1 = num_LJ1 + path.get(i).size();
            for (int ii = 0; ii < path.get(i).size(); ii++) {
                POI_NUM = 0;
                for (int j = 1; j < path.get(i).get(ii).size(); j++) {
                    POI_NUM += POIList[path.get(i).get(ii).get(j)].POI_Num;
                }
                //计算这种组合形式的欧式距离

                int x;
                int y;
                w = 0;
                for (int j = 0; j < path.get(i).get(ii).size()-1; j++) {
                    q_index = path.get(i).get(ii).get(j);
                    q_index1 = path.get(i).get(ii).get(j+1);
                    x = POIList[q_index].x - POIList[q_index1].x;
                    y = POIList[q_index].y - POIList[q_index1].y;
                    w += (int) sqrt(Math.pow(x,2)+Math.pow(y,2));
                }
                //判断是否需要剪枝
                if (LB.size() != 0){
                    if (w != 0)
                        score1 = (-a)*w + (1-a)*POI_NUM;
                    score_Min = LB.get(Find_LB_Num(LB)).score;
                    if (score1 <= score_Min){
                        num_LJ4++;
                        break;
                    }
                }
                w = 0;
                path1.clear();

                for (int j = 0; j < path.get(i).get(ii).size()-1; j++) {
                    q_index = path.get(i).get(ii).get(j);
                    q_index1 = path.get(i).get(ii).get(j+1);
                    q_BP1 = PointList.get(q_index).get(0);
                    w_BP1 = PointList.get(q_index).get(1);
                    if (POIList[q_index].SG == POIList[q_index1].SG){
                        for (int k1 = 0; k1 < List.get(q_index).size(); k1++) {
                            if (List.get(q_index).get(k1).ePoint == q_index1){
                                w = w+List.get(q_index).get(k1).w;
                                break;
                            }
                        }
                    }else {
                        if (q_BP1 != Integer.MAX_VALUE){
                            w = w + w_BP1 + BPList.get(q_BP1).get(POIList[q_index1].SG).w;
                            for (int k1 = 0; k1 < List.get(BPList.get(q_BP1).get(POIList[q_index1].SG).Target).size(); k1++) {
                                if (List.get(BPList.get(q_BP1).get(POIList[q_index1].SG).Target).get(k1).ePoint == q_index1){
                                    w = w+List.get(BPList.get(q_BP1).get(POIList[q_index1].SG).Target).get(k1).w;
                                    break;
                                }
                            }
                        }else {
                            w = 0;
                        }
                    }
                }
                //计算这组poi的路径长度w
                startTime2 = System.currentTimeMillis();
                Lower_bound nn = new Lower_bound();
                if (LB.size() < k){
                    LB.add(new Lower_bound());
                    LB.get(LB.size()-1).path.clear();
                    LB.get(LB.size()-1).path.addAll(path.get(i).get(ii));
                    LB.get(LB.size()-1).dis = w;
                    for (int kk = 0; kk < LB.get(LB.size()-1).path.size(); kk++) {
                        //LB.get(num).w_poi += POIList[LB.get(num).path.get(k)].POI_Num;
                        LB.get(LB.size()-1).w_poi += POIList[path.get(i).get(ii).get(kk)].POI_Num;
                    }
                    LB.get(LB.size()-1).score = (-a)*LB.get(LB.size()-1).dis+(1-a)*LB.get(LB.size()-1).w_poi;
                }else {
                    startTime2 = System.currentTimeMillis();
                    score_Min = LB.get(Find_LB_Num(LB)).score;

                    nn.path.clear();
                    nn.path.addAll(path.get(i).get(ii));
                    nn.dis = w;
                    nn.w_poi = POI_NUM;
//                            for (int kk = 0; kk < LB.get(LB.size()-1).path.size(); kk++) {
//                                //LB.get(num).w_poi += POIList[LB.get(num).path.get(k)].POI_Num;
//                                nn.w_poi += POIList[path.get(i).get(ii).get(kk)].POI_Num;
//                            }
                    nn.score = (-a)*nn.dis+(1-a)*nn.w_poi;
                    if (nn.score > score_Min){
                        LB.get(Find_LB_Num(LB)).path = nn.path;
                        LB.get(Find_LB_Num(LB)).dis = nn.dis;
                        LB.get(Find_LB_Num(LB)).score = nn.score;
                        LB.get(Find_LB_Num(LB)).w_poi = nn.w_poi;
                    }
                    endTime2 = System.currentTimeMillis();
                    time6 = endTime2 - startTime2 + time6;
                }

                endTime2 = System.currentTimeMillis();
                time5 = endTime2 - startTime2 + time5;
                num++;

            }



        }
//        System.out.println("第一部分所消耗的时间为："+ time2);
//        System.out.println("第二部分所消耗的时间为："+ time3);
//        System.out.println("第三部分所消耗的时间为："+ time3);
//        System.out.println("第四部分所消耗的时间为："+ time4);
//        System.out.println("第五部分所消耗的时间为："+ time5);
//        System.out.println("第六部分所消耗的时间为："+ time6);
        return LB;
    }

    public ArrayList<Lower_bound> Find_Top_k (ArrayList<Lower_bound> a,int k) {
        ArrayList<Lower_bound> top_k = new ArrayList<>();
        int n = 0;
        for (int i = 0; i < a.size(); i++) {
            if (top_k.size() < k && a.get(i).dis != 0){
                top_k.add(a.get(i));
            } else {
                if (a.size() != 0 && top_k.size() != 0){
                    n = Find_LB_Num(top_k);
                    if (a.get(i).score > top_k.get(n).score && a.get(i).dis != 0){
                        top_k.remove(n);
                        top_k.add(a.get(i));
                    }
                }

            }
        }
        return top_k;
    }

    public double Find_LB (ArrayList<Lower_bound> Top_k){
        //int LB = 0;
        double LB_score = Double.MAX_VALUE;
        for (int i = 0; i < Top_k.size(); i++) {
            if (LB_score > Top_k.get(i).score){
                LB_score = Top_k.get(i).score;
                //LB = i;
            }

        }
        return LB_score;
    }

    public int Find_LB_Num (ArrayList<Lower_bound> Top_k){
        int LB = 0;
        double LB_score = Double.MAX_VALUE;
        for (int i = 0; i < Top_k.size(); i++) {
            if (LB_score > Top_k.get(i).score){
                LB_score = Top_k.get(i).score;
                LB = i;
            }

        }
        return LB;
    }

    //计算多个数的全排列
    public static ArrayList<ArrayList<Integer>> allsort(int a[],ArrayList<ArrayList<Integer>> order, int currentlocal) // currentlocal当前指向的位置
    {
        ArrayList<ArrayList<Integer>> order2 = new ArrayList<>();
        if (currentlocal == a.length - 1) // 当当前位置指到最后一个元素时，该元素后面已经没有其他元素可以跟他交换位置，即已产生一个组合数
        {
            order.add(new ArrayList<Integer>());
            for (int number : a) {
                order.get(order.size()-1).add(number);
            }
            // System.out.println();
        } else {
            for (int i = currentlocal; i < a.length; i++) {
                int temp = a[i];
                a[i] = a[currentlocal];
                a[currentlocal] = temp;

                order = allsort(a, order,currentlocal + 1);//交换两个元素的位置后，对其后面的元素全排列

                temp = a[i];   //两个元素交换位置后重新恢复原位
                a[i] = a[currentlocal];
                a[currentlocal] = temp;

            }
        }
        return order;
    }

    public ArrayList<ArrayList<ArrayList<Integer>>> Find_Path2(ArrayList<ArrayList<Integer>> path, int[] POI_Type, POI[] POIList, ArrayList<ArrayList<Integer>> all,
                                                               int q, int flag, double LB, double a){
        ArrayList<ArrayList<ArrayList<Integer>>> find_Path = new ArrayList<>();
        ArrayList<Integer> x = new ArrayList<>();
        ArrayList<Integer> y = new ArrayList<>();
        boolean flag_x = true;
        boolean flag_y = true;
        double x_min = 0;
        double x_max = 0;
        double num16 = 0;
        ArrayList<FindTopk.x_ty> x_right = new ArrayList<>();
        ArrayList<FindTopk.x_ty> x_left = new ArrayList<>();
        ArrayList<FindTopk.x_ty> y_right = new ArrayList<>();
        ArrayList<FindTopk.x_ty> y_left = new ArrayList<>();
        ArrayList<ArrayList<Integer>> path3 = new ArrayList<>();
        for (int i = 0; i < POI_Type.length; i++) {
            path3.add(new ArrayList<Integer>());
        }
        for (int i = 0; i < path.size(); i++) {
            find_Path.add(new ArrayList<ArrayList<Integer>>());
            //找到x的两端
            x_right.clear();
            x_left.clear();
            x_max = 0;
            x_min = 0;
            for (int j = 0; j < path.get(i).size(); j++) { //计算x轴的分布情况
                if (POIList[path.get(i).get(j)].x >= POIList[q].x){ //如果这个点在q的右边
                    num16 = POIList[path.get(i).get(j)].x - POIList[q].x;
                    if (num16 < 0){
                        num16 = -num16;
                    }
                    if (num16 >= x_max){ //这个点在最右边
                        x_max = num16;
                        // x_right.add(path.get(i).get(j));
                        x_right.add(new FindTopk.x_ty(path.get(i).get(j),num16));
                    }else {
                        for (int k = 0; k < x_right.size(); k++) {
                            if (num16 <= x_right.get(k).num){
                                x_right.add(k,new FindTopk.x_ty(path.get(i).get(j),num16));
                                break;
                            }
                        }
                    }
                }else {
                    num16 = POIList[q].x - POIList[path.get(i).get(j)].x;
                    if (num16 < 0){
                        num16 = -num16;
                    }
                    if (num16 >= x_min){
                        x_min = num16;
                        x_left.add(new FindTopk.x_ty(path.get(i).get(j),num16));
                    }else {
                        for (int k = 0; k < x_left.size(); k++) {
                            if (num16 <= x_left.get(k).num){
                                x_left.add(k,new FindTopk.x_ty(path.get(i).get(j),num16));
                                break;
                            }
                        }

                    }
                }
            }

            y_right.clear();
            y_left.clear();
            x_max = 0;
            x_min = 0;

            for (int j = 0; j < path.get(i).size(); j++) { //计算y轴的分布情况
                if (POIList[path.get(i).get(j)].y >= POIList[q].y){ //如果这个点在q的右边
                    num16 = POIList[path.get(i).get(j)].y - POIList[q].y;
                    if (num16 < 0){
                        num16 = -num16;
                    }
                    if (num16 >= x_max){ //这个点在最右边
                        x_max = num16;
                        // x_right.add(path.get(i).get(j));
                        y_right.add(new FindTopk.x_ty(path.get(i).get(j),num16));
                    }else {
                        for (int k = 0; k < y_right.size(); k++) {
                            if (num16 <= y_right.get(k).num){
                                y_right.add(k,new FindTopk.x_ty(path.get(i).get(j),num16));
                                break;
                            }
                        }
                    }
                }else {
                    num16 = POIList[q].y - POIList[path.get(i).get(j)].y;
                    if (num16 < 0){
                        num16 = -num16;
                    }
                    if (num16 >= x_min){
                        x_min = num16;
                        y_left.add(new FindTopk.x_ty(path.get(i).get(j),num16));
                    }else {
                        for (int k = 0; k < y_left.size(); k++) {
                            if (num16 <= y_left.get(k).num){
                                y_left.add(k,new FindTopk.x_ty(path.get(i).get(j),num16));
                                break;
                            }
                        }
                    }
                }
            }

            x_left.add(0,new FindTopk.x_ty(q,0));
            x_right.add(0,new FindTopk.x_ty(q,0));
            y_left.add(0,new FindTopk.x_ty(q,0));
            y_right.add(0,new FindTopk.x_ty(q,0));
            if ((x_left.size()<=1&&y_left.size()<=1)||(x_left.size()<=1&&y_right.size()<=1)||
                    (x_right.size()<=1&&y_right.size()<=1)||(x_right.size()<=1&&y_left.size()<=1)){ //q是否在最左边或者最右边
                find_Path.get(find_Path.size()-1).add(new ArrayList<Integer>());
                //find_Path.get(find_Path.size()-1).get(find_Path.get(find_Path.size()-1).size()-1).add(q);
                if (x_left.size() <= 1){
                    for (int j = 0; j < x_right.size(); j++) {
                        find_Path.get(find_Path.size()-1).get(find_Path.get(find_Path.size()-1).size()-1).add(x_right.get(j).x);
                    }
                }else {
                    for (int j = 0; j < x_left.size(); j++) {
                        find_Path.get(find_Path.size()-1).get(find_Path.get(find_Path.size()-1).size()-1).add(x_left.get(j).x);
                    }
                }
                continue;
            }
            //判断左边远还是右边远
            if (LB != 0){
                int x1 = 0;
                if (x_max >= x_min){ //右边远，往左边走
                    for (int j = 0; j <= x_left.size()-2; j++) {
                        x1 += (POIList[x_left.get(j).x].x - POIList[x_left.get(j+1).x].x);
                    }
                    x1 = x1 + x1;
                    for (int j = 0; j < x_right.size()-2; j++) {
                        x1 += (POIList[x_right.get(j).x].x - POIList[x_right.get(j+1).x].x);
                    }
                }else {
                    for (int j = 0; j < x_right.size()-2; j++) {
                        x1 += (POIList[x_right.get(j).x].x - POIList[x_right.get(j+1).x].x);
                    }
                    x1 = x1 + x1;
                    for (int j = 0; j <= x_left.size()-2; j++) {
                        x1 += (POIList[x_left.get(j).x].x - POIList[x_left.get(j+1).x].x);
                    }
                }
                int POI_Num = 0;
                for (int j = 0; j < path.get(i).size(); j++) { //计算这一组兴趣点的兴趣值之和
                    POI_Num += POIList[path.get(i).get(j)].POI_Num;
                }
                double score_x = (-a)*x1 + (1-a)*POI_Num;
                if (score_x < LB){ //剪枝
                    continue;
                }
            }

            for (int j = 0; j < path3.size(); j++) {
                path3.get(j).clear();
                path3.get(j).add(path.get(i).get(j));
            }
            find_Path.get(find_Path.size()-1).addAll(Finf_Path1(all,path3,POI_Type,q));
            ArrayList<Integer> path22 = new ArrayList<>();
            for (int j = 0; j < find_Path.get(find_Path.size()-1).size(); j++) {
                path22.clear();
                path22.addAll(find_Path.get(find_Path.size()-1).get(j));
                find_Path.get(find_Path.size()-1).get(j).clear();
                find_Path.get(find_Path.size()-1).get(j).add(q);
                find_Path.get(find_Path.size()-1).get(j).addAll(path22);
            }

        }
        int num4 = 0;
        for (int i = 0; i < find_Path.size(); i++) {
            if (find_Path.get(i).size() != 0){
                num4 = num4 + find_Path.get(i).size();
            }
        }
        //num_LJ5 += num4;
        num_LJ2 = num_LJ2 + (num_LJ3 - num4);

        return find_Path;
    }


    public static ArrayList<ArrayList<Integer>> Finf_Path1(ArrayList<ArrayList<Integer>> all,
                                                           ArrayList<ArrayList<Integer>> path3,int[] POI_Type,int q){
        ArrayList<ArrayList<Integer>> path = new ArrayList<>();
        for (int n = 0; n < all.size(); n++) {
            //找到q所在子图中所有的符合要求的poi
            ArrayList<ArrayList<Integer>> path1 = new ArrayList<>();
            for (int i = 0; i < POI_Type.length; i++) {
                path1.add(new ArrayList<Integer>());
                // path1.get(i).add(q);
            }
            for (int i = 0; i < POI_Type.length; i++) {
                path1.get(i).addAll(path3.get(all.get(n).get(i)));
            }
            //找到所有的路线
            ArrayList<ArrayList<Integer>> path2 = new ArrayList<>();
            for (int i = 0; i < path1.get(0).size(); i++) {
                path2.add(new ArrayList<Integer>());
                path2.get(i).add(path1.get(0).get(i));
            }
            path.addAll(Find_Path(path1, path2,0));
        }
        return path;

    }

    public void Add_POIAsSG(ArrayList<ArrayList<Integer>> path3, int[] POI_Type, ArrayList<ArrayList<Integer>> SG,
                            POI[] POIList, ArrayList<ArrayList<Integer>> all,int q_SG){
        boolean flag12 = true;
        for (int i = 0; i < SG.get(q_SG).size(); i++) {
            if (POIList[SG.get(q_SG).get(i)].POI_Type != 0){
                for (int j = 0; j < POI_Type.length; j++) {
                    if (POIList[SG.get(q_SG).get(i)].POI_Type == POI_Type[j]){
                        path3.get(j).add(SG.get(q_SG).get(i));
                    }
                }
            }
        }
    }



}
