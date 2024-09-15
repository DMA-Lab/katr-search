//
// Created by sunnysab on 24-9-13.
//

#include <cstring>
#include <format>
#include <fstream>
#include <sstream>
#include <iostream>
#include "graph.h"

using namespace std;


std::pair<Graph, bool> open() {
    //USA-road-t.NY大图
    //my-graph小图
    //my-subgraph可划分子图
    auto path = "dataset/USA-road-t.NY.gr";

    Graph g;
    ifstream file;

    file.open(path, std::ios::binary);
    if (!file) {
        cerr << std::format("can't open file {}: {}\n", path, std::strerror(errno));
        return {g, false};
    }

    for (string line; getline(file, line);) {
        stringstream ss(line); //ss的用法与cin/cout一样

        char operation; //用来保存数据集的第一个操作符
        ss >> operation;

        unsigned int num_vertex, num_edge;
        switch (operation) {
        case'a': {
            Vertex start, end;
            Edge edgeweight;
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
    return {g, true};
}