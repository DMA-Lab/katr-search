package baseline.ROSE;

import entity.EdgeNode;
import entity.Graph;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;

public class NavigationUtil {
    //代表某节点是否在stack中,避免产生回路
    public static Map<Integer, Boolean> states = new HashMap<>();
    //存放放入stack中的节点
    public static Stack<Integer> stack = new Stack<>();
    /**
     * 查找两点之间的最短路径
     *
     * @param graph
     * @param i
     * @param j
     * @return
     */
    static LinkedList<Integer> linkedList = new LinkedList<>();//储存最短路径节点
    static boolean flag = true;//标识是否为第一条路径

    /**
     * 获取两点之间权值
     *
     * @param graph
     * @param i
     * @param j
     * @return
     */
    public static double getWeight(Graph graph, int i, int j) {
        double weight = 0;
        EdgeNode edge = graph.vertices[i].firstArc;
        while (edge != null) {
            if (edge.adjVertex == j) {
                return weight + edge.weight;
            }
            // 实际上像是一种遍历链表的行为
            edge = edge.next;
        }
        return -1;
    }

    /**
     * 判断两点是否连通
     *
     * @param graph
     * @param i
     * @param j
     * @return
     */
    public static boolean isConnected(Graph graph, int i, int j) {
        double w = 0;
        EdgeNode a = graph.vertices[i].firstArc;
        while (a != null) {
            if (a.adjVertex == j) {
                return true;
            }
            a = a.next; // 实际上像是一种遍历链表的行为
        }
        return false;
    }

    //得到x的邻接点为y的后一个邻接点位置,为-1说明没有找到
    public static int getNextNode(Graph graph, int x, int y) {
        int next_node = -1;
        EdgeNode edge = graph.vertices[x].firstArc;
        if (null != edge && y == -1) {
            int n = edge.adjVertex;
            //元素还不在stack中
            if (!states.get(n))
                return n;
            return -1;
        }

        while (null != edge) {
            //节点未访问
            if (edge.adjVertex == y) {
                if (null != edge.next) {
                    next_node = edge.next.adjVertex;

                    if (!states.get(next_node))
                        return next_node;
                } else
                    return -1;
            }
            edge = edge.next;
        }
        return -1;
    }

    public static Stack<Integer> visit(Graph graph, int x, int y) {
        //初始化所有节点在stack中的情况
        // TODO: sunnysab: 这里的代码逻辑有问题，应该是初始化所有节点的状态为 false，而不是初始化下标.
        for (int i = 0; i < graph.vertices.length; i++) {
            states.put(i, false);
        }
        //stack top元素
        int top_node;
        //存放当前top元素已经访问过的邻接点,若不存在则置-1,此时代表访问该top元素的第一个邻接点
        int adjvex_node = -1;
        int next_node;
        stack.add(x);
        states.put(x, true);

        while (!stack.isEmpty()) {
            top_node = stack.peek();
            //找到需要访问的节点
            if (top_node == y) {
                break;
            } else {
                //访问top_node的第advex_node个邻接点
                next_node = getNextNode(graph, top_node, adjvex_node);
                if (next_node != -1) {
                    stack.push(next_node);
                    //置当前节点访问状态为已在stack中
                    states.put(next_node, true);
                    //临接点重置
                    adjvex_node = -1;
                }
                //不存在临接点，将stack top元素退出
                else {
                    //当前已经访问过了top_node的第adjvex_node邻接点
                    adjvex_node = stack.pop();
                    //不在stack中
                    states.put(adjvex_node, false);
                }
            }
        }
        return stack;
    }
}
