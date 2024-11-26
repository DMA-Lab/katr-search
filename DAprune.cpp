#include <bits/stdc++.h>
#define rep(i,a,b) for(int i=a;i<=b;i++)
#include <vector>
#include <queue>
#include <unordered_set>
#include <cmath>
#include <limits>
#define ll long long
#define pb push_back
using namespace std;
const int N=1280005;
const int V=1280005; 
const double a=0.8;
const int INF = numeric_limits<int>::max();  // 无穷大值
// 定义路径结构
int K[N], NN[N];

struct Edge {
    int to;     // 边的目标顶点
    int weight; // 边的权重
};

// 定义图的节点
class Graph {
public:
    int V;  // 顶点数量
    vector<vector<Edge>> adjList;  // 邻接表

    Graph(int V) : V(V) {
        adjList.resize(V);
    }

    Graph(){
    }

    // 添加一条边
    void addEdge(int u, int v, int weight) {
        adjList[u].push_back({v, weight});
        adjList[v].push_back({u, weight});  // 如果是无向图，添加双向边
    }

    // 打印最短路径树
    void printSPTree(vector<int>& parent) {
        cout << "Edge \tWeight\n";
        for (int i = 1; i < V; ++i) {
            cout << parent[i] << " - " << i << "\t" << getWeight(parent[i], i) << "\n";
        }
    }

private:
    // 获取两点之间的边的权重
    int getWeight(int u, int v) {
        for (const auto& edge : adjList[u]) {
            if (edge.to == v) {
                return edge.weight;
            }
        }
        return INF;
    }
};//
 Graph graph(1280005); // 初始化图，4个顶点
struct pair_hash
{
    template<class T1, class T2>
    std::size_t operator() (const std::pair<T1, T2>& p) const
    {
        auto h1 = std::hash<T1>{}(p.first);
        auto h2 = std::hash<T2>{}(p.second);
        return h1 ^ h2;
    }
};

// 函数声明
unordered_map<pair<int,int> ,int, pair_hash>Dist; 
int dijkstra(Graph& graph, int src, int dest) {
	if(Dist.count({src,dest}))return Dist[{src,dest}];
    int V = graph.V;
    priority_queue<pair<int, int>, vector<pair<int, int>>, greater<pair<int, int>>> pq;  // 最小堆
	//cout<<V<<endl;
    Dist[{src,src}] = 0;  // 源点到自己的距离为0
	//cout<<src<<"! "<<dest<<endl;
	pq.push({0, src});  // 插入源点
    while (!pq.empty()) {
        int u = pq.top().second;  // 取距离最小的顶点
        int d = pq.top().first;
    //    cout<<u<<"?"<<endl;
        pq.pop();
        if (u == dest) {
        	
            return d;  // 找到目标点，返回最短距离
        }

        // 遍历当前顶点的所有邻接边
        for (const auto& edge : graph.adjList[u]) {
            int v = edge.to;
            int weight = edge.weight;

            if (!Dist.count({src,u})||Dist[{src,u}] + weight < Dist[{src,v}]) {
                Dist[{src,v}] = Dist[{src,u}] + weight;
                pq.push({Dist[{src,v}], v});
            }
        }
    }

    return INF;  // 如果目标点不可达，返回无穷大
}
vector<int> dist(V, INF);      // 存储从源点到每个顶点的最短距离
struct Path {
    vector<int> vertices;  // 路径上的顶点
    double cost;           // 路径的代价
    double lb;             // 路径的下界
    ll bit;
    int vm;
    // 重载比较运算符，用于优先队列中的排序
    void add(int x){
        cost += dijkstra(graph, vm, x);
        bit |= K[x];
        vertices.pb(x);
        vm = x;
    }
    bool operator<(const Path& other) const {
        return cost + dist[vm] > other.cost + dist[vm]; // 按照代价 + 下界排序，最小值优先
    }
};

double CalMaxLB(const Path& p);
double PathRefine(Path& p);

struct C {
    ll bit;
} c[N]; // SPT树预处理 

// DA-Prune 算法实现
int vvmx;
ll Qkw;

vector<int> parent(V, -1);     // 用于存储最短路径树


void dijkstraSPTree(Graph& graph, int src) {
    int V = graph.V;
    vector<bool> visited(V, false);  // 访问标记
    // 优先队列（最小堆），按距离升序排列
    priority_queue<pair<int, int>, vector<pair<int, int>>, greater<pair<int, int>>> pq;

    // 初始化源点
    dist[src] = 0;
    pq.push({0, src});
    parent[src] = -1;  // 源点的父节点为 -1

    while (!pq.empty()) {
        int u = pq.top().second;  // 取距离最小的顶点
        pq.pop();

        if (visited[u]) continue;  // 如果已经访问，跳过
        visited[u] = true;

        // 遍历邻接节点
        for (const auto& edge : graph.adjList[u]) {
            int v = edge.to;
            int weight = edge.weight;

            // 只有当新距离更小时，才更新距离和父节点
            if (!visited[v] && dist[u] + weight < dist[v]) {
                dist[v] = dist[u] + weight;
                c[v].bit = c[u].bit | K[v];
                pq.push({dist[v], v});
                parent[v] = u;  // 更新父节点，表示从 u 到 v 是最短路径
            }
        }
    }
}
vector<int>Dpsy[8];
int FindNN(int vi, ll bit) {
	int tot=0,mn=1e9,mv=-1;
	while((1<<tot)<=Qkw){
	//	cout<<(1<<tot)<<"?!!"<<endl;
	//	cout<<((1<<tot)&Qkw)<<" !!!!!@#!@#"<<((1<<tot)&bit)<<endl;
	//	cout<<(((1<<tot)&Qkw)&&(((1<<tot)&bit)==0))<<endl;
		if(((1<<tot)&Qkw)&&(((1<<tot)&bit)==0)){
			//cout<<endl<<tot<<endl;
			cout<<Dpsy[tot].size()<<" "<<tot<<endl;
			rep(i,0,Dpsy[tot].size()-1)
			{
			//	cout<<"Sd";
				int poi=Dpsy[tot][i];ll d=dijkstra(graph,poi,vi);//cout<<d<<" "<<poi<<"!1231232123321!!!!"<<endl;;
				if(d<mn){
					mn=d;
					mv=poi;
				}
			}
		}
		tot++;
	}
	
    return mv;
}

vector<Path> DA_Prune(Graph& graph, int s, int t, int k) {
    priority_queue<Path> PQ;  
    vector<Path> result;      
    double ub = numeric_limits<double>::max();  // 上界
    int m = 0;  
    dijkstraSPTree(graph, t);//cout<<dist[2]<<endl;
    Path start;
    start.vm=s;
    start.bit=K[start.vm];
    start.vertices.push_back(s);
    start.cost = 0;//cout<<"sd";
    
    start.lb = CalMaxLB(start);  // 初始下界
    PQ.push(start);
    while (!PQ.empty()) {
    	
        Path current = PQ.top(); PQ.pop();//cout<<"?";
		
        ll BIT = c[current.vm].bit;
	//	cout<<BIT<<"?"<<current.vm<<endl;
	//	cout<<current.bit<<"??"<<endl;
        if ((current.bit | BIT) == Qkw) {
        //	cout<<"!@@!!@"<<BIT<<"!!!"<<" "<<current.vm<<" "<<Qkw<<" "<<current.bit<<endl;
            result.push_back(current); //cout<<result.size()<<" "<<k<<endl;
            m++;
            if (m <= k) {
                ub = max(ub, current.cost + dist[current.vm]);
            }
            else ub = min(ub, current.cost + dist[current.vm]);

            if (result.size() == k) break;  
        }
		
        int now = current.vm;
        vector<int> deviations;
        while (now!=-1) {
            deviations.pb(now);
            //cout<<now<<"?"<<endl;
            now = parent[now];
        } 
			//cout<<current.bit<<"??!!"<<endl;
        int vpre = 0;
        for (int vi : deviations) {
        	cout<<vi<<" "<<deviations.size()<< endl;
        //	cout<<endl<<vi<<"!!!!"<<endl<<endl;
            Path newPath = current;//cout<<"Sd";
            if(vi!=current.vm)
			{
				newPath.add(vi);
			}
			
           // cout<<"sdeeeed";
            NN[vi] = FindNN(vi, newPath.bit);
		//	cout<<vi<<" "<<NN[vi]<<"!!!"<<endl<<" "<<newPath.bit<<endl;
		//	getchar();
			if(NN[vi]==-1)continue;//没有扩展的价值 
            if (vpre && (newPath.bit == current.bit) && NN[vi] == NN[vpre]) continue;  // Strategy 2 剪枝
			newPath.add(NN[vi]);
            newPath.lb = 0;//PathRefine(newPath) + CalMaxLB(newPath);
            int vmx = vvmx; 
            /*Path vpathm = newPath;
            vpathm.add(vmx);
            vpathm.add(t);
            if (vpathm.bit == Qkw) {
                m = m + 1;
                if (m <= k) {
                    ub = max(ub, vpathm.cost);
                }
                else ub = min(ub, vpathm.cost);
            }*/

            //if (m > k && newPath.lb > ub) continue;

            PQ.push(newPath);
            vpre = vi;
        }
    }

    return result;  // 返回找到的top-k路径
}

// 计算最大下界的函数
double CalMaxLB(const Path& p) {
    vvmx = 0;
    return 0;
}

// 路径精简函数，用于移除多余的POI
double PathRefine(Path& p) {
    // 假设通过一些启发式规则精简路径，返回更新后的代价
    // 这里可以实现更复杂的精简逻辑
    return 0;  // 假设精简后返回一个固定值
}
void get_newGraph() {
	string filename = "C:\\Users\\hp\\Desktop\\USA-road-d.FLA.gr";
    ifstream file(filename);  // 打开文件
    if (!file) {  // 文件打开失败
        cerr << "无法打开文件：" << filename << endl;
        return;
    }
    string line;
    // 跳过开头的注释部分
    while (getline(file, line)) {
        if (line[0] != 'a') continue;  // 只处理以 'a' 开头的行
        
        int u, v, w;  // u: 起点, v: 终点, w: 权重
        sscanf(line.c_str(), "a %d %d %d", &u, &v, &w);  // 读取边的起点、终点和权重
        graph.addEdge(u,v,w);
        //edges.push_back(make_tuple(u, v, w));  // 将边存储到边的列表
    }

    file.close();  // 关闭文件
}
// 主函数用于测试

ll val[N];
void gettype(){
	string filename = "C:\\Users\\hp\\Desktop\\USA-road-t.FLA.poi";
	ifstream file(filename);  // 打开文件
    if (!file.is_open()) {    // 检查文件是否成功打开
        cerr << "Error: Could not open the file!" << endl;
        return;
    }

    string line;
    vector<int> thirdColumn, lastColumn;
	//int totrr=0;
    // 按行读取文件
    while (getline(file, line)) {
        stringstream s(line);
        vector<int> values;
        int value;
        
        // 将每行的数据读取到values向量中
        while (s >> value) {
            values.push_back(value);
        }
	 
        // 提取第3列和最后一列数据
        if (values.size() >= 4) {
		//	cout<<"sd";
		//	if(c[values[2]].size()>=8)continue;
			val[values[1]]=values[4];
        	//p.pb(values[1]);//加入关键编号 
        	if(values[2]>5)continue;
        	K[values[1]]=((1<<(values[2]-1))&Qkw);
        	if(K[values[1]])
        	{
				Dpsy[values[2]-1].pb(values[1]);
			}
        	//if(values[2]==2)totrr++; 
        }
        
    }
    //cout<<totrr<<"!!"<<endl;
    file.close();  // 关闭文件
    
	//rep(i,1,200)_Poi[i].r=16000+rand(),_Poi[i].key=ss[(i%15+1)];  //给点的积分赋值 
	//rep(i,1,5)cout<<_Poi[i].key<<endl;
	
}
int main() {
   
	cout<<graph.V<<endl;
    // 添加边 (u, v, weight)
    
	Qkw=1|2|(1<<2);gettype();
    get_newGraph();auto start = std::chrono::high_resolution_clock::now();  // 开始时间
	
							
    //vector<int> keywords = {1, 2};    // 查询关键词集
    int s = 15;  // 源点
    int t = 7;  // 终点
    int k = 2;  // 找到top-k路径
	
    vector<Path> result = DA_Prune(graph, s, t, k);
	ll rmm;
    // 输出结果
    ll ans=0;
    for (const auto& path : result) {
        cout << "Path: ";ans=0;
        for (int v : path.vertices) {
            cout << v << " ";ans+=val[v];
        }
        cout << "Cost: " << path.cost +dist[path.vm] <<" "<<ans<< " "<<(1-a)*ans-a*0.001*(path.cost +dist[path.vm])<<endl;
    }
	auto end = std::chrono::high_resolution_clock::now();std::chrono::duration<double, std::milli> duration = end - start;  // 这里正确声明了 duration
double total_time=0;total_time += duration.count();	
		cout<<total_time<<" "<<endl;
    return 0;
}
