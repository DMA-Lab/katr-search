package entity;

import java.util.ArrayList;

public class KeywordList {
    public final int poiType;
    public final ArrayList<Integer> nodes;

    public KeywordList(int nodes) {
        this.poiType = nodes;
        this.nodes = new ArrayList<>();
    }
}
