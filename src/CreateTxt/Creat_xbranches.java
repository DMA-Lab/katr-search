package CreateTxt;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class Creat_xbranches {
    public static void main(String[] args) throws InterruptedException, IOException {
        FileReader file1 = null;
        ArrayList<ArrayList<String>> cc = new ArrayList<>();
        try {
            file1 = new FileReader("D://IDEA//gitee//zqyuGroup//ODIN - 副本//ODIN - 副本 (2)//data//NY//METIS-NY-4branches-300.txt");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br1 = new BufferedReader(file1);//读取文件
        try {
            String line1;
            int count = 0;
            while ((line1 = br1.readLine()) != null) {//按行读取
                String[] sp = null;
                sp = line1.split(",");//按逗号进行分割
                cc.add(new ArrayList<String>());
                for (int i = 0; i < 7; i++) {
                    //c[count][i] = sp[i];
                    cc.get(cc.size() - 1).add(String.valueOf(sp[i]));
                }
                //System.out.println("已经写入了第"+count+"条数据");
                count++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        Random random = new Random();

        int num1 = 4; //分成的树有多少层
        int[][] data1 = new int[cc.size() + 1][num1];
        for (int i = 0; i < cc.size(); i++) {
            for (int j = 0; j < num1; j++) {
                if (j == 0) {
                    data1[i][j] = Integer.parseInt(String.valueOf(0));
                } else {
                    int randomNum = random.nextInt(4);
                    data1[i][j] = Integer.parseInt(String.valueOf(randomNum));
                }
            }
        }
        //写入txr文档
        //1.创建字符输出流
        FileWriter writeFile = null;
        try {
            //2.数据想写入的路径及文件
            File file = new File("D://IDEA//gitee//zqyuGroup//ODIN - 副本//ODIN - 副本 (2)//data//NY//METIS-NY-8branches-300.txt");
            //3.如果该文件不存在，就创建
            if (!file.exists()) {
                file.createNewFile();
            }
            //4.给字节输出流赋予实例
            writeFile = new FileWriter(file);
            //5.通过循环将数组写入txt文件中
            int num;
            for (int i = 0; i < data1.length; i++) {
                //num = data1.get(i);
                for (int j = 0; j < num1; j++) {
                    writeFile.write(String.valueOf(data1[i][j]));
                    if (j != (num1 - 1)) {
                        writeFile.write(",");
                    }

                }
                //8.加上换行符
                writeFile.write("\n");
            }
            //9.把writeFile里的数据全部刷新一次，全部写入文件中
            writeFile.flush();
        } catch (Exception e) {//10.异常捕获
            e.printStackTrace();
        } finally {
            try {
                //11.如果writeFile不为空，就将其关闭
                if (writeFile != null)
                    writeFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
