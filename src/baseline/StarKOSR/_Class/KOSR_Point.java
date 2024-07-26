package StarKOSR._Class;

import java.util.ArrayList;

public class KOSR_Point {
    public int kewWordName;
    public ArrayList<Integer> poi;
    public ArrayList<Integer> poiWeight;

    public KOSR_Point() {
        this.kewWordName = 0;
        this.poi = new ArrayList<>();
        this.poiWeight = new ArrayList<>();
    }

    public KOSR_Point(int n) {
        this.kewWordName = n;
        this.poi = new ArrayList<>();
        this.poiWeight = new ArrayList<>();
    }
}
