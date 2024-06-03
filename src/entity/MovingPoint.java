package entity;

public class MovingPoint {
    public int id;
    // 兴趣点类型
    public int poiType;
    // 是否为边界点
    public int isBorder;
    // 兴趣值
    public int hotValue;

    public MovingPoint(int number) {
        this.id = number;
        this.poiType = 0;
        this.isBorder = 0;
        this.hotValue = 0;
    }
}
