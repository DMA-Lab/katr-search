package baseline.KATR;

import entity.*;

import java.util.ArrayList;

import static java.lang.Math.sqrt;

public class FindFirstTopK {

    public int num_LJ1 = 0;//路线剪枝1
    public int num_LJ2 = 0;//路线剪枝2
    public int num_LJ3 = 0;
    public int num_LJ4 = 0;

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

    public ArrayList<LowerBound> KATRFindTopk(Graph g, int q, int q_SG, int k, int[] Poi_Type, ArrayList<ArrayList<Integer>> SG, ArrayList<ArrayList<Path>> List, Poi[] PoiList,
                                              double a, ArrayList<ArrayList<PoiPath>> BPList, ArrayList<ArrayList<Integer>> PointMinBP, ArrayList<ArrayList<Integer>> BvList) {


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
        Add_PoiAsSG(path3, Poi_Type, SG, PoiList, q_SG);
        boolean flag4 = true;
        ArrayList<Integer> NoType = new ArrayList<>();
        int num44 = 0;
        for (int l = 0; l < path3.size(); l++) {//判断这个子图是否有全部的兴趣点
            if (path3.get(l).isEmpty()) {
                NoType.add(l);
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
        ArrayList<Integer> q_BP_Path = new ArrayList<>();
        path2 = findPath1(all1, path3, Poi_Type, q);
//        Find_Path2(ArrayList<ArrayList<Integer>> path,int[] Poi_Type,Poi[] PoiList,ArrayList<ArrayList<Integer>> all,
//        int q){
        // startTime1 = System.currentTimeMillis();
        path = Find_Path2(path2, Poi_Type, PoiList, all, q, 0, a);
        //从这些路径中选出k条q到第一节点最短的路径

        //ArrayList<ArrayList<Integer>> path_k = Find_path_k(q,path, k, q_SG,PoiList, List,PointMinBP,BPList);
        ArrayList<LowerBound> allPath = Find_allPath(path, List, PoiList, a, q_BP, Min_w, BPList, PointMinBP, k);
        // ArrayList<Lower_bound> allPath = new ArrayList<>();
        //endTime1 = System.currentTimeMillis();
        //time1 = endTime1 - startTime1;
        // System.out.println("路线剪枝测试成功,所用的时间为："+time1);

        ArrayList<LowerBound> topK = findTopK(allPath, k);
        double LB = Find_LB(topK);

        return topK;
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
        long startTime2; //开始获取时间
        long endTime2; //开始获取时间
        long time2 = 0;
        long time3 = 0;
        long time4 = 0;
        long time5 = 0;
        long time6 = 0;
        ArrayList<Integer> path_q2 = new ArrayList<>();
        ArrayList<Integer> path1 = new ArrayList<>(); //存放路径

        int Poi_NUM;
        int q_index;
        int q_index1;
        for (ArrayList<ArrayList<Integer>> arrayLists : path) {
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
                    w += (int) sqrt(Math.pow(x, 2) + Math.pow(y, 2));
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
                startTime2 = System.currentTimeMillis();
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

    public double Find_LB(ArrayList<LowerBound> Top_k) {
        double LB_score = Double.MAX_VALUE;
        for (LowerBound lowerBound : Top_k) {
            if (LB_score > lowerBound.score) {
                LB_score = lowerBound.score;
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

    public ArrayList<ArrayList<ArrayList<Integer>>> Find_Path2(ArrayList<ArrayList<Integer>> path, int[] Poi_Type, Poi[] PoiList, ArrayList<ArrayList<Integer>> all,
                                                               int q, double LB, double a) {
        ArrayList<ArrayList<ArrayList<Integer>>> find_Path = new ArrayList<>();
        double x_min;
        double x_max;
        double num16;
        ArrayList<FindTopK.x_ty> x_right = new ArrayList<>();
        ArrayList<FindTopK.x_ty> x_left = new ArrayList<>();
        ArrayList<FindTopK.x_ty> y_right = new ArrayList<>();
        ArrayList<FindTopK.x_ty> y_left = new ArrayList<>();
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
                        x_right.add(new FindTopK.x_ty(integer, num16));
                    } else {
                        for (int k = 0; k < x_right.size(); k++) {
                            if (num16 <= x_right.get(k).num) {
                                x_right.add(k, new FindTopK.x_ty(integer, num16));
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
                        x_left.add(new FindTopK.x_ty(integer, num16));
                    } else {
                        for (int k = 0; k < x_left.size(); k++) {
                            if (num16 <= x_left.get(k).num) {
                                x_left.add(k, new FindTopK.x_ty(integer, num16));
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
                        // x_right.add(path.get(i).get(j));
                        y_right.add(new FindTopK.x_ty(integer, num16));
                    } else {
                        for (int k = 0; k < y_right.size(); k++) {
                            if (num16 <= y_right.get(k).num) {
                                y_right.add(k, new FindTopK.x_ty(integer, num16));
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
                        y_left.add(new FindTopK.x_ty(integer, num16));
                    } else {
                        for (int k = 0; k < y_left.size(); k++) {
                            if (num16 <= y_left.get(k).num) {
                                y_left.add(k, new FindTopK.x_ty(integer, num16));
                                break;
                            }
                        }
                    }
                }
            }

            x_left.addFirst(new FindTopK.x_ty(q, 0));
            x_right.addFirst(new FindTopK.x_ty(q, 0));
            y_left.addFirst(new FindTopK.x_ty(q, 0));
            y_right.addFirst(new FindTopK.x_ty(q, 0));
            if ((x_left.size() <= 1 && y_left.size() <= 1) || (x_left.size() <= 1 && y_right.size() <= 1) ||
                    (x_right.size() <= 1 && y_right.size() <= 1) || (x_right.size() <= 1 && y_left.size() <= 1)) { //q是否在最左边或者最右边
                find_Path.getLast().add(new ArrayList<>());
                //find_Path.get(find_Path.size()-1).get(find_Path.get(find_Path.size()-1).size()-1).add(q);
                if (x_left.size() <= 1) {
                    for (FindTopK.x_ty xTy : x_right) {
                        find_Path.getLast().getLast().add(xTy.x);
                    }
                } else {
                    for (FindTopK.x_ty xTy : x_left) {
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
                    Poi_Num += PoiList[integer].Poi_Num;
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
        //num_LJ5 += num4;
        num_LJ2 = num_LJ2 + (num_LJ3 - num4);

        return find_Path;
    }

    public void Add_PoiAsSG(ArrayList<ArrayList<Integer>> path3, int[] Poi_Type, ArrayList<ArrayList<Integer>> SG,
                            Poi[] PoiList, int q_SG) {
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
}
