package KOSRAlgorithm._Class;

import java.util.ArrayList;

public class subPath {
    public int keyWordNum; //该子路线中已经保存了多少个兴趣点
    public ArrayList<Integer> subPath;
    public int subPathWeight;
    public boolean flag;
    //public ArrayList<subPath> next;

    public subPath(){
        this.keyWordNum = 0;
        this.subPath = new ArrayList<>();
        //this.next = new ArrayList<>();
        this.subPathWeight = 0;
        this.flag = true;
    }

//    public subPath(subPath n){
//        this.keyWordNum = 0;
//        this.subPath = new ArrayList<>();
//        this.next = n;
//        this.subPathWeight = 0;
//    }
}
