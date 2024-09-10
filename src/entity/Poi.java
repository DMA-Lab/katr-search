package entity;

// Point of Interest, 兴趣点
public class Poi {
    public int boundary;
    public int poiType;
    public int poiNum;
    public int subgraphId;
    public int x; //记录点在x轴的第几个图
    public int y; //记录点在y轴的第几个图

    public Poi(int poiType, int poiNum) {
        this.poiNum = poiNum;
        this.poiType = poiType;
    }

    public Poi(int Boundary, int poiType, int poiNum) {
        this.boundary = Boundary;
        this.poiNum = poiNum;
        this.poiType = poiType;
    }

    public Poi() {
        this.poiType = 0;
        this.poiNum = 0;
        this.subgraphId = 0;
        this.x = 0;
        this.y = 0;
    }
}
