package loader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class xiugai {
    public static void main(String[] args) throws InterruptedException {
        FileReader file1 = null;
        ArrayList<ArrayList<Integer>> c1 = new ArrayList<>();
        try {
            file1 = new FileReader("/mnt/public/share/DATA/BJ//BJ_List.txt");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br1 = new BufferedReader(file1);//读取文件
        try {
            String line1;
            while ((line1 = br1.readLine()) != null) {//按行读取
                String[] sp = null;
                sp = line1.split(" ");//按空格进行分割
                c1.add(new ArrayList<Integer>());
                for (int i = 1; i < 5; i++) {
                    c1.get(c1.size() - 1).add(Integer.valueOf(sp[i]));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        Random random = new Random();
        for (int i = 0; i < c1.size(); i++) {
            if (c1.get(i).get(3) > 9999999) {
                c1.get(i).remove(3);
                //c1.get(i).add()
            }
        }
    }

}
