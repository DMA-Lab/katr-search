//
// Created by sunnysab on 24-9-13.
//


#include <queue>
#include <algorithm>
#include <cassert>
#include <iostream>
#include <vector>
#include <climits>
#include "distance.h"
#include "graph.h"
#include "poi.h"
#include "file.h"


/// 代价得分
using BudgetScore = EdgeWeight;
/// 收益得分
using ObjectiveScore = Interest;

ObjectiveScore  ObjectiveScoreMax = USHRT_MAX;


/// 定义5："Each partial route is represented by a node label."
/// 由于我们的数据集中，每条边的权重都是整数，所以不需要计算和存储缩放后的路径长度
struct Label {
    /// 路径覆盖的 poi 类型。见定义5
    std::vector<PoiType> lambdas;
    /// 代价
    BudgetScore budget{};
    /// 收益
    ObjectiveScore objective{};

    Label() = default;

    /// 判断当前标签是否支配其他标签
    ///
    /// 定义6：Let L_i^k and L_i^l be two labels corresponding to two different paths from the source node v_s to node v_i. 
    /// We say L_i^k dominates L_i^l iff L_i^k.λ ⊇ L_i^l.λ, L_i^k.OˆS ≤  L_i^l.OˆS, and L_i^k.BS ≤ L_i^l.BS.
    [[nodiscard]] bool dominates(const Label &other) const {
        return budget >= other.budget && objective >= other.objective;
    }

    /// 扩展普通顶点
    ///
    /// 注意，此函数包含副作用，会修改当前标签的 budget 和 objective.
    Label extend(const BudgetScore &budget) {
        // 此处是一个 copy 行为，因为标签是不可变的
        // 但是，为什么 *this 默认是 impl Copy 的？
        this->budget += budget;
        return *this;
    }

    /// 扩展兴趣点
    ///
    /// 注意，此函数包含副作用，会修改当前标签的 budget 和 objective.
    Label extend(const BudgetScore &budget, const ObjectiveScore &objective, const PoiType type) {
        // 此处是一个 copy 行为，因为标签是不可变的
        // 但是，为什么 *this 默认是 impl Copy 的？
        this->budget += budget;
        this->objective += objective;
        if (type != INVALID_POI_TYPE && ranges::find(this->lambdas, type) == this->lambdas.end()) {
            this->lambdas.push_back(type);
        }
        return *this;
    }

    bool operator < (const Label &other) const {
        /* 我们希望标签覆盖的 poi 类型越多越好，收益越小越好，代价越小越好. 详见定义8. 
           不理解为什么希望收益小. */
        return this->lambdas.size() > other.lambdas.size()
        || (this->lambdas.size() == other.lambdas.size() && this->objective < other.objective)
        || (this->lambdas.size() == other.lambdas.size() && this->objective == other.objective && this->budget < other.budget);
    }
};

struct Path {
    std::vector<Vertex> vertices;
    Label label;

    Path() = default;
    explicit Path(Vertex v, PoiType type = INVALID_POI_TYPE) : vertices({v}) {
        if (type != INVALID_POI_TYPE) {
            label.lambdas.push_back(type);
        }
    }

    /// 在路径上扩展一个顶点，并更新 label
    ///
    /// budget 表示从当前路径最后一个顶点到 v 的权重, objective 表示加入点 v 带来的收益（即，v 的兴趣值）.
    [[nodiscard]] Path extend(const Vertex v, const BudgetScore budget, const optional<Poi> &poi_of_v) const {
        if (ranges::any_of(this->vertices, [v](const Vertex u) { return u == v; })) {
            return *this;
        }
        Path new_path = *this;
        new_path.vertices.push_back(v);

        if (poi_of_v.has_value()) {
            new_path.label.extend(budget, poi_of_v->interest, poi_of_v->type);
        } else {
            new_path.label.extend(budget);
        }

        return new_path;
    }

    /// 比较两个路径，用于优先队列. 比较规则同 label 的比较规则，详见定义8.
    bool operator < (const Path &other) const {
        return this->label < other.label;
    }

    /// 判断两个路径是否不等，用于 ::remove_from_pq
    bool operator != (const Path&other) const {
        return this->vertices != other.vertices;
    }

    /// 判断当前路径是否覆盖了所有想要的 POI 类型
    [[nodiscard]] bool was_covered(const std::vector<PoiType> &poi_types_wanted) const {
        return ranges::includes(label.lambdas, poi_types_wanted);
    }

    [[nodiscard]] std::string to_string() const {
        std::string s = "Path [";
        for (auto v : vertices) {
            s += std::to_string(v) + " ";
        }
        return s + "]";
    }
};


/// 计算代价得分矩阵
/// 按照论文要求，使用 Floyd-Warshall 算法计算任意两点之间的最短路径
DistanceMatrix<BudgetScore> calc_budget_score_matrix(const Graph &graph) {
    DistanceMatrix distances(graph.vertex_count, false, InfWeight);

    /* 初始化距离矩阵 */
    for (const Vertex i: graph.vertices) {
        for (auto [j, w]: graph.get_adjacent_vertices(i)) {
            if (w != InfWeight) {
                distances.set(i, j, w);
            }
        }
    }

    /* 计算所有点对的最短距离矩阵 */
    for (Vertex k: graph.vertices) {
        for (Vertex i: graph.vertices) {
            for (Vertex j: graph.vertices) {
                if (auto left = distances(i, k), right = distances(k, j);
                    left != InfWeight && right != InfWeight && left + right < distances(i, j)) {
                    const auto new_distance = left + right;
                    distances.set(i, j, new_distance);
                }
            }
        }
    }
    return distances;
}

/// 计算收益得分矩阵
/// 使用 Floyd-Warshall 算法计算任意两点之间的最小收益得分
DistanceMatrix<Interest> calc_objective_score_matrix(const Graph &graph,
    const std::unordered_map<Vertex, Interest>& interests) {
    // 两点间路径上兴趣值之和的最小值
    DistanceMatrix distances(graph.vertex_count, true, ObjectiveScoreMax);

    /* 初始化距离矩阵
       由于我们需要将权重放在顶点上，因此需要设置顶点自身到自身的距离为其兴趣值.
       此处，我们定义路径的距离为路径上所有顶点的兴趣值之和.
    */
    for (const Vertex v: graph.vertices) {
        const bool is_poi = interests.contains(v);
        const auto interest_at_v = is_poi ? interests.at(v) : 0;
        distances.set(v, v, interest_at_v);
        // 计算相邻顶点的“距离”。如 1-2 顶点，各自兴趣值为 3，则 1-2 之间的距离应该为 6.
        for (const auto [adj, _]: graph.get_adjacent_vertices(v)) {
            const bool is_adj_poi = interests.contains(adj);
            const auto interest_at_adj = is_adj_poi ? interests.at(adj) : 0;
            distances.set(v, adj, interest_at_adj + interest_at_v);
        }
    }

    auto interest_or_zero = [interests](const Vertex v) {
        if (const auto it = interests.find(v); it != interests.end()) {
            return it->second;
        }
        return static_cast<Interest>(0);
    };

    /* 计算所有点对的最短距离矩阵. */
    for (const Vertex k: graph.vertices) {
        for (const Vertex i: graph.vertices) {
            // 只有一个顶点的路径没有任何意义
            if (k == i) continue;
            for (const Vertex j: graph.vertices) {
                if (i == j || k == j) continue;

                if (Interest front = distances(i, k), back = distances(k, j);
                    front != distances.inf && back != distances.inf) {
                    if (const auto new_distance = front + back - interest_or_zero(k); new_distance < distances(i, j)) {
                        distances.set(i, j, new_distance);
                    }
                }
            }
        }
    }

    return distances;
}


class OSScaling {
    /// 完整的图
    const Graph &graph;
    /// POI 集合
    PoiSet pois;

    /// 每个顶点及其标签
    /// 
    /// At each node, we maintain a list of labels, each of which stores the information of a corresponding partial route from the source node to this node, 
    /// including the query keywords already covered, the scaled objective  score, the original objective score, and the budget score of the partial route.
    std::unordered_map<Vertex, std::vector<Label>> labels;

public:
    OSScaling(const Graph &graph, const PoiSet &pois) : graph(graph) {
        // 如今 graph 可能是一张大图的一部分，因此我们需要过滤 poi set, 仅保留图中存在的 POI.
        for (const auto& [v, poi] : pois.pois_map) {
            if (graph.contains(v)) {
                this->pois.add(poi);
            }
        }
    }

    std::optional<Path> run(Vertex source, Vertex target, BudgetScore limit, std::vector<PoiType> &poi_types_wanted);
};


template <typename T>
std::priority_queue<T> remove_from_pq(std::priority_queue<T> pq, T &item) {
    std::priority_queue<T> new_pq;
    while (!pq.empty()) {
        auto t = pq.top();
        pq.pop();
        if (t != item) {
            new_pq.push(t);
        }
    }
    return new_pq;
}

std::optional<Path> OSScaling::run(const Vertex source, const Vertex target, BudgetScore limit, std::vector<PoiType> &poi_types_wanted) {
    assert(graph.contains(source) && graph.contains(target));

    /* 初始化最小代价矩阵和最小收益矩阵，计算出任意两点 v_i, v_j 之间的小代价 BS(σ_{i,j}) 及最小收益 OS(\tao_{i, j}) */
    auto interests = pois.get_interests(poi_types_wanted);

    auto bs = calc_budget_score_matrix(graph);
    auto os = calc_objective_score_matrix(graph, interests);

    /* 初始化 */
    // 变量 U，记录收益的上界（第四页右下角）
    ObjectiveScore upper_bound = ObjectiveScoreMax;
    // 当前最佳路径的最后一个标签（label）
    // 这里没有按照论文里存储标签，而是存放了路径
    Path last_path;
    // 最小优先队列，类似 Dijkstra. Path 的比较在论文中有定义.
    std::priority_queue<Path> queue;

    // line4, 将源点 s 加入到优先队列
    auto source_poi_type = INVALID_POI_TYPE;
    if (const auto it = pois.pois_map.find(source); it != pois.pois_map.end()) {
        source_poi_type = it->second.type;
    }
    queue.emplace(source, source_poi_type);

    while (!queue.empty()) {
        // line6, 从优先队列中取出路径
        auto path = queue.top();
        queue.pop();
        cout << path.to_string() << endl;

        // 当前顶点
        auto v_i = path.vertices.back();
        // If the objective score of L_i^k plus the best objective score OS(τ_{i,t}) from v_i
        // to the target node v_t is larger than the current upper bound U , 
        // then the label definitely cannot contribute to the final result (line 7).
        if (path.label.objective + os(v_i, target) > upper_bound) {
            continue;
        }

        for (const auto [v_j, weight] : graph.get_adjacent_vertices(v_i)) {
            auto path_j = path.extend(v_j, weight, pois.get(v_j));
            auto &label_j = path_j.label;

            // line10, 判断 label_j 是否被 v_j 上的其他标签支配
            if (labels.contains(v_j)) {
                if (ranges::any_of(labels.at(v_j), [&](const Label &label) {
                    return label.dominates(label_j);
                })) {
                    continue;
                }
            } else {
                labels.insert({v_j, {}});
            }

            if (label_j.budget + bs(v_j,target) < limit
                && label_j.objective + os(v_j,target) < upper_bound) {
                if (!path_j.was_covered(poi_types_wanted)) {
                    // line12, push L_j^l
                    queue.push(path_j);
                    // line13-15, 从 Q 中删除所有 v_j 上的、被L_j^l支配的标签
                    for (auto label : labels.at(v_j)) {
                        queue = remove_from_pq(queue, path_j);
                    }
                }
                else {
                    if (label_j.budget + bs(v_j, target) < limit) {
                        upper_bound = label_j.objective + os(v_j, target);
                        last_path = path_j;
                    } else {
                        queue.push(path_j);
                    }
                }
            }

            labels[v_j].push_back(label_j);
            queue.push(path.extend(v_j, weight, pois.get(v_j)));
        }
    }

    if (upper_bound == INT_MAX) {
        return std::nullopt;
    }
    return last_path;
}


int main() {
    auto g = load_graph("/home/sunnysab/graph.txt");
    const PoiSet pois = load_poi("/home/sunnysab/poi.txt");

    OSScaling osscaling(g, pois);
    vector<PoiType> poi_wants = {1, 2, 3};
    if (auto path = osscaling.run(1, 4, 10000, poi_wants); path.has_value()) {
        std::cout << "Path found: ";
        for (auto v : path->vertices) {
            std::cout << v << " ";
        }
        std::cout << std::endl;
    } else {
        std::cout << "No path found." << std::endl;
    }
    return 0;
}