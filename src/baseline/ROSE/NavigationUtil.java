package baseline.ROSE;

import entity.EdgeNode;
import entity.Graph;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;

public class NavigationUtil {
    //代表某节点是否在stack中,避免产生回路
    public static final Map<Integer, Boolean> states = new HashMap<>();
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
        int next_node;
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

}
