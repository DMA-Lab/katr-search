package CreatTxt;

import GraphEntity.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Creat_metis_danbianshaung {
    public static void main(String[] args) throws InterruptedException, IOException {
        FileReader file = null;
        try {
            file = new FileReader("D://IDEA//POI_shuju//calcedge1.txt");
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
            file1 = new FileReader("D://IDEA//POI_shuju//calcedge1.txt");

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
                    cc.get(cc.size()-1).add(sp[i]);
                }
                cc.add(new ArrayList<String>());
                for (int i = 1; i < 4; i++) {
                    //c[count][i] = sp[i];
                    if (i == 1){
                        cc.get(cc.size()-1).add(sp[2]);
                    }else if(i ==2){
                        cc.get(cc.size()-1).add(sp[1]);
                    }else if(i == 3){
                        cc.get(cc.size()-1).add(sp[3]);
                    }

                }

                count++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        double[][] data = new double[cc.size()][4];
        for (int i = 0; i < cc.size(); i++) {
            for (int j = 1; j < 4; j++) {
                data[i][j] = Double.parseDouble(cc.get(i).get(j-1));
            }
        }
        int[][] data1 = new int[data.length][4];
        int n1 = 0;
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < 4; j++) {
                if (j ==3){
                    n1 = (int) (data[i][3]*10000);
                    data1[i][3] = n1;
                }else {
                    data1[i][j] = (int) data[i][j];
                }
            }
        }

//        for (int i = 0; i < data1.length; i++) {
//            if (data1[i][1] == i)
//        }
        int ccc = 0;

        for (int i = 0; i < data1.length; i++) {
            if (data1[i][1] >= ccc) {
                ccc = data1[i][1];
            }

        }
        int ccc1 = ccc+1;
        for (int i = 0; i < data1.length; i++) {
            for (int j = 1; j < 3 ; j++) {
                if (data1[i][j] == 0){
                    data1[i][j] = 10;
                }
            }
        }

        ArrayList<ArrayList<Integer>> all = new ArrayList<>();
        for (int i = 0; i < ccc1; i++) {
            all.add(new ArrayList<Integer>());
        }
        for (int i = 0; i < data1.length; i++) {
            for (int j = 0; j < data1.length; j++) {
                if (data1[j][1] == i){
                    all.get(i).add(data1[j][2]);
                    all.get(i).add(data1[j][3]);
                }
            }
        }
        int num3 = 0;
        for (int i = 0; i < all.size(); i++) {
            if(all.get(i).size() == 0){
                num3++;
            }
        }
        huoqu_txt.WriteTxt6(all,ccc1, data1.length);
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
