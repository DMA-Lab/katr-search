//
// Created by sunnysab on 24-9-13.
//

#include <cstring>
#include <format>
#include <fstream>
#include <sstream>
#include <iostream>
#include <limits.h>

#include "graph.h"
#include "poi.h"


Graph load_graph(const std::string &path, unsigned int limit) {
    Graph g;
    ifstream file;

    file.open(path, std::ios::in);
    if (!file) {
        throw std::runtime_error(std::format("can't open file {}: {}\n", path, std::strerror(errno)));
    }

    for (string line; std::getline(file, line);) {
        stringstream ss(line); //ss的用法与cin/cout一样

        char operation; //用来保存数据集的第一个操作符
        ss >> operation;

        switch (operation) {
        case'a': {
            Vertex start, end;
            EdgeWeight weight;
            ss >> start >> end >> weight; //读入这一行数据
            if (g.contains(start) && g.contains(end)) {
                g.add_directional_edge(start, end, weight);
            }
            break;
        }
        case'p': {
            unsigned int edge_count, vertex_count;
            string sp;
            ss >> sp >> vertex_count >> edge_count;
            cout << "该图中顶点数为：" << vertex_count << "," << "边数为：" << edge_count << endl;

            unsigned int count_to_load = min(vertex_count, limit);
            for (int v = 1; v <= count_to_load; ++v) {
                g.insert_vertex(v);
            }
            break;
        }
        case 'c': {
            //auto声明变量是自动类型，注意：使用auto必须要进行初始化！
            auto comment = line.substr(0);
            cout << comment << endl;
            break;
        }
        default: {
            cerr << "unknown operation: " << operation << endl;
            break;
            }
        }
    }
    return g;
}


PoiSet load_poi(std::string path) {
    PoiSet pois;
    ifstream file;

    file.open(path, std::ios::in);
    if (!file) {
        throw std::runtime_error(std::format("can't open file {}: {}\n", path, std::strerror(errno)));
    }

    // poi 文件的格式如下：
    // 0 56987 41 1 5
    // 0 56988 10 1 33
    // 0 61184 41 1 25
    // 第 0 列是所属的子图，忽略。第1列是对应顶点编号，第2列是POI类型，第3列是boundary（忽略），第4列是POI兴趣度（权重）
    for (string line; getline(file, line);) {
        stringstream ss(line);

        unsigned int _unused;
        Vertex v;
        PoiType type;
        Interest interest;
        ss >> _unused >> v >> type >> _unused >> interest;
        pois.add(Poi{v, type, interest});
    }

    return pois;
}