//
// Created by sunnysab on 24-9-30.
//

#pragma once

#include <queue>

#include "graph.h"
#include "distance.h"

struct ContinuousDijkstra
{
private:
    std::unordered_set<Vertex> visited;
    priority_queue<std::pair<EdgeWeight, Vertex>> pq;

public:
    const Graph &graph;
    const Vertex source;

    DistanceVector<EdgeWeight>  distance;

    ContinuousDijkstra(const Graph &g, Vertex source);

    /// 获取两点之间的权重，如果不存在路径则返回无穷大
    EdgeWeight get(Vertex target);

    /// 获取两点之间的权重，如果不存在路径则返回无穷大
    EdgeWeight operator[](const Vertex target) {
        return get(target);
    }
};

using Dijkstra = ContinuousDijkstra;