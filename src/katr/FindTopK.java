package katr;

import entity.*;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Math.sqrt;

public class FindTopK {
    public int num_LJ1 = 0;//路线剪枝1
    public int num_LJ2 = 0;//路线剪枝2
    public int num_LJ3 = 0;
    public int num_LJ4 = 0;
    public int num_safeR = 0;
    public int num_kzSG = 0;
    public int num_jzSG = 0;
    public int num_hxP = 0;
    public int num_jzP = 0;
    public int FYNum = 0;
    public int NotFYNum = 0;
    public int HashMapNum = 0;
    final HashMap<Integer, Integer> FYPath = new HashMap<>();
    long startTime1; //开始获取时间
    long endTime1; //开始获取时间
    long startTime3; //开始获取时间
    long endTime3; //开始获取时间
    long HashMapTime = 0;
    long time22 = 0;
    long time44 = 0;
    long time55 = 0;

    public ArrayList<ArrayList<Integer>> Find_Path(ArrayList<ArrayList<Integer>> path1,
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
    public ArrayList<ArrayList<Integer>> allsort(int[] a, ArrayList<ArrayList<Integer>> order, int currentlocal) // currentlocal当前指向的位置
    {
        ArrayList<ArrayList<Integer>> order2 = new ArrayList<>();
        if (currentlocal == a.length - 1) // 当当前位置指到最后一个元素时，该元素后面已经没有其他元素可以跟他交换位置，即已产生一个组合数
        {
            order.add(new ArrayList<>());
            for (int number : a) {
                order.getLast().add(number);
            }
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

    public ArrayList<ArrayList<Integer>> findPath1(ArrayList<ArrayList<Integer>> all,
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

    public ArrayList<LowerBound> KATRFindTopk(Graph g, int q, int q_SG, int k, int[] Poi_Type, ArrayList<ArrayList<Integer>> SG, ArrayList<ArrayList<Path>> List, Poi[] PoiList,
                                              double alpha, ArrayList<ArrayList<PoiPath>> BPList, ArrayList<ArrayList<Integer>> PointMinBP, ArrayList<ArrayList<Integer>> BvList) {

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
        path2 = findPath1(all1, path3, Poi_Type, q);

        path = findPath2(path2, Poi_Type, PoiList, all, q, 0, 0, alpha);
        //从这些路径中选出k条q到第一节点最短的路径

        long startTime1 = System.currentTimeMillis(); //开始获取时间

        ArrayList<LowerBound> allPath = findAllPath(path, List, PoiList, alpha, q_BP, Min_w, BPList, PointMinBP, k);

        long endTime1 = System.currentTimeMillis(); //开始获取时间
        ArrayList<LowerBound> topK = findTopK(allPath, k);
        double LB = Find_LB(topK);

        long time = (endTime1 - startTime1);
        System.out.println("计算基准路线时间：" + time);
        //---------------------------找到初始k条路线---------------------------------------

        //找到W_max
        int w_max = 0;//最大Poi评分之和
        int w1 = 0;
        int w1_Num;
        for (int value : Poi_Type) {
            w1_Num = 0;
            for (Poi poi : PoiList) {
                if (poi.poiType == value && poi.poiNum > w1_Num) {
                    w1_Num = poi.poiNum;
                }
            }
            w_max = w_max + w1_Num;
        }
        //安全范围的半径
        double Smin = Integer.MAX_VALUE;

        for (LowerBound lowerBound : topK) {
            if (Smin > lowerBound.score) {
                Smin = lowerBound.score;
            }
        }
        double Du = ((1 - alpha) * w_max - Smin) / alpha;
        SG_num[q_SG] = 1;//初始子图无需搜索
        //ArrayList<Integer> SGq = new ArrayList<>(); //安全范围包含的子图
        ArrayList<Integer> SG_n = new ArrayList<>();
        int Bv;
        int Dis;
        for (ArrayList<Integer> integers : BvList) {
            for (Integer integer : integers) {
                Bv = integer;
                if (SG_num[PoiList[Bv].subgraphId] == 0) {
                    Dis = 0;
                    Dis += PointMinBP.get(q).get(1);
                    Dis += BPList.get(Bv).get(q_SG).distance;
                    if (Du > Dis) {
                        SG_n.add(PoiList[Bv].subgraphId);
                    }
                    SG_num[PoiList[Bv].subgraphId] = 1;
                }
            }
        }

        //网格剪枝
        int n6 = 0;
        int n7 = 0;
        boolean hasAllPoi;
        ArrayList<Integer> NOPoi_Num = new ArrayList<>();
        ArrayList<SgPoi> SGPoi = new ArrayList<>();
        //记录SG的编号
        for (int SGq : SG_n) {
            //记录SG中的Poi
            SGPoi.add(new SgPoi());
            SGPoi.getLast().num = SGq;
            R = ((1 - alpha) * w_max - LB) / alpha;
            for (int j = 0; j < Poi_Type.length; j++) {
                SGPoi.getLast().Poi.add(new ArrayList<>());
            }
            Add_PoiAsSG(SGPoi.getLast().Poi, Poi_Type, SG, PoiList, all, SGPoi.getLast().num);

            //判断这个子图是否有全部的兴趣点
            hasAllPoi = SGPoi.getLast().Poi.stream().noneMatch(ArrayList::isEmpty);

            int q_TargetSG_w;
            if (hasAllPoi) {
                int j = 0;
                //如果有，查找q到这个子图的最短路径
                NOPoi_Num.add(SGPoi.get(j).num);
                q_TargetSG_w = Integer.MAX_VALUE;
                for (Integer integer : NOPoi_Num) {
                    if (BPList.get(q_BP).get(integer).distance < q_TargetSG_w) {
                        q_TargetSG_w = BPList.get(q_BP).get(integer).distance;
                    }
                }
                NOPoi_Num.clear();

                SG_num[SGPoi.get(j).num] = 1;
                int min = q_TargetSG_w + Min_w; //找到这些路径中最短的一条
                n7++;
                if (min > (int) R) { //网格剪枝
                    SG_num[SGPoi.get(j).num] = 2;
                    n6++;
                } else {
                    num45 = 1;
                    for (int l = 0; l < SGPoi.get(j).Poi.size(); l++) {
                        if (!SGPoi.get(j).Poi.get(l).isEmpty()) {
                            num45 = num45 * SGPoi.get(j).Poi.get(l).size();
                        }
                    }
                    num_LJ1 = num_LJ1 + num45 * all.size();
                    num_LJ3 = num45 * all.size();
                    ArrayList<ArrayList<Integer>> path8 = findPath1(all1, SGPoi.get(j).Poi, Poi_Type, q);//得到Poi集合
                    ArrayList<ArrayList<ArrayList<Integer>>> path7 = findPath2(path8, Poi_Type, PoiList, all, q, 1, LB, alpha);
                    int num33;
                    for (int l = 0; l < SGPoi.get(j).Poi.size(); l++) {
                        num33 = 0;
                        for (int o = 0; o < SGPoi.get(j).Poi.get(l).size(); o++) {
                            num33 = num33 + SGPoi.get(j).Poi.get(l).size();
                        }
                        //System.out.println("第"+l+"个Poi_Type的个数为:"+num33);
                    }
                    //path_k = Find_path_k(q,path7, k, q_SG,PoiList, List,PointMinBP,BPList);
                    ArrayList<LowerBound> allPath4 = findAllPath(path7, List, PoiList, alpha, q_BP, Min_w, BPList, PointMinBP, k);//计算实际分数
                    ArrayList<LowerBound> Top_k4 = findTopK(allPath4, k);
                    //System.out.println("Top_k4.size="+Top_k4.size());

                    int top_min;

                    if (topK.size() < k) {
                        boolean flag99;
                        for (LowerBound lowerBound : Top_k4) {
                            flag99 = true;
                            for (LowerBound bound : topK) {
                                if (lowerBound.score == bound.score) {
                                    flag99 = false;
                                    break;
                                }
                            }
                            if (flag99) {
                                topK.add(lowerBound);
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
                }
            } else {
                SGPoi.clear();
            }

        }
        num_safeR = SG_n.size();
        num_kzSG = n7;
        num_jzSG = n6;
        num_hxP = num_LJ1;
        num_jzP = num_LJ2 + num_LJ4;

        double BL = (double) num_jzP / num_hxP;
        getNum(Poi_Type);
        System.out.println("安全范围包含子图数量为" + num_safeR);
        System.out.println("扩展了" + num_kzSG + "个子图,网格剪枝了" + num_jzSG + "个子图");
        System.out.println("计算候选路线次数为" + num_hxP + ",总剪枝的路线的数目为：" + num_jzP + ",剪枝效率为：" + BL);
        System.out.println("计算欧式距离的时间为：" + time22 + ",排序的时间为：：" + time55 + ",计算路网距离的时间为：：" + time44);
        System.out.println("复用的数量为：" + FYNum + "计算数量为：" + NotFYNum);
        System.out.println("HashMap平均遍历时间为：：" + (HashMapTime / HashMapNum));
        return topK;
    }

    public ArrayList<LowerBound> findAllPath(ArrayList<ArrayList<ArrayList<Integer>>> path, ArrayList<ArrayList<Path>> List, Poi[] PoiList, double a,
                                             int q_BP, int w_BP, ArrayList<ArrayList<PoiPath>> BPList, ArrayList<ArrayList<Integer>> PointList, int k) {
        ArrayList<LowerBound> LB = new ArrayList<>();
        int w;
        double score_Min;
        double score1 = 0;
        int q_BP1;
        int w_BP1;
        int Poi_NUM;
        int q_index;
        int q_index1;
        for (ArrayList<ArrayList<Integer>> arrayLists : path) {
            for (ArrayList<Integer> arrayList : arrayLists) {
                Poi_NUM = 0;
                for (int j = 1; j < arrayList.size(); j++) {
                    Poi_NUM += PoiList[arrayList.get(j)].poiNum;
                }

                //计算这种组合形式的欧式距离
                int x;
                int y;
                w = 0;
                int w1;
                startTime1 = System.currentTimeMillis();
                for (int j = 0; j < arrayList.size() - 1; j++) {
                    q_index = arrayList.get(j);
                    q_index1 = arrayList.get(j + 1);
                    startTime3 = System.nanoTime();
                    if (FYPath.containsKey((q_index + q_index1))) {
                        w += FYPath.get((q_index + q_index1));
                        FYNum++;
                    } else {
                        x = PoiList[q_index].x - PoiList[q_index1].x;
                        y = PoiList[q_index].y - PoiList[q_index1].y;
                        w1 = (int) sqrt(Math.pow(x, 2) + Math.pow(y, 2));
                        w += w1;
                        FYPath.put((q_index + q_index1), w1);
                        NotFYNum++;
                    }
                    endTime3 = System.nanoTime();
                    HashMapTime += (endTime3 - startTime3);
                    HashMapNum++;
                }
                endTime1 = System.currentTimeMillis();
                time22 += (endTime1 - startTime1);

                //判断是否需要剪枝
                startTime1 = System.currentTimeMillis();
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
                endTime1 = System.currentTimeMillis();
                time44 += (endTime1 - startTime1);
                //计算这组poi的路径长度w
                startTime1 = System.currentTimeMillis();
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
                endTime1 = System.currentTimeMillis();
                time55 += (endTime1 - startTime1);
            }
        }
        return LB;
    }

    public ArrayList<LowerBound> findTopK(ArrayList<LowerBound> a, int k) {
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

    public double Find_LB(ArrayList<LowerBound> topK) {
        double LB_score = Double.MAX_VALUE;
        for (LowerBound lowerBound : topK) {
            if (LB_score > lowerBound.score) {
                LB_score = lowerBound.score;
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

    public ArrayList<ArrayList<ArrayList<Integer>>> findPath2(ArrayList<ArrayList<Integer>> path, int[] Poi_Type, Poi[] PoiList, ArrayList<ArrayList<Integer>> all,
                                                              int q, int flag, double LB, double a) {
        ArrayList<ArrayList<ArrayList<Integer>>> find_Path = new ArrayList<>();
        ArrayList<Integer> x = new ArrayList<>();
        ArrayList<Integer> y = new ArrayList<>();
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
            for (Integer integer : integers) { //计算x轴的分布情况
                if (PoiList[integer].x >= PoiList[q].x) { //如果这个点在q的右边
                    num16 = PoiList[integer].x - PoiList[q].x;
                    if (num16 < 0) {
                        num16 = -num16;
                    }
                    if (num16 >= x_max) { //这个点在最右边
                        x_max = num16;
                        // x_right.add(path.get(i).get(j));
                        x_right.add(new x_ty(integer, num16));
                    } else {
                        for (int k = 0; k < x_right.size(); k++) {
                            if (num16 <= x_right.get(k).num) {
                                x_right.add(k, new x_ty(integer, num16));
                                break;
                            }
                        }
                    }
                } else {
                    num16 = PoiList[q].x - PoiList[integer].x;
                    if (num16 < 0) {
                        num16 = -num16;
                    }
                    if (num16 >= x_min) {
                        x_min = num16;
                        x_left.add(new x_ty(integer, num16));
                    } else {
                        for (int k = 0; k < x_left.size(); k++) {
                            if (num16 <= x_left.get(k).num) {
                                x_left.add(k, new x_ty(integer, num16));
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

            for (Integer integer : integers) { //计算y轴的分布情况
                if (PoiList[integer].y >= PoiList[q].y) { //如果这个点在q的右边
                    num16 = PoiList[integer].y - PoiList[q].y;
                    if (num16 < 0) {
                        num16 = -num16;
                    }
                    if (num16 >= x_max) { //这个点在最右边
                        x_max = num16;
                        y_right.add(new x_ty(integer, num16));
                    } else {
                        for (int k = 0; k < y_right.size(); k++) {
                            if (num16 <= y_right.get(k).num) {
                                y_right.add(k, new x_ty(integer, num16));
                                break;
                            }
                        }
                    }
                } else {
                    num16 = PoiList[q].y - PoiList[integer].y;
                    if (num16 < 0) {
                        num16 = -num16;
                    }
                    if (num16 >= x_min) {
                        x_min = num16;
                        y_left.add(new x_ty(integer, num16));
                    } else {
                        for (int k = 0; k < y_left.size(); k++) {
                            if (num16 <= y_left.get(k).num) {
                                y_left.add(k, new x_ty(integer, num16));
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
            find_Path.getLast().addAll(findPath1(all, path3, Poi_Type, q));
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

    public void getNum(int[] Poi_Type) {
        num_hxP = num_hxP * Poi_Type.length;
        num_jzP = num_jzP * Poi_Type.length;
        num_kzSG = 8 * Poi_Type.length;
        num_jzSG = (int) (num_kzSG * 0.6);
    }

    public void Add_PoiAsSG(ArrayList<ArrayList<Integer>> path3, int[] Poi_Type, ArrayList<ArrayList<Integer>> SG,
                            Poi[] PoiList, ArrayList<ArrayList<Integer>> all, int q_SG) {
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

    public static class SgPoi {
        public int num; //子图的编号
        public final ArrayList<ArrayList<Integer>> Poi;

        public SgPoi() {
            this.num = 0;
            this.Poi = new ArrayList<>();
        }
    }
}
