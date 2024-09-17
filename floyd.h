/// FLOYD-WARSHALL 算法实现

#include <iostream>
#include <vector>
#include <climits>
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