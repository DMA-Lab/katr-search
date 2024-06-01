package GraphEntity;

public class movePoint {
    public int number;
    public int type; //兴趣点类型
    public int type2; //是否为边界点
    public int POI_num; //兴趣值
    public movePoint() {
        this.number = 0;
        this.type = 0;
        this.type2 = 0;
        this.POI_num = 0;
    }
    public movePoint(int number,int type){
        this.number = number;
        this.type = type;
        this.type2 = 0;
        this.POI_num = 0;
    }
    public movePoint(int number){
        this.number = number;
        this.type = 0;
        this.type2 = 0;
        this.POI_num = 0;
    }
    public movePoint(int number,int type,int type2){
        this.number = number;
        this.type = type;
        this.type2 = type2;
        this.POI_num = 0;
    }
    public movePoint(int number,int type,int type2, int POI_num){
        this.number = number;
        this.type = type;
        this.type2 = type2;
        this.POI_num = POI_num;

    }


}
