package CreatData;

import CreatGraph.*;
import GraphEntity.*;
import KATRAlgorithm.FindTopk;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class GraphData {
    public  ArrayList<ArrayList<Integer>> SG ;
    public  POI[] POIList ;
    public  ArrayList<ArrayList<list>> List;
    public  ArrayList<ArrayList<Class_BPList>> BPList ;
    public  ArrayList<ArrayList<Integer>> PointMinBP;
    public  ArrayList<ArrayList<Integer>> BvList ;
    public MyGraph g;

    public  GraphData()  {
        this.BPList = new ArrayList<>();
        this.BvList = new ArrayList<>();

    }





}
