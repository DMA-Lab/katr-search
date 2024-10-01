//
// Created by sunnysab on 24-9-29.
//
// DOI: 10.1109/ICDE.2018.00058

#include <ranges>
#include <vector>
#include <algorithm>
#include <queue>
#include <iostream>

#include "graph.h"
#include "poi.h"
#include "file.h"
#include "dijkstra.h"


struct Path {
    std::vector<PoiType> included_poi_types;
    std::vector<Vertex> vertices;
    // 全长的权重
    unsigned int distance;
    // 路径上两点之间的距离
    std::vector<EdgeWeight> weights;

    explicit Path(Vertex v, PoiType type = INVALID_POI_TYPE) : vertices({v}) {
        if (type != INVALID_POI_TYPE) {
            included_poi_types.push_back(type);
        }
    }

    void pop() {
        distance -= weights.back();
        included_poi_types.pop_back();
        vertices.pop_back();
        weights.pop_back();
    }

    [[nodiscard]] Vertex front() const {
        return vertices.front();
    }

    [[nodiscard]] Vertex back() const {
        return vertices.back();
    }

    [[nodiscard]] size_t size() const {
        return vertices.size();
    }

    /// 路径主导 （定义6）
    ///
    /// Consider a given category sequence C = 〈C1, \cdots, Cj〉 and two partially explored candidate routes (witnesses)
    /// P1 = 〈s, v11, \cdots, vq1〉 and P2 = 〈s, v12, \cdots , vq2〉 (1 ≤ q ≤ j). If vq1 = vq2 and w(P1) ≤ w(P2) holds,
    /// P1 dominates P2 w.r.t C, denoted as P1 ≺C P2.
    /// p4 左下角.
    [[nodiscard]] bool dominate(const Path &other) const {
        return this->front() == other.front() && this->back() == other.back()
            && this->distance < other.distance;
    }

    /// 在路径上扩展一个顶点
    [[nodiscard]] Path extend(const Vertex v, const EdgeWeight weight, const optional<Poi> &poi_of_v) const {
        // 阻止路径成环
        if (ranges::any_of(this->vertices, [v](const Vertex u) { return u == v; })) {
            return *this;
        }

        Path new_path = *this;
        new_path.vertices.push_back(v);
        new_path.distance += weight;
        new_path.weights.push_back(weight);

        {
            const auto poi_type = poi_of_v.has_value() ? poi_of_v->type : INVALID_POI_TYPE;
            new_path.included_poi_types.push_back(poi_type);
        }
        return new_path;
    }

    /// 比较两个路径，用于优先队列. 比较依据是两个路径的长度
    bool operator < (const Path &other) const {
        return this->distance < other.distance;
    }

    /// 判断两个路径是否不等，用于 ::remove_from_pq
    bool operator!=(const Path &other) const {
        return this->vertices != other.vertices;
    }

    /// 判断相等
    bool operator==(const Path &other) const {
        return this->vertices == other.vertices;
    }

    /// 截取路径的前 n 个顶点
    [[nodiscard]] Path take(const size_t n) const {
        auto result = *this;
        result.vertices = std::vector(vertices.begin(), vertices.begin() + static_cast<long>(n));
        result.included_poi_types = {};
        return result;
    }

    /// 判断当前路径是否覆盖了所有想要的 POI 类型
    [[nodiscard]] bool cover(const std::vector<PoiType> &poi_types_wanted) const {
        // bug: includes 对元素顺序有要求，应该使用 all_of
        // return ranges::includes(label.lambdas, poi_types_wanted);
        return std::ranges::all_of(poi_types_wanted, [&](const PoiType type) {
            return contains_poi_type(type);
        });
    }

    [[nodiscard]] bool contains_poi_type(const PoiType type) const {
        return ranges::find(included_poi_types, type) != included_poi_types.end();
    }

    [[nodiscard]] bool contains(const Vertex v) const {
        return vertices.end() != ranges::find(vertices, v);
    }

    [[nodiscard]] std::string to_string() const {
        std::string s = "Path [ ";
        for (auto v : vertices) {
            s += std::to_string(v) + " ";
        }
        return s + "]";
    }
};


class kOSR
{
    PoiSet &pois;
    Graph &graph;

public:
    kOSR(Graph &graph, PoiSet &pois) : pois(pois), graph(graph) {}

    /// 查找 v_i 在 C_{i+1} 中的第 x 近顶点. 这里使用了论文 p6 中提到的一个朴素的方法，即，每次使用一个 Dijkstra 算法求解.
    [[nodiscard]] Vertex find_nn(const Vertex v, const PoiType poi_type, const unsigned int k) const {
        Dijkstra dijkstra(graph, v);
        std::vector<Vertex> candidates = this->pois.vertex_of_type(poi_type);

        ranges::sort(candidates, [&](const Vertex _u, const Vertex _v) {
            return dijkstra.get(_u) < dijkstra.get(_v);
        });
        return candidates[k];
    }

    /// PruningKOSR algorithm, as the Algorithm 2 in the paper.
    [[nodiscard]] std::vector<Path> run(const Vertex source, const Vertex target, const std::vector<PoiType> &sequence, const unsigned int k) const {
        std::vector<Path> result;

        // HT≺C: a hash table storing the best path for each vertex v.
        // where key is the size of the partially explored dominating route that has been extended at v, and the value is the route itself.
        std::unordered_map<Vertex, std::unordered_map<size_t, Path>> ht1;
        // Another one is HT>C for dominated routes, where key represents the size of dominated route, and value is a
        // priority queue for the routes with the size of key that have reached v and been dominated,
        // the dominated routes are ordered according to their costs in ascending order.
        std::unordered_map<Vertex, std::unordered_map<size_t, priority_queue<Path>>> ht2;

        std::priority_queue<std::pair<Path, unsigned int>> Q;
        Q.emplace(Path(source), 1);
        while (!Q.empty() && result.size() < k) {
            auto [p, x] = Q.top();
            Q.pop();

            // q 是最后一个顶点的次序
            auto q = p.size() - 1;
            auto v_q = p.back();
            if (q == sequence.size() + 1) { /* line 6 */
                result.push_back(p); /* line7 */
                // comment: reconsider dominated routes
                for (auto i = 1; i < q; ++i) {
                    if (auto it = ht1[v_q].find(i + 1); it != ht1[v_q].end() && p.take(i) == it->second) {
                        auto p_prime = ht2[v_q][i].top();
                        Q.emplace(p_prime, 0);
                        ht1[v_q].erase(ht1[v_q].find(i + 1));
                    }
                }
            } else {
                // pruning dominated routes
                /* line14 */
                if (auto length_of_p = p.size(); !ht1[v_q].contains(length_of_p)) {
                    ht1[v_q].insert({p.size(), p});
                    auto next_v_q = find_nn(v_q, sequence[q + 1], 1);
                    auto new_path = p.extend(next_v_q, graph.get_weight(v_q, next_v_q), pois.get(next_v_q));
                    Q.emplace(new_path, 1);
                } else {
                    if (!ht2[v_q].contains(p.size())) {
                        ht2[v_q].insert({p.size(), priority_queue<Path>()});
                    }
                    ht2[v_q][p.size()].emplace(p);
                }

                if (q > 0) {
                    // line 21
                    auto previous_v_q = p.vertices[q - 1];
                    auto v_q_prime = find_nn(previous_v_q, sequence[q], x + 1);
                    // line 22
                    p.pop();
                    auto new_path = p.extend(v_q_prime, graph.get_weight(v_q, v_q_prime), pois.get(v_q_prime));
                    Q.emplace(new_path, x + 1);
                }
            }
        }
        return result;
    }
};


int main() {
    auto g = load_graph("USA-road-t.NY-stripped-1000.gr");
    auto pois = load_poi("USA-road-t.NY-stripped-1000.poi");

    kOSR kosr(g, pois);
    vector<PoiType> poi_wants = {1, 2, 5};
    if (auto path = kosr.run(810, 1020, poi_wants, 1); !path.empty()) {
        std::cout << path.size() << " path(s) found." << std::endl;
    } else {
        std::cout << "No path found." << std::endl;
    }
    return 0;
}