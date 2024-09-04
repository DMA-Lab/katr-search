package baseline.ROSE;

import entity.*;

import java.util.ArrayList;

public class Find_Topk_NoOpt {
    public static int num_LJ1 = 0;//路线剪枝1
    public static int num_LJ2 = 0;//路线剪枝2
    long startTime1 = System.currentTimeMillis(); //开始获取时间
    long endTime1 = System.currentTimeMillis(); //开始获取时间
    long time1 = 0;

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

    public static ArrayList<ArrayList<Integer>> Finf_Path1(ArrayList<ArrayList<Integer>> all,
                                                           ArrayList<ArrayList<Integer>> path3, int[] Poi_Type, int q) {
        ArrayList<ArrayList<Integer>> path = new ArrayList<>();
        for (int n = 0; n < all.size(); n++) {
            //找到q所在子图中所有的符合要求的poi
            ArrayList<ArrayList<Integer>> path1 = new ArrayList<>();
            for (int i = 0; i < Poi_Type.length; i++) {
                path1.add(new ArrayList<Integer>());
                // path1.get(i).add(q);
            }
            for (int i = 0; i < Poi_Type.length; i++) {
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

    public ArrayList<LowerBound> Top_k_NOOPT(Graph g, int q, int q_SG, int k, int[] Poi_Type, ArrayList<ArrayList<Integer>> SG, ArrayList<ArrayList<Path>> List, Poi[] PoiList,
                                             double a, ArrayList<ArrayList<PoiPath>> BPList, ArrayList<ArrayList<Integer>> PointMinBP) {
        //找到查询点q所在的子图
        boolean flag1 = false;
        //int q_SG = 0;
        int[] SG_num = new int[SG.size()]; //判断这个子图是否已经被搜索过

        int[] a2 = new int[Poi_Type.length];
        for (int i = 0; i < Poi_Type.length; i++) {
            a2[i] = i;
        }

        //获得这些数字的全排列
        ArrayList<ArrayList<Integer>> order1 = new ArrayList<>();
        ArrayList<ArrayList<Integer>> all = allsort(a2, order1, 0);
        ArrayList<ArrayList<Integer>> path = new ArrayList<>();
        ArrayList<ArrayList<Poi_B>> path3 = new ArrayList<>();
        boolean flag12 = true;
        //将q_SG中的Poi加入path3
        for (int i = 0; i < Poi_Type.length; i++) {
            path3.add(new ArrayList<Poi_B>());

        }
        Add_PoiAsSG(path3, Poi_Type, SG, PoiList, all, q_SG);
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
            System.out.println("初始子图中不包括全部的Poi类型");
            boolean flag6 = false;
            for (int m = 0; m < num44; m++) {
                for (int i = 0; i < SG.size(); i++) {
                    flag6 = false;
                    for (int j = 0; j < SG.get(i).size(); j++) {
                        if (PoiList[SG.get(i).get(j)].Poi_Type == Poi_Type[NoType.get(m)]) {
                            path3.get(NoType.get(m)).add(new Poi_B());
                            path3.get(NoType.get(m)).get(path3.get(NoType.get(m)).size() - 1).x = PoiList[SG.get(i).get(j)].x;
                            path3.get(NoType.get(m)).get(path3.get(NoType.get(m)).size() - 1).y = PoiList[SG.get(i).get(j)].y;
                            path3.get(NoType.get(m)).get(path3.get(NoType.get(m)).size() - 1).path.add(SG.get(i).get(j));

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
        int q_BP = PointMinBP.get(q).get(0);
        int Min_w = PointMinBP.get(q).get(1);
        ArrayList<Integer> q_BP_Path = new ArrayList<>();
        ArrayList<ArrayList<Integer>> path5 = new ArrayList<>();
        for (int i = 0; i < Poi_Type.length; i++) {
            path5.add(new ArrayList<Integer>());
        }
        for (int i = 0; i < Poi_Type.length; i++) {
            for (int j = 0; j < path3.get(i).size(); j++) {
                path5.get(i).addAll(path3.get(i).get(j).path);
            }
        }
        path = Finf_Path1(all, path5, Poi_Type, q);
        startTime1 = System.currentTimeMillis();
        ArrayList<LowerBound> allPath = Find_allPath(path, path3, q, q_SG, List, PoiList, a, g, q_BP, Min_w, all, BPList, SG, PointMinBP, k);
        endTime1 = System.currentTimeMillis();
        time1 = endTime1 - startTime1;
        System.out.println("路线剪枝测试成功,所用的时间为：" + time1);
        ArrayList<LowerBound> Top_k = Find_Top_k(allPath, k);
        double LB = Find_LB(Top_k);
        //找到W_max
        int w_max = 0;
        int w1 = 0;
        int w1_Num = 0;
        for (int i = 0; i < Poi_Type.length; i++) {
            w1_Num = 0;
            for (int j = 0; j < PoiList.length; j++) {
                if (PoiList[j].Poi_Type == Poi_Type[i] && PoiList[j].Poi_Num > w1_Num) {
                    w1_Num = PoiList[j].Poi_Num;
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
        ArrayList<ArrayList<Poi_B>> NoPoi = new ArrayList<>();
        ArrayList<Integer> NOPoi_Num = new ArrayList<>();
        for (int i = 0; i < Poi_Type.length; i++) {
            NoPoi.add(new ArrayList<Poi_B>());
        }
        while (flag) {
            ArrayList<Integer> SG3 = new ArrayList<>();
            n2 = 0;//用来存放应该加入SG_n的子图编号
            n5 = 0;
            n6 = 0;
            for (int i = 0; i < SG_n.size(); i++) {
                System.out.println("w_max=" + w_max + ",LB=" + LB);
                R = ((1 - a) * w_max - LB) / a;
                n = SG_n.get(i);
                int[] n1 = {n - m + 1, n - m, n - m - 1, n - 1, n + 1, n + m - 1, n + m, n + m + 1};
                ArrayList<SGPoi> SGPoi = new ArrayList<>();
                //计算这个子图中所有符合条件的Poi
                for (int j = 0; j < 8; j++) {
                    if (n1[j] < SG.size()) {//SG.size()
                        if (n1[j] >= 0 && SG_num[n1[j]] == 0) {
                            n5++;
                            SGPoi.add(new SGPoi());
                            SGPoi.get(SGPoi.size() - 1).num = n1[j];
                            SG_num[n1[j]] = 1;
                            for (int nn = 0; nn < Poi_Type.length; nn++) {
                                SGPoi.get(SGPoi.size() - 1).Poi.add(new ArrayList<Poi_B>());

                            }
                            Add_PoiAsSG(SGPoi.get(SGPoi.size() - 1).Poi, Poi_Type, SG, PoiList, all, SGPoi.get(SGPoi.size() - 1).num);
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
                    //将NoPoi中的Poi加入SGPoi
                    n = SGPoi.get(j).num;
                    int[] n22 = {n - m + 1, n - m, n - m - 1, n - 1, n + 1, n + m - 1, n + m, n + m + 1};
                    for (int l = 0; l < n22.length; l++) { //只添加SGPoi.get(j)子图周围八个子图中的Poi
                        if (n22[l] < SG.size() && n22[l] > 0) {
                            //System.out.println("开始添加SGPoi.get(j)子图周围八个子图中的Poi");
                            for (int o = 0; o < NoPoi.size(); o++) {
                                for (int p = 0; p < NoPoi.get(o).size(); p++) {
                                    flag3 = true;
                                    if (NoPoi.get(o).get(p).SG == n22[l]) {
                                        for (int r = 0; r < SGPoi.get(j).Poi.get(o).size(); r++) {
                                            if (SGPoi.get(j).Poi.get(o).get(r).x == NoPoi.get(o).get(p).x &&
                                                    SGPoi.get(j).Poi.get(o).get(r).y == NoPoi.get(o).get(p).y) {
                                                SGPoi.get(j).Poi.get(o).get(r).path.addAll(NoPoi.get(o).get(p).path);
                                                flag3 = false;
                                            }
                                        }
                                        if (!flag3) {
                                            SGPoi.get(j).Poi.get(o).add(new Poi_B());
                                            //SGPoi.get(j).Poi.get(o).addAll((Collection<? extends Poi_B>) NoPoi.get(o).get(p));
                                            SGPoi.get(j).Poi.get(o).get(SGPoi.get(j).Poi.get(o).size() - 1).x = NoPoi.get(o).get(p).x;
                                            SGPoi.get(j).Poi.get(o).get(SGPoi.get(j).Poi.get(o).size() - 1).y = NoPoi.get(o).get(p).y;
                                            SGPoi.get(j).Poi.get(o).get(SGPoi.get(j).Poi.get(o).size() - 1).path.addAll(NoPoi.get(o).get(p).path);
                                            SGPoi.get(j).Poi.get(o).get(SGPoi.get(j).Poi.get(o).size() - 1).otherPath.addAll(NoPoi.get(o).get(p).otherPath);
                                            SGPoi.get(j).Poi.get(o).get(SGPoi.get(j).Poi.get(o).size() - 1).SG = NoPoi.get(o).get(p).SG;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    System.out.println("判断这个子图周围的n个子图是否符合条件");
                    if (SG_num[SGPoi.get(j).num] >= 0) {
                        flag3 = true;
                        for (int l = 0; l < SGPoi.get(j).Poi.size(); l++) {//判断这个子图是否有全部的兴趣点
                            if (SGPoi.get(j).Poi.get(l).size() == 0) {
                                flag3 = false;
                                //break;
                                break;
                            }
                        }
                        int q_TargetSG_w = 0;
                        System.out.println("判断这个子图是否有全部兴趣点：" + flag3);
                        if (flag3) {
                            for (int l = 0; l < NoPoi.size(); l++) {
                                NoPoi.get(l).clear();
                            }
                            //如果有，查找q到这个子图的最短路径
                            NOPoi_Num.add(SGPoi.get(j).num);
                            q_TargetSG_w = Integer.MAX_VALUE;
                            for (int l = 0; l < NOPoi_Num.size(); l++) {
                                if (BPList.get(q_BP).get(NOPoi_Num.get(l)).weight < q_TargetSG_w) {
                                    q_TargetSG_w = BPList.get(q_BP).get(NOPoi_Num.get(l)).weight;
                                }
                            }
                            NOPoi_Num.clear();

                            SG_num[SGPoi.get(j).num] = 1;
                            int min = q_TargetSG_w + Min_w; //找到这些路径中最短的一条
                            System.out.println("min=" + min + ",R=" + R);
                            n7++;
                            if (true) {
                                num22++;
                                n2++;
                                //n6++;
                                for (int l = 0; l < SGPoi.get(j).Poi.size(); l++) { //将子图中的兴趣点加入path1中
                                    // path3.get(l).addAll(SGPoi.get(j).Poi.get(l));
                                }
                                ArrayList<ArrayList<Integer>> path6 = new ArrayList<>();
                                for (int mm = 0; mm < Poi_Type.length; mm++) {
                                    path6.add(new ArrayList<Integer>());
                                }
                                for (int l = 0; l < Poi_Type.length; l++) {
                                    for (int o = 0; o < SGPoi.get(j).Poi.get(l).size(); o++) {
                                        path6.get(l).addAll(SGPoi.get(j).Poi.get(l).get(o).path);
                                    }
                                }
                                ArrayList<ArrayList<Integer>> path7 = Finf_Path1(all, path6, Poi_Type, q);
                                //startTime1 = System.currentTimeMillis();
                                int num33 = 0;
                                for (int l = 0; l < SGPoi.get(j).Poi.size(); l++) {
                                    num33 = 0;
                                    for (int o = 0; o < SGPoi.get(j).Poi.get(l).size(); o++) {
                                        num33 = num33 + SGPoi.get(j).Poi.get(l).get(o).path.size();
                                    }
                                    System.out.println("第" + l + "个Poi_Type的个数为:" + num33);
                                }
                                long startTime2 = System.currentTimeMillis(); //开始获取时间
                                ArrayList<LowerBound> allPath4 = Find_allPath(path7, SGPoi.get(j).Poi, q, q_SG, List, PoiList, a, g, q_BP, Min_w, all, BPList, SG, PointMinBP, k);
                                long endTime2 = System.currentTimeMillis(); //开始获取时间
                                long time2 = endTime2 - startTime2;

                                //endTime1 = System.currentTimeMillis();
                                time2 = endTime2 - startTime2;
                                ArrayList<LowerBound> Top_k4 = Find_Top_k(allPath4, k);
                                int top_min;
                                //修改Top_k
                                System.out.println("网格剪枝阶段计算全部路径所用时间为：" + time2);
                                System.out.println("   ");
                                System.out.println("   ");
                                System.out.println("   ");
                                System.out.println("   ");

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
                                System.out.println("Top_K中含有的路径数目为：" + Top_k.size());
                                //修改LB
                                LB = Find_LB(Top_k);
                                //System.out.println("1");
                            }
                            // System.out.println("1");
                        } else {
                            for (int l = 0; l < NoPoi.size(); l++) {
                                NoPoi.get(l).addAll(SGPoi.get(j).Poi.get(l));
                            }
                            NOPoi_Num.add(SGPoi.get(j).num);
                            System.out.println("这个图不符合要求,加入NOPoi,目前NOPoi中的子图为：" + NOPoi_Num);
                            n2++;
                        }
                    } else {
                        continue;
                    }

                }
                SG_num[SG_n.get(i)] = 1;
            }
            System.out.println("n2=" + n2);
            //判断是否需要推出循环
            System.out.println("n5=" + n5 + ",n6=" + n6);
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
        System.out.println("一共划分了" + SG_num.length + "个子图，其中计算了" + n7 + "个子图,网格剪枝了" + n6 + "个子图");
        System.out.println("一共进行的路线查找次数" + num_LJ1 + ",其中剪枝的路线个数为" + num_LJ2);

        return Top_k;
    }

    public ArrayList<LowerBound> Find_allPath(ArrayList<ArrayList<Integer>> path, ArrayList<ArrayList<Poi_B>> path3, int q, int q_SG, ArrayList<ArrayList<Path>> List, Poi[] PoiList, double a,
                                              Graph g, int q_BP, int w_BP, ArrayList<ArrayList<Integer>> all, ArrayList<ArrayList<PoiPath>> BPList, ArrayList<ArrayList<Integer>> SG, ArrayList<ArrayList<Integer>> PointList, int k) {
        ArrayList<LowerBound> LB = new ArrayList<>();
        boolean flag = false;
        int num = 0;
        int w = 0;
        boolean flag11 = true;
        int num11 = 0;
        double score_Min = 0;
        double score1 = 0;
        boolean flag5 = true;
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
        for (int i = 0; i < path.size(); i++) {
            num_LJ1++; //路线剪枝测试
            //System.out.println("i测试："+i+",path测试："+path.size()+",all测试"+all.size());
            w = 0;
            path1.clear();
            flag11 = true;
            //找到Top_k中最小的那个

            startTime2 = System.currentTimeMillis();
            for (int j = 0; j < path3.size(); j++) { //首先判断这个path路径中是否有已经被剪枝的点
                if (flag11) {
                    //System.out.println("path3测试："+all.get(i).get(j));

                    for (int l = 0; l < path3.size(); l++) {//找到这条path中的编号顺序

                        for (int mm = 0; mm < path3.get(l).size(); mm++) {
                            for (int nn = 0; nn < path3.get(l).get(mm).path.size(); nn++) {
                                if (path3.get(l).get(mm).path.get(nn) == path.get(i).get(j)) {
                                    if (flag11) {
                                        for (int m = 0; m < path.get(i).size(); m++) {
                                            //System.out.println("path.get(i).size()测试："+path.get(i).size()+",m="+m);
                                            for (int n = 0; n < path3.get(l).get(mm).otherPath.size(); n++) {
                                                // System.out.println("path3.get(all.get(i).get(j)).get(k).otherPath.size()测试："+path3.get(all.get(i).get(j)).get(k).otherPath.size()+",n="+n);
                                                if (path.get(i).get(m) == path3.get(l).get(mm).otherPath.get(n)) {
                                                    num_LJ2++;//路线剪枝测试
                                                    flag11 = false;
                                                    num11++;
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                    }

                }
            }
            endTime2 = System.currentTimeMillis();
            time2 = endTime2 - startTime2 + time2;

            if (flag11) {
                int q_index = q;
                int q_index1 = 0;
                for (int j = 0; j < path.get(i).size(); j++) {

                    if (q_index == q) {
                        q_index1 = path.get(i).get(j);
                    }

                    //寻找距离查询点q最近的一个边界顶点
                    if (q_index != q) {
                        q_BP1 = PointList.get(q_index).get(0);
                        w_BP1 = PointList.get(q_index).get(1);
                    }

                    startTime2 = System.currentTimeMillis();
                    if (PoiList[q_index].SG == PoiList[q_index1].SG) {
                        startTime2 = System.currentTimeMillis();
                        for (int k1 = 0; k1 < List.get(q_index).size(); k1++) {
                            flag = false;
                            if (List.get(q_index).get(k1).end == q_index1) {
                                w = w + List.get(q_index).get(k1).weight;
                                flag = true;
                            }
                            if (flag) {
                                break;
                            }
                        }


                    } else {

                        if (q_BP1 != Integer.MAX_VALUE) {
                            w = w + w_BP1 + BPList.get(q_BP1).get(PoiList[q_index1].SG).weight;
                        } else {
                            w = 0;
                        }

                    }
                    endTime2 = System.currentTimeMillis();
                    time3 = endTime2 - startTime2 + time3;


                    //startTime2 = System.currentTimeMillis();
                    //开始判断是否需要剪枝
                    if (w != Integer.MAX_VALUE)
                        score1 = (-a) * w + (1 - a) * PoiList[q_index1].Poi_Num;

                    startTime2 = System.currentTimeMillis();
                    if (LB.size() != 0) {
                        if (q_index != q) {
                            if (score1 <= score_Min || w == 0) {
                                flag5 = true;
                                //找到q_2
                                for (int k1 = 0; k1 < path3.size(); k1++) {
                                    if (flag5) {
                                        for (int l = 0; l < path3.get(k1).size(); l++) {
                                            if (flag5) {
                                                for (int m = 0; m < path3.get(k1).get(l).path.size(); m++) {
                                                    if (path3.get(k1).get(l).path.get(m) == q_index1) {
                                                        path_q2 = path3.get(k1).get(l).path;
                                                        flag5 = false;
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                flag5 = true;
                                //找到q_1
                                for (int k1 = 0; k1 < path3.size(); k1++) {
                                    if (flag5) {
                                        for (int l = 0; l < path3.get(k1).size(); l++) {
                                            if (flag5) {
                                                for (int m = 0; m < path3.get(k1).get(l).path.size(); m++) {
                                                    if (path3.get(k1).get(l).path.get(m) == q_index) {
                                                        path3.get(k1).get(l).otherPath.addAll(path_q2);
                                                        flag5 = false;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                            }
                        }

                    }
                    endTime2 = System.currentTimeMillis();
                    time4 = endTime2 - startTime2 + time4;

                    if (j < (path.get(i).size() - 1)) {
                        q_index = path.get(i).get(j);
                        q_index1 = path.get(i).get(j + 1);
                    }

                }
                startTime2 = System.currentTimeMillis();
                if (w != Integer.MAX_VALUE) {
                    LowerBound nn = new LowerBound();
                    if (LB.size() < k) {
                        LB.add(new LowerBound());
                        LB.get(LB.size() - 1).path.clear();
                        LB.get(LB.size() - 1).path.addAll(path.get(i));
                        LB.get(LB.size() - 1).dis = w;
                        for (int kk = 0; kk < LB.get(LB.size() - 1).path.size(); kk++) {
                            //LB.get(num).w_poi += PoiList[LB.get(num).path.get(k)].Poi_Num;
                            LB.get(LB.size() - 1).totalInterest += PoiList[path.get(i).get(kk)].Poi_Num;
                        }
                        LB.get(LB.size() - 1).score = (-a) * LB.get(LB.size() - 1).dis + (1 - a) * LB.get(LB.size() - 1).totalInterest;
                    } else {
                        startTime2 = System.currentTimeMillis();
                        // score_Min = Find_Top_k_Min(LB,1).get(0).score;
                        score_Min = LB.get(Find_LB_Num(LB)).score;

                        nn.path.clear();
                        nn.path.addAll(path.get(i));
                        nn.dis = w;
                        for (int kk = 0; kk < LB.get(LB.size() - 1).path.size(); kk++) {
                            //LB.get(num).w_poi += PoiList[LB.get(num).path.get(k)].Poi_Num;
                            nn.totalInterest += PoiList[path.get(i).get(kk)].Poi_Num;
                        }
                        nn.score = (-a) * nn.dis + (1 - a) * nn.totalInterest;
                        if (nn.score > score_Min) {
                            LB.get(Find_LB_Num(LB)).path = nn.path;
                            LB.get(Find_LB_Num(LB)).dis = nn.dis;
                            LB.get(Find_LB_Num(LB)).score = nn.score;
                            LB.get(Find_LB_Num(LB)).totalInterest = nn.totalInterest;
                        }
                        endTime2 = System.currentTimeMillis();
                        time6 = endTime2 - startTime2 + time6;
                    }
                }
                endTime2 = System.currentTimeMillis();
                time5 = endTime2 - startTime2 + time5;
                num++;

            }
        }
        System.out.println("第一部分所消耗的时间为：" + time2);
        System.out.println("第二部分所消耗的时间为：" + time3);
        System.out.println("第三部分所消耗的时间为：" + time3);
        System.out.println("第四部分所消耗的时间为：" + time4);
        System.out.println("第五部分所消耗的时间为：" + time5);
        System.out.println("第六部分所消耗的时间为：" + time6);
        return LB;
    }

    public ArrayList<LowerBound> Find_Top_k(ArrayList<LowerBound> a, int k) {
        ArrayList<LowerBound> top_k = new ArrayList<>();
        int n = 0;
        for (int i = 0; i < a.size(); i++) {
            if (top_k.size() < k && a.get(i).dis != 0) {
                top_k.add(a.get(i));
            } else {
                n = Find_LB_Num(top_k);
                if (a.get(i).score > top_k.get(n).score && a.get(i).dis != 0) {
                    top_k.remove(n);
                    top_k.add(a.get(i));
                }
            }
        }
        return top_k;
    }

    public ArrayList<LowerBound> Find_Top_k_Min(ArrayList<LowerBound> a, int k) {
        ArrayList<LowerBound> top_k = new ArrayList<>();
        int n = 0;
        for (int i = 0; i < a.size(); i++) {
            if (top_k.size() < k && a.get(i).dis != 0) {
                top_k.add(a.get(i));
            } else {
                n = Find_LB_Num_Min(top_k);
                if (a.get(i).score < top_k.get(n).score && a.get(i).dis != 0) {
                    top_k.remove(n);
                    top_k.add(a.get(i));
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

    public void Add_PoiAsSG(ArrayList<ArrayList<Poi_B>> path3, int[] Poi_Type, ArrayList<ArrayList<Integer>> SG,
                            Poi[] PoiList, ArrayList<ArrayList<Integer>> all, int q_SG) {
        boolean flag12 = true;
        for (int i = 0; i < Poi_Type.length; i++) {
            for (int j = 0; j < SG.get(q_SG).size(); j++) {
                if (PoiList[SG.get(q_SG).get(j)].Poi_Type == Poi_Type[all.get(0).get(i)]) {
                    //path3.get(i).add(SG.get(q_SG).get(j));
                    if (path3.get(i).size() != 0) {
                        flag12 = true;
                        for (int l = 0; l < path3.get(i).size(); l++) {
                            if (PoiList[SG.get(q_SG).get(j)].x == path3.get(i).get(l).x &&
                                    PoiList[SG.get(q_SG).get(j)].y == path3.get(i).get(l).y) {
                                path3.get(i).get(l).path.add(SG.get(q_SG).get(j));
                                //path3.get(i).get(l).path_SG.add(q_SG);
                                flag12 = false;
                                break;
                            }
                        }
                        if (flag12) {
                            path3.get(i).add(new Poi_B());
                            path3.get(i).get(path3.get(i).size() - 1).x = PoiList[SG.get(q_SG).get(j)].x;
                            path3.get(i).get(path3.get(i).size() - 1).y = PoiList[SG.get(q_SG).get(j)].y;
                            path3.get(i).get(path3.get(i).size() - 1).path.add(SG.get(q_SG).get(j));
                            path3.get(i).get(path3.get(i).size() - 1).SG = PoiList[SG.get(q_SG).get(j)].SG;
                            //path3.get(i).get(path3.get(i).size()-1).path_SG.add(q_SG);
                        }
                    } else {
                        path3.get(i).add(new Poi_B());
                        path3.get(i).get(path3.get(i).size() - 1).x = PoiList[SG.get(q_SG).get(j)].x;
                        path3.get(i).get(path3.get(i).size() - 1).y = PoiList[SG.get(q_SG).get(j)].y;
                        path3.get(i).get(path3.get(i).size() - 1).path.add(SG.get(q_SG).get(j));
                        path3.get(i).get(path3.get(i).size() - 1).SG = PoiList[SG.get(q_SG).get(j)].SG;
                        // path3.get(i).get(path3.get(i).size()-1).path_SG.add(q_SG);

                    }
                }
            }
        }
    }

//    public static int BP_FindShortestPath(SGPoi a,int p,ArrayList<ArrayList<list>> List){
//        int w = 0;
//        int w1 = 0;
//        int p1;
//        int Poi1 = 0;
//        int[] num1 = new int[a.Poi.size()];
//        boolean flag1 = true;
//        int num2 = 0;//用于计数
//        while (flag1){
//            p1 = p;
//            num2 = 0;
//            for (int i = 0; i < num1.length; i++) {
//                if (num1[i] == 1){
//                    num2++;
//                }
//            }
//            if (num2 == num1.length){
//                break;
//            }else {
//                int num3 = Integer.MAX_VALUE;
//                for (int i = 0; i < num1.length; i++) {
//                    if (num1[i] == 0){
//                        num3 = Integer.MAX_VALUE;
//                        for (int j = 0; j < a.Poi.size(); j++) {//Poi有多少个
//                            if (num1[j] == 0){
//                                for (int n = 0; n < a.Poi.get(j).size(); n++) {//每个Poi有多少个元素
//                                    for (int k = 0; k < List.get(a.num).size(); k++) { //找到这条边
//                                        if ((List.get(a.num).get(k).sPoint == p1 && List.get(a.num).get(k).ePoint == a.Poi.get(j).get(n))
//                                                || ( List.get(a.num).get(k).ePoint == p1 && List.get(a.num).get(k).sPoint == a.Poi.get(j).get(n))){
//                                            w = List.get(a.num).get(k).w;
//                                            break;
//                                        }
//                                    }
//                                    if (w<num3){
//                                        num3 = w;
//                                        Poi1 = j;
//                                        p = a.Poi.get(j).get(n);
//                                    }
//                                }
//                            }
//
//                        }
//                        num1[Poi1] = 1;
//                        w1 = w1+w;
//                        p1 = p;
//                    }
//
//                }
//            }
//        }
//        return w1;
//    }

    public class Poi_P {
        public int num; //顶点编号
        public int SG;//顶点所在子图
        public int Poi_Type; //兴趣点类型
        public int Poi_Num;//兴趣值
        public int x; //顶点所在x轴子图
        public int y;//顶点所在y轴子图

        public Poi_P() {

        }
    }

    public class Poi_B {
        public int x; //顶点所在x轴子图
        public int y;//顶点所在y轴子图
        public int SG;
        public ArrayList<Integer> path;//在这个小小子图中的顶点的编号
        // public ArrayList<Integer> path_SG ;//在这个小小子图中的顶点所在的子图
        public ArrayList<Integer> otherPath;//这组顶点中对其他组进行剪枝的点的编号

        public Poi_B() {
            this.x = 0;
            this.y = 0;
            this.SG = 0;
            this.path = new ArrayList<>();
            this.otherPath = new ArrayList<>();
        }
    }

    public class SGPoi {
        public int num; //子图的编号
        public ArrayList<ArrayList<Poi_B>> Poi;

        public SGPoi() {
            this.num = 0;
            this.Poi = new ArrayList<>();
        }
    }


}
