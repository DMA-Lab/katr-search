package loader;

import GraphEntity.Class_BPList;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Creatbplist {
    public ArrayList<ArrayList<Class_BPList>> CreatBPList_CA(ArrayList<ArrayList<Class_BPList>> BPList, int num) {
        //int[] BP = new int[num];
        FileReader file1 = null;
        int num55 = 0;
        //BufferedReader br3 = null;
        //int num1 = 0;
        //ArrayList<ArrayList<String>> BP1 = new ArrayList<>();
        try {
            file1 = new FileReader("/mnt/public/share/DATA/calcedge//calcedge_BPList1_100.txt");//calcedge_BPList_1_to_100.txt

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br4 = new BufferedReader(file1);//读取文件
        try {
            String line1;
            //int num22= 0;
            while ((line1 = br4.readLine()) != null) {//按行读取
//                num55++;
//                System.out.println("正在读取的行数为"+num55);
                String[] sp;
                sp = line1.split(" ");//按空格进行分割
                // BP1.add(new ArrayList<>());
//                for (int i = 0; i < sp.length; i++) {
//                    BP1.get(BP1.size()-1).add(sp[i]);
//                }
                BPList.get(Integer.parseInt(sp[2])).add(new Class_BPList());
                BPList.get(Integer.parseInt(sp[2])).get(BPList.get(Integer.parseInt(sp[2])).size() - 1).Target = Integer.parseInt(sp[3]);
                BPList.get(Integer.parseInt(sp[2])).get(BPList.get(Integer.parseInt(sp[2])).size() - 1).w = Integer.parseInt(sp[4]);
//                for (int i = 6; i < sp.length; i++) {
//                    BPList.get(Integer.parseInt(sp[2])).get(BPList.get(Integer.parseInt(sp[2])).size()-1).path.add(Integer.parseInt(sp[i]));
//                }
                // sp = null;
                //BP[num1] = Integer.parseInt(sp[0]);
                //num1++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println("1");


        return BPList;
    }


    public ArrayList<ArrayList<Class_BPList>> CreatBPList_COL(ArrayList<ArrayList<Class_BPList>> BPList, int num) {
        //int[] BP = new int[num];
        FileReader file1 = null;
        int num55 = 0;
        //BufferedReader br3 = null;
        //int num1 = 0;
        //ArrayList<ArrayList<String>> BP1 = new ArrayList<>();
        try {
            file1 = new FileReader("/mnt/public/share/DATA/COL//COL_BPList1_100.txt");//calcedge_BPList_1_to_100.txt

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br4 = new BufferedReader(file1);//读取文件
        try {
            String line1;
            //int num22= 0;
            while ((line1 = br4.readLine()) != null) {//按行读取
//                num55++;
//                System.out.println("正在读取的行数为"+num55);
                String[] sp;
                sp = line1.split(" ");//按空格进行分割
                // BP1.add(new ArrayList<>());
//                for (int i = 0; i < sp.length; i++) {
//                    BP1.get(BP1.size()-1).add(sp[i]);
//                }
                BPList.get(Integer.parseInt(sp[2])).add(new Class_BPList());
                BPList.get(Integer.parseInt(sp[2])).get(BPList.get(Integer.parseInt(sp[2])).size() - 1).Target = Integer.parseInt(sp[3]);
                BPList.get(Integer.parseInt(sp[2])).get(BPList.get(Integer.parseInt(sp[2])).size() - 1).w = Integer.parseInt(sp[4]);
//                for (int i = 6; i < sp.length; i++) {
//                    BPList.get(Integer.parseInt(sp[2])).get(BPList.get(Integer.parseInt(sp[2])).size()-1).path.add(Integer.parseInt(sp[i]));
//                }
                // sp = null;
                //BP[num1] = Integer.parseInt(sp[0]);
                //num1++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println("1");


        return BPList;
    }


    public ArrayList<ArrayList<Class_BPList>> CreatBPList_NY(ArrayList<ArrayList<Class_BPList>> BPList, int num) {
        //int[] BP = new int[num];
        FileReader file1 = null;
        int num55 = 0;
        //BufferedReader br3 = null;
        //int num1 = 0;
        //ArrayList<ArrayList<String>> BP1 = new ArrayList<>();
        try {
            file1 = new FileReader("/mnt/public/share/DATA/NY//NY_BPList1_200.txt");//calcedge_BPList_1_to_100.txt

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br4 = new BufferedReader(file1);//读取文件
        try {
            String line1;
            //int num22= 0;
            while ((line1 = br4.readLine()) != null) {//按行读取
//                num55++;
//                System.out.println("正在读取的行数为"+num55);
                String[] sp;
                sp = line1.split(" ");//按空格进行分割
                // BP1.add(new ArrayList<>());
//                for (int i = 0; i < sp.length; i++) {
//                    BP1.get(BP1.size()-1).add(sp[i]);
//                }
                BPList.get(Integer.parseInt(sp[2])).add(new Class_BPList());
                BPList.get(Integer.parseInt(sp[2])).get(BPList.get(Integer.parseInt(sp[2])).size() - 1).Target = Integer.parseInt(sp[3]);
                BPList.get(Integer.parseInt(sp[2])).get(BPList.get(Integer.parseInt(sp[2])).size() - 1).w = Integer.parseInt(sp[4]);
//                for (int i = 6; i < sp.length; i++) {
//                    BPList.get(Integer.parseInt(sp[2])).get(BPList.get(Integer.parseInt(sp[2])).size()-1).path.add(Integer.parseInt(sp[i]));
//                }
                // sp = null;
                //BP[num1] = Integer.parseInt(sp[0]);
                //num1++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println("1");


        return BPList;
    }
}
