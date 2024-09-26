//
// Created by sunnysab on 24-9-26.
//

#include <cassert>
#include <iostream>
#include <random>
#include <vector>
#include <unordered_set>

#include "graph.h"
#include "poi.h"
#include "file.h"


PoiSet generate_random_pois(const std::unordered_set<Vertex>& vertex_set, size_t poi_count, size_t type_count) {
    PoiSet pois;
    std::vector<Vertex> vertices(vertex_set.begin(), vertex_set.end());
    std::unordered_set<Vertex> used_vertices;
    std::random_device rd;
    std::mt19937 gen(rd());
    std::uniform_int_distribution<> vertex_dist(0, vertices.size() - 1);
    std::uniform_int_distribution<> type_dist(1, type_count);
    std::uniform_int_distribution<> interest_dist(1, 100); // Assuming interest ranges from 1 to 100

    while (pois.size() < poi_count && used_vertices.size() < vertices.size()) {
        Vertex v = vertices[vertex_dist(gen)];
        if (!used_vertices.contains(v)) {
            PoiType type = type_dist(gen);
            Interest interest = interest_dist(gen);
            pois.add(Poi{v, type, interest});
            used_vertices.insert(v);
        }
    }

    return pois;
}

int main(int argc, char *argv[]) {
    if (argc != 4) {
        std::cerr << "Usage: " << argv[0] << " <graph_path> <count of poi> <count of type>" << std::endl;
        return 1;
    }

    const std::string input_graph_file = argv[1];
    assert(input_graph_file.ends_with(".gr"));
    const std::string output_mos_file = input_graph_file.substr(0, input_graph_file.size() - 2) + "mos";
    std::cout << "MOS file will be written to " << output_mos_file << std::endl;

    const auto poi_count = std::stoi(argv[2]);
    const auto type_count = std::stoi(argv[3]);
    const auto graph = load_graph(argv[1]);
    const auto pois = generate_random_pois(graph.vertices, poi_count, type_count);

    save_poi(pois, output_mos_file);
    cout << "done." << endl;
    return 0;
}