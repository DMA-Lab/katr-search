package entity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class TextWriter {
    public static void main(String[] args) throws IOException {
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
            if(!file.exists()) {
                file.createNewFile();
            }
            //4.给字节输出流赋予实例
            writeFile = new FileWriter(file);
            //5.通过循环将数组写入txt文件中
            for(int i = 0; i < suiji.length; i++) {
                writeFile.write(String.valueOf(suiji[i]));
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
                if(writeFile != null)
                    writeFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("1");
    }

    public static void WriteTxt(int[] a) throws IOException {
        //1.创建字符输出流
        FileWriter writeFile = null;
        try {
            //2.数据想写入的路径及文件
            File file = new File("D://IDEA//USA-road-t.NY.gr//AHP//NY_fenlei.txt");
            //3.如果该文件不存在，就创建
            if (!file.exists()) {
                file.createNewFile();
            }
            //4.给字节输出流赋予实例
            writeFile = new FileWriter(file);
            //5.通过循环将数组写入txt文件中
            for (int i = 0; i < a.length; i++) {
                writeFile.write(String.valueOf(a[i]));
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


    public static void writeArray(String path, int[] data) throws IOException {
        File file = new File(path);
        FileWriter writer = new FileWriter(file);

        for (int num: data) {
            writer.write(String.valueOf(num));
            writer.write('\n');
        }
    }

    public static void writeMatrix(String path, ArrayList<ArrayList<Integer>> data, int num) throws IOException {
        File file = new File(path);
        FileWriter writer = new FileWriter(file);

        for (ArrayList<Integer> line: data) {
            int count = 0;
            for (Integer value: line) {
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

    public static void WriteTxt3(ArrayList<ArrayList<MovingPoint>> data, int num) throws IOException {
//        FileWriter writeFile = null;
//        int num2 = 0;
//        for (int i = 0; i < data.size(); i++) {
//            num2 += data.get(i).size();
//        }
//        int num1 = num;
//        //1.创建字符输出流
//        try {
//            //2.数据想写入的路径及文件
//            File file = new File("D://IDEA//POI_shuju//NY//NY_POIPoint_SG2.txt");
//            //3.如果该文件不存在，就创建
//            if(!file.exists()) {
//                file.createNewFile();
//            }
//            //4.给字节输出流赋予实例
//            writeFile = new FileWriter(file);
//            //5.通过循环将数组写入txt文件中
//            for(int i = 0; i < data.size(); i++) {
//                for (int j = 0; j < data.get(i).size(); j++) {
//                    writeFile.write(String.valueOf(i));
//                    writeFile.write(" ");
//                    writeFile.write(String.valueOf(data.get(i).get(j).number));
//                    writeFile.write(" ");
//                    writeFile.write(String.valueOf(data.get(i).get(j).type));
//                    writeFile.write(" ");
//                    writeFile.write(String.valueOf(data.get(i).get(j).type2));
//                    writeFile.write(" ");
//                    writeFile.write(String.valueOf(data.get(i).get(j).POI_num));
//                    writeFile.write("\n");
//                }
//                //8.加上换行符
//
//            }
//            //9.把writeFile里的数据全部刷新一次，全部写入文件中
//            writeFile.flush();
//        } catch (Exception e) {//10.异常捕获
//            e.printStackTrace();
//        } finally {
//            try {
//                //11.如果writeFile不为空，就将其关闭
//                if(writeFile != null)
//                    writeFile.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }

    public static void writeTxt4(ArrayList<ArrayList<Integer>> data) throws IOException {
        TextWriter.writeMatrix("D://IDEA//POI_shuju//COL//COL_List.txt", data, 0);
    }


    public static void writeTxt5(int[] data) throws IOException {
        TextWriter.writeArray("D://IDEA//USA-road-t.NY.gr//DKSP//NY_1300_BountPoint.txt", data);
    }

    public static void WriteTxt6(ArrayList<ArrayList<Integer>> all, int ccc, int num) throws IOException {
        //1.创建字符输出流
        FileWriter writeFile = null;
        try {
            //2.数据想写入的路径及文件
            File file = new File("D://IDEA//POI_shuju//NY//NY_int.txt");
            //3.如果该文件不存在，就创建
            if (!file.exists()) {
                file.createNewFile();
            }
            //4.给字节输出流赋予实例
            writeFile = new FileWriter(file);
            //5.通过循环将数组写入txt文件中
            writeFile.write(String.valueOf(ccc));
            writeFile.write(" ");
            writeFile.write(String.valueOf(num));
            writeFile.write(" ");
            writeFile.write(String.valueOf(001));
            writeFile.write(" ");
            writeFile.write("\n");
            for (int i = 0; i < all.size(); i++) {
                System.out.println("写入的行数为：" + i);
                System.out.println("写入的个数为：" + all.get(i).size());
                for (int j = 0; j < all.get(i).size(); j++) {
                    writeFile.write(String.valueOf(all.get(i).get(j)));
                    writeFile.write(" ");
                }
                writeFile.write("\n");
                System.out.println("写入完毕：" + i);
            }
            System.out.println("应该写入行数为：" + all.size());
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


    public static void WriteTxt7(ArrayList<ArrayList<Integer>> data) throws IOException {
        //1.创建字符输出流
        FileWriter writeFile = null;
        try {
            //2.数据想写入的路径及文件
            File file = new File("D://IDEA//POI_shuju//COL//COL_BPList1_100.txt");
            //3.如果该文件不存在，就创建
            if (!file.exists()) {
                file.createNewFile();
            }
            //4.给字节输出流赋予实例
            writeFile = new FileWriter(file);
            //5.通过循环将数组写入txt文件中
            int num;
            for (int i = 0; i < data.size(); i++) {
                num = data.get(i).size();
                for (int j = 0; j < num; j++) {
                    writeFile.write(String.valueOf(data.get(i).get(j)));
                    writeFile.write(" ");
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


    public static void WriteTxt10(ArrayList<ArrayList<Integer>> num111) throws IOException {
        //1.创建字符输出流
        FileWriter writeFile = null;
        try {
            //2.数据想写入的路径及文件
            File file = new File("D://IDEA//POI_shuju//COL//Point_MinBP.txt");
            //3.如果该文件不存在，就创建
            if (!file.exists()) {
                file.createNewFile();
            }
            //4.给字节输出流赋予实例
            writeFile = new FileWriter(file);
            //5.通过循环将数组写入txt文件中
            int num;
            for (int i = 0; i < num111.size(); i++) {
                for (int j = 0; j < num111.get(i).size(); j++) {
                    writeFile.write(String.valueOf(num111.get(i).get(j)));
                    writeFile.write(" ");
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
