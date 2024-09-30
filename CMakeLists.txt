cmake_minimum_required(VERSION 3.30)
project(katr_cpp)

set(CMAKE_CXX_STANDARD 20)

add_executable(poi-generator poi-generator.cpp
        file.cpp
        graph.cpp)

add_executable(katr_cpp katr.cpp)

add_executable(osscaling osscaling.cpp
        file.cpp
        graph.cpp)

add_executable(kosr kosr.cpp
        dijkstra.cpp)

