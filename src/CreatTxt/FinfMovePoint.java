package CreatTxt;

import CreatGraph.Creatsg;
import GraphEntity.MyGraph;
import GraphEntity.huoqu_txt;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class FinfMovePoint {
    public static void main(String[] args) throws IOException {
        FileReader file = null;
        FileReader file1 = null;
        ArrayList<ArrayList<Double>> c1 = new ArrayList<>();
        try {
            file1 = new FileReader("D://IDEA//POI_shuju//calcedge//calcedge1.txt");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br1 = new BufferedReader(file1);//读取文件
        try {
            String line1;
            while ((line1 = br1.readLine()) != null) {//按行读取
                String[] sp = null;
                sp = line1.split(" ");//按空格进行分割
                c1.add(new ArrayList<Double>());
                for (int i = 1; i < 4; i++) {
                    c1.get(c1.size()-1).add(Double.valueOf(sp[i]));
                }
                c1.add(new ArrayList<Double>());
                for (int i = 1; i < 4; i++) {
                    //c[count][i] = sp[i];
                    if (i == 1){
                        c1.get(c1.size()-1).add(Double.valueOf(sp[2]));
                    }else if(i ==2){
                        c1.get(c1.size()-1).add(Double.valueOf(sp[1]));
                    }else if(i == 3){
                        c1.get(c1.size()-1).add(Double.valueOf(sp[3]));
                    }

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        double[][] data = new double[c1.size()][4];
        for (int i = 0; i < c1.size(); i++) {
            for (int j = 1; j < 4; j++) {
                data[i][j] = Double.parseDouble(String.valueOf(c1.get(i).get(j-1)));
            }
        }
        int[][] data1 = new int[data.length+1][4];
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
        data1[data.length][0] = 0;
        data1[data.length][1] = 0;
        data1[data.length][2] = 1;
        data1[data.length][3] = 100;
        int num1 = c1.size()+1;
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

        int num = c1.size()+1;
        //划分子图
        System.out.println("1");
        try {
            file1 = new FileReader("D://IDEA//POI_shuju//calcedge//calcedge_100.txt");

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
        int num2 = 100; // 存储多少个子图的骨架图中的节点
        Creatsg SG1 = new Creatsg();
        BufferedReader br3 = new BufferedReader(file1);//读取文件
        ArrayList<ArrayList<Integer>> SG = SG1.CreatSG_CA(num2); //存储前num2个子图的骨架图中的节点


        //每个子图获取多个个随机的移动节点
        ArrayList<ArrayList<Integer>> movePoint = new ArrayList<>();
        int numPoint = 15; //每个子图获得的移动节点数目
        int numGraph = 100; //计算多少个子图
        for (int i = 0; i < numGraph; i++) {
            movePoint.add(new ArrayList<Integer>());
        }
        Random random = new Random();
        int i = 0;
        for (int j = 0; j < numGraph; j++) {
            for (int k = 0; k < numPoint; k++) {
                i = random.nextInt(SG.get(j).size());//获取随机数字
                movePoint.get(j).add(SG.get(j).get(i));
            }
        }
        huoqu_txt.WriteTxt2(movePoint,numPoint);
        System.out.println("1");




    }
}
