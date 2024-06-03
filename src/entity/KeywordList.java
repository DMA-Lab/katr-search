package entity;

import java.util.ArrayList;

public class KeywordList {
    public int poiType;
    public ArrayList<Integer> nodes;

    public KeywordList(int nodes) {
        this.poiType = nodes;
        this.nodes = new ArrayList<>();
    }
}
