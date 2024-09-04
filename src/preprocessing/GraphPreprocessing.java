package preprocessing;

import entity.PoiPath;
import entity.Graph;
import entity.Poi;
import entity.Path;

import java.util.ArrayList;

public class GraphPreprocessing {
    public ArrayList<ArrayList<Integer>> SG;
    public Poi[] PoiList;
    public ArrayList<ArrayList<Path>> List;
    public ArrayList<ArrayList<PoiPath>> BPList;
    public ArrayList<ArrayList<Integer>> PointMinBP;
    public ArrayList<ArrayList<Integer>> BvList;
    public Graph graph;

    public GraphPreprocessing() {
        this.BPList = new ArrayList<>();
        this.BvList = new ArrayList<>();
    }
}
