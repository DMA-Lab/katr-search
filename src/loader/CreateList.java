package loader;

import entity.Path;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CreateList {
    public ArrayList<ArrayList<Path>> CreatList(String path, int num) {
        ArrayList<ArrayList<Path>> list = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            list.add(new ArrayList<>());
        }

        FileReader file1 = null;
        try {
            file1 = new FileReader(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert file1 != null;
        BufferedReader br4 = new BufferedReader(file1); // 读取文件
        try {
            String line1;
            while ((line1 = br4.readLine()) != null) {// 按行读取
                String[] sp;
                sp = line1.split(" ");//按空格进行分割
                list.get(Integer.parseInt(sp[0])).add(new Path());
                list.get(Integer.parseInt(sp[0])).getLast().start = Integer.parseInt(sp[1]);
                list.get(Integer.parseInt(sp[0])).getLast().end = Integer.parseInt(sp[2]);
                list.get(Integer.parseInt(sp[0])).getLast().weight = Integer.parseInt(sp[4]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    public ArrayList<ArrayList<Path>> CreatList_NY(int num) {
        return CreatList("/mnt/lab/everyone/share/DATA/NY_List_100.txt", num);
    }

    public ArrayList<ArrayList<Path>> CreatList_CA(int num) {
        return CreatList("/mnt/lab/everyone/share/DATA/calcedge/calcedge_List_100.txt", num);
    }

    public ArrayList<ArrayList<Path>> CreatList_COL(int num) {
        return CreatList("/mnt/lab/everyone/share/DATA/COL/COL_List.txt", num);
    }
}
