package GraphEntity;

import java.util.ArrayList;
import java.util.Stack;

public class keyWordList {
    public int name;
    public ArrayList<Integer> Node;

    public keyWordList(){
        this.name = 0;
        this.Node = new ArrayList<>();
    }
    public keyWordList(int Node){
        this.name = Node;
        this.Node = new ArrayList<>();
    }


}
