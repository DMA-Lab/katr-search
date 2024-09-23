//
// Created by sunnysab on 24-9-13.
//

#include <cstring>
#include <format>
#include <fstream>
#include <sstream>
#include <iostream>
#include "graph.h"
#include "poi.h"


Graph load_graph(const std::string &path) {
    Graph g;
    ifstream file;

    file.open(path, std::ios::binary);
    if (!file) {
        throw std::runtime_error(std::format("can't open file {}: {}\n", path, std::strerror(errno)));
    }

    for (string line; getline(file, line);) {
        stringstream ss(line); //ss的用法与cin/cout一样

        char operation; //用来保存数据集的第一个操作符
        ss >> operation;

        unsigned int num_vertex, num_edge;
        switch (operation) {
        case'a': {
            Vertex start, end;
            EdgeWeight edgeweight;
            ss >> start >> end >> edgeweight; //读入这一行数据
            g.add_directional_edge(start, end, edgeweight);
            break;
        }
        case'p': {
            string sp;
            ss >> sp >> num_vertex >> num_edge;
            cout << "该图中顶点数为：" << num_vertex << "," << "边数为：" << num_edge << endl;
            for (int v = 1; v <= num_vertex; ++v) {
                g.insert_vertex(v);
            }
            break;
        }
        case'c': {
            //auto声明变量是自动类型，注意：使用auto必须要进行初始化！
            auto comment = line.substr(0);
            cout << comment << endl;
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

    file.open(path, std::ios::binary);
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