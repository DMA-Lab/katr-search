package test;

import preprocessing.GraphPreprocessing;
import entity.Graph;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MappingDistance {

    public static void main(String[] args) {

        int[] Poi_Type = {43, 25, 5, 18, 19, 26, 48, 47};//43,25,14,28,19,26,48,47时间，43,25,5,18,19,26,48,47剪枝效率
        int k1 = 10;
        double a = 0.5;//α
        int num = 10; //循环次数

        GraphPreprocessing GraphPreprocessing = new GraphPreprocessing();
        MappingDistance.CreatData(Poi_Type, k1, a, GraphPreprocessing);
    }


    public static void CreatData(int[] Poi_Type2, int k1, double a, GraphPreprocessing GraphPreprocessing) {

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
        GraphPreprocessing.graph = new Graph(ccc1, num1);
        GraphPreprocessing.graph.init(ccc1, num1, data);
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
        int distMin = Integer.MAX_VALUE;
        int distMAX = 0;
        for (int[] datum : data) {
            if (datum[3] > distMAX) {
                distMAX = datum[3];
            }
            if (datum[3] < distMin) {
                distMin = datum[3];
            }
        }
    }
}
