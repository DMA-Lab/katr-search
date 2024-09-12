package CreatGraph;

import GraphEntity.list;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Creatlist {
    public ArrayList<ArrayList<list>> CreatList_NY(int num2){
        ArrayList<ArrayList<list>> List = new ArrayList<>();
        for (int i = 0; i < num2; i++) {
            List.add(new ArrayList<list>());
        }

        FileReader file1 = null;
        BufferedReader br3 = null;
        try {
            file1 = new FileReader("/mnt/lab/everyone/share/DATA/NY_List_100.txt");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br4 = new BufferedReader(file1);//读取文件
        try {
            String line1;
            //int num22= 0;
            while ((line1 = br4.readLine()) != null) {//按行读取
                String[] sp;
                sp = line1.split(" ");//按空格进行分割
                List.get(Integer.parseInt(sp[0])).add(new list());
                List.get(Integer.parseInt(sp[0])).get(List.get(Integer.parseInt(sp[0])).size()-1).sPoint = Integer.parseInt(sp[1]);
                List.get(Integer.parseInt(sp[0])).get(List.get(Integer.parseInt(sp[0])).size()-1).ePoint = Integer.parseInt(sp[2]);
                List.get(Integer.parseInt(sp[0])).get(List.get(Integer.parseInt(sp[0])).size()-1).w = Integer.parseInt(sp[4]);
//                for (int i = 5; i < sp.length; i++) {
//                    List.get(Integer.parseInt(sp[0])).get(List.get(Integer.parseInt(sp[0])).size()-1).path.add(Integer.parseInt(sp[i]));
//                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return List;
    }

    public ArrayList<ArrayList<list>> CreatList_CA(int num2){
        ArrayList<ArrayList<list>> List = new ArrayList<>();
        for (int i = 0; i < num2; i++) {
            List.add(new ArrayList<list>());
        }

        FileReader file1 = null;
        BufferedReader br3 = null;
        try {
            file1 = new FileReader("src//DATA//calcedge//calcedge_List_100.txt");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br4 = new BufferedReader(file1);//读取文件
        try {
            String line1;
            //int num22= 0;
            while ((line1 = br4.readLine()) != null) {//按行读取
                String[] sp;
                sp = line1.split(" ");//按空格进行分割
                List.get(Integer.parseInt(sp[1])).add(new list());
                List.get(Integer.parseInt(sp[1])).get(List.get(Integer.parseInt(sp[1])).size()-1).sPoint = Integer.parseInt(sp[1]);
                List.get(Integer.parseInt(sp[1])).get(List.get(Integer.parseInt(sp[1])).size()-1).ePoint = Integer.parseInt(sp[2]);
                List.get(Integer.parseInt(sp[1])).get(List.get(Integer.parseInt(sp[1])).size()-1).w = Integer.parseInt(sp[4]);
//                for (int i = 5; i < sp.length; i++) {
//                    List.get(Integer.parseInt(sp[0])).get(List.get(Integer.parseInt(sp[0])).size()-1).path.add(Integer.parseInt(sp[i]));
//                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return List;
    }

    public ArrayList<ArrayList<list>> CreatList_COL(int num2){
        ArrayList<ArrayList<list>> List = new ArrayList<>();
        for (int i = 0; i < num2; i++) {
            List.add(new ArrayList<list>());
        }

        FileReader file1 = null;
        BufferedReader br3 = null;
        try {
            file1 = new FileReader("src//DATA//COL//COL_List.txt");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br4 = new BufferedReader(file1);//读取文件
        try {
            String line1;
            //int num22= 0;
            while ((line1 = br4.readLine()) != null) {//按行读取
                String[] sp;
                sp = line1.split(" ");//按空格进行分割
                List.get(Integer.parseInt(sp[0])).add(new list());
                List.get(Integer.parseInt(sp[0])).get(List.get(Integer.parseInt(sp[0])).size()-1).sPoint = Integer.parseInt(sp[1]);
                List.get(Integer.parseInt(sp[0])).get(List.get(Integer.parseInt(sp[0])).size()-1).ePoint = Integer.parseInt(sp[2]);
                List.get(Integer.parseInt(sp[0])).get(List.get(Integer.parseInt(sp[0])).size()-1).w = Integer.parseInt(sp[4]);
//                for (int i = 5; i < sp.length; i++) {
//                    List.get(Integer.parseInt(sp[0])).get(List.get(Integer.parseInt(sp[0])).size()-1).path.add(Integer.parseInt(sp[i]));
//                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return List;
    }
}
