# KATR-Search

This repository contains the code for KATR-Search and all baseline algorithms.

## Dataset

The references for the road network and POI set included by each datset are as follows:

Road network are from [9th DIMACS Implementation Challenge - Shortest Paths](https://www.diag.uniroma1.it/challenge9/download.shtml).

|     | ROAD NETWORK                                                                                                                                                                                  | POI SET                                                                                               |
|-----|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------|
| CA  | [Direct](https://www.diag.uniroma1.it//challenge9/data/USA-road-d/USA-road-d.CAL.gr.gz), [Google Drive](https://drive.google.com/file/d/153jKmeP_43DsPFH-7vegfKrBvg7e8hqc/view?usp=drive_link) | [Google Drive](https://drive.google.com/file/d/184Zk81Et1gI57EKsdw-GBn423z_kg-js/view?usp=drive_link) |
| COL | [Direct](https://www.diag.uniroma1.it//challenge9/data/USA-road-d/USA-road-d.COL.gr.gz), [Google Drive](https://drive.google.com/file/d/1h-OlQ0ZWE-psMUh04Vn5z1NW2ROs1OhF/view?usp=drive_link) | [Google Drive]()                                                                                      |
| BJ  | [Google Drive](https://drive.google.com/file/d/1uNEpO455SnvYN9DQVGYZVF3ItSzISe2E/view?usp=drive_link)                                                                                                                                                                              | [Google Drive](https://drive.google.com/file/d/1HVb4NPtvAt_kAciPYb4WkkteJGHhH2S9/view?usp=drive_link) |


## Getting Started

The project is constructed and built with CMake, so you can build all targets and run algorithm separately.
You can modify the values of the parameters `POT_Type`, `k`, and `alpha` as mentioned in our evaluation. The variable `input_path` specifies the path to the dataset, allowing you to switch between different datasets by changing this variable.

Starting the comparison algorithms is similar, so it is not detailed here.
