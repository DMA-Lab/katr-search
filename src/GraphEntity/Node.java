package GraphEntity;

import java.util.Stack;

public class Node {
    public Stack<Integer> path;
    public String name;

    public Node() {
        this.path = new Stack<Integer>();
        this.name = null;
    }
    public Node(Stack<Integer> path) {
        this.path = path;
        this.name = null;
    }


    public void add(Integer integer) {
        this.path.add(integer);
    }
    public void addAll(Stack<Integer> path) {
        this.path.addAll(path);
    }
}
