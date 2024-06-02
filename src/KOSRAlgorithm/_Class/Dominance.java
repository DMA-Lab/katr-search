package KOSRAlgorithm._Class;

import java.util.ArrayList;

public class Dominance {
    public int pointName;
    public ArrayList<Integer> keyWordNum;
    public ArrayList<Integer> minPathWegh;

    public Dominance() {
        this.pointName = 0;
        this.keyWordNum = new ArrayList<>();
        this.minPathWegh = new ArrayList<>();
    }

    public Dominance(int num) {
        this.pointName = num;
        this.keyWordNum = new ArrayList<>();
        this.minPathWegh = new ArrayList<>();
    }
}
