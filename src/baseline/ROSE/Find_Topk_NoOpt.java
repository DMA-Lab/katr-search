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
            for (ArrayList<Integer> integers : path2) {
                for (int j = 0; j < path1.get(k + 1).size(); j++) {
                    path.add(new ArrayList<>());
                    for (Integer integer : integers) {
                        path.get(num).add(integer);
                    }
                    path.get(num).add(path1.get(k + 1).get(j));

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
            order.add(new ArrayList<>());
            for (int number : a) {
                order.getLast().add(number);
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
        return Find_TopK_A_db3.Find_Min(SP);
    }

    public static ArrayList<ArrayList<Integer>> Finf_Path1(ArrayList<ArrayList<Integer>> all,
                                                           ArrayList<ArrayList<Integer>> path3, int[] Poi_Type, int q) {
        ArrayList<ArrayList<Integer>> path = new ArrayList<>();
        for (ArrayList<Integer> integers : all) {
            //找到q所在子图中所有的符合要求的poi
            ArrayList<ArrayList<Integer>> path1 = new ArrayList<>();
            for (int i = 0; i < Poi_Type.length; i++) {
                path1.add(new ArrayList<>());
                // path1.get(i).add(q);
            }
            for (int i = 0; i < Poi_Type.length; i++) {
                path1.get(i).addAll(path3.get(integers.get(i)));
            }
            //找到所有的路线
            ArrayList<ArrayList<Integer>> path2 = new ArrayList<>();
            for (int i = 0; i < path1.getFirst().size(); i++) {
                path2.add(new ArrayList<>());
                path2.get(i).add(path1.getFirst().get(i));
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
            path3.add(new ArrayList<>());

        }
        Add_PoiAsSG(path3, Poi_Type, SG, PoiList, all, q_SG);
        boolean flag4 = true;
        ArrayList<Integer> NoType = new ArrayList<>();
        int num44 = 0;
        for (int l = 0; l < path3.size(); l++) {//判断这个子图是否有全部的兴趣点
            if (path3.get(l).isEmpty()) {
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
                for (ArrayList<Integer> integers : SG) {
                    flag6 = false;
                    for (Integer integer : integers) {
                        if (PoiList[integer].Poi_Type == Poi_Type[NoType.get(m)]) {
                            path3.get(NoType.get(m)).add(new Poi_B());
                            path3.get(NoType.get(m)).getLast().x = PoiList[integer].x;
                            path3.get(NoType.get(m)).getLast().y = PoiList[integer].y;
                            path3.get(NoType.get(m)).getLast().path.add(integer);

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
            path5.add(new ArrayList<>());
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
        for (int element : Poi_Type) {
            w1_Num = 0;
            for (Poi poi : PoiList) {
                if (poi.Poi_Type == element && poi.Poi_Num > w1_Num) {
                    w1_Num = poi.Poi_Num;
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
            NoPoi.add(new ArrayList<>());
        }
        while (true) {
            ArrayList<Integer> SG3 = new ArrayList<>();
            n2 = 0;//用来存放应该加入SG_n的子图编号
            n5 = 0;
            n6 = 0;
            for (Integer item : SG_n) {
                System.out.println("w_max=" + w_max + ",LB=" + LB);
                R = ((1 - a) * w_max - LB) / a;
                n = item;
                int[] n1 = {n - m + 1, n - m, n - m - 1, n - 1, n + 1, n + m - 1, n + m, n + m + 1};
                ArrayList<SGPoi> SGPoi = new ArrayList<>();
                //计算这个子图中所有符合条件的Poi
                for (int j = 0; j < 8; j++) {
                    if (n1[j] < SG.size()) {//SG.size()
                        if (n1[j] >= 0 && SG_num[n1[j]] == 0) {
                            n5++;
                            SGPoi.add(new SGPoi());
                            SGPoi.getLast().num = n1[j];
                            SG_num[n1[j]] = 1;
                            for (int nn = 0; nn < Poi_Type.length; nn++) {
                                SGPoi.getLast().Poi.add(new ArrayList<Poi_B>());

                            }
                            Add_PoiAsSG(SGPoi.getLast().Poi, Poi_Type, SG, PoiList, all, SGPoi.getLast().num);
                        } else {
                            n2++;
                        }
                    }
                }
                for (Find_Topk_NoOpt.SGPoi poi : SGPoi) {
                    SG3.add(poi.num);
                }

                int n3 = 0;//用来记录SG编号

                //寻找周围几个子图中每个边界顶点的最短路径
                boolean flag3 = true;

                for (Find_Topk_NoOpt.SGPoi sgPoi : SGPoi) {//判断这个子图周围的n个子图是否符合条件
                    //将NoPoi中的Poi加入SGPoi
                    n = sgPoi.num;
                    int[] n22 = {n - m + 1, n - m, n - m - 1, n - 1, n + 1, n + m - 1, n + m, n + m + 1};
                    for (int value : n22) { //只添加SGPoi.get(j)子图周围八个子图中的Poi
                        if (value < SG.size() && value > 0) {
                            //System.out.println("开始添加SGPoi.get(j)子图周围八个子图中的Poi");
                            for (int o = 0; o < NoPoi.size(); o++) {
                                for (int p = 0; p < NoPoi.get(o).size(); p++) {
                                    flag3 = true;
                                    if (NoPoi.get(o).get(p).SG == value) {
                                        for (int r = 0; r < sgPoi.Poi.get(o).size(); r++) {
                                            if (sgPoi.Poi.get(o).get(r).x == NoPoi.get(o).get(p).x &&
                                                    sgPoi.Poi.get(o).get(r).y == NoPoi.get(o).get(p).y) {
                                                sgPoi.Poi.get(o).get(r).path.addAll(NoPoi.get(o).get(p).path);
                                                flag3 = false;
                                            }
                                        }
                                        if (!flag3) {
                                            sgPoi.Poi.get(o).add(new Poi_B());
                                            //SGPoi.get(j).Poi.get(o).addAll((Collection<? extends Poi_B>) NoPoi.get(o).get(p));
                                            sgPoi.Poi.get(o).getLast().x = NoPoi.get(o).get(p).x;
                                            sgPoi.Poi.get(o).getLast().y = NoPoi.get(o).get(p).y;
                                            sgPoi.Poi.get(o).getLast().path.addAll(NoPoi.get(o).get(p).path);
                                            sgPoi.Poi.get(o).getLast().otherPath.addAll(NoPoi.get(o).get(p).otherPath);
                                            sgPoi.Poi.get(o).getLast().SG = NoPoi.get(o).get(p).SG;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    System.out.println("判断这个子图周围的n个子图是否符合条件");
                    if (SG_num[sgPoi.num] >= 0) {
                        flag3 = true;
                        for (int l = 0; l < sgPoi.Poi.size(); l++) {//判断这个子图是否有全部的兴趣点
                            if (sgPoi.Poi.get(l).isEmpty()) {
                                flag3 = false;
                                //break;
                                break;
                            }
                        }
                        int q_TargetSG_w = 0;
                        System.out.println("判断这个子图是否有全部兴趣点：" + flag3);
                        if (flag3) {
                            for (ArrayList<Poi_B> poiBs : NoPoi) {
                                poiBs.clear();
                            }
                            //如果有，查找q到这个子图的最短路径
                            NOPoi_Num.add(sgPoi.num);
                            q_TargetSG_w = Integer.MAX_VALUE;
                            for (Integer integer : NOPoi_Num) {
                                if (BPList.get(q_BP).get(integer).distance < q_TargetSG_w) {
                                    q_TargetSG_w = BPList.get(q_BP).get(integer).distance;
                                }
                            }
                            NOPoi_Num.clear();

                            SG_num[sgPoi.num] = 1;
                            int min = q_TargetSG_w + Min_w; //找到这些路径中最短的一条
                            System.out.println("min=" + min + ",R=" + R);
                            n7++;
                            if (true) {
                                num22++;
                                n2++;
                                //n6++;
                                for (int l = 0; l < sgPoi.Poi.size(); l++) { //将子图中的兴趣点加入path1中
                                    // path3.get(l).addAll(SGPoi.get(j).Poi.get(l));
                                }
                                ArrayList<ArrayList<Integer>> path6 = new ArrayList<>();
                                for (int mm = 0; mm < Poi_Type.length; mm++) {
                                    path6.add(new ArrayList<Integer>());
                                }
                                for (int l = 0; l < Poi_Type.length; l++) {
                                    for (int o = 0; o < sgPoi.Poi.get(l).size(); o++) {
                                        path6.get(l).addAll(sgPoi.Poi.get(l).get(o).path);
                                    }
                                }
                                ArrayList<ArrayList<Integer>> path7 = Finf_Path1(all, path6, Poi_Type, q);
                                //startTime1 = System.currentTimeMillis();
                                int num33 = 0;
                                for (int l = 0; l < sgPoi.Poi.size(); l++) {
                                    num33 = 0;
                                    for (int o = 0; o < sgPoi.Poi.get(l).size(); o++) {
                                        num33 = num33 + sgPoi.Poi.get(l).get(o).path.size();
                                    }
                                    System.out.println("第" + l + "个Poi_Type的个数为:" + num33);
                                }
                                long startTime2 = System.currentTimeMillis(); //开始获取时间
                                ArrayList<LowerBound> allPath4 = Find_allPath(path7, sgPoi.Poi, q, q_SG, List, PoiList, a, g, q_BP, Min_w, all, BPList, SG, PointMinBP, k);
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
                                    for (LowerBound bound : Top_k4) {
                                        flag99 = true;
                                        for (LowerBound lowerBound : Top_k) {
                                            if (bound.score == lowerBound.score) {
                                                flag99 = false;
                                                break;
                                            }
                                        }
                                        if (flag99) {
                                            Top_k.add(bound);
                                        }
                                    }
                                } else {
                                    for (LowerBound lowerBound : Top_k4) {
                                        top_min = Find_LB_Num(Top_k);
                                        if (lowerBound.score > Top_k.get(top_min).score && lowerBound.score < Double.MAX_VALUE) {
                                            Top_k.remove(top_min);
                                            Top_k.add(lowerBound);
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
                                NoPoi.get(l).addAll(sgPoi.Poi.get(l));
                            }
                            NOPoi_Num.add(sgPoi.num);
                            System.out.println("这个图不符合要求,加入NOPoi,目前NOPoi中的子图为：" + NOPoi_Num);
                            n2++;
                        }
                    } else {
                        continue;
                    }

                }
                SG_num[item] = 1;
            }
            System.out.println("n2=" + n2);
            //判断是否需要推出循环
            System.out.println("n5=" + n5 + ",n6=" + n6);
            if (n6 == n5) {
                //flag = false;
                break;
            }
            int n8 = 0;
            for (int i : SG_num) {
                if (i > 0) {
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
        for (int j : SG_num) {
            if (j > 0) {
                num12++;
            }
        }
        System.out.println("一共划分了" + SG_num.length + "个子图，其中计算了" + n7 + "个子图,网格剪枝了" + n6 + "个子图");
        System.out.println("一共进行的路线查找次数" + num_LJ1 + ",其中剪枝的路线个数为" + num_LJ2);

        return Top_k;
    }

    public ArrayList<LowerBound> Find_allPath(ArrayList<ArrayList<Integer>> path, ArrayList<ArrayList<Poi_B>> path3, int q, int q_SG, ArrayList<ArrayList<Path>> List, Poi[] PoiList, double a,
                                              Graph g, int q_BP, int w_BP, ArrayList<ArrayList<Integer>> all, ArrayList<ArrayList<PoiPath>> BPList, ArrayList<ArrayList<Integer>> SG, ArrayList<ArrayList<Integer>> PointList, int k) {
        ArrayList<LowerBound> LB = new ArrayList<>();
        boolean flag;
        int num = 0;
        int w;
        boolean flag11;
        int num11 = 0;
        double score_Min = 0;
        double score1 = 0;
        boolean flag5;
        int q_BP1 = q_BP;
        int w_BP1 = w_BP;
        System.currentTimeMillis();
        long startTime2; //开始获取时间
        System.currentTimeMillis();
        long endTime2; //开始获取时间
        long time2 = 0;
        long time3 = 0;
        long time4 = 0;
        long time5 = 0;
        long time6 = 0;
        ArrayList<Integer> path_q2 = new ArrayList<>();
        ArrayList<Integer> path1 = new ArrayList<>(); //存放路径
        for (ArrayList<Integer> integers : path) {
            num_LJ1++; //路线剪枝测试
            //System.out.println("i测试："+i+",path测试："+path.size()+",all测试"+all.size());
            w = 0;
            flag11 = true;
            //找到Top_k中最小的那个

            startTime2 = System.currentTimeMillis();
            for (int j = 0; j < path3.size(); j++) { //首先判断这个path路径中是否有已经被剪枝的点
                if (flag11) {
                    //System.out.println("path3测试："+all.get(i).get(j));

                    for (ArrayList<Poi_B> poiBs : path3) {//找到这条path中的编号顺序

                        for (Poi_B poiB : poiBs) {
                            for (int nn = 0; nn < poiB.path.size(); nn++) {
                                if (poiB.path.get(nn) == integers.get(j)) {
                                    if (flag11) {
                                        for (Integer integer : integers) {
                                            //System.out.println("path.get(i).size()测试："+path.get(i).size()+",m="+m);
                                            for (int n = 0; n < poiB.otherPath.size(); n++) {
                                                // System.out.println("path3.get(all.get(i).get(j)).get(k).otherPath.size()测试："+path3.get(all.get(i).get(j)).get(k).otherPath.size()+",n="+n);
                                                if (integer == poiB.otherPath.get(n)) {
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
                for (int j = 0; j < integers.size(); j++) {

                    if (q_index == q) {
                        q_index1 = integers.get(j);
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
                            w = w + w_BP1 + BPList.get(q_BP1).get(PoiList[q_index1].SG).distance;
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
                    if (!LB.isEmpty()) {
                        if (q_index != q) {
                            if (score1 <= score_Min || w == 0) {
                                flag5 = true;
                                //找到q_2
                                for (ArrayList<Poi_B> poi_bs : path3) {
                                    if (flag5) {
                                        for (Poi_B poiB : poi_bs) {
                                            if (flag5) {
                                                for (int m = 0; m < poiB.path.size(); m++) {
                                                    if (poiB.path.get(m) == q_index1) {
                                                        path_q2 = poiB.path;
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
                                for (ArrayList<Poi_B> poiBs : path3) {
                                    if (flag5) {
                                        for (Poi_B poiB : poiBs) {
                                            if (flag5) {
                                                for (int m = 0; m < poiB.path.size(); m++) {
                                                    if (poiB.path.get(m) == q_index) {
                                                        poiB.otherPath.addAll(path_q2);
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

                    if (j < (integers.size() - 1)) {
                        q_index = integers.get(j);
                        q_index1 = integers.get(j + 1);
                    }

                }
                startTime2 = System.currentTimeMillis();
                if (w != Integer.MAX_VALUE) {
                    LowerBound nn = new LowerBound();
                    if (LB.size() < k) {
                        LB.add(new LowerBound());
                        LB.getLast().path.clear();
                        LB.getLast().path.addAll(integers);
                        LB.getLast().dis = w;
                        for (int kk = 0; kk < LB.getLast().path.size(); kk++) {
                            //LB.get(num).w_poi += PoiList[LB.get(num).path.get(k)].Poi_Num;
                            LB.getLast().totalInterest += PoiList[integers.get(kk)].Poi_Num;
                        }
                        LB.getLast().score = (-a) * LB.getLast().dis + (1 - a) * LB.getLast().totalInterest;
                    } else {
                        startTime2 = System.currentTimeMillis();
                        // score_Min = Find_Top_k_Min(LB,1).get(0).score;
                        score_Min = LB.get(Find_LB_Num(LB)).score;

                        nn.path.clear();
                        nn.path.addAll(integers);
                        nn.dis = w;
                        for (int kk = 0; kk < LB.getLast().path.size(); kk++) {
                            //LB.get(num).w_poi += PoiList[LB.get(num).path.get(k)].Poi_Num;
                            nn.totalInterest += PoiList[integers.get(kk)].Poi_Num;
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
        int n;
        for (LowerBound lowerBound : a) {
            if (top_k.size() < k && lowerBound.dis != 0) {
                top_k.add(lowerBound);
            } else {
                n = Find_LB_Num(top_k);
                if (lowerBound.score > top_k.get(n).score && lowerBound.dis != 0) {
                    top_k.remove(n);
                    top_k.add(lowerBound);
                }
            }
        }
        return top_k;
    }

    public ArrayList<LowerBound> Find_Top_k_Min(ArrayList<LowerBound> a, int k) {
        ArrayList<LowerBound> top_k = new ArrayList<>();
        int n = 0;
        for (LowerBound lowerBound : a) {
            if (top_k.size() < k && lowerBound.dis != 0) {
                top_k.add(lowerBound);
            } else {
                n = Find_LB_Num_Min(top_k);
                if (lowerBound.score < top_k.get(n).score && lowerBound.dis != 0) {
                    top_k.remove(n);
                    top_k.add(lowerBound);
                }
            }
        }
        return top_k;
    }

    public double Find_LB(ArrayList<LowerBound> Top_k) {
        //int LB = 0;
        double LB_score = Double.MAX_VALUE;
        for (LowerBound lowerBound : Top_k) {
            if (LB_score > lowerBound.score) {
                LB_score = lowerBound.score;
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
        boolean flag12;
        for (int i = 0; i < Poi_Type.length; i++) {
            for (int j = 0; j < SG.get(q_SG).size(); j++) {
                if (PoiList[SG.get(q_SG).get(j)].Poi_Type == Poi_Type[all.getFirst().get(i)]) {
                    //path3.get(i).add(SG.get(q_SG).get(j));
                    if (!path3.get(i).isEmpty()) {
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
                            path3.get(i).getLast().x = PoiList[SG.get(q_SG).get(j)].x;
                            path3.get(i).getLast().y = PoiList[SG.get(q_SG).get(j)].y;
                            path3.get(i).getLast().path.add(SG.get(q_SG).get(j));
                            path3.get(i).getLast().SG = PoiList[SG.get(q_SG).get(j)].SG;
                            //path3.get(i).get(path3.get(i).size()-1).path_SG.add(q_SG);
                        }
                    } else {
                        path3.get(i).add(new Poi_B());
                        path3.get(i).getLast().x = PoiList[SG.get(q_SG).get(j)].x;
                        path3.get(i).getLast().y = PoiList[SG.get(q_SG).get(j)].y;
                        path3.get(i).getLast().path.add(SG.get(q_SG).get(j));
                        path3.get(i).getLast().SG = PoiList[SG.get(q_SG).get(j)].SG;
                        // path3.get(i).get(path3.get(i).size()-1).path_SG.add(q_SG);

                    }
                }
            }
        }
    }

    public static class Poi_B {
        public int x; //顶点所在x轴子图
        public int y;//顶点所在y轴子图
        public int SG;
        public final ArrayList<Integer> path;//在这个小小子图中的顶点的编号
        // public ArrayList<Integer> path_SG ;//在这个小小子图中的顶点所在的子图
        public final ArrayList<Integer> otherPath;//这组顶点中对其他组进行剪枝的点的编号

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
        public final ArrayList<ArrayList<Poi_B>> Poi;

        public SGPoi() {
            this.num = 0;
            this.Poi = new ArrayList<>();
        }
    }


}
