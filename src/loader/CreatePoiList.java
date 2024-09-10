package loader;

import entity.Poi;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class CreatePoiList {
    private Poi[] loadGraph(String poiPath, int count) throws IOException {
        Poi[] poiList = new Poi[count];
        for (int i = 0; i < count; i++) {
            poiList[i] = new Poi();
        }

        FileReader file = new FileReader(poiPath);
        BufferedReader bufferedReader = new BufferedReader(file);//读取文件
        String line;

        Integer[] sp;
        while ((line = bufferedReader.readLine()) != null) {
            sp = Arrays.stream(line.split(" ")).map(Integer::parseInt).toArray(Integer[]::new);

            int poiIndex = sp[1];
            poiList[poiIndex].boundary = sp[3];
            poiList[poiIndex].poiType = sp[2];
            poiList[poiIndex].poiNum = sp[4];
        }
        return poiList;
    }

    private static class VertexPos {
        public int id;
        public int x;
        public int y;

        public VertexPos(int id, int x, int y) {
            this.id = id;
            this.x = x;
            this.y = y;
        }
    }

    private VertexPos[] loadVertexPos(String vertexPosPath, int count) throws IOException {
        VertexPos[] vertexPosList = new VertexPos[count];

        FileReader file = new FileReader(vertexPosPath);
        BufferedReader bufferedReader = new BufferedReader(file);
        String line;

        int lastVertex = 0;
        Integer[] sp;
        while ((line = bufferedReader.readLine()) != null) {
            assert line.startsWith("v ");
            sp = Arrays.stream(line.split(" ")).skip(1).map(Integer::parseInt).toArray(Integer[]::new);
            vertexPosList[lastVertex++] = new VertexPos(sp[0], sp[1], sp[2]);
        }
        return vertexPosList;
    }

    public Poi[] loadPoiNY(int count, ArrayList<ArrayList<Integer>> SG) throws IOException {
        Poi[] poiList = loadGraph("/mnt/lab/everyone/share/DATA/NY/NY_POIPoint_SG.txt", count);

        for (int i = 0; i < SG.size(); i++) {
            for (int j = 0; j < SG.get(i).size(); j++) {
                poiList[SG.get(i).get(j)].subgraphId = i;
            }
        }

        VertexPos[] vertexPosList = loadVertexPos("/mnt/lab/everyone/share/DATA/NY/USA-road-node.NY.txt", count);
        for (int i = 0; i < count; i++) {
            poiList[i].x = vertexPosList[i].x;
            poiList[i].y = vertexPosList[i].y;
        }
        return poiList;
    }

    public Poi[] loadPoiCA(int count, ArrayList<ArrayList<Integer>> SG) throws IOException {
        Poi[] poiList = loadGraph("/mnt/lab/everyone/share/DATA/calcedge/calcedge_PoiPoint_SG_15_100.txt", count);
        for (int i = 0; i < SG.size(); i++) {
            for (int j = 0; j < SG.get(i).size(); j++) {
                poiList[SG.get(i).get(j)].subgraphId = i;
            }
        }

        VertexPos[] vertexPosList = loadVertexPos("/mnt/lab/everyone/share/DATA/calcedge/calcnode.txt", count);
        for (int i = 0; i < count; i++) {
            poiList[i].x = vertexPosList[i].x;
            poiList[i].y = vertexPosList[i].y;
        }
        return poiList;
    }

    public Poi[] loadPoiCOL(int count) throws IOException {
        return loadGraph("/mnt/lab/everyone/share/DATA/COL/COL_PoiPoint_SG.txt", count);
    }
}
