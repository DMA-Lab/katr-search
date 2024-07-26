package DataPreprocessing;

import entity.BpPath;
import entity.Graph;
import entity.POI;
import entity.Path;

import java.util.ArrayList;

public class GraphPreprocessing {
    public ArrayList<ArrayList<Integer>> SG;
    public POI[] POIList;
    public ArrayList<ArrayList<Path>> List;
    public ArrayList<ArrayList<BpPath>> BPList;
    public ArrayList<ArrayList<Integer>> PointMinBP;
    public ArrayList<ArrayList<Integer>> BvList;
    public Graph g;

    public GraphPreprocessing() {
        this.BPList = new ArrayList<>();
        this.BvList = new ArrayList<>();
    }
}
