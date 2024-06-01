package CreatTxt;

import CreatGraph.Creatbountpoint;
import CreatGraph.Creatpoilist;
import CreatGraph.Creatsg;
import GraphEntity.MyGraph;
import GraphEntity.POI;
import GraphEntity.huoqu_txt;
import KKRSAlgorithm.Dijkstia;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreatBPList {
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
                    cc.get(cc.size()-1).add(sp[i]);
                }
                //System.out.println("已经写入了第"+count+"条数据");
                count++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        int[][] data1 = new int[cc.size()+1][4];
        for (int i = 1; i < cc.size()+1; i++) {
            for (int j = 1; j < 4; j++) {
                data1[i][j] = Integer.parseInt(String.valueOf(cc.get(i-1).get(j-1)));
            }
        }

        data1[0][0] = 0;
        data1[0][1] = 0;
        data1[0][2] = 1;
        data1[0][3] = 100;
        int num1 = cc.size()+1;
        //获取数据中共有多少点
        int ccc = 0;
        for (int i = 0; i < data1.length; i++) {
            if (data1[i][1] >= ccc) {
                ccc = data1[i][1];
            }
        }
        int ccc1 = ccc + 1;
        MyGraph g = new MyGraph(ccc1, num1);
        g.createMyGraph(g, ccc1, num1, data1);

        int num = cc.size()+1;
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
        //构建边界顶点集合
        Creatbountpoint bp1 = new Creatbountpoint();
        int[] BP = bp1.CreatBP_BJ(num);
        //______________________________________________________________________________________________
        //构建POI索引POIList，存储POI的类型和数值
        Creatpoilist POIList1 = new Creatpoilist();
        POI[] POIList = POIList1.CreatPOIList_BJ(ccc1,SG);
        //______________________________________________________________________________________________
        //构建距离索引list
        ArrayList<ArrayList<Integer>> data2 = new ArrayList<>();
        int num11 = 1;
        int num22 = 0;
        int num33 = 0;
        Dijkstia ksp_00 =new Dijkstia();
        int sPoint = 0;
        int ePoint = 0;

        ArrayList<ArrayList<Integer>> SGBP = new ArrayList<>();
        for (int i = 0; i < SG.size(); i++) {
            SGBP.add(new ArrayList<Integer>());
        }

        for (int i = 0; i < SG.size(); i++) {
            for (int j = 0; j < SG.get(i).size(); j++) {
                if (BP[SG.get(i).get(j)] == 1){
                    SGBP.get(i).add(SG.get(i).get(j));
                }
            }
        }
        //--------------------------------------------------------------------------------------
//        按照地理坐标给图划分子图
//        记录每个顶点的坐标
//        ArrayList<CreatSG.POI_Type> POI_Type = new ArrayList<>();
//        try {
//            file1 = new FileReader("D://IDEA//POI_shuju//NY//USA-road-node.NY.txt");
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        BufferedReader br4 = new BufferedReader(file1);//读取文件
//        try {
//            String line1;
//
//            while ((line1 = br4.readLine()) != null) {//按行读取
//                String[] sp = null;
//                sp = line1.split(" ");//按空格进行分割
//                //SG.get(Integer.parseInt(sp[0])).add(Integer.parseInt(sp[1]));
//                POI_Type.add(new CreatSG.POI_Type());
//                POI_Type.get(POI_Type.size()-1).num = Integer.parseInt(sp[1]);
//                POI_Type.get(POI_Type.size()-1).x = Double.parseDouble(sp[2]);
//                POI_Type.get(POI_Type.size()-1).y = Double.parseDouble(sp[3]);
//
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        double x_min = Double.MAX_VALUE;
//        double x_max = -Double.MAX_VALUE;
//        double y_min = Double.MAX_VALUE;
//        double y_max = -Double.MAX_VALUE;
//        for (int i = 0; i < POI_Type.size(); i++) {
//            if (POI_Type.get(i).x > x_max){
//                x_max = POI_Type.get(i).x;
//            }
//            if (POI_Type.get(i).x < x_min){
//                x_min = POI_Type.get(i).x;
//            }
//        }
//        for (int i = 0; i < POI_Type.size(); i++) {
//            if (POI_Type.get(i).y > y_max){
//                y_max = POI_Type.get(i).y;
//            }
//            if (POI_Type.get(i).y < y_min){
//                y_min = POI_Type.get(i).y;
//            }
//        }
//        double x_d = (x_max - x_min)/100;
//        double y_d = (y_max - y_min)/100;
//        double x_min1 = x_min;
//        double y_min1 = y_min;
//        double x_min2 = x_min;
//        double y_min2 = y_min;
//        int F = 100; //分成多少块
//        ArrayList<ArrayList<Integer>> Point11 = new ArrayList<>();
//        for (int i = 0; i < F; i++) {
//            for (int j = 0; j < F; j++) {
//                Point11.add(new ArrayList<>());
//                Point11.get(Point11.size()-1).add(i);
//                Point11.get(Point11.size()-1).add(j);
//            }
//        }
//        int num122 = 0;
//        for (int m = 0; m < POI_Type.size(); m++) {
//           if(POIList[POI_Type.get(m).num].POI_Type != 0){
//               x_min1 = x_min;
//               y_min1 = y_min;
//               for (int i = 0; i < F; i++) {
//                   x_min1 = x_min1+x_d;
//                   x_min2 = x_min1+x_d;
//                   //x_p++;
//                   if (POI_Type.get(m).x <= x_min2 && POI_Type.get(m).x >= x_min1){
//                       for (int j = 0; j < F; j++) {
//                           y_min1 = y_min1+y_d;
//                           y_min2 = y_min1+y_d;
//                           //x_p++;
//                           if (POI_Type.get(m).y <= y_min2 && POI_Type.get(m).y >= y_min1){
//                               Point11.get(F*i+j).add(m);
//                               num122++;
//                               //System.out.println("找到了第"+m+"个点");
//                           }
//                       }
//                   }
//
//               }
//           }
//        }
//        int num133 = 0;
//        for (int i = 0; i < POIList.length; i++) {
//            if (POIList[i].POI_Type != 0){
//                num133++;
//            }
//        }
//        int num112 = 0;
//        for (int i = 0; i < Point11.size(); i++) {
//            if (Point11.get(i).size() > 2){
//                num112++;
//            }
//        }
//        ArrayList<ArrayList<Integer>> Point_BF = new ArrayList<>(); //将POI按照位置分组
//        for (int i = 0; i < Point11.size(); i++) {
//            if (Point11.get(i).size() > 2){
//                Point_BF.add(new ArrayList<>());
//                Point_BF.get(Point_BF.size()-1).addAll((Point11.get(i)));
//            }
//        }

        //--------------------------------------------------------------------------------------
        List<Dijkstia.MyPath> p00 = null;
        Dijkstia.MyPath p00_min = null;
        for (int i = 0; i <SGBP.size(); i++) {//循环多少个子图,SGBP.size()
            for (int j = 0; j < SGBP.get(i).size(); j++) {//计算每个边界顶点到其他顶点的最短路径,SGBP.get(i).size()
                for (int k = 0; k < SGBP.size(); k++) { // 这个顶点到其他99个子图的最短路径
                    for (int l = 0; l < 1; l++) { //SGBP.get(k).size()
                        p00 = ksp_00.ShortestPath (g, SGBP.get(i).get(j), SGBP.get(k).get(l),1);
                    }
                    int NUM = Integer.MAX_VALUE;
                    for (int l = 0; l < p00.size(); l++) {
                        if ((int)p00.get(l).weight < NUM){
                            NUM = (int) p00.get(l).weight;
                            p00_min = p00.get(l);
                            sPoint = p00.get(l).path.get(0);
                            ePoint = p00.get(l).path.get(p00.get(l).path.size()-1);
                        }

                    }
                    data2.add(new ArrayList<Integer>());
                    data2.get(data2.size()-1).add(i);//起点的子图编号
                    data2.get(data2.size()-1).add(k);//终点的子图编号
                    data2.get(data2.size()-1).add(sPoint);//起点
                    data2.get(data2.size()-1).add(ePoint);//终点
                    data2.get(data2.size()-1).add((int) p00_min.weight);//路径长度
                    p00.clear();
                    System.out.println("子图" +i+ "中第" + j+"个顶点"+"到子图"+k);
                    num22++;
                    num33++;
                }
            }

        }
        //--------------------------------------------------------------------------------------
        huoqu_txt.WriteTxt7(data2);


    }

}
