package CreatGraph;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Creat_MinBP {
    public static ArrayList<ArrayList<Integer>> CreatMinBP(){
        ArrayList<ArrayList<Integer>> data = new ArrayList<>();
        FileReader file1 = null;
        try {
            file1 = new FileReader("/mnt/public/share/DATA/calcedge//Point_MinBP.txt");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br1 = new BufferedReader(file1);//读取文件
        try {
            String line1;
            while ((line1 = br1.readLine()) != null) {//按行读取
                String[] sp = null;
                sp = line1.split(" ");//按空格进行分割
                data.add(new ArrayList<Integer>());
                for (int i = 0; i < 2; i++) {
                    data.get(data.size()-1).add(Integer.valueOf(sp[i]));
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static ArrayList<ArrayList<Integer>> CreatMinBP_NY(){
        ArrayList<ArrayList<Integer>> data = new ArrayList<>();
        FileReader file1 = null;
        try {
            file1 = new FileReader("/mnt/public/share/DATA/NY//Point_MinBP.txt");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br1 = new BufferedReader(file1);//读取文件
        try {
            String line1;
            while ((line1 = br1.readLine()) != null) {//按行读取
                String[] sp = null;
                sp = line1.split(" ");//按空格进行分割
                data.add(new ArrayList<Integer>());
                for (int i = 0; i < 2; i++) {
                    data.get(data.size()-1).add(Integer.valueOf(sp[i]));
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static ArrayList<ArrayList<Integer>> CreatMinBP_COL(){
        ArrayList<ArrayList<Integer>> data = new ArrayList<>();
        FileReader file1 = null;
        try {
            file1 = new FileReader("/mnt/public/share/DATA/COL//Point_MinBP.txt");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br1 = new BufferedReader(file1);//读取文件
        try {
            String line1;
            while ((line1 = br1.readLine()) != null) {//按行读取
                String[] sp = null;
                sp = line1.split(" ");//按空格进行分割
                data.add(new ArrayList<Integer>());
                for (int i = 0; i < 2; i++) {
                    data.get(data.size()-1).add(Integer.valueOf(sp[i]));
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
