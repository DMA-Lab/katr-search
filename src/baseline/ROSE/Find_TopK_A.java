package baseline.ROSE;

import entity.*;

import java.util.ArrayList;

public class Find_TopK_A {
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
            for (ArrayList<Integer> integers : path2) {
                for (int j = 0; j < path1.get(k + 1).size(); j++) {
                    path.add(new ArrayList<>());

                    for (Integer integer : integers) {
                        path.get(num).add(integer);
                        //path.get(num).add(path2.size(), 1);
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

    public static ArrayList<ArrayList<ArrayList<Integer>>> Find_Path2(ArrayList<ArrayList<Integer>> path, int[] Poi_Type, Poi[] PoiList, ArrayList<ArrayList<Integer>> all,
                                                                      int q) {
        ArrayList<ArrayList<ArrayList<Integer>>> find_Path = new ArrayList<>();
        ArrayList<Integer> x = new ArrayList<>();
        ArrayList<Integer> y = new ArrayList<>();
        ArrayList<ArrayList<Integer>> path3 = new ArrayList<>();
        for (int i = 0; i < Poi_Type.length; i++) {
            path3.add(new ArrayList<>());
        }
        for (ArrayList<Integer> integers : path) {
            find_Path.add(new ArrayList<>());
            x.clear();
            y.clear();
            //判断这几个点桐樱岛x轴和y轴的顺序是不是一致
            x = Find_xy(integers, PoiList, 0);
            y = Find_xy(integers, PoiList, 1);

            for (int j = 0; j < x.size(); j++) {
                if (x.get(j) != y.get(j)) {
                    break;
                }
            }
            for (int j = 0; j < x.size(); j++) {
                if (x.get(j) != y.get(y.size() - 1 - j)) {
                    break;
                }
            }

            for (int j = 0; j < path3.size(); j++) {
                path3.get(j).clear();
                path3.get(j).add(integers.get(j));
            }
            find_Path.getLast().addAll(FindPath1(all, path3, Poi_Type, q));
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
        num_LJ2 = num_LJ2 + (num_LJ3 - num4);

        return find_Path;
    }

    public static ArrayList<Integer> Find_xy(ArrayList<Integer> path, Poi[] PoiList, int x) {
        ArrayList<Integer> Find_xy = new ArrayList<>();
        ArrayList<Integer> path1 = new ArrayList<>(path);
        int num;
        while (!path1.isEmpty()) {
            num = 0;
            if (x == 0) {
                if (path1.size() == 1) {
                    Find_xy.add(path1.getFirst());
                    path1.removeFirst();
                } else {
                    for (int i = 1; i < path1.size(); i++) {
                        if (PoiList[path1.get(i)].x < PoiList[path1.get(num)].x) {
                            num = i;
                        }
                    }
                    Find_xy.add(path1.get(num));
                    path1.remove(num);
                }
            } else {
                if (path1.size() == 1) {
                    Find_xy.add(path1.getFirst());
                    path1.removeFirst();
                } else {
                    for (int i = 1; i < path1.size(); i++) {
                        if (PoiList[path1.get(i)].y < PoiList[path1.get(num)].y) {
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

    public static ArrayList<ArrayList<Integer>> FindPath1(ArrayList<ArrayList<Integer>> all,
                                                          ArrayList<ArrayList<Integer>> path3, int[] Poi_Type, int q) {
        ArrayList<ArrayList<Integer>> path = new ArrayList<>();
        for (ArrayList<Integer> integers : all) {
            //找到q所在子图中所有的符合要求的poi
            ArrayList<ArrayList<Integer>> path1 = new ArrayList<>();
            for (int i = 0; i < Poi_Type.length; i++) {
                path1.add(new ArrayList<>());
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

    public ArrayList<LowerBound> TopK(Graph g, int q, int q_SG, int k, int[] Poi_Type, ArrayList<ArrayList<Integer>> SG, ArrayList<ArrayList<Path>> List, Poi[] PoiList,
                                      double a, ArrayList<ArrayList<PoiPath>> BPList, ArrayList<ArrayList<Integer>> PointMinBP) {
        //找到查询点q所在的子图
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
                        if (PoiList[integer].Poi_Type == Poi_Type[NoType.get(num55)]) {
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
        path2 = FindPath1(all1, path3, Poi_Type, q);

        startTime1 = System.currentTimeMillis();
        path = Find_Path2(path2, Poi_Type, PoiList, all, q);
        //从这些路径中选出k条q到第一节点最短的路径

        //ArrayList<ArrayList<Integer>> path_k = Find_path_k(q,path, k, q_SG,PoiList, List,PointMinBP,BPList);
        endTime1 = System.currentTimeMillis();
        time11 = time11 + endTime1 - startTime1;
        startTime1 = System.currentTimeMillis();
        ArrayList<LowerBound> allPath = FindAllPath(path, List, PoiList, a, q_BP, Min_w, BPList, PointMinBP, k);
        endTime1 = System.currentTimeMillis();
        time33 = time33 + endTime1 - startTime1;

        ArrayList<LowerBound> Top_k = Find_Top_k(allPath, k);
        double LB = Find_LB(Top_k);
        //找到W_max
        int w_max = 0;
        int w1_Num;
        for (int value : Poi_Type) {
            w1_Num = 0;
            for (Poi poi : PoiList) {
                if (poi.Poi_Type == value && poi.Poi_Num > w1_Num) {
                    w1_Num = poi.Poi_Num;
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

        //网格剪枝
        int n5; //用来标记这次循环需要检查多少个图
        int n6;
        int n7 = 0;
        ArrayList<SGPoi> NoPoi = new ArrayList<>();
        ArrayList<Integer> NOPoi_Num = new ArrayList<>();

        while (true) {
            ArrayList<Integer> SG3 = new ArrayList<>();
            n2 = 0;//用来存放应该加入SG_n的子图编号
            n5 = 0;
            n6 = 0;
            for (Integer integer : SG_n) {
                // System.out.println("w_max="+w_max+",LB="+LB);
                R = ((1 - a) * w_max - LB) / a;
                n = integer;
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
                for (Find_TopK_A.SGPoi sgPoi : SGPoi) {
                    SG3.add(sgPoi.num);
                }
                //寻找周围几个子图中每个边界顶点的最短路径
                boolean flag3;

                for (Find_TopK_A.SGPoi sgPoi : SGPoi) {//判断这个子图周围的n个子图是否符合条件
                    //将NoPoi中的Poi加入SGPoi
                    n = sgPoi.num;
                    int[] n22 = {n - m + 1, n - m, n - m - 1, n - 1, n + 1, n + m - 1, n + m, n + m + 1};
                    for (int i : n22) { //只添加SGPoi.get(j)子图周围八个子图中的Poi
                        if (i < SG.size() && i > 0) {
                            //System.out.println("开始添加SGPoi.get(j)子图周围八个子图中的Poi");
                            for (Find_TopK_A.SGPoi poi : NoPoi) {
                                if (poi.num == i) {
                                    for (int p = 0; p < Poi_Type.length; p++) {
                                        sgPoi.Poi.get(p).addAll(poi.Poi.get(p));
                                    }
                                }
                            }
                        }
                    }

                    //System.out.println("判断这个子图周围的n个子图是否符合条件");
                    if (SG_num[sgPoi.num] >= 0) {
                        flag3 = true;
                        for (int l = 0; l < sgPoi.Poi.size(); l++) {//判断这个子图是否有全部的兴趣点
                            if (sgPoi.Poi.get(l).isEmpty()) {
                                flag3 = false;
                                break;
                            }
                        }
                        int q_TargetSG_w;
                        // System.out.println("判断这个子图是否有全部兴趣点："+flag3);
                        if (flag3) {
                            NoPoi.clear();
                            //如果有，查找q到这个子图的最短路径
                            NOPoi_Num.add(sgPoi.num);
                            q_TargetSG_w = Integer.MAX_VALUE;
                            for (Integer value : NOPoi_Num) {
                                if (BPList.get(q_BP).get(value).distance < q_TargetSG_w) {
                                    q_TargetSG_w = BPList.get(q_BP).get(value).distance;
                                }
                            }
                            NOPoi_Num.clear();

                            SG_num[sgPoi.num] = 1;
                            int min = q_TargetSG_w + Min_w; //找到这些路径中最短的一条
                            //System.out.println("min="+min+",R="+R);
                            n7++;
                            if (min > (int) R) { //网格剪枝
                                SG_num[sgPoi.num] = 2;
                                n2++;
                                n6++;
                            } else {
                                num45 = 1;
                                for (int l = 0; l < sgPoi.Poi.size(); l++) {
                                    if (!sgPoi.Poi.get(l).isEmpty()) {
                                        num45 = num45 * sgPoi.Poi.get(l).size();
                                    }
                                }
                                num_LJ1 = num_LJ1 + num45 * all.size();
                                num_LJ3 = num45 * all.size();
                                ArrayList<ArrayList<Integer>> path8 = FindPath1(all1, sgPoi.Poi, Poi_Type, q);
                                startTime1 = System.currentTimeMillis();
                                ArrayList<ArrayList<ArrayList<Integer>>> path7 = Find_Path2(path8, Poi_Type, PoiList, all, q);
                                endTime1 = System.currentTimeMillis();
                                time11 = time11 + endTime1 - startTime1;
                                int num33;
                                for (int l = 0; l < sgPoi.Poi.size(); l++) {
                                    num33 = 0;
                                    for (int o = 0; o < sgPoi.Poi.get(l).size(); o++) {
                                        num33 = num33 + sgPoi.Poi.get(l).size();
                                    }
                                    //System.out.println("第"+l+"个Poi_Type的个数为:"+num33);
                                }
                                //path_k = Find_path_k(q,path7, k, q_SG,PoiList, List,PointMinBP,BPList);

                                startTime1 = System.currentTimeMillis();
                                ArrayList<LowerBound> allPath4 = FindAllPath(path7, List, PoiList, a, q_BP, Min_w, BPList, PointMinBP, k);
                                endTime1 = System.currentTimeMillis();
                                time33 = time33 + endTime1 - startTime1;

                                ArrayList<LowerBound> Top_k4 = Find_Top_k(allPath4, k);
                                //System.out.println("Top_k4.size="+Top_k4.size());

                                int top_min;
                                //修改Top_k
//                                System.out.println("网格剪枝阶段计算全部路径所用时间为："+time2);

                                if (Top_k.size() < k) {
                                    boolean flag99;
                                    for (LowerBound lowerBound : Top_k4) {
                                        flag99 = true;
                                        for (LowerBound bound : Top_k) {
                                            if (lowerBound.score == bound.score) {
                                                flag99 = false;
                                                break;
                                            }
                                        }
                                        if (flag99) {
                                            Top_k.add(lowerBound);
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
                                // System.out.println("Top_K中含有的路径数目为："+Top_k.size());
                                //修改LB
                                LB = Find_LB(Top_k);
                                //System.out.println("1");
                            }
                            // System.out.println("1");
                        } else {
                            NoPoi.add(new SGPoi());
                            NoPoi.getLast().Poi.addAll(sgPoi.Poi);

                            NOPoi_Num.add(sgPoi.num);
                            //System.out.println("这个图不符合要求,加入NOPoi,目前NOPoi中的子图为："+NOPoi_Num);
                            n2++;
                        }
                    }
                }
                SG_num[integer] = 1;
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
        //计算多少个子图被计算过
        int num12 = 0;
        for (int j : SG_num) {
            if (j > 0) {
                num12++;
            }
        }
        System.out.println("A算法(只进行网格剪枝优化)一共划分了" + SG_num.length + "个子图，其中计算了" + n7 + "个子图,网格剪枝了" + n6 + "个子图");
        double num17 = num_LJ2 + num_LJ4;
        double num18 = num_LJ1;
        double BL = num17 / num18;
        System.out.println("A算法(只进行网格剪枝优化)一共进行的路线查找次数" + num_LJ1 + ",总剪枝的路线的数目为：" + (num_LJ2 + num_LJ4) + ",剪枝效率为：" + BL +
                ",其中第一阶段剪枝的路线为：" + num_LJ2 + ",第二阶段剪枝的路线为：" + num_LJ4);
        System.out.println("第一阶段剪枝消耗的时间为" + time11 + ",第二阶段剪枝消耗的时间为：" + time33);
//        System.out.print("A找到的最优路径的score为：");
//        for (int i = 0; i < Top_k.size(); i++) {
//            System.out.print(Top_k.get(i).score);
//            System.out.print(", ");
//        }
        return Top_k;
    }

    public ArrayList<LowerBound> FindAllPath(ArrayList<ArrayList<ArrayList<Integer>>> path, ArrayList<ArrayList<Path>> List, Poi[] PoiList, double a,
                                             int q_BP, int w_BP, ArrayList<ArrayList<PoiPath>> BPList, ArrayList<ArrayList<Integer>> PointList, int k) {
        ArrayList<LowerBound> LB = new ArrayList<>();
        int w;
        int num = 0;
        double score_Min;
        double score1 = 0;
        int q_BP1;
        int w_BP1;
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
                    Poi_NUM += PoiList[arrayList.get(j)].Poi_Num;
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

                for (int j = 0; j < arrayList.size() - 1; j++) {
                    q_index = arrayList.get(j);
                    q_index1 = arrayList.get(j + 1);
                    q_BP1 = PointList.get(q_index).get(0);
                    w_BP1 = PointList.get(q_index).get(1);
                    if (PoiList[q_index].SG == PoiList[q_index1].SG) {
                        for (int k1 = 0; k1 < List.get(q_index).size(); k1++) {
                            if (List.get(q_index).get(k1).end == q_index1) {
                                w = w + List.get(q_index).get(k1).weight;
                                break;
                            }
                        }
                    } else {
                        if (q_BP1 != Integer.MAX_VALUE) {
                            w = w + w_BP1 + BPList.get(q_BP1).get(PoiList[q_index1].SG).distance;
                            for (int k1 = 0; k1 < List.get(BPList.get(q_BP1).get(PoiList[q_index1].SG).target).size(); k1++) {
                                if (List.get(BPList.get(q_BP1).get(PoiList[q_index1].SG).target).get(k1).end == q_index1) {
                                    w = w + List.get(BPList.get(q_BP1).get(PoiList[q_index1].SG).target).get(k1).weight;
                                    break;
                                }
                            }
                        } else {
                            w = 0;
                        }
                    }
                }
                //计算这组poi的路径长度w
                LowerBound nn = new LowerBound();
                if (LB.size() < k) {
                    LB.add(new LowerBound());
                    LB.getLast().path.clear();
                    LB.getLast().path.addAll(arrayList);
                    LB.getLast().dis = w;
                    for (int kk = 0; kk < LB.getLast().path.size(); kk++) {
                        //LB.get(num).w_poi += PoiList[LB.get(num).path.get(k)].Poi_Num;
                        LB.getLast().totalInterest += PoiList[arrayList.get(kk)].Poi_Num;
                    }
                    LB.getLast().score = (-a) * LB.getLast().dis + (1 - a) * LB.getLast().totalInterest;
                } else {
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
                }
                num++;
            }
        }
        return LB;
    }

    public ArrayList<LowerBound> Find_Top_k(ArrayList<LowerBound> a, int k) {
        ArrayList<LowerBound> top_k = new ArrayList<>();
        int n;
        for (LowerBound lowerBound : a) {
            if (top_k.size() < k && lowerBound.dis != 0) {
                top_k.add(lowerBound);
            } else {
                if (!top_k.isEmpty()) {
                    n = Find_LB_Num(top_k);
                    if (lowerBound.score > top_k.get(n).score && lowerBound.dis != 0) {
                        top_k.remove(n);
                        top_k.add(lowerBound);
                    }
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

    public void Add_PoiAsSG(ArrayList<ArrayList<Integer>> path3, int[] Poi_Type, ArrayList<ArrayList<Integer>> SG,
                            Poi[] PoiList, ArrayList<ArrayList<Integer>> all, int q_SG) {
        for (int i = 0; i < SG.get(q_SG).size(); i++) {
            if (PoiList[SG.get(q_SG).get(i)].Poi_Type != 0) {
                for (int j = 0; j < Poi_Type.length; j++) {
                    if (PoiList[SG.get(q_SG).get(i)].Poi_Type == Poi_Type[j]) {
                        path3.get(j).add(SG.get(q_SG).get(i));
                    }
                }
            }
        }
    }

    public ArrayList<ArrayList<Integer>> Find_path_k(int q, ArrayList<ArrayList<Integer>> path, int k, int q_SG, Poi[] PoiList, ArrayList<ArrayList<Path>> List,
                                                     ArrayList<ArrayList<Integer>> PointMinBP, ArrayList<ArrayList<PoiPath>> BPList) {
        num_LJ1 += path.size();
        ArrayList<ArrayList<Integer>> path_k = new ArrayList<>();
        ArrayList<Integer> path1 = new ArrayList<>();
        int w1;
        int w_max;
        int num = 0;
        for (ArrayList<Integer> integers : path) {
            if (path_k.size() < k) {
                path_k.add(new ArrayList<>());
                path_k.getLast().addAll(integers);
                if (q_SG == PoiList[integers.getFirst()].SG) { //如果在同一个子图中
                    for (int j = 0; j < List.get(q).size(); j++) {
                        if (List.get(q).get(j).end == integers.getFirst()) {
                            path_k.getLast().add(List.get(q).get(j).weight);
                            break;
                        }
                    }
                } else {
                    w1 = 0;
                    w1 += PointMinBP.get(q).get(1);
                    w1 += BPList.get(PointMinBP.get(q).get(0)).get(PoiList[integers.getFirst()].SG).distance;
                    w1 += PointMinBP.get(integers.getFirst()).get(1);
                    path_k.getLast().add(w1);
                }
            } else {
                //查找path_k中查询点到第一个顶点最长的一个
                w_max = Integer.MAX_VALUE;
                long startTime1 = System.currentTimeMillis();
                for (int j = 0; j < path_k.size(); j++) {
                    if (w_max > path_k.get(j).getLast()) {
                        w_max = path_k.get(j).getLast();
                        num = j;
                    }
                }
                long endTime1 = System.currentTimeMillis();
                time22 = time22 + endTime1 - startTime1;
                //判断查询点和第一个顶点是否在同一个子图中
                w1 = 0;
                if (q_SG == PoiList[integers.getFirst()].SG) { //如果在同一个子图中
                    for (int j = 0; j < List.get(q).size(); j++) {
                        if (List.get(q).get(j).end == integers.getFirst()) {
                            w1 = List.get(q).get(j).weight;
                            //path_k.get(path_k.size()-1).add(List.get(q).get(j).w);
                            break;
                        }
                    }

                } else {
                    w1 += PointMinBP.get(q).get(1);
                    w1 += BPList.get(PointMinBP.get(q).get(0)).get(PoiList[integers.getFirst()].SG).distance;
                    w1 += PointMinBP.get(integers.getFirst()).get(1);
                    //path_k.get(path_k.size()-1).add(w1);
                }
                if (w1 < w_max) {
                    path_k.get(num).clear();
                    path_k.get(num).addAll(integers);
                    path_k.get(num).add(w1);
                }

            }
        }
        for (ArrayList<Integer> integers : path_k) {
            path1.clear();
            path1.addAll(integers);
            integers.clear();
            integers.add(q);
            integers.addAll(path1);
            integers.removeLast();
        }

        num_LJ2 += (path.size() - path_k.size());
        return path_k;
    }

    public static class SGPoi {
        public int num; //子图的编号
        public final ArrayList<ArrayList<Integer>> Poi;

        public SGPoi() {
            this.num = 0;
            this.Poi = new ArrayList<>();
        }
    }


}
