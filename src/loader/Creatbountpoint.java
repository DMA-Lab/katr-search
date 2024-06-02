package loader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Creatbountpoint {
    public int[] CreatBP_NY(int num) {
        int[] BP = new int[num];
        FileReader file1 = null;
        BufferedReader br3 = null;
        int num1 = 0;
        try {
            file1 = new FileReader("/mnt/public/share/DATA/NY_BountPoint.txt");

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
                BP[num1] = Integer.parseInt(sp[0]);
                num1++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return BP;
    }

    public int[] CreatBP_ca(int num) {
        int[] BP = new int[num];
        FileReader file1 = null;
        BufferedReader br3 = null;
        int num1 = 0;
        try {
            file1 = new FileReader("/mnt/public/share/DATA/calcedge//calcedge_BountPoint.txt");

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
                BP[num1] = Integer.parseInt(sp[0]);
                num1++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return BP;
    }

    public int[] CreatBP_BJ(int num) {
        int[] BP = new int[num];
        FileReader file1 = null;
        BufferedReader br3 = null;
        int num1 = 0;
        try {
            file1 = new FileReader("/mnt/public/share/DATA/COL//COL_BountPoint.txt");

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
                BP[num1] = Integer.parseInt(sp[0]);
                num1++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return BP;
    }
}
