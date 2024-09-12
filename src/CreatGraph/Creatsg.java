package CreatGraph;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

    public class Creatsg {

    public ArrayList<ArrayList<Integer>> CreatSG_NY(int num2){
        ArrayList<ArrayList<Integer>> SG = new ArrayList<>();
        for (int i = 0; i < num2; i++) {
            SG.add(new ArrayList<Integer>());
        }
        FileReader file1 = null;
        try {
            file1 = new FileReader("/mnt/lab/everyone/share/DATA/NY/NY_POIPoint_SG.txt");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br3 = new BufferedReader(file1);//读取文件
        try {
            String line1;
            while ((line1 = br3.readLine()) != null) {//按行读取
                String[] sp = null;
                sp = line1.split(" ");//按空格进行分割
                //SG.get(Integer.parseInt(sp[0])).add(Integer.parseInt(sp[1]));
                if (Integer.parseInt(sp[0]) < num2)
                SG.get(Integer.parseInt(sp[0])).add(Integer.parseInt(sp[1]));//添加顶点编号
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return SG;
    }

        public ArrayList<ArrayList<Integer>> CreatSG_CA(int num2){
            ArrayList<ArrayList<Integer>> SG = new ArrayList<>();
            for (int i = 0; i < num2; i++) {
                SG.add(new ArrayList<Integer>());
            }
            FileReader file1 = null;
            try {
                file1 = new FileReader("src//DATA//calcedge//SG//calcedge_POIPoint_SG_POI1000.txt");

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            BufferedReader br3 = new BufferedReader(file1);//读取文件
            try {
                String line1;
                int num = 0;
                while ((line1 = br3.readLine()) != null) {//按行读取
                    String[] sp = null;
                    sp = line1.split(" ");//按空格进行分割
                    //SG.get(Integer.parseInt(sp[0])).add(Integer.parseInt(sp[1]));
                    if (Integer.parseInt(sp[0]) < num2){
                        SG.get(Integer.parseInt(sp[0])).add(num);//添加顶点编号
                        num++;
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            return SG;
        }

        public ArrayList<ArrayList<Integer>> CreatSG_COL(int num2){
            ArrayList<ArrayList<Integer>> SG = new ArrayList<>();
            for (int i = 0; i < num2; i++) {
                SG.add(new ArrayList<Integer>());
            }
            FileReader file1 = null;
            try {
                file1 = new FileReader("src//DATA//COL//COL_4000.txt");

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            BufferedReader br3 = new BufferedReader(file1);//读取文件
            try {
                String line1;
                int num = 0;
                while ((line1 = br3.readLine()) != null) {//按行读取
                    String[] sp = null;
                    sp = line1.split(" ");//按空格进行分割
                    //SG.get(Integer.parseInt(sp[0])).add(Integer.parseInt(sp[1]));
                    if (Integer.parseInt(sp[0]) < num2){
                        SG.get(Integer.parseInt(sp[0])).add(num);//添加顶点编号
                        num++;
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            return SG;
        }

        public ArrayList<ArrayList<Integer>> CreatSG_NY2(int num2){
            ArrayList<ArrayList<Integer>> SG = new ArrayList<>();
            for (int i = 0; i < num2; i++) {
                SG.add(new ArrayList<Integer>());
            }
            FileReader file1 = null;
            try {
                file1 = new FileReader("src//DATA//DKSP//NY_1300.txt");

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            BufferedReader br3 = new BufferedReader(file1);//读取文件
            try {
                String line1;
                int num = 0;
                while ((line1 = br3.readLine()) != null) {//按行读取
                    String[] sp = null;
                    sp = line1.split(" ");//按空格进行分割
                    //SG.get(Integer.parseInt(sp[0])).add(Integer.parseInt(sp[1]));
                    if (Integer.parseInt(sp[0]) < num2){
                        SG.get(Integer.parseInt(sp[0])).add(num);//添加顶点编号
                        num++;
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            return SG;
        }


}
