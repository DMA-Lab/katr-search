package entity;

// Point of Interest, 兴趣点
public class Poi {
    public int Boundary;
    public int Poi_Type;
    public int Poi_Num;
    public int SG;
    public int x; //记录点在x轴的第几个图
    public int y; //记录点在y轴的第几个图

    public Poi(int Poi_Type, int Poi_Num) {
        this.Poi_Num = Poi_Num;
        this.Poi_Type = Poi_Type;
    }

    public Poi(int Boundary, int Poi_Type, int Poi_Num) {
        this.Boundary = Boundary;
        this.Poi_Num = Poi_Num;
        this.Poi_Type = Poi_Type;
    }

    public Poi() {
        this.Poi_Type = 0;
        this.Poi_Num = 0;
        this.SG = 0;
        this.x = 0;
        this.y = 0;
    }
}
