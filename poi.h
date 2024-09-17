//
// Created by sunnysab on 24-9-17.
//

#pragma once


/// POI 点类型
using PoiType = unsigned int;

/// 无效的 POI 类型
PoiType INVALID_POI_TYPE = 0;


struct Poi {
    /// POI 对应顶点
    unsigned int v;
    /// POI 类型
    PoiType type;
    /// POI 兴趣度（权重）
    unsigned int interest;
};


/// POI 集合
struct PoiSet {
    std::unordered_map<Vertex, Poi> pois_map;

    void add(const Poi &poi) {
        this->pois_map[poi.v] = poi;
    }

    void remove(const Poi &poi) {
        this->pois_map.erase(poi.v);
    }

    void clear() {
        this->pois_map.clear();
    }

    const Poi& operator[](const Vertex &v) const {
        return this->pois_map.at(v);
    }
};