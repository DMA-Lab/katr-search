package KKRSAlgorithm;

import GraphEntity.Class_BPList;
import GraphEntity.MyGraph;
import GraphEntity.POI;
import GraphEntity.list;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Find_path_MTDOSR {
    public ArrayList<Path> Find_Path(MyGraph g, int q, int[] POI_Type, int ccc1, POI[] POIList, ArrayList<ArrayList<list>> List,
                                     ArrayList<ArrayList<Class_BPList>> BPList,ArrayList<ArrayList<Integer>> PointMinBP,int k3) throws InterruptedException {
        int nn1 = 0;//当只需要找一条路径的时候使用这个数来代替get(0)
        ArrayList<Path> path1 = new ArrayList<>();
        path1.add(new Path());
        //创建顶点标签
        ArrayList<ArrayList<Integer>> POI_List = new ArrayList<>();
        ArrayList<Integer> POI_Type1 = new ArrayList<>();
        for (int i = 0; i < POI_Type.length; i++) {
            POI_Type1.add(POI_Type[i]);
        }
        for (int i = 0; i < POI_Type.length; i++) {
            POI_List.add(new ArrayList<Integer>());
        }
        boolean flag = true;
        boolean flag1 = true;
        int q_Index = q;
        int startIndex = q;

        //查找全部类型的POI各自有那些顶顶点
        for (int i = 0; i < POIList.length; i++) {
            for (int j = 0; j < POI_List.size(); j++) {
                if (POIList[i].POI_Type == POI_Type[j]) {
                    POI_List.get(j).add(i);
                }
            }
        }

        ArrayList <Dijkstia.MyPath> path2 = new ArrayList<>();
        int k1 = q;
        double MinWeight ;
        int w = 0;
        int index1 = q;
        int index2 = 0;
        double w1 = Double.MAX_VALUE;
        int j1 = Integer.MAX_VALUE;
        Dijkstia dj = new Dijkstia();
        List<Dijkstia.MyPath> allPath = new ArrayList<>();
        double w_Max = Double.MAX_VALUE;
        double w_index = 0;



        for (int i = 0; i < POI_List.size()-1; i++) {

            for (int j = 0; j < POI_List.get(i).size(); j++) {
                w_index = 0;
                index2 = POI_List.get(i).get(j);
                allPath =  dj.ShortestPath(g,index1,index2,1);
                w_index += allPath.get(allPath.size()-1).weight;
                if (w_index >= w_Max){
                    allPath.remove(allPath.size()-1);
                    continue;
                }else {
                    w_Max = w_index;
                }
            }
            j1 = Integer.MAX_VALUE;
            w1 = Double.MAX_VALUE;
            for (int j = 0; j < allPath.size(); j++) {
                if (allPath.get(j).weight < w1){
                    w1 = allPath.get(j).weight;
                    j1 = j;
                }
            }
            index1 = POI_List.get(i).get(j1);
        }



//        for (int i = 0; i < path.size(); i++) {
//            w = 0;
//            for (int j = 0; j < path.get(i).size()-1; j++) {
//                q_BP1 = PointMinBP.get(path.get(i).get(j)).get(0);
//                w_BP1 = PointMinBP.get(path.get(i).get(j)).get(1);
//                if (POIList[path.get(i).get(j)].SG == POIList[path.get(i).get(j+1)].SG){
//                    for (int k = 0; k < List.get(path.get(i).get(j)).size(); k++) {
//                        if (List.get(path.get(i).get(j)).get(k).ePoint == path.get(i).get(j+1)){
//                            w += List.get(path.get(i).get(j)).get(k).w;
//                            break;
//                        }
//                    }
//                }else {
//                    if (q_BP1 != Integer.MAX_VALUE){
//                        w = w + w_BP1 + BPList.get(q_BP1).get(POIList[path.get(i).get(j+1)].SG).w;
//                        for (int k2 = 0; k2 < List.get(BPList.get(q_BP1).get(POIList[path.get(i).get(j+1)].SG).Target).size(); k2++) {
//                            if (List.get(BPList.get(q_BP1).get(POIList[path.get(i).get(j+1)].SG).Target).get(k2).ePoint == path.get(i).get(j+1)){
//                                w = w+List.get(BPList.get(q_BP1).get(POIList[path.get(i).get(j+1)].SG).Target).get(k2).w;
//                                break;
//                            }
//                        }
//                    }else {
//                        break;
//                    }
//
//                }
//
//            }
//            System.out.println("已经完成的组数："+i);
//            allPath.add(w);
//        }
//
//        int w_Min = Integer.MAX_VALUE;
//        int q2 = 0;
//        for (int i = 0; i < k3 ; i++) {
//            for (int j = 0; j < allPath.size(); j++) {
//                if (allPath.get(j) < w_Min){
//                    w_Min = allPath.get(j);
//                    q2 = j;
//                }
//                allPath.remove(q2);
//            }
//        }
       // System.out.println("1");


        return path1;

}






    public class Point_List{
        public int num;
        public ArrayList<Integer> POI_Type; //兴趣点类型
        public int Target;
        public int OS;//顶点所在子图
        public int BS; //顶点所在x轴子图
        public int Point_Type ;


        public Point_List(){
            this.num = 0;
            this.Target = 0;
            this.BS = 0;
            this.OS = 0;
            this.POI_Type = new ArrayList<>();
            this.Point_Type = 0;

        }
    }

    public class Path{
        public ArrayList<Integer> POI_Type; //兴趣点类型
        public ArrayList<Integer> path;
        public int OS;//顶点所在子图
        public int BS; //顶点所在x轴子图


        public Path(){
            this.BS = 0;
            this.OS = 0;
            this.POI_Type = new ArrayList<>();
            this.path = new ArrayList<>();
        }
    }

    public boolean isnum(int n,ArrayList<Integer> path){
        boolean flag = false;
        for (int j:path) {
            if (j == n){
                flag = true;
                break;
            }
        }

        return flag;
    }

    public List < Integer > getMinimumPath(MyGraph g, int sIndex, int tIndex, int[] path) {
        List < Integer > result = new ArrayList < > ();
        Stack< Integer > stack = new Stack < > ();
        stack.push(tIndex);
        int i = path[tIndex];
        while (i != -1) {
            stack.push(i);
            i = path[i];
        }
        while (!stack.isEmpty()) {
            result.add(g.point[stack.pop()].data);
        }
        //System.out.println();
        return result;
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
}
