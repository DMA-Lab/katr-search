package GraphEntity;

public class POI {
    public int Boundary;
    public int POI_Type;
    public int POI_Num;
    public int SG;
    public int x; //记录点在x轴的第几个图
    public int y; //记录点在y轴的第几个图

    public POI(int POI_Type,int POI_Num){
        this.POI_Num = POI_Num;
        this.POI_Type = POI_Type;
    }

    public POI(int Boundary, int POI_Type,int POI_Num){
        this.Boundary = Boundary;
        this.POI_Num = POI_Num;
        this.POI_Type = POI_Type;
    }

    public POI(){
        this.POI_Type = 0;
        this.POI_Num = 0;
        this.SG = 0;
        this.x = 0;
        this.y = 0;
    }


}
