/// 本文件实现了一个距离矩阵的存储.
/// 由于距离矩阵是对称的，因此只需要存储一半的距离即可.

#include <vector>
#include <unordered_map>
#include "graph.h"


class DistanceMatrix {
    std::vector<EdgeWeight> distances;
    std::unordered_map<Vertex, size_t> vertex_to_index;
    size_t last_index = 0;
    bool self_circle = false;

    inline size_t get_index(Vertex vertex) const {
        return vertex_to_index.at(vertex);
    }

    size_t get_index_mut(Vertex vertex) {
        if (vertex_to_index.find(vertex) == vertex_to_index.end()) {
            vertex_to_index[vertex] = last_index++;
        }
        return vertex_to_index[vertex];
    }

    // 计算两点距离在距离矩阵中的位置
    inline size_t calc_pos(size_t i, size_t j) const {
        if (i > j) std::swap(i, j);
        return i * (i - 1) / 2 + j;
    }

public:
    DistanceMatrix(int n, bool self_circle = false) : distances(n * (n - 1) / 2, 0), self_circle(self_circle) {}

    void set(Vertex i, Vertex j, EdgeWeight distance) {
        if (i == j) {
            if (self_circle) {
                auto index = get_index_mut(i);
                distances[calc_pos(index, index)] = distance;
            } else {
                throw std::invalid_argument("Self circle is not allowed.");
            }
        } else {
            size_t pos = calc_pos(get_index_mut(i), get_index_mut(j));
            distances[pos] = distance;
        }
    }

    EdgeWeight get(Vertex i, Vertex j) const {
        if (i == j && !self_circle) {
            return 0;
        }
        size_t pos = calc_pos(get_index(i), get_index(j));
        return distances[pos];
    }

    inline EdgeWeight operator()(Vertex i, Vertex j) const {
        return get(i, j);
    }

    inline size_t size() const {
        return distances.size();
    }
};