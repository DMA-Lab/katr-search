cmake_minimum_required(VERSION 3.30)
project(katr_cpp)

set(CMAKE_CXX_STANDARD 20)

add_executable(katr_cpp main.cpp
        file.cpp
        graph.cpp
        graph.h)

add_executable(osscaling osscaling.cpp
        file.cpp
        graph.cpp
        graph.h)
