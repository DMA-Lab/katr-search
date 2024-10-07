cmake_minimum_required(VERSION 3.30)
project(katr_cpp)

set(CMAKE_CXX_STANDARD 20)

add_executable(poi-generator poi-generator.cpp
        file.cpp
        graph.cpp)

add_executable(debug-dijkstra dijkstra.cpp
        file.cpp
        graph.cpp)

add_executable(KATR-Search katr-search.cpp)

add_executable(ROSE rose.cpp)

add_executable(OSSCaling osscaling.cpp
        file.cpp
        graph.cpp)

add_executable(KOSR kosr.cpp
        graph.cpp
        file.cpp
        dijkstra.cpp)

