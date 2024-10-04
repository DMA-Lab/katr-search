#include <bits/stdc++.h>
#include <vector>
#include <queue>
#include <limits>
#include <cmath>
#include <unordered_map>
#include <set>
#define rep(i,a,b) for(int i=a;i<=b;i++) 
#define pb push_back
using namespace std;

const int INF = 1e9;

// ͼ�Ľṹ��ʹ���ڽӱ��ʾ
struct Edge {
    int to;
    int weight;
};
int n;
class Graph {
public:
    int n; // �ڵ�����
    vector<vector<Edge>> adjList; // �ڽӱ�

    Graph(int nodes) : n(nodes) {
        adjList.resize(n);
    }

    void addEdge(int u, int v, int w) {
        adjList[u].push_back({v, w});
        adjList[v].push_back({u, w});
    }

    // Dijkstra �㷨�����ڼ��㵥Դ���·��
    int dijkstra(int start, int end){//cout<<"!!";
    	vector<int> dist(n, INF);
    	priority_queue<pair<int, int>, vector<pair<int, int>>, greater<pair<int, int>>> pq;
        dist[start] = 0;pq.push({0,start});
    	while (!pq.empty()) {
            int d = pq.top().first;
            int u = pq.top().second;
            //cout<<u<<"?"<<endl;
            pq.pop();
            if(d > dist[u]) continue;
			if(u == end) 
			{
				//cout<<dist[u]<<"!@!@#@!#"<<endl;
				return dist[u];
			}
            for (Edge& edge : adjList[u]) {
                int v = edge.to;
                int w = edge.weight;
                if (dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    pq.push({dist[v], v});
                }
            }
        }
	}
    
    vector<int> dijkstra(int start) {
        vector<int> dist(n, INF);
        priority_queue<pair<int, int>, vector<pair<int, int>>, greater<pair<int, int>>> pq;
        dist[start] = 0;
        pq.push({0, start});

        while (!pq.empty()) {
            int d = pq.top().first;
            int u = pq.top().second;
            pq.pop();

            if (d > dist[u]) continue;

            for (Edge& edge : adjList[u]) {
                int v = edge.to;
                int w = edge.weight;
                if (dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    pq.push({dist[v], v});
                }
            }
        }

        return dist;
    }
};

// RNII����
class RNII {
public:
    vector<int> referenceNodes;
    unordered_map<int, vector<int>> poiDistances;

    RNII() {}
    
    void GetNodes(Graph& graph, const vector<int>& pois) {
        // ������ϣ�����ѡ�� 50 ���ο��ڵ�
        int numReferences = min(n-2,10);
        vector<int> allNodes = pois; // ���Դ� POI �б���ѡ��ο��ڵ�
        random_device rd;
        mt19937 g(rd());
        shuffle(allNodes.begin(), allNodes.end(), g);
        // ѡ��ǰ 50 ����Ϊ�ο��ڵ�
        referenceNodes.assign(allNodes.begin(), allNodes.begin() + numReferences);
    }

    void buildIndex(Graph& graph, const vector<int>& pois) {
        for (int ref : referenceNodes) {
            vector<int> dist = graph.dijkstra(ref);
            for (int poi : pois) {
            	
                poiDistances[poi].push_back(dist[poi]);
            }
        }
		
    }
	int tot=0;
    int estimateDistance(int poi1, int poi2) {
        int lowerBound = 0;
       // cout<<poiDistances[1120].size()<<" "<<++tot<<endl;
        for (int i = 0; i < referenceNodes.size(); i++) {
        	//cout<<poi1<<" "<<poi2<<endl;	
        //	if(tot>=55137) 
		//	cout<<"sd"<<" "<<tot<<" "<<poi1<<" "<<poi2<<" "<<i<<" "<<referenceNodes.size()<<endl;
            int dist1 = poiDistances[poi1][i];
           // if(tot>=55137) 
			//cout<<poiDistances[poi1][i]<<" "<<referenceNodes[i]<<" "<<poiDistances[poi2].size()<<" "<<poi2<<endl;	if(tot==55137)cout<<tot<<" "<<poiDistances[poi2][i]<<endl;
            int dist2 = poiDistances[poi2][i];//cout<<"Sd!";
		
            lowerBound = max(lowerBound, abs(dist1 - dist2));
        }//cout<<"sd!";
        
        return lowerBound;
    }
};
const int TT=25,CC=155,KK=15;
int k;
int T;
struct Compare {
	    bool operator()(const pair<int, vector<int>>& a, const pair<int, vector<int>>& b) {
	        if(a.first!=b.first)return a.first < b.first; return a.second.size()<b.second.size();
	    }
	};
// KROSE�㷨
int start, ed;int K;vector<int>c[CC];//װ����ĳ�ؼ��ʵ����е�
class KROSE {
public:
    Graph& graph;
    RNII& rnii;
    int costThresh; // ��֦��ֵ
    KROSE(Graph& g, RNII& r) : graph(g), rnii(r), costThresh(INF) {}



	map<pair<int,int> ,int>diss;
	int getdis(int u,int v){
		if(u>v)swap(u,v);
		if(diss.count({u,v}))return diss[{u,v}];
		return rnii.estimateDistance(u,v);
	}
	pair<int,vector<int> > OS[TT][CC][KK];
	int n;
	
 
	
    // ��̬�滮��չTop-k·������POI˳��Լ��
    vector<pair<int, vector<int> > > Top_K_ose(const vector<int>& poiOrder) {
        vector<int> dist = graph.dijkstra(start);
        vector<int> nullpath,nowpath;
        nullpath.clear();
        vector<pair<int, vector<int>> > paths;
        //vector<vector<int>> paths = greedyPaths(start, end, poiOrder);
			rep(i,1,T){
				rep(j,0,c[poiOrder[i]].size()-1){
					if(i!=1){
						//cout<<i<<"?"<<endl;
						priority_queue<pair<int, vector<int>>, vector<pair<int, vector<int>>>, Compare> Q;
						rep(l,0,c[poiOrder[i-1]].size()-1){
							
							rep(k,1,K)
							{
								nowpath=OS[i-1][l][k].second;
								nowpath.pb(c[poiOrder[i]][j]);//if(i==2)cout<<nowpath.size()<<"?"<<endl;

								Q.push({OS[i-1][l][k].first+getdis(c[poiOrder[i-1]][l],c[poiOrder[i]][j]),nowpath});
							}
							while(Q.size()>K)
							{
								
							//	exit(0);
								Q.pop();//�����ڵ�k�����ų� 
							}
						}
						int nowk=K;
						/*
						if(i==2){
						cout<<Q.size()<<endl;
							while(Q.size()){
									cout<<Q.top().first<<"!!"<<endl;
									for(int u:Q.top().second)cout<<u<<"��"<<endl;
									Q.pop();
									
								}
								exit(0);
							//exit(0);
						}*/
						while(!Q.empty()){
							OS[i][j][nowk--]=Q.top();
						/*		if(i==2){
								cout<<Q.top().first<<"!!"<<" "<<i<<" "<<j<<" "<<nowk+1<<endl;
								cout<<Q.top().second.size()<<"!!!"<<endl;//exit(0);
								for(int u:Q.top().second)cout<<u<<"��"<<endl;
								//exit(0);
								}*/
							Q.pop();//�Ӵ�С 
						}
					}
					else{
							nowpath=nullpath;
							if(c[poiOrder[i]][j]!=start)
							nowpath.pb(start);
							nowpath.pb(c[poiOrder[i]][j]);
						rep(k,1,K){
							
							if(k==1){
								OS[i][j][k]={getdis(start,c[poiOrder[i]][j]),nowpath};
								
							}
							else
							OS[i][j][k]={1e8,nowpath};
						}
					}
				}
			}
			priority_queue<pair<int, vector<int>>, vector<pair<int, vector<int>>>, Compare> QQ;
			rep(j,0,c[poiOrder[T]].size()-1)
			{
				
				rep(k,1,K)
				{
					nowpath=OS[T][j][k].second;
					//if(ed!=c[poiOrder[T]][j])nowpath.pb(ed);
				//	cout<<OS[T][j][k].first<<"?"<<c[T][j]<<" "<<ed<<endl;
					QQ.push({OS[T][j][k].first//+getdis(c[poiOrder[T]][j],ed)
					,nowpath});//cout<<"sd";
					while(QQ.size()>K)QQ.pop();//�öѰѴ���߳�ȥ 
				}
			}							

			while(!QQ.empty()){
				
					paths.pb(QQ.top());QQ.pop();//�Ӵ�Сװǰk��·�� 
			}
        return paths;
    }

    // ����Top-k·���ľ�ȷ����
    vector<int> computeExactDistances(const vector<pair<int,vector<int>> >& paths) {
        vector<int> totalCosts(k, 0);
        for (int i = 0; i < k; i++) {
        	//cout<<paths[i].second.size()<<endl;

            for (size_t j = 1; j < paths[i].second.size(); j++) {
                int disss = graph.dijkstra(paths[i].second[j - 1], paths[i].second[j]);
                diss[{paths[i].second[j-1],paths[i].second[j]}]=diss[{paths[i].second[j],paths[i].second[j-1]}]=disss;
				totalCosts[i] += disss;
            }
        }
        return totalCosts;
    }
	
    // ִ��KROSE�㷨
    int tot=0;
    vector<pair<int,vector<int>> > run(const vector<int>& poiOrder) {
		vector<pair<int,vector<int>> > newPaths;
        while (true) {
            newPaths = Top_K_ose(poiOrder);
            
			vector<int> newCosts = computeExactDistances(newPaths);
			
            bool updated = false;
            for (int i = 0; i < k; i++) {
            //	cout<<i<<" "<<newCosts[i]<<" "<<newPaths[i].first<<endl;
            	//for(auto u:newPaths[i].second)cout<<u<<endl;
            	//if(++tot>=10)exit(0);
                if (newCosts[i]!=newPaths[i].first) {//Ԥ�������ھ�ȷ 
                    updated = true;
                }
            }

            if (!updated) {
                break; // ���û�и����κ�·�������������
            }
        }

        return newPaths;
    }
};


void get_Graph(Graph &graph) {
	string filename = "C:\\Users\\hp\\Desktop\\USA-road-t.NY-stripped-1000.gr";
    ifstream file(filename);  // ���ļ�
    if (!file) {  // �ļ���ʧ��
        cerr << "�޷����ļ���" << filename << endl;
        return;
    }
    string line;
    // ������ͷ��ע�Ͳ���
    while (getline(file, line)) {
        if (line[0] != 'a') continue;  // ֻ������ 'a' ��ͷ����
        int u, v, w;  // u: ���, v: �յ�, w: Ȩ��
        sscanf(line.c_str(), "a %d %d %d", &u, &v, &w);  // ��ȡ�ߵ���㡢�յ��Ȩ��
        graph.addEdge(u, v, w);
        //edges.push_back(make_tuple(u, v, w));  // ���ߴ洢���ߵ��б�
    }

    file.close();  // �ر��ļ�
}
int val[10005];
vector<int>p;
void gettype(){
	string filename = "C:\\Users\\hp\\Desktop\\USA-road-t.NY-stripped-1000.poi";
	ifstream file(filename);  // ���ļ�
    if (!file.is_open()) {    // ����ļ��Ƿ�ɹ���
        cerr << "Error: Could not open the file!" << endl;
        return;
    }

    string line;
    vector<int> thirdColumn, lastColumn;
	//int totrr=0;
    // ���ж�ȡ�ļ�
    while (getline(file, line)) {
        stringstream s(line);
        vector<int> values;
        int value;
        
        // ��ÿ�е����ݶ�ȡ��values������
        while (s >> value) {
            values.push_back(value);
        }
	 
        // ��ȡ��3�к����һ������
        if (values.size() >= 4) {
		//	cout<<"sd";
		//	if(c[values[2]].size()>=8)continue;
        	p.pb(values[1]);//����ؼ���� 
        	val[values[1]]=values[4];
        	c[values[2]].pb(values[1]);
        	//if(values[2]==2)totrr++; 
        }
        
    }
    //cout<<totrr<<"!!"<<endl;
    file.close();  // �ر��ļ�
    
	//rep(i,1,200)_Poi[i].r=16000+rand(),_Poi[i].key=ss[(i%15+1)];  //����Ļ��ָ�ֵ 
	//rep(i,1,5)cout<<_Poi[i].key<<endl;
	
}
int main() {
    // ����ͼ
    n = 10005;
    Graph graph(n);gettype();
	get_Graph(graph);
    // ��ӱ�
  //  getchar();
    // ����POI˳��
	
    vector<int> poiOrder = {0, 1, 2, 5,6}; // POI����˳��1 -> 2 -> 3
	
    // ����RNII����
    RNII rnii;
    rnii.GetNodes(graph,p); // �ο��ڵ�
    rnii.buildIndex(graph,p);
    // ����KROSE�㷨-
    K  = 10; // Top-kֵ
    k= 10 ;
    start = 810;
   // ed = 1103;
    T=4;
    KROSE krose(graph, rnii);
    
    
    vector<pair<int,vector<int>> > result = krose.run(poiOrder);
	sort(result.begin(),result.end(),Compare());
    cout << "Top-k optimal paths (with POI order):" << endl;
    for (int i = 0; i < k; i++) {
    	long long res=0;
		rep(j,1,T){
    		res+=val[result[i].second[j]];
		}
        cout << "Path " << i + 1 << ": "<<result[i].first/1000<<" "<<res<<" "<<0.2*res-0.0008*result[i].first<<endl;
        for (auto node : result[i].second) {
            cout << node << " ";
        }
        cout << endl;
    }
	
    return 0;
}

