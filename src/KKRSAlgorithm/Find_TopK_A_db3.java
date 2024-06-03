package KKRSAlgorithm;

import entity.*;

import java.util.ArrayList;

public class Find_TopK_A_db3 {
    public static int num_LJ1 = 0;//路线剪枝1
    public static int num_LJ2 = 0;//路线剪枝2
    public static int num_LJ3 = 0;
    public static int num_LJ4 = 0;
    //public static int num_LJ5 = 0;//判断每次总的组合里面有多少条组合形式
    long startTime1 = System.currentTimeMillis(); //开始获取时间
    long endTime1 = System.currentTimeMillis(); //开始获取时间
    long time11 = 0;
    long time22 = 0;
    long time33 = 0;

    public static ArrayList<ArrayList<Integer>> Find_Path(ArrayList<ArrayList<Integer>> path1,
                                                          ArrayList<ArrayList<Integer>> path2, int k) {
        ArrayList<ArrayList<Integer>> path = new ArrayList<>();
        if (k <= path1.size() - 2) {
            int num = 0;
            for (int i = 0; i < path2.size(); i++) {
                for (int j = 0; j < path1.get(k + 1).size(); j++) {
                    path.add(new ArrayList<Integer>());
//                    path.get(num).addAll(path2.get(i));
//                    path.get(num).addAll(path1.get(j));
                    for (int l = 0; l < path2.get(i).size(); l++) {
                        path.get(num).add(path2.get(i).get(l));
                    }
                    path.get(num).add(path1.get(k + 1).get(j));
//                    if (path1.get(k+1).size() > 1){
//
//                    }else {
//                        path.get(num).add(path1.get(k+1).get(0));
//                    }

                    num++;
                }
            }
            path2 = Find_Path(path1, path, k + 1);
        } else if (k > path1.size() - 2) {
            return path2;
        }

        return path2;
    }

    //计算多个数的全排列
    public static ArrayList<ArrayList<Integer>> allsort(int[] a, ArrayList<ArrayList<Integer>> order, int currentlocal) // currentlocal当前指向的位置
    {
        ArrayList<ArrayList<Integer>> order2 = new ArrayList<>();
        if (currentlocal == a.length - 1) // 当当前位置指到最后一个元素时，该元素后面已经没有其他元素可以跟他交换位置，即已产生一个组合数
        {
            order.add(new ArrayList<Integer>());
            for (int number : a) {
                order.get(order.size() - 1).add(number);
            }
            // System.out.println();
        } else {
            for (int i = currentlocal; i < a.length; i++) {
                int temp = a[i];
                a[i] = a[currentlocal];
                a[currentlocal] = temp;

                order = allsort(a, order, currentlocal + 1);//交换两个元素的位置后，对其后面的元素全排列

                temp = a[i];   //两个元素交换位置后重新恢复原位
                a[i] = a[currentlocal];
                a[currentlocal] = temp;

            }
        }
        return order;
    }

    public static int Find_Min(ArrayList<Integer> SP) {
        int q = Integer.MAX_VALUE;
        for (int i : SP) {
            if (q > i) {
                q = i;
            }
        }
        return q;
    }

    public static ArrayList<ArrayList<ArrayList<Integer>>> Find_Path2(ArrayList<ArrayList<Integer>> path, int[] POI_Type, POI[] POIList, ArrayList<ArrayList<Integer>> all,
                                                                      int q) {
        ArrayList<ArrayList<ArrayList<Integer>>> find_Path = new ArrayList<>();
        ArrayList<Integer> x = new ArrayList<>();
        ArrayList<Integer> y = new ArrayList<>();
        boolean flag_x = true;
        boolean flag_y = true;
        ArrayList<ArrayList<Integer>> path3 = new ArrayList<>();
        for (int i = 0; i < POI_Type.length; i++) {
            path3.add(new ArrayList<Integer>());
        }
        for (int i = 0; i < path.size(); i++) {
            find_Path.add(new ArrayList<ArrayList<Integer>>());
            x.clear();
            y.clear();
            //判断这几个点桐樱岛x轴和y轴的顺序是不是一致
            x = Find_xy(path.get(i), POIList, 0);
            y = Find_xy(path.get(i), POIList, 1);
            //判断x顺序和y顺序是否一致
            flag_x = true;
            flag_y = true;
            for (int j = 0; j < x.size(); j++) {
                if (x.get(j) != y.get(j)) {
                    flag_x = false;
                    break;
                }
            }
            for (int j = 0; j < x.size(); j++) {
                if (x.get(j) != y.get(y.size() - 1 - j)) {
                    flag_y = false;
                    break;
                }
            }
//            if (flag_x == true || flag_y == true){
//                find_Path.get(find_Path.size()-1).add(new ArrayList<>());
//                find_Path.get(find_Path.size()-1).get(find_Path.get(find_Path.size()-1).size()-1).add(q);
//                if (flag_x == true){
//                    if (POIList[q].x > POIList[x.get(0)].x){ //q在最右边
//                        for (int j = x.size()-1; j < 0 ; j--) {
//                            find_Path.get(find_Path.size()-1).get(find_Path.get(find_Path.size()-1).size()-1).add(x.get(j));
//                        }
//                    }else { //q在最左边
//                        for (int j = 0; j < x.size()-1 ; j++) {
//                            find_Path.get(find_Path.size()-1).get(find_Path.get(find_Path.size()-1).size()-1).add(x.get(j));
//                        }
//                    }
//                }else{
//                    if (POIList[q].x > POIList[x.get(0)].x){ //q在最右边
//                        for (int j = x.size()-1; j < 0 ; j--) {
//                            find_Path.get(find_Path.size()-1).get(find_Path.get(find_Path.size()-1).size()-1).add(y.get(j));
//                        }
//                    }else { //q在最左边
//                        for (int j = 0; j < x.size()-1 ; j++) {
//                            find_Path.get(find_Path.size()-1).get(find_Path.get(find_Path.size()-1).size()-1).add(y.get(j));
//                        }
//                    }
//                }
//            }else {
            for (int j = 0; j < path3.size(); j++) {
                path3.get(j).clear();
                path3.get(j).add(path.get(i).get(j));
            }
            find_Path.get(find_Path.size() - 1).addAll(Finf_Path1(all, path3, POI_Type, q));
            ArrayList<Integer> path22 = new ArrayList<>();
            for (int j = 0; j < find_Path.get(find_Path.size() - 1).size(); j++) {
                path22.clear();
                path22.addAll(find_Path.get(find_Path.size() - 1).get(j));
                find_Path.get(find_Path.size() - 1).get(j).clear();
                find_Path.get(find_Path.size() - 1).get(j).add(q);
                find_Path.get(find_Path.size() - 1).get(j).addAll(path22);
            }
//            }

        }
        int num4 = 0;
        for (int i = 0; i < find_Path.size(); i++) {
            if (find_Path.get(i).size() != 0) {
                num4 = num4 + find_Path.get(i).size();
            }
        }
        //num_LJ5 += num4;
        num_LJ2 = num_LJ2 + (num_LJ3 - num4);

        return find_Path;
    }

    public static ArrayList<Integer> Find_xy(ArrayList<Integer> path, POI[] POIList, int x) {
        ArrayList<Integer> Find_xy = new ArrayList<>();
        ArrayList<Integer> path1 = new ArrayList<>();
        path1.addAll(path);
        int num = 0;
        while (path1.size() != 0) {
            num = 0;
            if (x == 0) {
                if (path1.size() == 1) {
                    Find_xy.add(path1.get(0));
                    path1.remove(0);
                } else {
                    for (int i = 1; i < path1.size(); i++) {
                        if (POIList[path1.get(i)].x < POIList[path1.get(num)].x) {
                            num = i;
                        }
                    }
                    Find_xy.add(path1.get(num));
                    path1.remove(num);
                }
            } else {
                if (path1.size() == 1) {
                    Find_xy.add(path1.get(0));
                    path1.remove(0);
                } else {
                    for (int i = 1; i < path1.size(); i++) {
                        if (POIList[path1.get(i)].y < POIList[path1.get(num)].y) {
                            num = i;
                        }
                    }
                    Find_xy.add(path1.get(num));
                    path1.remove(num);
                }
            }
        }
        return Find_xy;
    }

    public static ArrayList<ArrayList<Integer>> Finf_Path1(ArrayList<ArrayList<Integer>> all,
                                                           ArrayList<ArrayList<Integer>> path3, int[] POI_Type, int q) {
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
            path.addAll(Find_Path(path1, path2, 0));
        }
        return path;

    }

    public ArrayList<LowerBound> Top_k_db3(Graph g, int q, int q_SG, int k, int[] POI_Type, ArrayList<ArrayList<Integer>> SG, ArrayList<ArrayList<Path>> List, POI[] POIList,
                                           double a, ArrayList<ArrayList<BpPath>> BPList, ArrayList<ArrayList<Integer>> PointMinBP) throws InterruptedException {
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
        if (!flag4) {
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
                    if (flag6) {
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
        startTime1 = System.currentTimeMillis();
        path = Find_Path2(path2, POI_Type, POIList, all, q);
        //从这些路径中选出k条q到第一节点最短的路径

        //ArrayList<ArrayList<Integer>> path_k = Find_path_k(q,path, k, q_SG,POIList, List,PointMinBP,BPList);
        endTime1 = System.currentTimeMillis();
        time11 = time11 + endTime1 - startTime1;
        // startTime1 = System.currentTimeMillis();
        startTime1 = System.currentTimeMillis();
        ArrayList<LowerBound> allPath = Find_allPath(path, List, POIList, a, q_BP, Min_w, BPList, PointMinBP, k);
        endTime1 = System.currentTimeMillis();
        time33 = time33 + endTime1 - startTime1;
        // ArrayList<Lower_bound> allPath = new ArrayList<>();
        //endTime1 = System.currentTimeMillis();
        //time1 = endTime1 - startTime1;
        // System.out.println("路线剪枝测试成功,所用的时间为："+time1);

        ArrayList<LowerBound> Top_k = Find_Top_k(allPath, k);
        double LB = Find_LB(Top_k);
        //找到W_max
        int w_max = 0;
        int w1 = 0;
        int w1_Num = 0;
        for (int i = 0; i < POI_Type.length; i++) {
            w1_Num = 0;
            for (int j = 0; j < POIList.length; j++) {
                if (POIList[j].POI_Type == POI_Type[i] && POIList[j].POI_Num > w1_Num) {
                    w1_Num = POIList[j].POI_Num;
                }
            }
            w_max = w_max + w1_Num;
        }

        SG_num[q_SG] = 1;
        int SG_num1 = 1;//已经搜索了多少个子图
        ArrayList<Integer> SG_n = new ArrayList<>(); //判断现在有多少个图需要扩展
        int n2 = 0; // n2大于等于7时，算法结束
        int m = (int) Math.sqrt(SG.size());
        SG_n.add(q_SG);
        int n;
        boolean flag = true;

        //网格剪枝
        int num22 = 0;
        int n5 = 0; //用来标记这次循环需要检查多少个图
        int n6 = 0;
        int n7 = 0;
        ArrayList<SGPoi> NoPOI = new ArrayList<>();
        ArrayList<Integer> NOPOI_Num = new ArrayList<>();
//        for (int i = 0; i < POI_Type.length; i++) {
//            NoPOI.add(new ArrayList<>());
//        }
        while (flag) {
            ArrayList<Integer> SG3 = new ArrayList<>();
            n2 = 0;//用来存放应该加入SG_n的子图编号
            n5 = 0;
            n6 = 0;
            for (int i = 0; i < SG_n.size(); i++) {
                // System.out.println("w_max="+w_max+",LB="+LB);
                R = ((1 - a) * w_max - LB) / a;
                n = SG_n.get(i);
                int[] n1 = {n - m + 1, n - m, n - m - 1, n - 1, n + 1, n + m - 1, n + m, n + m + 1};
                ArrayList<SGPoi> SGPoi = new ArrayList<>();
                //计算这个子图中所有符合条件的POI
                for (int j = 0; j < 8; j++) {
                    if (n1[j] < SG.size()) {//SG.size()
                        if (n1[j] >= 0 && SG_num[n1[j]] == 0) {
                            n5++;
                            SGPoi.add(new SGPoi());
                            SGPoi.get(SGPoi.size() - 1).num = n1[j];
                            SG_num[n1[j]] = 1;
                            for (int nn = 0; nn < POI_Type.length; nn++) {
                                SGPoi.get(SGPoi.size() - 1).POI.add(new ArrayList<Integer>());
                            }
                            Add_POIAsSG(SGPoi.get(SGPoi.size() - 1).POI, POI_Type, SG, POIList, all, SGPoi.get(SGPoi.size() - 1).num);
                        } else {
                            n2++;
                        }
                    }
                }
                for (int j = 0; j < SGPoi.size(); j++) {
                    SG3.add(SGPoi.get(j).num);
                }

                int n3 = 0;//用来记录SG编号

                //寻找周围几个子图中每个边界顶点的最短路径
                boolean flag3 = true;

                for (int j = 0; j < SGPoi.size(); j++) {//判断这个子图周围的n个子图是否符合条件
                    //将NoPOI中的POI加入SGPoi
                    n = SGPoi.get(j).num;
                    int[] n22 = {n - m + 1, n - m, n - m - 1, n - 1, n + 1, n + m - 1, n + m, n + m + 1};
                    for (int l = 0; l < n22.length; l++) { //只添加SGPoi.get(j)子图周围八个子图中的POI
                        if (n22[l] < SG.size() && n22[l] > 0) {
                            //System.out.println("开始添加SGPoi.get(j)子图周围八个子图中的POI");
                            for (int o = 0; o < NoPOI.size(); o++) {
                                flag3 = true;
                                if (NoPOI.get(o).num == n22[l]) {
                                    for (int p = 0; p < POI_Type.length; p++) {
                                        SGPoi.get(j).POI.get(p).addAll(NoPOI.get(o).POI.get(p));
                                    }
                                }
                            }
                        }
                    }

                    //System.out.println("判断这个子图周围的n个子图是否符合条件");
                    if (SG_num[SGPoi.get(j).num] >= 0) {
                        flag3 = true;
                        for (int l = 0; l < SGPoi.get(j).POI.size(); l++) {//判断这个子图是否有全部的兴趣点
                            if (SGPoi.get(j).POI.get(l).size() == 0) {
                                flag3 = false;
                                //break;
                                break;
                            }
                        }
                        int q_TargetSG_w = 0;
                        // System.out.println("判断这个子图是否有全部兴趣点："+flag3);
                        if (flag3) {
                            NoPOI.clear();
                            //如果有，查找q到这个子图的最短路径
                            NOPOI_Num.add(SGPoi.get(j).num);
                            q_TargetSG_w = Integer.MAX_VALUE;
                            for (int l = 0; l < NOPOI_Num.size(); l++) {
                                if (BPList.get(q_BP).get(NOPOI_Num.get(l)).weight < q_TargetSG_w) {
                                    q_TargetSG_w = BPList.get(q_BP).get(NOPOI_Num.get(l)).weight;
                                }
                            }
                            NOPOI_Num.clear();

                            SG_num[SGPoi.get(j).num] = 1;
                            int min = q_TargetSG_w + Min_w; //找到这些路径中最短的一条
                            //System.out.println("min="+min+",R="+R);
                            n7++;

                            num45 = 1;
                            for (int l = 0; l < SGPoi.get(j).POI.size(); l++) {
                                if (SGPoi.get(j).POI.get(l).size() != 0) {
                                    num45 = num45 * SGPoi.get(j).POI.get(l).size();
                                }
                            }
                            num_LJ1 = num_LJ1 + num45 * all.size();
                            num_LJ3 = num45 * all.size();
                            ArrayList<ArrayList<Integer>> path8 = Finf_Path1(all1, SGPoi.get(j).POI, POI_Type, q);
                            startTime1 = System.currentTimeMillis();
                            ArrayList<ArrayList<ArrayList<Integer>>> path7 = Find_Path2(path8, POI_Type, POIList, all, q);
                            endTime1 = System.currentTimeMillis();
                            time11 = time11 + endTime1 - startTime1;
                            //startTime1 = System.currentTimeMillis();
                            int num33 = 0;
                            for (int l = 0; l < SGPoi.get(j).POI.size(); l++) {
                                num33 = 0;
                                for (int o = 0; o < SGPoi.get(j).POI.get(l).size(); o++) {
                                    num33 = num33 + SGPoi.get(j).POI.get(l).size();
                                }
                                //System.out.println("第"+l+"个POI_Type的个数为:"+num33);
                            }
                            //path_k = Find_path_k(q,path7, k, q_SG,POIList, List,PointMinBP,BPList);
                            //long startTime2 = System.currentTimeMillis(); //开始获取时间
                            startTime1 = System.currentTimeMillis();
                            ArrayList<LowerBound> allPath4 = Find_allPath(path7, List, POIList, a, q_BP, Min_w, BPList, PointMinBP, k);
                            endTime1 = System.currentTimeMillis();
                            time33 = time33 + endTime1 - startTime1;
                            //long endTime2 = System.currentTimeMillis(); //开始获取时间
                            //long time2 = endTime2 - startTime2;

                            //endTime1 = System.currentTimeMillis();
                            //time2 = endTime2 - startTime2;
                            ArrayList<LowerBound> Top_k4 = Find_Top_k(allPath4, k);
                            //System.out.println("Top_k4.size="+Top_k4.size());

                            int top_min;
                            //修改Top_k
//                                System.out.println("网格剪枝阶段计算全部路径所用时间为："+time2);
//                                System.out.println("   ");
//                                System.out.println("   ");
//                                System.out.println("   ");
//                                System.out.println("   ");

                            if (Top_k.size() < k) {
                                boolean flag99 = true;
                                for (int l = 0; l < Top_k4.size(); l++) {
                                    flag99 = true;
                                    for (int o = 0; o < Top_k.size(); o++) {
                                        if (Top_k4.get(l).score == Top_k.get(o).score) {
                                            flag99 = false;
                                            break;
                                        }
                                    }
                                    if (flag99) {
                                        Top_k.add(Top_k4.get(l));
                                    }
                                }
                            } else {
                                for (int l = 0; l < Top_k4.size(); l++) {
                                    top_min = Find_LB_Num(Top_k);
                                    if (Top_k4.get(l).score > Top_k.get(top_min).score && Top_k4.get(l).score < Double.MAX_VALUE) {
                                        Top_k.remove(top_min);
                                        Top_k.add(Top_k4.get(l));
                                    }
                                }
                            }
                            // System.out.println("Top_K中含有的路径数目为："+Top_k.size());
                            //修改LB
                            LB = Find_LB(Top_k);
                            //System.out.println("1");

                            // System.out.println("1");
                        } else {
                            NoPOI.add(new SGPoi());
                            NoPOI.get(NoPOI.size() - 1).POI.addAll(SGPoi.get(j).POI);

                            NOPOI_Num.add(SGPoi.get(j).num);
                            //System.out.println("这个图不符合要求,加入NOPOI,目前NOPOI中的子图为："+NOPOI_Num);
                            n2++;
                        }
                    } else {
                        continue;
                    }

                }
                SG_num[SG_n.get(i)] = 1;
            }
            //System.out.println("n2="+n2);
            //判断是否需要推出循环
            // System.out.println("n5="+n5+",n6="+n6);
            if (n6 == n5) {
                //flag = false;
                break;
            }
            int n8 = 0;
            for (int j = 0; j < SG_num.length; j++) {
                if (SG_num[j] > 0) {
                    n8++;
                }
            }
            if (n8 == SG_num.length) {
                //flag = false;
                break;
            }

            SG_n.clear();
            SG_n.addAll(SG3);
            SG3.clear();
        }
        //System.out.println("1");
        //计算多少个子图被计算过
        int num12 = 0;
        //int num22 = 0;
        for (int i = 0; i < SG_num.length; i++) {
            if (SG_num[i] > 0) {
                num12++;
            }
//            if (SG_num[i] == 2){
//                num22++;
//            }
        }
        System.out.println("A算法(不进行优化)一共划分了" + SG_num.length + "个子图，其中计算了" + n7 + "个子图,网格剪枝了" + n6 + "个子图");
        double num17 = num_LJ2 + num_LJ4;
        double num18 = num_LJ1;
        double BL = num17 / num18;
        System.out.println("A算法(不进行优化)一共进行的路线查找次数" + num_LJ1 + ",总剪枝的路线的数目为：" + (num_LJ2 + num_LJ4) + ",剪枝效率为：" + BL +
                ",其中第一阶段剪枝的路线为：" + num_LJ2 + ",第二阶段剪枝的路线为：" + num_LJ4);
        System.out.println("第一阶段剪枝消耗的时间为" + time11 + ",第二阶段剪枝消耗的时间为：" + time33);
//        System.out.print("A找到的最优路径的score为：");
//        for (int i = 0; i < Top_k.size(); i++) {
//            System.out.print(Top_k.get(i).score);
//            System.out.print(", ");
//        }
//        System.out.println("");
        //System.out.println("路线剪枝共消耗时间为："+time11);
        //System.out.println("路线剪枝中一部分消耗时间为："+time22);
        return Top_k;
    }

    public ArrayList<LowerBound> Find_allPath(ArrayList<ArrayList<ArrayList<Integer>>> path, ArrayList<ArrayList<Path>> List, POI[] POIList, double a,
                                              int q_BP, int w_BP, ArrayList<ArrayList<BpPath>> BPList, ArrayList<ArrayList<Integer>> PointList, int k) throws InterruptedException {
        ArrayList<LowerBound> LB = new ArrayList<>();
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
        ArrayList<Integer> path1 = new ArrayList<>(); //存放路径
        //num_LJ1 += path.size();
        int POI_NUM;
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
                for (int j = 0; j < path.get(i).get(ii).size() - 1; j++) {
                    q_index = path.get(i).get(ii).get(j);
                    q_index1 = path.get(i).get(ii).get(j + 1);
                    x = POIList[q_index].x - POIList[q_index1].x;
                    y = POIList[q_index].y - POIList[q_index1].y;
                    w += (int) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
                }
                //判断是否需要剪枝
                if (LB.size() != 0) {
                    if (w != 0)
                        score1 = (-a) * w + (1 - a) * POI_NUM;
                    score_Min = LB.get(Find_LB_Num(LB)).score;
                    if (score1 <= score_Min) {
                        num_LJ4++;
                        break;
                    }
                }
                w = 0;
                path1.clear();

                for (int j = 0; j < path.get(i).get(ii).size() - 1; j++) {
                    q_index = path.get(i).get(ii).get(j);
                    q_index1 = path.get(i).get(ii).get(j + 1);
                    q_BP1 = PointList.get(q_index).get(0);
                    w_BP1 = PointList.get(q_index).get(1);
                    if (POIList[q_index].SG == POIList[q_index1].SG) {
                        for (int k1 = 0; k1 < List.get(q_index).size(); k1++) {
                            if (List.get(q_index).get(k1).end == q_index1) {
                                w = w + List.get(q_index).get(k1).weight;
                                break;
                            }
                        }
                    } else {
                        if (q_BP1 != Integer.MAX_VALUE) {
                            w = w + w_BP1 + BPList.get(q_BP1).get(POIList[q_index1].SG).weight;
                            for (int k1 = 0; k1 < List.get(BPList.get(q_BP1).get(POIList[q_index1].SG).target).size(); k1++) {
                                if (List.get(BPList.get(q_BP1).get(POIList[q_index1].SG).target).get(k1).end == q_index1) {
                                    w = w + List.get(BPList.get(q_BP1).get(POIList[q_index1].SG).target).get(k1).weight;
                                    break;
                                }
                            }
                        } else {
                            w = 0;
                        }
                    }
                }
                //计算这组poi的路径长度w
                startTime2 = System.currentTimeMillis();
                LowerBound nn = new LowerBound();
                if (LB.size() < k) {
                    LB.add(new LowerBound());
                    LB.get(LB.size() - 1).path.clear();
                    LB.get(LB.size() - 1).path.addAll(path.get(i).get(ii));
                    LB.get(LB.size() - 1).dis = w;
                    for (int kk = 0; kk < LB.get(LB.size() - 1).path.size(); kk++) {
                        //LB.get(num).w_poi += POIList[LB.get(num).path.get(k)].POI_Num;
                        LB.get(LB.size() - 1).w_poi += POIList[path.get(i).get(ii).get(kk)].POI_Num;
                    }
                    LB.get(LB.size() - 1).score = (-a) * LB.get(LB.size() - 1).dis + (1 - a) * LB.get(LB.size() - 1).w_poi;
                } else {
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
                    nn.score = (-a) * nn.dis + (1 - a) * nn.w_poi;
                    if (nn.score > score_Min) {
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

    public ArrayList<LowerBound> Find_Top_k(ArrayList<LowerBound> a, int k) {
        ArrayList<LowerBound> top_k = new ArrayList<>();
        int n = 0;
        for (int i = 0; i < a.size(); i++) {
            if (top_k.size() < k && a.get(i).dis != 0) {
                top_k.add(a.get(i));
            } else {
                if (a.size() != 0 && top_k.size() != 0) {
                    n = Find_LB_Num(top_k);
                    if (a.get(i).score > top_k.get(n).score && a.get(i).dis != 0) {
                        top_k.remove(n);
                        top_k.add(a.get(i));
                    }
                }

            }
        }
        return top_k;
    }

    public double Find_LB(ArrayList<LowerBound> Top_k) {
        //int LB = 0;
        double LB_score = Double.MAX_VALUE;
        for (int i = 0; i < Top_k.size(); i++) {
            if (LB_score > Top_k.get(i).score) {
                LB_score = Top_k.get(i).score;
                //LB = i;
            }

        }
        return LB_score;
    }

    public int Find_LB_Num(ArrayList<LowerBound> Top_k) {
        int LB = 0;
        double LB_score = Double.MAX_VALUE;
        for (int i = 0; i < Top_k.size(); i++) {
            if (LB_score > Top_k.get(i).score) {
                LB_score = Top_k.get(i).score;
                LB = i;
            }

        }
        return LB;
    }

    public int Find_LB_Num_Min(ArrayList<LowerBound> Top_k) {
        int LB = 0;
        double LB_score = Double.MAX_VALUE;
        for (int i = 0; i < Top_k.size(); i++) {
            if (LB_score < Top_k.get(i).score) {
                LB_score = Top_k.get(i).score;
                LB = i;
            }

        }
        return LB;
    }

    public void Add_POIAsSG(ArrayList<ArrayList<Integer>> path3, int[] POI_Type, ArrayList<ArrayList<Integer>> SG,
                            POI[] POIList, ArrayList<ArrayList<Integer>> all, int q_SG) {
        boolean flag12 = true;
        for (int i = 0; i < SG.get(q_SG).size(); i++) {
            if (POIList[SG.get(q_SG).get(i)].POI_Type != 0) {
                for (int j = 0; j < POI_Type.length; j++) {
                    if (POIList[SG.get(q_SG).get(i)].POI_Type == POI_Type[j]) {
                        path3.get(j).add(SG.get(q_SG).get(i));
                    }
                }
            }
        }
    }

    public ArrayList<ArrayList<Integer>> Find_path_k(int q, ArrayList<ArrayList<Integer>> path, int k, int q_SG, POI[] POIList, ArrayList<ArrayList<Path>> List,
                                                     ArrayList<ArrayList<Integer>> PointMinBP, ArrayList<ArrayList<BpPath>> BPList) {
        num_LJ1 += path.size();
        ArrayList<ArrayList<Integer>> path_k = new ArrayList<>();
        ArrayList<Integer> path1 = new ArrayList<>();
        int w1 = 0;
        int w_max = 0;
        int num = 0;
        for (int i = 0; i < path.size(); i++) {
            if (path_k.size() < k) {
                path_k.add(new ArrayList<Integer>());
                path_k.get(path_k.size() - 1).addAll(path.get(i));
                if (q_SG == POIList[path.get(i).get(0)].SG) { //如果在同一个子图中
                    for (int j = 0; j < List.get(q).size(); j++) {
                        if (List.get(q).get(j).end == path.get(i).get(0)) {
                            path_k.get(path_k.size() - 1).add(List.get(q).get(j).weight);
                            break;
                        }
                    }
                } else {
                    w1 = 0;
                    w1 += PointMinBP.get(q).get(1);
                    w1 += BPList.get(PointMinBP.get(q).get(0)).get(POIList[path.get(i).get(0)].SG).weight;
                    w1 += PointMinBP.get(path.get(i).get(0)).get(1);
                    path_k.get(path_k.size() - 1).add(w1);
                }
            } else {
                //查找path_k中查询点到第一个顶点最长的一个
                w_max = Integer.MAX_VALUE;
                long startTime1 = System.currentTimeMillis();
                for (int j = 0; j < path_k.size(); j++) {
                    if (w_max > path_k.get(j).get(path_k.get(j).size() - 1)) {
                        w_max = path_k.get(j).get(path_k.get(j).size() - 1);
                        num = j;
                    }
                }
                long endTime1 = System.currentTimeMillis();
                time22 = time22 + endTime1 - startTime1;
                //判断查询点和第一个顶点是否在同一个子图中
                w1 = 0;
                if (q_SG == POIList[path.get(i).get(0)].SG) { //如果在同一个子图中
                    for (int j = 0; j < List.get(q).size(); j++) {
                        if (List.get(q).get(j).end == path.get(i).get(0)) {
                            w1 = List.get(q).get(j).weight;
                            //path_k.get(path_k.size()-1).add(List.get(q).get(j).w);
                            break;
                        }
                    }

                } else {
                    w1 += PointMinBP.get(q).get(1);
                    w1 += BPList.get(PointMinBP.get(q).get(0)).get(POIList[path.get(i).get(0)].SG).weight;
                    w1 += PointMinBP.get(path.get(i).get(0)).get(1);
                    //path_k.get(path_k.size()-1).add(w1);
                }
                if (w1 < w_max) {
                    path_k.get(num).clear();
                    path_k.get(num).addAll(path.get(i));
                    path_k.get(num).add(w1);
                }

            }
        }
        for (int i = 0; i < path_k.size(); i++) {
            path1.clear();
            path1.addAll(path_k.get(i));
            path_k.get(i).clear();
            path_k.get(i).add(q);
            path_k.get(i).addAll(path1);
            path_k.get(i).remove(path_k.get(i).size() - 1);
        }

        num_LJ2 += (path.size() - path_k.size());
        return path_k;
    }

    public class POI_B {
        public int x; //顶点所在x轴子图
        public int y;//顶点所在y轴子图
        public int SG;
        public ArrayList<Integer> path;//在这个小小子图中的顶点的编号
        // public ArrayList<Integer> path_SG ;//在这个小小子图中的顶点所在的子图
        public ArrayList<Integer> otherPath;//这组顶点中对其他组进行剪枝的点的编号

        public POI_B() {
            this.x = 0;
            this.y = 0;
            this.SG = 0;
            this.path = new ArrayList<>();
            this.otherPath = new ArrayList<>();
        }
    }

    public class SGPoi {
        public int num; //子图的编号
        public ArrayList<ArrayList<Integer>> POI;

        public SGPoi() {
            this.num = 0;
            this.POI = new ArrayList<>();
        }
    }


}
