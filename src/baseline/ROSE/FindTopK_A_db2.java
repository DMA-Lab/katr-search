package baseline.ROSE;

import entity.*;

import java.util.ArrayList;

public class FindTopK_A_db2 {
    public static int num_LJ1 = 0;//路线剪枝1
    public static int num_LJ2 = 0;//路线剪枝2
    public static int num_LJ3 = 0;
    public static int num_LJ4 = 0;
    //public static int num_LJ5 = 0;//判断每次总的组合里面有多少条组合形式
    long startTime1 = System.currentTimeMillis(); //开始获取时间
    long endTime1 = System.currentTimeMillis(); //开始获取时间
    long time11 = 0;
    long time33 = 0;

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

    public void Top_k_db2(Graph g, int q, int q_SG, int k, int[] Poi_Type, ArrayList<ArrayList<Integer>> SG, ArrayList<ArrayList<Path>> List, Poi[] PoiList,
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
        ArrayList<ArrayList<Integer>> all1 = new ArrayList<>();
        ArrayList<ArrayList<ArrayList<Integer>>> path;
        ArrayList<ArrayList<Integer>> path2;
        ArrayList<ArrayList<Integer>> path3 = new ArrayList<>();
        boolean flag12 = true;
        all1.add(new ArrayList<>());
        for (int i = 0; i < Poi_Type.length; i++) {
            all1.getFirst().add(i);
        }
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
            //System.out.println("初始子图中不包括全部的Poi类型");
            boolean flag6;
            int num55 = 0;
            while (num44 > 0) {
                for (ArrayList<Integer> integers : SG) {
                    flag6 = false;
                    for (Integer integer : integers) {
                        if (PoiList[integer].poiType == Poi_Type[NoType.get(num55)]) {
                            path3.get(NoType.get(num55)).add(integer);
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
        for (ArrayList<Integer> integers : path3) {
            if (!integers.isEmpty()) {
                num45 = num45 * integers.size();
            }
        }
        num_LJ1 += num45 * all.size();
        num_LJ3 = num45 * all.size();
        int q_BP = PointMinBP.get(q).get(0);
        int Min_w = PointMinBP.get(q).get(1);
        ArrayList<Integer> q_BP_Path = new ArrayList<>();
        path2 = Finf_Path1(all1, path3, Poi_Type, q);
//        Find_Path2(ArrayList<ArrayList<Integer>> path,int[] Poi_Type,Poi[] PoiList,ArrayList<ArrayList<Integer>> all,
//        int q){
        startTime1 = System.currentTimeMillis();
        path = Find_Path2(path2, Poi_Type, PoiList, all, q, 0, 0, a);
        //从这些路径中选出k条q到第一节点最短的路径

        //ArrayList<ArrayList<Integer>> path_k = Find_path_k(q,path, k, q_SG,PoiList, List,PointMinBP,BPList);
        endTime1 = System.currentTimeMillis();
        time11 = time11 + endTime1 - startTime1;
        // startTime1 = System.currentTimeMillis();
        startTime1 = System.currentTimeMillis();
        ArrayList<LowerBound> allPath = Find_allPath(path, List, PoiList, a, q_BP, Min_w, BPList, PointMinBP, k);
        endTime1 = System.currentTimeMillis();
        time33 = time33 + endTime1 - startTime1;

        ArrayList<LowerBound> topK = Find_Top_k(allPath, k);
        double LB = Find_LB(topK);
        //找到W_max
        int w_max = 0;
        int w1 = 0;
        int w1_Num;
        for (int i1 : Poi_Type) {
            w1_Num = 0;
            for (Poi poi : PoiList) {
                if (poi.poiType == i1 && poi.poiNum > w1_Num) {
                    w1_Num = poi.poiNum;
                }
            }
            w_max = w_max + w1_Num;
        }

        SG_num[q_SG] = 1;
        int SG_num1 = 1;//已经搜索了多少个子图
        ArrayList<Integer> SG_n = new ArrayList<>(); //判断现在有多少个图需要扩展
        int n2; // n2大于等于7时，算法结束
        int m = (int) Math.sqrt(SG.size());
        SG_n.add(q_SG);
        int n;
        boolean flag = true;

        //网格剪枝
        int num22 = 0;
        int n5; //用来标记这次循环需要检查多少个图
        int n6 = 0;
        int n7 = 0;
        ArrayList<SGPoi> NoPoi = new ArrayList<>();
        ArrayList<Integer> NOPoi_Num = new ArrayList<>();
        while (true) {
            ArrayList<Integer> SG3 = new ArrayList<>();
            n2 = 0;//用来存放应该加入SG_n的子图编号
            n5 = 0;
            n6 = 0;
            for (Integer element : SG_n) {
                // System.out.println("w_max="+w_max+",LB="+LB);
                R = ((1 - a) * w_max - LB) / a;
                n = element;
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
                                SGPoi.getLast().Poi.add(new ArrayList<>());
                            }
                            Add_PoiAsSG(SGPoi.getLast().Poi, Poi_Type, SG, PoiList, all, SGPoi.getLast().num);
                        } else {
                            n2++;
                        }
                    }
                }
                for (FindTopK_A_db2.SGPoi item : SGPoi) {
                    SG3.add(item.num);
                }

                int n3 = 0;//用来记录SG编号

                //寻找周围几个子图中每个边界顶点的最短路径
                boolean flag3;

                for (FindTopK_A_db2.SGPoi poi : SGPoi) {//判断这个子图周围的n个子图是否符合条件
                    //将NoPoi中的Poi加入SGPoi
                    n = poi.num;
                    int[] n22 = {n - m + 1, n - m, n - m - 1, n - 1, n + 1, n + m - 1, n + m, n + m + 1};
                    for (int value : n22) { //只添加SGPoi.get(j)子图周围八个子图中的Poi
                        if (value < SG.size() && value > 0) {
                            //System.out.println("开始添加SGPoi.get(j)子图周围八个子图中的Poi");
                            for (FindTopK_A_db2.SGPoi sgPoi : NoPoi) {
                                if (sgPoi.num == value) {
                                    for (int p = 0; p < Poi_Type.length; p++) {
                                        poi.Poi.get(p).addAll(sgPoi.Poi.get(p));
                                    }
                                }
                            }
                        }
                    }

                    //System.out.println("判断这个子图周围的n个子图是否符合条件");
                    if (SG_num[poi.num] >= 0) {
                        flag3 = true;
                        for (int l = 0; l < poi.Poi.size(); l++) {//判断这个子图是否有全部的兴趣点
                            if (poi.Poi.get(l).isEmpty()) {
                                flag3 = false;
                                //break;
                                break;
                            }
                        }
                        int q_TargetSG_w;
                        // System.out.println("判断这个子图是否有全部兴趣点："+flag3);
                        if (flag3) {
                            NoPoi.clear();
                            //如果有，查找q到这个子图的最短路径
                            NOPoi_Num.add(poi.num);
                            q_TargetSG_w = Integer.MAX_VALUE;
                            for (Integer integer : NOPoi_Num) {
                                if (BPList.get(q_BP).get(integer).distance < q_TargetSG_w) {
                                    q_TargetSG_w = BPList.get(q_BP).get(integer).distance;
                                }
                            }
                            NOPoi_Num.clear();

                            SG_num[poi.num] = 1;
                            int min = q_TargetSG_w + Min_w; //找到这些路径中最短的一条
                            //System.out.println("min="+min+",R="+R);
                            n7++;
                            if (min > (int) R) { //网格剪枝
                                SG_num[poi.num] = 2;
                                num22++;
                                n2++;
                                n6++;
                                continue;
                            } else {
                                num45 = 1;
                                for (int l = 0; l < poi.Poi.size(); l++) {
                                    if (!poi.Poi.get(l).isEmpty()) {
                                        num45 = num45 * poi.Poi.get(l).size();
                                    }
                                }
                                num_LJ1 = num_LJ1 + num45 * all.size();
                                num_LJ3 = num45 * all.size();
                                ArrayList<ArrayList<Integer>> path8 = Finf_Path1(all1, poi.Poi, Poi_Type, q);
                                startTime1 = System.currentTimeMillis();
                                ArrayList<ArrayList<ArrayList<Integer>>> path7 = Find_Path2(path8, Poi_Type, PoiList, all, q, 1, LB, a);
                                endTime1 = System.currentTimeMillis();
                                time11 = time11 + endTime1 - startTime1;
                                //startTime1 = System.currentTimeMillis();
                                int num33;
                                for (int l = 0; l < poi.Poi.size(); l++) {
                                    num33 = 0;
                                    for (int o = 0; o < poi.Poi.get(l).size(); o++) {
                                        num33 = num33 + poi.Poi.get(l).size();
                                    }
                                    //System.out.println("第"+l+"个Poi_Type的个数为:"+num33);
                                }
                                startTime1 = System.currentTimeMillis();
                                ArrayList<LowerBound> allPath4 = Find_allPath(path7, List, PoiList, a, q_BP, Min_w, BPList, PointMinBP, k);
                                endTime1 = System.currentTimeMillis();
                                time33 = time33 + endTime1 - startTime1;

                                ArrayList<LowerBound> Top_k4 = Find_Top_k(allPath4, k);
                                //System.out.println("Top_k4.size="+Top_k4.size());

                                int top_min;

                                if (topK.size() < k) {
                                    boolean flag99;
                                    for (LowerBound bound : Top_k4) {
                                        flag99 = true;
                                        for (LowerBound lowerBound : topK) {
                                            if (bound.score == lowerBound.score) {
                                                flag99 = false;
                                                break;
                                            }
                                        }
                                        if (flag99) {
                                            topK.add(bound);
                                        }
                                    }
                                } else {
                                    for (LowerBound lowerBound : Top_k4) {
                                        top_min = Find_LB_Num(topK);
                                        if (lowerBound.score > topK.get(top_min).score && lowerBound.score < Double.MAX_VALUE) {
                                            topK.remove(top_min);
                                            topK.add(lowerBound);
                                        }
                                    }
                                }
                                // System.out.println("Top_K中含有的路径数目为："+Top_k.size());
                                //修改LB
                                LB = Find_LB(topK);
                                //System.out.println("1");
                            }
                            // System.out.println("1");
                        } else {
                            NoPoi.add(new SGPoi());
                            NoPoi.getLast().Poi.addAll(poi.Poi);

                            NOPoi_Num.add(poi.num);
                            //System.out.println("这个图不符合要求,加入NOPoi,目前NOPoi中的子图为："+NOPoi_Num);
                            n2++;
                        }
                    } else {
                        continue;
                    }

                }
                SG_num[element] = 1;
            }
            //System.out.println("n2="+n2);
            //判断是否需要推出循环
            // System.out.println("n5="+n5+",n6="+n6);
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
        //System.out.println("222222222222222222222222222");
        double num17 = num_LJ2 + num_LJ4;
        double num18 = num_LJ1;
        double BL = num17 / num18;
        System.out.println("A的折返优化算法一共划分了" + SG_num.length + "个子图，其中计算了" + n7 + "个子图,网格剪枝了" + n6 + "个子图");
        System.out.println("A的折返优化算法一共划分了" + SG_num.length + "个子图");
        System.out.println("A的折返优化算法一共进行的路线查找次数" + num_LJ1 + ",总剪枝的路线的数目为：" + (num_LJ2 + num_LJ4) + ",实际计算的路线的数目为：" + (num_LJ1 - num_LJ2 - num_LJ4) + ",剪枝效率为：" + BL +
                ",其中第一阶段剪枝的路线为：" + num_LJ2 + ",第二阶段剪枝的路线为：" + num_LJ4);
        System.out.println("第一阶段剪枝消耗的时间为" + time11 + ",第二阶段剪枝消耗的时间为：" + time33);
    }

    public ArrayList<LowerBound> Find_allPath(ArrayList<ArrayList<ArrayList<Integer>>> path, ArrayList<ArrayList<Path>> List, Poi[] PoiList, double a,
                                              int q_BP, int w_BP, ArrayList<ArrayList<PoiPath>> BPList, ArrayList<ArrayList<Integer>> PointList, int k) {
        ArrayList<LowerBound> LB = new ArrayList<>();
        int w;
        int num = 0;
        double score_Min;
        double score1 = 0;
        int q_BP1;
        int w_BP1;
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
        //num_LJ1 += path.size();
        int Poi_NUM;
        int q_index;
        int q_index1;
        for (ArrayList<ArrayList<Integer>> arrayLists : path) {
            //num_LJ1 = num_LJ1 + path.get(i).size();
            for (ArrayList<Integer> arrayList : arrayLists) {
                Poi_NUM = 0;
                for (int j = 1; j < arrayList.size(); j++) {
                    Poi_NUM += PoiList[arrayList.get(j)].poiNum;
                }
                //计算这种组合形式的欧式距离

                int x;
                int y;
                w = 0;
                for (int j = 0; j < arrayList.size() - 1; j++) {
                    q_index = arrayList.get(j);
                    q_index1 = arrayList.get(j + 1);
                    x = PoiList[q_index].x - PoiList[q_index1].x;
                    y = PoiList[q_index].y - PoiList[q_index1].y;
                    w += (int) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
                }
                //判断是否需要剪枝
                if (!LB.isEmpty()) {
                    if (w != 0)
                        score1 = (-a) * w + (1 - a) * Poi_NUM;
                    score_Min = LB.get(Find_LB_Num(LB)).score;
                    if (score1 <= score_Min) {
                        num_LJ4++;
                        break;
                    }
                }
                w = 0;
                path1.clear();

                for (int j = 0; j < arrayList.size() - 1; j++) {
                    q_index = arrayList.get(j);
                    q_index1 = arrayList.get(j + 1);
                    q_BP1 = PointList.get(q_index).get(0);
                    w_BP1 = PointList.get(q_index).get(1);
                    if (PoiList[q_index].subgraphId == PoiList[q_index1].subgraphId) {
                        for (int k1 = 0; k1 < List.get(q_index).size(); k1++) {
                            if (List.get(q_index).get(k1).end == q_index1) {
                                w = w + List.get(q_index).get(k1).weight;
                                break;
                            }
                        }
                    } else {
                        if (q_BP1 != Integer.MAX_VALUE) {
                            w = w + w_BP1 + BPList.get(q_BP1).get(PoiList[q_index1].subgraphId).distance;
                            for (int k1 = 0; k1 < List.get(BPList.get(q_BP1).get(PoiList[q_index1].subgraphId).target).size(); k1++) {
                                if (List.get(BPList.get(q_BP1).get(PoiList[q_index1].subgraphId).target).get(k1).end == q_index1) {
                                    w = w + List.get(BPList.get(q_BP1).get(PoiList[q_index1].subgraphId).target).get(k1).weight;
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
                    LB.getLast().path.clear();
                    LB.getLast().path.addAll(arrayList);
                    LB.getLast().dis = w;
                    for (int kk = 0; kk < LB.getLast().path.size(); kk++) {
                        //LB.get(num).w_poi += PoiList[LB.get(num).path.get(k)].Poi_Num;
                        LB.getLast().totalInterest += PoiList[arrayList.get(kk)].poiNum;
                    }
                    LB.getLast().score = (-a) * LB.getLast().dis + (1 - a) * LB.getLast().totalInterest;
                } else {
                    startTime2 = System.currentTimeMillis();
                    score_Min = LB.get(Find_LB_Num(LB)).score;

                    nn.path.clear();
                    nn.path.addAll(arrayList);
                    nn.dis = w;
                    nn.totalInterest = Poi_NUM;
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

                endTime2 = System.currentTimeMillis();
                time5 = endTime2 - startTime2 + time5;
                num++;

            }


        }
        return LB;
    }

    public ArrayList<LowerBound> Find_Top_k(ArrayList<LowerBound> a, int k) {
        ArrayList<LowerBound> top_k = new ArrayList<>();
        int n;
        for (int i = 0; i < a.size(); i++) {
            if (top_k.size() < k && a.get(i).dis != 0) {
                top_k.add(a.get(i));
            } else {
                if (!top_k.isEmpty()) {
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

    public double Find_LB(ArrayList<LowerBound> topK) {
        //int LB = 0;
        double LB_score = Double.MAX_VALUE;
        for (LowerBound lowerBound : topK) {
            if (LB_score > lowerBound.score) {
                LB_score = lowerBound.score;
                //LB = i;
            }

        }
        return LB_score;
    }

    public int Find_LB_Num(ArrayList<LowerBound> topK) {
        int LB = 0;
        double LB_score = Double.MAX_VALUE;
        for (int i = 0; i < topK.size(); i++) {
            if (LB_score > topK.get(i).score) {
                LB_score = topK.get(i).score;
                LB = i;
            }

        }
        return LB;
    }

    public ArrayList<ArrayList<ArrayList<Integer>>> Find_Path2(ArrayList<ArrayList<Integer>> path, int[] Poi_Type, Poi[] PoiList, ArrayList<ArrayList<Integer>> all,
                                                               int q, int flag, double LB, double a) {
        ArrayList<ArrayList<ArrayList<Integer>>> find_Path = new ArrayList<>();
        ArrayList<Integer> x = new ArrayList<>();
        ArrayList<Integer> y = new ArrayList<>();
        boolean flag_x = true;
        boolean flag_y = true;
        double x_min;
        double x_max;
        double num16;
        ArrayList<x_ty> x_right = new ArrayList<>();
        ArrayList<x_ty> x_left = new ArrayList<>();
        ArrayList<x_ty> y_right = new ArrayList<>();
        ArrayList<x_ty> y_left = new ArrayList<>();
        ArrayList<ArrayList<Integer>> path3 = new ArrayList<>();
        for (int i = 0; i < Poi_Type.length; i++) {
            path3.add(new ArrayList<>());
        }
        for (ArrayList<Integer> integers : path) {
            find_Path.add(new ArrayList<>());
            //找到x的两端
            x_right.clear();
            x_left.clear();
            x_max = 0;
            x_min = 0;
            for (Integer item : integers) { //计算x轴的分布情况
                if (PoiList[item].x >= PoiList[q].x) { //如果这个点在q的右边
                    num16 = PoiList[item].x - PoiList[q].x;
                    if (num16 < 0) {
                        num16 = -num16;
                    }
                    if (num16 >= x_max) { //这个点在最右边
                        x_max = num16;
                        // x_right.add(path.get(i).get(j));
                        x_right.add(new x_ty(item, num16));
                    } else {
                        for (int k = 0; k < x_right.size(); k++) {
                            if (num16 <= x_right.get(k).num) {
                                x_right.add(k, new x_ty(item, num16));
                                break;
                            }
                        }
                    }
                } else {
                    num16 = PoiList[q].x - PoiList[item].x;
                    if (num16 < 0) {
                        num16 = -num16;
                    }
                    if (num16 >= x_min) {
                        x_min = num16;
                        x_left.add(new x_ty(item, num16));
                    } else {
                        for (int k = 0; k < x_left.size(); k++) {
                            if (num16 <= x_left.get(k).num) {
                                x_left.add(k, new x_ty(item, num16));
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

            for (Integer value : integers) { //计算y轴的分布情况
                if (PoiList[value].y >= PoiList[q].y) { //如果这个点在q的右边
                    num16 = PoiList[value].y - PoiList[q].y;
                    if (num16 < 0) {
                        num16 = -num16;
                    }
                    if (num16 >= x_max) { //这个点在最右边
                        x_max = num16;
                        // x_right.add(path.get(i).get(j));
                        y_right.add(new x_ty(value, num16));
                    } else {
                        for (int k = 0; k < y_right.size(); k++) {
                            if (num16 <= y_right.get(k).num) {
                                y_right.add(k, new x_ty(value, num16));
                                break;
                            }
                        }
                    }
                } else {
                    num16 = PoiList[q].y - PoiList[value].y;
                    if (num16 < 0) {
                        num16 = -num16;
                    }
                    if (num16 >= x_min) {
                        x_min = num16;
                        y_left.add(new x_ty(value, num16));
                    } else {
                        for (int k = 0; k < y_left.size(); k++) {
                            if (num16 <= y_left.get(k).num) {
                                y_left.add(k, new x_ty(value, num16));
                                break;
                            }
                        }
                    }
                }
            }

            x_left.addFirst(new x_ty(q, 0));
            x_right.addFirst(new x_ty(q, 0));
            y_left.addFirst(new x_ty(q, 0));
            y_right.addFirst(new x_ty(q, 0));
            if ((x_left.size() <= 1 && y_left.size() <= 1) || (x_left.size() <= 1 && y_right.size() <= 1) ||
                    (x_right.size() <= 1 && y_right.size() <= 1) || (x_right.size() <= 1 && y_left.size() <= 1)) { //q是否在最左边或者最右边
                find_Path.getLast().add(new ArrayList<>());
                //find_Path.get(find_Path.size()-1).get(find_Path.get(find_Path.size()-1).size()-1).add(q);
                if (x_left.size() <= 1) {
                    for (x_ty xTy : x_right) {
                        find_Path.getLast().getLast().add(xTy.x);
                    }
                } else {
                    for (x_ty xTy : x_left) {
                        find_Path.getLast().getLast().add(xTy.x);
                    }
                }
                continue;
            }
            //判断左边远还是右边远
            if (LB != 0) {
                int x1 = 0;
                if (x_max >= x_min) { //右边远，往左边走
                    for (int j = 0; j <= x_left.size() - 2; j++) {
                        x1 += (PoiList[x_left.get(j).x].x - PoiList[x_left.get(j + 1).x].x);
                    }
                    x1 = x1 + x1;
                    for (int j = 0; j < x_right.size() - 2; j++) {
                        x1 += (PoiList[x_right.get(j).x].x - PoiList[x_right.get(j + 1).x].x);
                    }
                } else {
                    for (int j = 0; j < x_right.size() - 2; j++) {
                        x1 += (PoiList[x_right.get(j).x].x - PoiList[x_right.get(j + 1).x].x);
                    }
                    x1 = x1 + x1;
                    for (int j = 0; j <= x_left.size() - 2; j++) {
                        x1 += (PoiList[x_left.get(j).x].x - PoiList[x_left.get(j + 1).x].x);
                    }
                }
                int Poi_Num = 0;
                for (Integer integer : integers) { //计算这一组兴趣点的兴趣值之和
                    Poi_Num += PoiList[integer].poiNum;
                }
                double score_x = (-a) * x1 + (1 - a) * Poi_Num;
                if (score_x < LB) { //剪枝
                    continue;
                }
            }

            for (int j = 0; j < path3.size(); j++) {
                path3.get(j).clear();
                path3.get(j).add(integers.get(j));
            }
            find_Path.getLast().addAll(Finf_Path1(all, path3, Poi_Type, q));
            ArrayList<Integer> path22 = new ArrayList<>();
            for (int j = 0; j < find_Path.getLast().size(); j++) {
                path22.clear();
                path22.addAll(find_Path.getLast().get(j));
                find_Path.getLast().get(j).clear();
                find_Path.getLast().get(j).add(q);
                find_Path.getLast().get(j).addAll(path22);
            }

        }
        int num4 = 0;
        for (ArrayList<ArrayList<Integer>> arrayLists : find_Path) {
            if (!arrayLists.isEmpty()) {
                num4 = num4 + arrayLists.size();
            }
        }
        //num_LJ5 += num4;
        num_LJ2 = num_LJ2 + (num_LJ3 - num4);

        return find_Path;
    }

    public void Add_PoiAsSG(ArrayList<ArrayList<Integer>> path3, int[] Poi_Type, ArrayList<ArrayList<Integer>> SG,
                            Poi[] PoiList, ArrayList<ArrayList<Integer>> all, int q_SG) {
        boolean flag12 = true;
        for (int i = 0; i < SG.get(q_SG).size(); i++) {
            if (PoiList[SG.get(q_SG).get(i)].poiType != 0) {
                for (int j = 0; j < Poi_Type.length; j++) {
                    if (PoiList[SG.get(q_SG).get(i)].poiType == Poi_Type[j]) {
                        path3.get(j).add(SG.get(q_SG).get(i));
                    }
                }
            }
        }
    }

    public static class SGPoi {
        public int num; //子图的编号
        public final ArrayList<ArrayList<Integer>> Poi;

        public SGPoi() {
            this.num = 0;
            this.Poi = new ArrayList<>();
        }
    }

    public static class x_ty {
        public final int x;
        public final double num;

        public x_ty() {
            this.x = 0;
            this.num = 0;
        }

        public x_ty(int x, double num) {
            this.x = x;
            this.num = num;
        }
    }
}
