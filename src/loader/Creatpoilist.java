package loader;

import GraphEntity.POI;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Creatpoilist {
    public POI[] CreatPOIList_NY(int ccc1, ArrayList<ArrayList<Integer>> SG) {
        POI[] POIList = new POI[ccc1];
        for (int i = 0; i < ccc1; i++) {
            POIList[i] = new POI();
        }

        FileReader file1 = null;
        BufferedReader br3 = null;
        try {
            file1 = new FileReader("/mnt/public/share/DATA/NY//NY_POIPoint_SG.txt");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br4 = new BufferedReader(file1);//读取文件
        try {
            String line1;

            while ((line1 = br4.readLine()) != null) {//按行读取
                String[] sp = null;
                sp = line1.split(" ");//按空格进行分割
                //SG.get(Integer.parseInt(sp[0])).add(Integer.parseInt(sp[1]));
                POIList[Integer.parseInt(sp[1])].Boundary = Integer.parseInt(sp[3]);
                POIList[Integer.parseInt(sp[1])].POI_Type = Integer.parseInt(sp[2]);
                POIList[Integer.parseInt(sp[1])].POI_Num = Integer.parseInt(sp[4]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < SG.size(); i++) {
            for (int j = 0; j < SG.get(i).size(); j++) {
                POIList[SG.get(i).get(j)].SG = i;
            }
        }
        //给每个顶点赋予坐标
        double num33 = 0;
        int num22 = 0;
        try {
            file1 = new FileReader("/mnt/public/share/DATA/NY//USA-road-node.NY.txt");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        br4 = new BufferedReader(file1);//读取文件
        try {
            String line1;

            while ((line1 = br4.readLine()) != null) {//按行读取
                String[] sp = null;
                sp = line1.split(" ");//按空格进行分割
                num33 = Double.parseDouble(sp[2]);
                POIList[num22].x = (int) (num33);
                num33 = Double.parseDouble(sp[3]);
                POIList[num22].y = (int) (num33);
                num22++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return POIList;
    }

    public POI[] CreatPOIList_ca(int ccc1, ArrayList<ArrayList<Integer>> SG) {
        POI[] POIList = new POI[ccc1];
        for (int i = 0; i < ccc1; i++) {
            POIList[i] = new POI();
        }

        FileReader file1 = null;
        BufferedReader br3 = null;
        try {
            file1 = new FileReader("/mnt/public/share/DATA/calcedge//calcedge_POIPoint_SG_15_100.txt");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br4 = new BufferedReader(file1);//读取文件
        try {
            String line1;

            while ((line1 = br4.readLine()) != null) {//按行读取
                String[] sp = null;
                sp = line1.split(" ");//按空格进行分割
                //SG.get(Integer.parseInt(sp[0])).add(Integer.parseInt(sp[1]));
                POIList[Integer.parseInt(sp[1])].Boundary = Integer.parseInt(sp[3]);
                POIList[Integer.parseInt(sp[1])].POI_Type = Integer.parseInt(sp[2]);
                POIList[Integer.parseInt(sp[1])].POI_Num = Integer.parseInt(sp[4]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < SG.size(); i++) {
            for (int j = 0; j < SG.get(i).size(); j++) {
                POIList[SG.get(i).get(j)].SG = i;
            }
        }
        //给每个顶点赋予坐标
        double num33 = 0;
        int num22 = 0;
        try {
            file1 = new FileReader("/mnt/public/share/DATA/calcedge//calcnode.txt");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        br4 = new BufferedReader(file1);//读取文件
        try {
            String line1;

            while ((line1 = br4.readLine()) != null) {//按行读取
                String[] sp = null;
                sp = line1.split(" ");//按空格进行分割
                num33 = Double.parseDouble(sp[1]);
                POIList[num22].x = (int) (num33 * 10000);
                num33 = Double.parseDouble(sp[2]);
                POIList[num22].y = (int) (num33 * 10000);
                num22++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return POIList;
    }


    public POI[] CreatPOIList_BJ(int ccc1, ArrayList<ArrayList<Integer>> SG) {
        POI[] POIList = new POI[ccc1];
        for (int i = 0; i < ccc1; i++) {
            POIList[i] = new POI();
        }

        FileReader file1 = null;
        BufferedReader br3 = null;
        try {
            file1 = new FileReader("/mnt/public/share/DATA/BJ//BJ_POIPoint_SG.txt");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br4 = new BufferedReader(file1);//读取文件
        try {
            String line1;

            while ((line1 = br4.readLine()) != null) {//按行读取
                String[] sp = null;
                sp = line1.split(" ");//按空格进行分割
                //SG.get(Integer.parseInt(sp[0])).add(Integer.parseInt(sp[1]));
                POIList[Integer.parseInt(sp[1])].Boundary = Integer.parseInt(sp[3]);
                POIList[Integer.parseInt(sp[1])].POI_Type = Integer.parseInt(sp[2]);
                POIList[Integer.parseInt(sp[1])].POI_Num = Integer.parseInt(sp[4]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < SG.size(); i++) {
            for (int j = 0; j < SG.get(i).size(); j++) {
                POIList[SG.get(i).get(j)].SG = i;
            }
        }

        return POIList;
    }

    public POI[] CreatPOIList_COL(int ccc1, ArrayList<ArrayList<Integer>> SG) {
        POI[] POIList = new POI[ccc1];
        for (int i = 0; i < ccc1; i++) {
            POIList[i] = new POI();
        }

        FileReader file1 = null;
        BufferedReader br3 = null;
        try {
            file1 = new FileReader("/mnt/public/share/DATA/COL//COL_POIPoint_SG.txt");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br4 = new BufferedReader(file1);//读取文件
        try {
            String line1;

            while ((line1 = br4.readLine()) != null) {//按行读取
                String[] sp = null;
                sp = line1.split(" ");//按空格进行分割
                //SG.get(Integer.parseInt(sp[0])).add(Integer.parseInt(sp[1]));
                POIList[Integer.parseInt(sp[1])].Boundary = Integer.parseInt(sp[3]);
                POIList[Integer.parseInt(sp[1])].POI_Type = Integer.parseInt(sp[2]);
                POIList[Integer.parseInt(sp[1])].POI_Num = Integer.parseInt(sp[4]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < SG.size(); i++) {
            for (int j = 0; j < SG.get(i).size(); j++) {
                POIList[SG.get(i).get(j)].SG = i;
            }
        }

        return POIList;
    }
}
