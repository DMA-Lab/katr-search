package loader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CreateMinBpTable {

    public static ArrayList<ArrayList<Integer>> load(String path) throws IOException {
        ArrayList<ArrayList<Integer>> data = new ArrayList<>();
        FileReader reader = new FileReader(path);
        BufferedReader bufferedReader = new BufferedReader(reader);//读取文件

        String line;
        while ((line = bufferedReader.readLine()) != null) { // 按行读取
            String[] sp = line.split(" "); // 按空格分割
            ArrayList<Integer> row = new ArrayList<>(); // 创建新行
            for (int i = 0; i < 2; i++) { // 读取两个数据点
                row.add(Integer.valueOf(sp[i])); // 转换为Integer并添加到行
            }
            data.add(row); // 添加行到主数据集
        }
        return data;
    }

    public static ArrayList<ArrayList<Integer>> CreatMinBP_NY() throws IOException {
        return CreateMinBpTable.load("/mnt/public/share/DATA/NY/Point_MinBP.txt");
    }

    public static ArrayList<ArrayList<Integer>> CreatMinBP_COL() throws IOException {
        return CreateMinBpTable.load("/mnt/public/share/DATA/COL/calcedge_Point_MinBP.txt");
    }

    public static ArrayList<ArrayList<Integer>> CreateMinBP_CA() throws IOException {
        return CreateMinBpTable.load("/mnt/public/share/DATA/calcedge/Point_MinBP.txt");
    }
}
