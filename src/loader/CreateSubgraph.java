package loader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CreateSubgraph {

    private ArrayList<ArrayList<Integer>> load(String path, int limit) throws IOException {
        ArrayList<ArrayList<Integer>> subgraph = new ArrayList<>();
        for (int i = 0; i < limit; i++) {
            subgraph.add(new ArrayList<>());
        }
        FileReader file = new FileReader(path);;
        BufferedReader bufferedReader = new BufferedReader(file);

        String line;
        while ((line = bufferedReader.readLine()) != null) {//按行读取
            String[] sp;
            sp = line.split(" ");//按空格进行分割
            if (Integer.parseInt(sp[0]) < limit)
                subgraph.get(Integer.parseInt(sp[0])).add(Integer.parseInt(sp[1]));//添加顶点编号
        }
        return subgraph;
    }

    public ArrayList<ArrayList<Integer>> load_NY(int limit) throws IOException {
        return load("/mnt/lab/everyone/share/DATA/NY/NY_POIPoint_SG.txt", limit);
    }

    public ArrayList<ArrayList<Integer>> CreatSG_CA(int limit) throws IOException {
        return load("/mnt/lab/everyone/share/DATA/calcedge/SG/calcedge_PoiPoint_SG_Poi1000.txt", limit);
    }

    public ArrayList<ArrayList<Integer>> CreatSG_COL(int limit) throws IOException {
        return load("/mnt/lab/everyone/share/DATA/COL/COL_4000.txt", limit);
    }
}
