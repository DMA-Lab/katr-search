//
// Created by sunnysab on 24-9-13.
//


#include <queue>
#include <algorithm>
#include "graph.h"
#include "floyd.h"
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

    /// 扩展标签
    [[nodiscard]] Label extend(const Vertex &v, const BudgetScore &budget, const ObjectiveScore &objective) const {
        // 此处是一个 copy 行为，因为标签是不可变的
        // 但是，为什么 *this 默认是 impl Copy 的？
        Label new_label = *this;
        new_label.budget += budget;
        new_label.objective += objective;
        return new_label;
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
    [[nodiscard]] Path extend(const Vertex v, const BudgetScore budget, const ObjectiveScore objective) const {
        Path new_path = *this;
        new_path.vertices.push_back(v);
        new_path.label = new_path.label.extend(v, budget, objective);
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
};


class OSScaling {
    /// 完整的图
    const Graph &graph;
    /// POI 集合
    const PoiSet &pois;

    /// 每个顶点及其标签
    /// 
    /// At each node, we maintain a list of labels, each of which stores the information of a corresponding partial route from the source node to this node, 
    /// including the query keywords already covered, the scaled objective  score, the original objective score, and the budget score of the partial route.
    std::unordered_map<Vertex, std::vector<Label>> labels;

public:
    OSScaling(const Graph &graph, const PoiSet &pois) : graph(graph), pois(pois) {}

    std::optional<Path> run(Vertex source, Vertex target, EdgeWeight budget_limit, std::vector<PoiType> &poi_types_wanted);
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


std::optional<Path> OSScaling::run(const Vertex source, const Vertex target, EdgeWeight budget_limit, std::vector<PoiType> &poi_types_wanted) {
    /* 初始化最小代价矩阵和最小收益矩阵，计算出任意两点 v_i, v_j 之间的小代价 BS(σ_{i,j}) 及最小收益 OS(\tao_{i, j}) */
    auto interests = pois.get_interests();

    auto bs = floyd_warshall(graph);
    auto os = floyd_warshall_on_vertex(graph, interests);

    /* 初始化 */
    // 变量 U，记录收益的上界（第四页右下角）
    ObjectiveScore upper_bound = ObjectiveScoreMax;
    // 当前最佳路径的最后一个标签（label）
    // 这里没有按照论文里存储标签，而是存放了路径
    Path last_path;
    // 最小优先队列，类似 Dijkstra. Path 的比较在论文中有定义.
    std::priority_queue<Path> queue;

    // line4, 将源点 s 加入到优先队列
    queue.emplace(source);

    while (!queue.empty()) {
        // line6, 从优先队列中取出路径
        auto path = queue.top();
        queue.pop();

        // 当前顶点
        auto v_i = path.vertices.back();
        // If the objective score of L_i^k plus the best objective score OS(τ_{i,t}) from v_i
        // to the target node v_t is larger than the current upper bound U , 
        // then the label definitely cannot contribute to the final result (line 7).
        // 问题是，怎样才能得到 OS(τ_{i,t})？
        if (path.label.objective + os(v_i, target) > upper_bound) {
            continue;
        }

        for (const auto [v_j, weight] : graph.get_adjacent_vertices(v_i)) {
            auto path_j = path.extend(v_j, weight, pois[v_j].interest);
            auto &label_j = path_j.label;
            
            // line10, 判断 label_j 是否被 v_j 上的其他标签支配
            bool dominated = ranges::all_of(labels.at(v_j), [&](const Label &label) {
                return label.dominates(label_j);
            });
            if (!dominated 
                && label_j.budget + bs(v_j,target) < budget_limit 
                && label_j.objective + os(v_j,target) < upper_bound) {
                if (!path.was_covered(poi_types_wanted)) {
                    // line12, push L_j^l
                    queue.push(path_j);
                    // line13-15, 从 Q 中删除所有 v_j 上的、被L_j^l支配的标签
                    for (auto label : labels.at(v_j)) {
                        queue = remove_from_pq(queue, path_j);
                    }
                }
                else {
                    if (label_j.budget + bs(v_j, target) < budget_limit) {
                        upper_bound = label_j.objective + os(v_j, target);
                        last_path = path_j;
                    } else {
                        queue.push(path_j);
                    }
                }
            }

            labels[v_j].push_back(label_j);
            queue.push(path.extend(v_j, weight, pois[v_j].interest));
        }
    }

    if (upper_bound == INT_MAX) {
        return std::nullopt;
    }
    return last_path;
}


int main() {
    auto g = load_graph("/home/sunnysab/YTU-lab/data/NY/USA-road-t.NY.txt");
    const PoiSet pois = load_poi("/home/sunnysab/YTU-lab/data/NY/NY_POIPoint_SG.txt");

    OSScaling osscaling(g, pois);
    vector<PoiType> poi_wants = {1, 2, 3};
    auto path = osscaling.run(0, 1, 100, poi_wants);
    if (path) {
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