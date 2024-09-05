package entity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class TextWriter {
    public static void main(String[] args) {
        //获取10万个随机的移动顶点
        int random;
        int max;
        int min;
        int[] suiji = new int[50000];
        int num = 0;
        for (int i = 1; i < 1001; i++) {
            max = 200 * i;
            min = 200 * (i - 1);
            for (int j = 0; j < 50; j++) {
                random = (int) ((max - min + 1) * Math.random() + min);
                suiji[num] = random;
                num++;
            }

        }
        suiji[0] = 10;

        //1.创建字符输出流
        FileWriter writeFile = null;
        try {
            //2.数据想写入的路径及文件
            File file = new File("D://IDEA//USA-road-t.NY.gr//AHP//NY_yidongjiedian.txt");
            //3.如果该文件不存在，就创建
            if (!file.exists()) {
                file.createNewFile();
            }
            //4.给字节输出流赋予实例
            writeFile = new FileWriter(file);
            //5.通过循环将数组写入txt文件中
            for (int j : suiji) {
                writeFile.write(String.valueOf(j));
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
        System.out.println("1");
    }

    public static void writeMatrix(String path, ArrayList<ArrayList<Integer>> data, int num) throws IOException {
        File file = new File(path);
        FileWriter writer = new FileWriter(file);

        for (ArrayList<Integer> line : data) {
            int count = 0;
            for (Integer value : line) {
                if (num != 0 && count > num) {
                    break;
                }
                count++;
                writer.write(String.valueOf(value));
                writer.write(' ');
            }
            writer.write('\n');
        }
    }

    public static void WriteTxt1(ArrayList<ArrayList<Integer>> data, int num) throws IOException {
        TextWriter.writeMatrix("D://IDEA//USA-road-t.NY.gr//AHP//NY_20_bianjiejiedian.txt", data, num);
    }
}
