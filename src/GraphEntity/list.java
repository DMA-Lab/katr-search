package GraphEntity;

import java.util.ArrayList;

public class list {
    public int sPoint;
    public int ePoint;
    public int w; //路径长度
    public ArrayList<Integer> path; //路径

    //public ArrayList<Integer> listNum; //目标顶点
    public list() {
        this.sPoint = 0;
        this.ePoint = 0;
        this.path = new ArrayList<>();
        this.w = 0;
    }

    public list(int sPoint, int ePoint, ArrayList<Integer> path) {
        this.sPoint = sPoint;
        this.ePoint = ePoint;
        this.path = path;
    }
//    public List(int num){
//        this.num = num;
//        this.destination = new ArrayList<>();
//        this.listNum = new ArrayList<>();
//    }
}
