package CreateTxt;

import loader.Creatsg;
import GraphEntity.EdegeNode;
import GraphEntity.MyGraph;
import GraphEntity.huoqu_txt;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CreatBountPoint {
    public static void main(String[] args) throws IOException {
        FileReader file1 = null;
        ArrayList<ArrayList<String>> cc = new ArrayList<>();
        try {
            file1 = new FileReader("D://IDEA//USA-road-t.NY.gr//USA-road-t.NY.txt");

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
        MyGraph g = new MyGraph(ccc1, num1);
        g.createMyGraph(g, ccc1, num1, data1);

        int num = ccc1;
        //划分子图
        //System.out.println("1");
        try {
            file1 = new FileReader("D://IDEA//USA-road-t.NY.gr//DKSP//NY_1300.txt");

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
        int num2 = 650; // 存储多少个子图的骨架图中的节点
        Creatsg SG1 = new Creatsg();
        BufferedReader br3 = new BufferedReader(file1);//读取文件
        ArrayList<ArrayList<Integer>> SG = SG1.CreatSG_NY2(num2); //存储前num2个子图的骨架图中的节点

        //______________________________________________________________________________________________
        //计算边界边界顶点
        //int[] bountPoint = new int[ccc1];
        int[] index_1 = new int[ccc1]; //0为普通节点，1为边界节点，
        int i1;//i1是当前节点所在的子图
        int i3;//i1是i1指向的节点所在的子图
        EdegeNode index = new EdegeNode();
        for (int i = 0; i < num; i++) {
            if (Subgraph[i] != null) {
                i1 = Integer.parseInt(Subgraph[i]);
            } else {
                continue;
            }
            index = (EdegeNode) MyGraph.point[i].firstArc;
            while (index != null) {
                if (Subgraph[index.adjvex] != null) {
                    i3 = Integer.parseInt(Subgraph[index.adjvex]);
                    if (i1 != i3) {
                        index_1[i] = 1;
                        break;
                    }
                    index = index.nextEdge;
                } else {
                    index = index.nextEdge;
                }
            }
        }
        huoqu_txt.WriteTxt5(index_1);

        System.out.println("1");
    }
}
