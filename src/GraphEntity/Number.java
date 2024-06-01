package GraphEntity;

public class Number implements Comparable<Number>{
    public int num;
    public int dist;

    public int getDist() {
        return dist;
    }

    public void setDist(int dist) {
        this.dist = dist;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "Number{" +
                "num=" + num +
                ", dist=" + dist +
                '}';
    }

    public Number() {
    }

    public Number(int num,int dist) {
        this.num = num;
        this.dist = dist;
    }

    @Override
    public int compareTo(Number o) {
        return this.getDist() - o.getDist();  //按照路径长度排序
    }
}
