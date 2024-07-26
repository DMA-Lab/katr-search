package CreateTxt;

import KKRSAlgorithm.Dijkstia;
import entity.Graph;
import entity.POI;
import entity.TextWriter;
import loader.Creatpoilist;
import loader.Creatsg;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreatList {

    public static void main(String[] args) throws InterruptedException, IOException {
        FileReader file1 = null;
        ArrayList<ArrayList<String>> cc = new ArrayList<>();
        try {
            file1 = new FileReader("D://IDEA//POI_shuju//COL//USA-road-d.COL.txt");

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

        int[][] data1 = new int[cc.size() + 1][4];
        for (int i = 1; i < cc.size() + 1; i++) {
            for (int j = 1; j < 4; j++) {
                data1[i][j] = Integer.parseInt(String.valueOf(cc.get(i - 1).get(j - 1)));
            }
        }

        data1[0][0] = 0;
        data1[0][1] = 0;
        data1[0][2] = 1;
        data1[0][3] = 100;
        int num1 = cc.size() + 1;
        //获取数据中共有多少点
        int ccc = 0;
        for (int i = 0; i < data1.length; i++) {
            if (data1[i][1] >= ccc) {
                ccc = data1[i][1];
            }
        }
        int ccc1 = ccc + 1;
        Graph g = new Graph(ccc1, num1);
        g.createMyGraph(g, ccc1, num1, data1);

        int num = cc.size() + 1;
        //划分子图
        //System.out.println("1");
        try {
            file1 = new FileReader("D://IDEA//POI_shuju//COL//COL_4000.txt");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br2 = new BufferedReader(file1);//读取文件
        String[] Subgraph = new String[ccc1];
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
        int num2 = 100; // 存储多少个子图的骨架图中的节点
        Creatsg SG1 = new Creatsg();
        //BufferedReader br3 = new BufferedReader(file1);//读取文件
        ArrayList<ArrayList<Integer>> SG = SG1.CreatSG_COL(num2); //存储前num2个子图的骨架图中的节点

        //______________________________________________________________________________________________
        //构建POI索引POIList，存储POI的类型和数值
        Creatpoilist POIList1 = new Creatpoilist();
        POI[] POIList = POIList1.CreatPOIList_BJ(ccc1, SG);

        //______________________________________________________________________________________________
        //构建距离索引list
        ArrayList<ArrayList<Integer>> data2 = new ArrayList<>();
        int num11 = 1;
        int num22 = 0;
        int num33 = 0;
        Dijkstia ksp_00 = new Dijkstia();
        List<Dijkstia.MyPath> p00;
        int k1 = 1; // 计算多少条最短路径
        for (int i = 0; i < SG.size(); i++) { //循环多少个子图,SG.size()
            for (int j = 0; j < SG.get(i).size(); j++) { //计算每个边界顶点到其他顶点的最短路径,SG.get(i).size()
                for (int k = j + 1; k < SG.get(i).size(); k++) {
                    p00 = Dijkstia.ShortestPath(g, SG.get(i).get(j), SG.get(i).get(k), 1);
                    //index_1[SG.get(i).get(j)] = 3;//该节点今后不需要运算
                    for (int l = 0; l < p00.size(); l++) { //存入全部的最短路径
                        data2.add(new ArrayList<Integer>());
                        data2.get(num33).add(num11 - 1);//第几个子图
                        data2.get(num33).add(SG.get(i).get(j));//起始节点
                        data2.get(num33).add(SG.get(i).get(k));//终点
                        data2.get(num33).add(p00.get(l).path.size());//路径共有多少个节点
                        data2.get(num33).add((int) p00.get(l).weight);//路径长度
//                        for (int m = 0; m < p00.get(l).path.size(); m++) {
//                            data2.get(num33).add(p00.get(l).path.get(m));
//                        }
                        num33++;
                    }
                    num22++;
                    System.out.println("已经计算点的个数：" + num11 + "," + num22);
                    p00.clear();

                }
            }
            num11++;
        }
        TextWriter.writeTxt4(data2);


    }

}
