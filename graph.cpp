//
// Created by sunnysab on 24-9-13.
//

#include <vector>
#include "graph.h"


bool Graph::insert_vertex(const Vertex v) {
    //插入顶点集后返回的是插入的位置和一个是否插入成功的判断
    auto [position, result] = vertices.insert(v);
    if (result) {
        num_vertex++;
    }
    return result;
}

// 添加有向边
void Graph::add_directional_edge(const Vertex v1, const Vertex v2, Edge weight) {
    const auto tier1_end = edges.end();
    const auto tier1_it = edges.find(v1);

    if (tier1_it == tier1_end) {
        edges[v1] = {{v2, weight}};
    }
    else {
        auto tier2_end = tier1_it->second.end();
        auto tier2_it = tier1_it->second.find(v2);
        if (tier2_it == tier2_end) {
            tier1_it->second[v2] = weight;
        }
        else {
            tier2_it->second = weight;
        }
    }
}

// 连接两顶点，其实是在添加一条无向边，也就是执行两遍添加有向边操作
void Graph::connect(const Vertex v1, const Vertex v2, Edge weight) {
    add_directional_edge(v1, v2, weight);
    add_directional_edge(v2, v1, weight);
}

// 判断是否相连
bool Graph::is_connected(const Vertex v1, const Vertex v2) const {
    auto tier1_end = edges.end();
    auto tier1_it = edges.find(v1);
    if (tier1_it == tier1_end) {
        return false;
    }
    const auto tier2_end = tier1_it->second.end();
    const auto tier2_it = tier1_it->second.find(v2);
    return tier2_it != tier2_end;
}


// 获得两个顶点之间的权值
Edge Graph::get_weight(const Vertex v1, const Vertex v2) const {
    // 无环路，顶点等于自身的时候权值为0
    if (v1 == v2) {
        return 0;
    }

    const auto tier1_end = edges.end();
    const auto tier1_it = edges.find(v1);
    if (tier1_it == tier1_end) {
        return InfEdge;
    }

    const auto tier2_end = tier1_it->second.end();
    const auto tier2_it = tier1_it->second.find(v2);
    if (tier2_it == tier2_end) {
        return InfEdge;
    }

    return tier2_it->second;
}

// 获得某顶点的相邻顶点及其权值，返回的是一个迭代器。如果 src 没有相邻边，抛出异常
EdgeSet Graph::get_adjacent_vertices(const Vertex src) const {
    const auto tier1_end = edges.end();
    const auto tier1_it = edges.find(src);

    if (tier1_it == tier1_end) {
        return EdgeSet::empty();
    }

    const auto tier2_map = &tier1_it->second;
    return EdgeSet{tier2_map};
}


GraphByArray::GraphByArray(size_t size) {
    this->num_vertex = size;
    this->matrix = new SymmetricMatrix(size, InfEdge);
}

GraphByArray::GraphByArray(const GraphByAdjacencyList& other) {
    this->num_vertex = other.num_vertex;
    this->matrix = new SymmetricMatrix(other.num_vertex, InfEdge);
    for (const auto& [src, tier2_map] : other.edges) {
        for (const auto& [dst, weight] : tier2_map) {
            if (src < dst) {
                this->matrix->set(src, dst, weight);
            }
        }
    }
}

GraphByArray::~GraphByArray() {
    delete this->matrix;
    this->matrix = nullptr;

    this->vertices.clear();
}


bool GraphByArray::insert_vertex(const Vertex v) {
    // 插入顶点集后返回的是插入的位置和一个是否插入成功的判断
    auto [position, result] = vertices.insert(v);
    if (result) {
        num_vertex++;
    }
    return result;
}

//添加有向边
void GraphByArray::add_directional_edge(const Vertex v1, const Vertex v2, Edge weight) {
    this->matrix->set(v1, v2, weight);
}

// 连接两顶点，其实是在添加一条无向边，也就是执行两遍添加有向边操作
void GraphByArray::connect(const Vertex v1, const Vertex v2, Edge weight) {
    this->add_directional_edge(v1, v2, weight);
}

// 判断是否相连
bool GraphByArray::is_connected(const Vertex v1, const Vertex v2) const {
    return this->get_weight(v1, v2) != InfEdge;
}


// 获得两个顶点之间的权值
Edge GraphByArray::get_weight(const Vertex v1, const Vertex v2) const {
    return this->matrix->get(v1, v2);
}

// 获得某顶点的相邻顶点及其权值，返回的是一个迭代器。如果 src 没有相邻边，抛出异常
vector<pair<Vertex, Edge>> GraphByArray::get_adjacent_vertices(const Vertex src) const {
    vector<pair<Vertex, Edge>> result = {};
    for (auto v: this->vertices) {
        auto weight = this->get_weight(src, v);
        if (weight != InfEdge) {
            result.emplace_back(v, weight);
        }
    }
    return result;
}