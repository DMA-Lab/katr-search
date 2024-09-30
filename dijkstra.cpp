//
// Created by sunnysab on 24-9-30.
//

#include "graph.h"
#include "dijkstra.h"


Dijkstra::ContinuousDijkstra(const Graph& g, Vertex source)
    : graph(const_cast<Graph&>(g)), source(source), distance(source) {
    pq.emplace(0, source);
    distance[source] = 0;
}

/// Return inf if there is no path between source and target
EdgeWeight Dijkstra::get(const Vertex target) {
    while (!pq.empty()) {
        auto [d, u] = pq.top();
        pq.pop();

        if (u == target) return d;
        if (visited.contains(u)) continue;
        visited.insert(u);

        for (const auto& [v, w] : graph.get_adjacent_vertices(u)) {
            if (distance[v] > distance[u] + w) {
                distance[v] = distance[u] + w;
                pq.emplace(distance[v], v);
            }
        }
    }
    return InfWeight;
}
