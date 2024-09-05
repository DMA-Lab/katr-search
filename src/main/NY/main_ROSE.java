// Top k Optimal Sequenced Route Query with Poi Preferences
// https://link.springer.com/article/10.1007/s41019-022-00177-5

package main.NY;

import baseline.ROSE.Find_Topk_NoOpt;
import baseline.ROSE.Find_path_MTDOSR;
import entity.PoiPath;
import entity.Graph;
import entity.Poi;
import entity.Path;
import loader.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class main_ROSE {

    static long time2;
    static final int num5 = 1; //循环次数

    public static void main(String[] args) throws IOException {

        int[] Poi_Type = {43, 25, 14, 28, 19, 26};//43,25,14,28,19,26,48,47时间，43,25,5,18,19,26,48,47剪枝效率
        int k1 = 6;
        double a = 0.5;//α
        int endIndex = 6000;
        ArrayList<Find_path_MTDOSR.Path> Top_k_MTDOSR = MTDOSR(Poi_Type, k1);
    }

    public static ArrayList<Find_path_MTDOSR.Path> MTDOSR(int[] Poi_Type2, int k1) throws IOException {
        FileReader file = null;
        try {
            file = new FileReader("D://IDEA//USA-road-t.NY.gr//USA-road-t.NY.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int num = 1;
        assert file != null;
        BufferedReader br = new BufferedReader(file);
        String line;
        try {
            br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            while (br.readLine() != null) {//按行读取
                num++;
            }
            file.close();
            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        //将流中的拘束存储到data[][]中
        int[][] data = new int[num + 1][4];
        FileReader file1 = null;

        //String []sp = null;
        String[][] c = new String[num][4];
        try {
            file1 = new FileReader("D://IDEA//USA-road-t.NY.gr//USA-road-t.NY.txt");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert file1 != null;
        BufferedReader br1 = new BufferedReader(file1);//读取文件
        try {
            String line1;
            int count = 0;
            while ((line1 = br1.readLine()) != null) {//按行读取
                String[] sp;
                sp = line1.split(" ");//按空格进行分割
                System.arraycopy(sp, 1, c[count], 1, 3);
                count++;
            }
            for (int i = 0; i < num; i++) {
                for (int j = 1; j < 4; j++) {
                    data[i][j] = Integer.parseInt(c[i][j]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        data[num][0] = 0;
        data[num][1] = 0;
        data[num][2] = 1;
        data[num][3] = 100;
        int num1 = num + 1;
        //获取数据中共有多少点
        int ccc = 0;
        for (int i = 0; i < num; i++) {
            if (data[i][1] >= ccc) {
                ccc = data[i][1];

            }

        }
        int ccc1 = ccc + 1;
        Graph g = new Graph(ccc1, num1);
        g.init(ccc1, num1, data);
        //划分子图
        try {
            file1 = new FileReader("D://IDEA//USA-road-t.NY.gr//AHP//nyJiaquan.txt.part_2000.txt");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br2 = new BufferedReader(file1);//读取文件
        String[] Subgraph = new String[num];
        try {
            String line2;
            int count = 0;
            while ((line2 = br2.readLine()) != null) {//按行读取
                Subgraph[count] = line2;
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //______________________________________________________________________________________________
        //将各个点放入对应的子图中
        int num2 = 200; // 存储多少个子图的骨架图中的节点
        CreateSubgraph SG1 = new CreateSubgraph();
        //BufferedReader br3 = new BufferedReader(file1);//读取文件
        ArrayList<ArrayList<Integer>> SG = SG1.CreatSG_NY(num2); //存储前num2个子图的骨架图中的节点

        //______________________________________________________________________________________________
        //构建Poi索引PoiList，存储Poi的类型和数值，并给每个顶点赋予坐标
        CreatePoiList PoiList1 = new CreatePoiList();
        Poi[] PoiList = PoiList1.CreatPoiList_NY(ccc1, SG);
        //______________________________________________________________________________________________
        // //构建距离索引list
        CreateList list1 = new CreateList();
        ArrayList<ArrayList<Path>> List = list1.CreatList_NY(ccc1);
        //System.out.println("1");
        boolean flag;
        ArrayList<Integer> Poi_Type_Num = new ArrayList<>();
        for (Poi poi : PoiList) {
            if (poi.Poi_Type != 0) {
                flag = true;
                for (Integer integer : Poi_Type_Num) {
                    if (poi.Poi_Type == integer) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    Poi_Type_Num.add(poi.Poi_Type);
                }
            }
        }
        //______________________________________________________________________________________________
        // //构建边界顶点索引BPList
        ArrayList<ArrayList<PoiPath>> BPList = new ArrayList<>();
        for (int i = 0; i < ccc1; i++) {
            BPList.add(new ArrayList<>());
        }
        CreateBpList BPList1 = new CreateBpList();
        //System.out.println("111");
        BPList1.CreatBPList_NY(BPList, ccc1);
        //______________________________________________________________________________________________

        //计算全部点到最近的边界顶点的距离
        ArrayList<ArrayList<Integer>> PointMinBP = CreateMinBpTable.CreatMinBP_NY();


        //查找top_k
        int k = k1; //计算多少条路径
        int q = 56988; //查询点
        double a = 0.2; //α
        int q_SG = 0;
        boolean flag1 = true;
        //int[] Poi_Type = {11,12,16} ;//所求的Poi的类型
        int[] Poi_Type = Poi_Type2;//36,54,50,1,6,3,9多，49,48,33,38,23,58,11少
        ArrayList<Find_path_MTDOSR.Path> Top_k = new ArrayList<>();
        for (int ii = 0; ii < num5; ii++) {
            Find_path_MTDOSR topk = new Find_path_MTDOSR();
            Find_Topk_NoOpt topk_No = new Find_Topk_NoOpt();
            long startTime1 = System.currentTimeMillis(); //开始获取时间
            Top_k = topk.FindPath(g, q, Poi_Type, PoiList);
            long endTime1 = System.currentTimeMillis(); //开始获取时间
//         if (ii != 0 || num5 == 1){
//             time2 += endTime1 - startTime1;
//         }
            time2 += endTime1 - startTime1;
        }
//        long startTime2 = System.currentTimeMillis(); //开始获取时间
//        ArrayList<Lower_bound> Top_k_NoOpt = topk_No.Top_k_NOOPT(g,q,q_SG,k,Poi_Type,SG,List,PoiList,a,BPList,PointMinBP);
//        long endTime2 = System.currentTimeMillis(); //开始获取时间
//        long time2 = endTime2 - startTime2;

        //测试图中各个兴趣点的数量以及分布情况
//        ArrayList<Integer> Poi_Type1 = new ArrayList<>();
//        ArrayList<ArrayList<Integer>> Poi_SGNum = new ArrayList<>();
//        for (int i = 0; i < SG.size(); i++) {
//            for (int j = 0; j < SG.get(i).size(); j++) {
//               if (PoiList[SG.get(i).get(j)].Poi_Type != 0){
//                   if (isnum(PoiList[SG.get(i).get(j)].Poi_Type,Poi_Type1) == false){
//                       Poi_Type1.add(PoiList[SG.get(i).get(j)].Poi_Type);
//                   }
//               }
//            }
//        }
//        for (int i = 0; i < Poi_Type1.size(); i++) {
//            Poi_SGNum.add(new ArrayList<>());
//        }
//
//        for (int ii = 0; ii < Poi_Type1.size(); ii++) {
//            for (int i = 0; i < SG.size(); i++) {
//                for (int j = 0; j < SG.get(i).size(); j++) {
//                    if (PoiList[SG.get(i).get(j)].Poi_Type == Poi_Type1.get(ii)){
//                        Poi_SGNum.get(ii).add(i);
//                        break;
//                    }
//                }
//            }
//        }
//        for (int i = 0; i < Poi_Type1.size(); i++) {
//            System.out.println(Poi_Type1.get(i)+"所在子图的数量为："+Poi_SGNum.get(i).size());
//        }


        return Top_k;

    }
}
