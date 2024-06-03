package CreateTxt;

import entity.EdegeNode;
import entity.Graph;
import entity.TextWriter;
import entity.MovingPoint;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class CreatSG {

    public static void main(String[] args) throws IOException {
        FileReader file = null;
        FileReader file1 = null;
        ArrayList<ArrayList<Integer>> cc = new ArrayList<>();
        try {
            file1 = new FileReader("dataset/USA-road-t.NY.txt");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br1 = new BufferedReader(file1);//读取文件
        try {
            String line1;
            while ((line1 = br1.readLine()) != null) {//按行读取
                String[] sp = null;
                sp = line1.split(" ");//按空格进行分割
                cc.add(new ArrayList<Integer>());
                for (int i = 1; i < 4; i++) {
                    cc.get(cc.size() - 1).add(Integer.valueOf(sp[i]));
                }
                //cc.add(new ArrayList<>());

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
            file1 = new FileReader("dataset/nyJiaquan.txt.part_2000.txt");

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


        //将各个点放入对应的子图中
        int num2 = 200; // 存储多少个子图的骨架图中的节点
        ArrayList<ArrayList<MovingPoint>> SG = new ArrayList<>(); //存储前num2个子图的骨架图中的节点
        for (int i = 0; i < num2; i++) {
            SG.add(new ArrayList<MovingPoint>());
        }
        for (int i = 0; i < Subgraph.length - 1; i++) {
            if (Subgraph[i] != null && Integer.parseInt(Subgraph[i]) < num2) {
                SG.get(Integer.parseInt(Subgraph[i])).add(new MovingPoint(i));
            }
        }


        //读取移动节点文件
//        int numPoint = 15; //记录每个子图选了多少个兴趣点
//        ArrayList<ArrayList<Integer>> movePoint = new ArrayList<>();
//        int numGraph = 100; //计算多少个子图
//        for (int i = 0; i < numGraph; i++) {
//            movePoint.add(new ArrayList<Integer>());
//        }
//        try {
//            file1 = new FileReader("D://IDEA//POI_shuju//calcedge//calcedge_15_100_movePoint.txt");
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        BufferedReader br3 = new BufferedReader(file1);//读取文件
//        try {
//            String line2;
//            int count = 0;
//            while ((line2 = br3.readLine()) != null) {//按行读取
//                String[] sp = null;
//                sp = line2.split(" ");//按空格进行分割
//                for (int i = 0; i < numPoint; i++) {
//                    movePoint.get(count).add(Integer.valueOf(sp[i]));
//                }
//                count++;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        //标记边界顶点
        int[] index_1 = new int[ccc1]; //0为普通节点，1为边界节点，
        for (int i = 0; i < ccc1; i++) {
            index_1[i] = 0;
        }
        int i1;//i1是当前节点所在的子图
        int i3;//i1是i1指向的节点所在的子图
        EdegeNode index = new EdegeNode();
        for (int i = 0; i < ccc1; i++) {
            if (Subgraph[i] != null) {
                i1 = Integer.parseInt(Subgraph[i]);
            } else {
                continue;
            }
            index = (EdegeNode) Graph.point[i].firstArc;
            while (index != null) {
                if (Subgraph[index.adj_vertex] != null) {
                    i3 = Integer.parseInt(Subgraph[index.adj_vertex]);
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
        //修改是否为边界节点
        int num11;

        for (int i = 0; i < num2; i++) {
            for (int j = 0; j < SG.get(i).size(); j++) {
                if (index_1[SG.get(i).get(j).id] == 1) {
                    SG.get(i).get(j).isBorder = 1;
                }
            }
        }
        //Random random = new Random();
//        int m = random.nextInt(40);//获取不包括10以内的数字。

        //修改兴趣点类型
//        for (int i = 0; i < num2; i++) {
//            num11 = 0;
//            for (int j = 0; j < numPoint; j++) {
//                for (int k = 0; k < SG.get(i).size(); k++) {
//                    if (SG.get(i).get(k).number == movePoint.get(i).get(j)){
//                        SG.get(i).get(k).type = num11;
//                        while (true){
//                            m = random.nextInt(40);
//                            if (m != 0){
//                                SG.get(i).get(k).POI_num = m;
//                                break;
//                            }
//                        }
//                        num11++;
//                        break;
//                    }
//                }
//            }
//        }
        ArrayList<Integer> mP = new ArrayList<>();

        int num99 = 0;
        int num7 = 0;
        int num88 = 0;
        Random random = new Random();
        for (int i = 0; i < SG.size(); i++) {
            num7 = 0;
            while (true) {
                num99 = random.nextInt(SG.get(i).size());
                if (SG.get(i).get(num99).poiType == 0) {
                    SG.get(i).get(num99).poiType = random.nextInt(50);
                    SG.get(i).get(num99).hotValue = random.nextInt(50);
                    num7++;
                }
                num88 = 0;
                for (int j = 0; j < SG.get(i).size(); j++) {
                    if (SG.get(i).get(j).hotValue != 0) {
                        num88++;
                    }
                }
                if (num88 >= 45) { //每个子图有多少个兴趣点
                    System.out.println("已完成子图的数量为：" + i);
                    break;
                }
            }
        }


//        try {
//            file1 = new FileReader("D://IDEA//POI_shuju//calcedge//calcedge_15_100_movePoint.txt");
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
//                for (int i = 0; i < sp.length; i++) {
//                    mP.add(Integer.parseInt(sp[i]));
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//       //记录每个顶点的坐标
//        ArrayList<POI_Type> POI_Type = new ArrayList<>();
//        try {
//            file1 = new FileReader("D://IDEA//POI_shuju//calcedge//calcnode.txt");
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//         br4 = new BufferedReader(file1);//读取文件
//        try {
//            String line1;
//
//            while ((line1 = br4.readLine()) != null) {//按行读取
//                String[] sp = null;
//                sp = line1.split(" ");//按空格进行分割
//                //SG.get(Integer.parseInt(sp[0])).add(Integer.parseInt(sp[1]));
//                POI_Type.add(new POI_Type());
//                POI_Type.get(POI_Type.size()-1).num = Integer.parseInt(sp[0]);
//                POI_Type.get(POI_Type.size()-1).x = Double.parseDouble(sp[1]);
//                POI_Type.get(POI_Type.size()-1).y = Double.parseDouble(sp[2]);
//
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//        //记录所有POI
//        ArrayList<POI_Type1> POI_Type1 = new ArrayList<>();
//        try {
//            file1 = new FileReader("D://IDEA//POI_shuju//calcedge//caldata.txt");
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        br4 = new BufferedReader(file1);//读取文件
//        try {
//            String line1;
//
//            while ((line1 = br4.readLine()) != null) {//按行读取
//                String[] sp = null;
//                sp = line1.split(" ");//按空格进行分割
//                //SG.get(Integer.parseInt(sp[0])).add(Integer.parseInt(sp[1]));
//                POI_Type1.add(new POI_Type1());
//                POI_Type1.get(POI_Type1.size()-1).x = Double.parseDouble(sp[0]);
//                POI_Type1.get(POI_Type1.size()-1).y = Double.parseDouble(sp[1]);
//                POI_Type1.get(POI_Type1.size()-1).POI_Type = Integer.parseInt(sp[2]);
//                POI_Type1.get(POI_Type1.size()-1).POI_Num = Integer.parseInt(sp[3]);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        //查找一共有多少个type类型
//        int num33 = 0;
//        for (int i = 0; i < POI_Type1.size(); i++) {
//            if (POI_Type1.get(i).POI_Type > num33){
//                num33 = POI_Type1.get(i).POI_Type;
//            }
//        }
//
//        int[] POI_Type_Num = new int[num33+1];
//        for (int i = 0; i < POI_Type1.size(); i++) {
//            POI_Type_Num[POI_Type1.get(i).POI_Type]++;
//        }
//
//        int[] Point2 = new int[ccc1]; //记录这些顶点是否已经有了type
//        ArrayList<ArrayList<Integer>> Point3 = new ArrayList<>();
//        int num44 = 0;
//
//        ArrayList<ArrayList<POI_Type1>> POI_Type2 = new ArrayList<>();
//        for (int i = 0; i < num33+1; i++) {
//            POI_Type2.add(new ArrayList<>());
//        }
//        for (int i = 0; i < POI_Type1.size(); i++) {
//            POI_Type2.get(POI_Type1.get(i).POI_Type).add(POI_Type1.get(i));
//        }
//        POI_Type1.clear();
//        System.out.println("123");
//        boolean flag = true;
//        int n4 = 0;
//        ArrayList<ArrayList<Integer>> POI_Type4 = new ArrayList<>();
//        ArrayList<Integer> num111 = new ArrayList<>();
//
//        for (int i = 0; i < POI_Type2.size(); i++) {
//            if (POI_Type2.get(i).size()>1000){
//                num111.add(i);
//                for (int j = 0; j < POI_Type2.get(i).size(); j++) {
//                    flag = true;
//                    for (int k = 0; k < POI_Type.size(); k++) {
//                        if (POI_Type.get(k).x >= POI_Type2.get(i).get(j).x-0.01 && POI_Type.get(k).x <= POI_Type2.get(i).get(j).x+0.01 &&
//                            POI_Type.get(k).y >= POI_Type2.get(i).get(j).y-0.01 && POI_Type.get(k).y <= POI_Type2.get(i).get(j).y+0.01
//                            && Point2[k] == 0){
//                            POI_Type4.add(new ArrayList<>());
//                            POI_Type4.get(POI_Type4.size()-1).add(k);
//                            POI_Type4.get(POI_Type4.size()-1).add(POI_Type2.get(i).get(j).POI_Type);
//                            POI_Type4.get(POI_Type4.size()-1).add(POI_Type2.get(i).get(j).POI_Num);
//                            n4++;
//                            System.out.println("已经修改的个数为"+n4);
//                            break;
//                        }
//                    }
//                }
//            }
//
//        }
//
//        int[] nn2 = new int[100];
//        n4 = 0;
//        for (int i = 0; i < POI_Type4.size(); i++) {
//            for (int j = 0; j < SG.size(); j++) {
//                for (int k = 0; k < SG.get(j).size(); k++) {
//                    if (SG.get(j).get(k).number == POI_Type4.get(i).get(0)){
//                        SG.get(j).get(k).type = POI_Type4.get(i).get(1);
//                        SG.get(j).get(k).POI_num = POI_Type4.get(i).get(2);
//                        n4++;
//                        System.out.println("SG中已经修改的个数为"+n4);
//                        nn2[j]++;
//                    }
//                }
//            }
//        }
//        System.out.println("234");
        //计算共有多少个点
        num11 = 0;
        for (int i = 0; i < SG.size(); i++) {
            num11 = num11 + SG.get(i).size();
        }
        //将构建的SG图写入txt文件
        TextWriter.WriteTxt3(SG, num11);
        //huoqu_txt.WriteTxt9(num111);
        System.out.println("完成");
    }


    public static class POI_Type {
        public int num;
        public double x;
        public double y;

//        public POI_Type(int POI_Type,int POI_Num){
//            this.POI_Num = POI_Num;
//            this.POI_Type = POI_Type;
//        }
//
//        public POI_Type(int Boundary, int POI_Type,int POI_Num){
//            this.Boundary = Boundary;
//            this.POI_Num = POI_Num;
//            this.POI_Type = POI_Type;
//        }

        public POI_Type() {
            this.num = 0;
            this.x = 0;
            this.y = 0;
        }


    }


    public static class POI_Type1 {
        public double x;
        public double y;
        public int POI_Type;
        public int POI_Num;

//        public POI_Type(int POI_Type,int POI_Num){
//            this.POI_Num = POI_Num;
//            this.POI_Type = POI_Type;
//        }
//
//        public POI_Type(int Boundary, int POI_Type,int POI_Num){
//            this.Boundary = Boundary;
//            this.POI_Num = POI_Num;
//            this.POI_Type = POI_Type;
//        }

        public POI_Type1() {
            //this.num = 0;
            this.x = 0;
            this.y = 0;
            this.POI_Num = 0;
            this.POI_Type = 0;
        }


    }
}
