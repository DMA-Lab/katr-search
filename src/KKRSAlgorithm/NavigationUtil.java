package KKRSAlgorithm;

import GraphEntity.EdegeNode;
import GraphEntity.MyGraph;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;

public class NavigationUtil {
    //代表某节点是否在stack中,避免产生回路
    public static Map<Integer, Boolean> states = new HashMap();
    //存放放入stack中的节点
    public static Stack<Integer> stack = new Stack();
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

    public static double getEdgeWight(MyGraph graph, int i, int j) {
        //long startTime1=System.currentTimeMillis(); //开始获取时间
        double w = 0;
        EdegeNode a = null;
        a = (EdegeNode) MyGraph.point[i].firstArc;
        while (a != null) {
            if (a.adjvex == j) {
                return w + a.value;
            }
            a = a.nextEdge;                                                    //实际上像是一种遍历链表的行为
        }
        // long endTime1=System.currentTimeMillis(); //获取结束时间
        //System.out.println("获取两点之间权值的时间为"+(endTime1 - startTime1));
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
    public static boolean isConnected(MyGraph graph, int i, int j) {
        double w = 0;
        EdegeNode a = null;
        a = (EdegeNode) MyGraph.point[i].firstArc;
        while (a != null) {
            if (a.adjvex == j) {
                return true;
            }
            a = a.nextEdge;                                                    //实际上像是一种遍历链表的行为
        }
        return false;
    }

    //打印stack中信息,即路径信息
    public static void printPath() {
        if (flag) {
            for (Integer i : stack) {
                linkedList.add(i);
            }
            flag = false;
        }
        if (linkedList.size() < stack.size()) {
            return;
        }
        linkedList.clear();
        //StringBuilder sb = new StringBuilder();
        for (Integer i : stack) {
            linkedList.add(i);
        }

    }

    //得到x的邻接点为y的后一个邻接点位置,为-1说明没有找到
    public static int getNextNode(MyGraph graph, int x, int y) {
        int next_node = -1;
        EdegeNode edge = (EdegeNode) MyGraph.point[x].firstArc;
        if (null != edge && y == -1) {
            int n = edge.adjvex;
            //元素还不在stack中
            if (!states.get(n))
                return n;
            return -1;
        }

        while (null != edge) {
            //节点未访问
            if (edge.adjvex == y) {
                if (null != edge.nextEdge) {
                    next_node = edge.nextEdge.adjvex;

                    if (!states.get(next_node))
                        return next_node;
                } else
                    return -1;
            }
            edge = edge.nextEdge;
        }
        return -1;
    }


    public static Stack<Integer> visit(MyGraph graph, int x, int y) {
        //初始化所有节点在stack中的情况
        for (int i = 0; i < MyGraph.point.length; i++) {
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
