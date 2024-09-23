/// FLOYD-WARSHALL 算法实现

#include <iostream>
#include <vector>
#include <climits>
#include "poi.h"
#include "graph.h"
#include "distance.h"

DistanceMatrix floyd_warshall(const Graph &graph) {
    size_t n = graph.vertex_count;
    DistanceMatrix distances(n);

    /* 初始化距离矩阵 */
    for (size_t i = 0; i < n; ++i) {
        for (size_t j = 0; j < n; ++j) {
            if (graph.is_connected(i, j)) {
                auto w = graph.get_weight(i, j);
                distances.set(i, j, w);
            } 
        }
    }

    /* 计算所有点对的最短距离矩阵 */
    for (size_t k = 0; k < n; ++k) {
        for (size_t i = 0; i < n; ++i) {
            for (size_t j = 0; j < n; ++j) {
                if (distances(i, k) + distances(k, j) < distances(i, j)) {
                    auto new_distance = distances(i, k) + distances(k, j);
                    distances.set(i, j, new_distance);
                }
            }
        }
    }
    return distances;
}

DistanceMatrix floyd_warshall_on_vertex(const Graph &graph, const std::unordered_map<Vertex, Interest> interests) {
    size_t n = graph.vertex_count;
    // 两点间路径上兴趣值之和的最小值
    DistanceMatrix distances(n, true);

    /* 初始化距离矩阵
       由于我们需要将权重放在顶点上，因此需要设置顶点自身到自身的距离为其兴趣值.
       此处，我们定义路径的距离为路径上所有顶点的兴趣值之和.
    */
    for (Vertex v = 0; v < n; ++v) {
        distances.set(v, v, interests.at(v));
    }

    /* 计算所有点对的最短距离矩阵. */
    for (Vertex k = 0; k < n; ++k) {
        for (Vertex i = 0; i < n; ++i) {
            for (Vertex j = 0; j < n; ++j) {
                auto new_distance = distances(i, k) + distances(k, j) - interests.at(k);
                if (new_distance < distances(i, j)) {
                    distances.set(i, j, new_distance);
                }
            }
        }
    }

    return distances;
}