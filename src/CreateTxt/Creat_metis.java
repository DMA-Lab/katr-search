package CreateTxt;

import GraphEntity.huoqu_txt;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Creat_metis {
    public static void main(String[] args) throws InterruptedException, IOException {
        FileReader file = null;
        try {
            file = new FileReader("D://IDEA//POI_shuju//NY//USA-road-t.NY.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int num = 1;
        BufferedReader br = new BufferedReader(file);
        String line;
        try {
            line = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            while ((line = br.readLine()) != null) {//按行读取
                num++;
            }
            file.close();
            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        //将流中的拘束存储到data[][]中

        FileReader file1 = null;

        //String []sp = null;
        String[][] c = new String[num][4];
        ArrayList<ArrayList<String>> cc = new ArrayList<>();
        try {
            file1 = new FileReader("D://IDEA//POI_shuju//NY//USA-road-t.NY.txt");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br1 = new BufferedReader(file1);//读取文件
        try {
            String line1;
            int count = 0;
            while ((line1 = br1.readLine()) != null) {//按行读取
                String[] sp = null;
                sp = line1.split(" ");//按空格进行分割
                cc.add(new ArrayList<String>());
                for (int i = 1; i < 4; i++) {
                    //c[count][i] = sp[i];
                    cc.get(cc.size() - 1).add(sp[i]);
                }
                //System.out.println("已经写入了第"+count+"条数据");
                count++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<ArrayList<Integer>> data1 = new ArrayList<>();
        //int[][] data1 = new int[cc.size()][4];
        for (int i = 0; i < cc.size(); i++) {
            data1.add(new ArrayList<Integer>());
            for (int j = 1; j < 4; j++) {
                //data1[i][j] = Integer.parseInt(cc.get(i).get(j-1));
                data1.get(data1.size() - 1).add(Integer.parseInt(cc.get(i).get(j - 1)));
            }
        }
//        int[][] data1 = new int[data.length][4];
//        int n1 = 0;
//        for (int i = 0; i < data.length; i++) {
//            for (int j = 0; j < 4; j++) {
//                if (j ==3){
//                   // n1 = (int) (data[i][3]*10000);
//                    data1[i][3] = n1;
//                }else {
//                    data1[i][j] = (int) data[i][j];
//                }
//            }
//        }

//        for (int i = 0; i < data1.length; i++) {
//            if (data1[i][1] == i)
//        }
        int ccc = 0;

        for (int i = 0; i < data1.size(); i++) {
            if (data1.get(i).get(1) >= ccc) {
                ccc = data1.get(i).get(1);
            }

        }
        int ccc1 = ccc + 1;
        //把顶点0换成10
        ArrayList<Integer> path77 = new ArrayList<>();
        for (int i = 0; i < data1.size(); i++) {
            for (int j = 0; j < 2; j++) {
                if (data1.get(i).get(j) == 0) {
                    path77.addAll(data1.get(i));
                    data1.get(i).clear();
                    for (int k = 0; k < path77.size(); k++) {
                        if (k == 1) {
                            data1.get(i).add(10);
                        } else {
                            data1.get(i).add(path77.get(k));
                        }
                    }
                    path77.clear();
                }
            }
        }

        ArrayList<ArrayList<Integer>> all = new ArrayList<>();
        for (int i = 0; i < ccc1; i++) {
            all.add(new ArrayList<Integer>());
        }
        for (int i = 0; i < data1.size(); i++) {
            all.get(data1.get(i).get(0)).add(data1.get(i).get(1));
            all.get(data1.get(i).get(0)).add(data1.get(i).get(2));
        }
        System.out.println("1");
//        int num999 = 0;
//        for (int i = 0; i < all.size(); i++) {
//            if (all.get(i).size() == 0){
//                num999++;
//            }
//        }


//            for (int j = 0; j < data1.length; j++) {
//                if (data1[j][1] == i){
//                    all.get(i).add(data1[j][2]);
//                    all.get(i).add(data1[j][3]);
//                }
//            }

        int num111 = 0;
        for (int i = 0; i < all.size(); i++) {
            num111 = num111 + all.get(i).size() / 2;
        }

        huoqu_txt.WriteTxt6(all, ccc1 - 1, num111 / 2);

        //检查一下有没有size为0的点
        //int num55 = 0;
        int num66 = data1.size();
        ArrayList<Integer> path33 = new ArrayList<>();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).size() == 0 && i != 10) {
                if (i == 0) {
                    all.get(i).add(1);
                    all.get(i).add(100);
                    num66 = num66 + 1;
                } else {
                    all.get(i).add(10);
                    all.get(i).add(100);
                    all.get(10).add(i);
                    all.get(10).add(100);
                    num66 = num66 + 2;
                }


            } else if (all.get(i).size() == 0 && i == 10) {
                if (i == 0) {
                    all.get(i).add(1);
                    all.get(i).add(100);
                    num66 = num66 + 1;
                } else {
                    all.get(i).add(11);
                    all.get(i).add(100);
                    all.get(11).add(i);
                    all.get(11).add(100);
                    num66 = num66 + 2;
                }
            } else {
                for (int j = 0; j < all.get(i).size(); j++) {
                    if (all.get(i).get(j) < 0) {
                        // num55++;
                        path33.addAll(all.get(i));
                        all.get(i).clear();
                        for (int k = 0; k < path33.size(); k++) {
                            if (k == j) {
                                all.get(i).add((-path33.get(k)));
                            } else {
                                all.get(i).add(path33.get(k));
                            }
                        }
                        path33.clear();
                    } else if (all.get(i).get(j) == 0) {
                        //num55++;
                        path33.addAll(all.get(i));
                        all.get(i).clear();
                        for (int k = 0; k < path33.size(); k++) {
                            if (k == j) {
                                all.get(i).add(100);
                            } else {
                                all.get(i).add(path33.get(k));
                            }
                        }
                        path33.clear();
                    }
                }
            }

        }
        int num3 = 0;
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).size() == 0) {
                num3++;
            }
        }
        int num99 = 0;
        for (int i = 0; i < all.size(); i++) {
            num99 = num99 + (all.get(i).size() / 2);
        }
        //huoqu_txt.WriteTxt6(all,ccc1-1, num66-(all.get(0).size()/2));
        System.out.println("1");
//        for (int i = 1; i < ccc1; i++) {
//            g2 = (EdegeNode) g.point[i].firstArc;
//            while (g2 != null) {
//                all.get(i).add(g2.adjvex);
//                all.get(i).add(g2.value);
//                g2 = g2.nextEdge;
//            }
//        }

//        data[num][0] = 0;
//        data[num][1] = 0;
//        data[num][2] = 1;
//        data[num][3] = 100;
//        int num1 = num + 1;
//        //获取数据中共有多少点
//        int ccc = 0;
//        for (int i = 0; i < num; i++) {
//            if (data[i][1] >= ccc) {
//                ccc = (int) data[i][1];
//
//            }
//
//        }
//        int[][] data2 = new int[1][1];
//        int ccc1 = ccc + 1;
//        MyGraph g = new MyGraph(ccc1, num1);
//        g.createMyGraph(g, ccc1, num1, data1);
//        //划分子图
//        try {
//            file1 = new FileReader("D://IDEA//USA-road-t.NY.gr//AHP//nyJiaquan.txt.part_2000.txt");
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        BufferedReader br2 = new BufferedReader(file1);//读取文件
//        String[] Subgraph = new String[num];
//        try {
//            String line2;
//            int count = 0;
//            while ((line2 = br2.readLine()) != null) {//按行读取
//                Subgraph[count] = line2;
//                count++;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        //______________________________________________________________________________________________
//        //将各个点放入对应的子图中
//        int num2 = 100; // 存储多少个子图的骨架图中的节点
//        Creatsg SG1 = new Creatsg();
//        BufferedReader br3 = new BufferedReader(file1);//读取文件
//        ArrayList<ArrayList<Integer>> SG = SG1.CreatSG(num2); //存储前num2个子图的骨架图中的节点
//        //______________________________________________________________________________________________
//        //构建边界顶点集合
//        Creatbountpoint bp1 = new Creatbountpoint();
//        int[] BP = bp1.CreatBP(num);
//
//        //______________________________________________________________________________________________
//        //构建POI索引POIList，存储POI的类型和数值
//        Creatpoilist POIList1 = new Creatpoilist();
//        POI[] POIList = POIList1.CreatPOIList(ccc1);
//        POIList[75284].POI_Type = 2;
//        POIList[75284].POI_Num = 10;
//        POIList[75290].POI_Type = 3;
//        POIList[75290].POI_Num = 12;
//        POIList[75463].POI_Type = 4;
//        POIList[75463].POI_Num = 16;
//
//        //______________________________________________________________________________________________
//        // //构建距离索引list
//        Creatlist list1 = new Creatlist();
//        ArrayList<ArrayList<list>> List = list1.CreatList(num2);
//        System.out.println("1");
//
//        //查找top_k
//        int k = 5; //计算多少条路径
//        int q = 57807; //查询点
//        double a = 0.001 ; //α
//        int[] POI_Type = {2,3,4}; //所求的POI的类型
//        Find_Topk topk = new Find_Topk();
//        ArrayList<Lower_bound> Top_k = topk.Top_k(g,q,k,POI_Type,SG,List,POIList,a,BP);

    }
}
