/// FLOYD-WARSHALL 算法实现

#pragma once

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
    EdgeWeight w;
    for (Vertex i: graph.vertices) {
        for (Vertex j: graph.vertices) {
            if (i != j && (w = graph.get_weight(i, j)) != InfEdge) {
                distances.set(i, j, w);
            } 
        }
    }

    /* 计算所有点对的最短距离矩阵 */
    for (Vertex k: graph.vertices) {
        for (Vertex i: graph.vertices) {
            for (Vertex j: graph.vertices) {
                if (distances(i, k) + distances(k, j) < distances(i, j)) {
                    const auto new_distance = distances(i, k) + distances(k, j);
                    distances.set(i, j, new_distance);
                }
            }
        }
    }
    return distances;
}

DistanceMatrix floyd_warshall_on_vertex(const Graph &graph, const std::unordered_map<Vertex, Interest>& interests) {
    size_t n = graph.vertex_count;
    // 两点间路径上兴趣值之和的最小值
    DistanceMatrix distances(n, true);

    /* 初始化距离矩阵
       由于我们需要将权重放在顶点上，因此需要设置顶点自身到自身的距离为其兴趣值.
       此处，我们定义路径的距离为路径上所有顶点的兴趣值之和.
    */
    for (auto [v, interest]: interests) {
        distances.set(v, v, interest);
    }
    auto interest_or_zero = [interests](const Vertex v) {
        if (interests.find(v) != interests.end()) {
            return interests.at(v);
        }
        return static_cast<Interest>(0);
    };

    /* 计算所有点对的最短距离矩阵. */
    for (Vertex k: graph.vertices) {
        for (Vertex i: graph.vertices) {
            if (k == i) { /* 只有一个顶点的路径没有任何意义 */
                continue;
            }
            for (Vertex j: graph.vertices) {
                if (i == j || k == j) {
                    continue;
                }

                if (EdgeWeight front = distances.get_or_inf(i, k), back = distances.get_or_inf(k, j);
                    front != InfEdge && back != InfEdge) {
                    const auto new_distance = front + back - interest_or_zero(k);
                    if (new_distance < distances(i, j)) {
                        distances.set(i, j, new_distance);
                    }
                }
            }
        }
    }

    return distances;
}