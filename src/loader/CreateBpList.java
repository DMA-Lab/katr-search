package loader;

import entity.PoiPath;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CreateBpList {
    public ArrayList<ArrayList<PoiPath>> CreatBPList_CA(ArrayList<ArrayList<PoiPath>> BPList, int num) {
        FileReader file1 = null;
        try {
            //calcedge_BPList_1_to_100.txt
            file1 = new FileReader("/mnt/public/share/DATA/calcedge//calcedge_BPList1_100.txt");
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
                BPList.get(Integer.parseInt(sp[2])).add(new PoiPath());
                BPList.get(Integer.parseInt(sp[2])).getLast().target = Integer.parseInt(sp[3]);
                BPList.get(Integer.parseInt(sp[2])).getLast().weight = Integer.parseInt(sp[4]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return BPList;
    }


    public ArrayList<ArrayList<PoiPath>> CreatBPList_COL(ArrayList<ArrayList<PoiPath>> BPList, int num) {
        FileReader file1 = null;
        int num55 = 0;
        try {
            file1 = new FileReader("/mnt/public/share/DATA/COL//COL_BPList1_100.txt");//calcedge_BPList_1_to_100.txt

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br4 = new BufferedReader(file1);//读取文件
        try {
            String line1;
            while ((line1 = br4.readLine()) != null) {//按行读取
                String[] sp;
                sp = line1.split(" ");//按空格进行分割
                BPList.get(Integer.parseInt(sp[2])).add(new PoiPath());
                BPList.get(Integer.parseInt(sp[2])).get(BPList.get(Integer.parseInt(sp[2])).size() - 1).target = Integer.parseInt(sp[3]);
                BPList.get(Integer.parseInt(sp[2])).get(BPList.get(Integer.parseInt(sp[2])).size() - 1).weight = Integer.parseInt(sp[4]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return BPList;
    }


    public ArrayList<ArrayList<PoiPath>> CreatBPList_NY(ArrayList<ArrayList<PoiPath>> BPList, int num) {
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
            while ((line1 = br4.readLine()) != null) {//按行读取
                String[] sp;
                sp = line1.split(" ");//按空格进行分割
                BPList.get(Integer.parseInt(sp[2])).add(new PoiPath());
                BPList.get(Integer.parseInt(sp[2])).get(BPList.get(Integer.parseInt(sp[2])).size() - 1).target = Integer.parseInt(sp[3]);
                BPList.get(Integer.parseInt(sp[2])).get(BPList.get(Integer.parseInt(sp[2])).size() - 1).weight = Integer.parseInt(sp[4]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return BPList;
    }
}
