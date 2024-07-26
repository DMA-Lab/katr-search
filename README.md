# Katr-Search

This repository includes the code for KATR-Search and all baseline algorithms. Each folder contains the code for its respective algorithm: "KATR-Search", "DAPrune", "ROSE-GM", "OSS-caling", and "StarKOSR".

## Dataset

The references for the road network and POI set included by each datset are as follows:

Road network: [9th DIMACS Implementation Challenge - Shortest Paths](https://www.diag.uniroma1.it/challenge9/download.shtml).
graph file: [NY](https://www.diag.uniroma1.it//challenge9/data/USA-road-d/USA-road-d.NY.gr.gz), [CAL](https://www.diag.uniroma1.it//challenge9/data/USA-road-d/USA-road-d.CAL.gr.gz), [COL](https://www.diag.uniroma1.it//challenge9/data/USA-road-d/USA-road-d.COL.gr.gz) (direct link, gziped)

POI set: [OpenStreetMap](https://www.openstreetmap.org)

All can be found on [Google drive](https://drive.google.com/drive/folders/1LncerbFXiM8UbE5fhuvu06L6afE3I8kY?usp=drive_link).

## Getting Started

The `src/main` directory contains the entry points for KATR-Search and all baseline algorithms. The main class is main_KATR. You can modify the values of the parameters `POT_Type`, `k`, and `alpha` as mentioned in our evaluation. The variable `input_path` specifies the path to the dataset, allowing you to switch between different datasets by changing this variable.

Starting the comparison algorithms is similar, so it is not detailed here.
