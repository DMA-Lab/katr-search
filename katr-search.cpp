#include <bits/stdc++.h>
using namespace std;
#define rep(i,a,b) for(int i=a;i<=b;i++)
#define rep2(i,a,b) for(int i=a;i>=b;i--)
#define sc(x) scanf("%d",&x)
#define sl(x) scanf("%lld",&x)
#define pb push_back
#define ll long long 
const double alpha=0.8;
const int N=3e5+5;
/* run this program using the console pauser or add your own getch, system("pause") or input loop */
struct Poi{
	ll SD;
	string key;
	int keyid=-1;
	int id;
	ll r;
	double x,y;
}_Poi[N];
vector<pair<int,int> >E[N];
bool operator <(const Poi &a,const Poi &b){
	return a.SD>b.SD;
}
void printroad(Poi a,Poi b);
struct query{
	unordered_map<string,int>t_id;
	vector<string>t;
	int k;
	Poi vq;
	int m;//t.size()
	query(){
	}
	query(vector<string>t,Poi vq,int k){
		m=t.size();
		this->k=k;
		this->vq=vq;
		this->t.assign(t.begin(),t.end());
		rep(i,0,t.size()-1){
			this->t_id[t[i]]=i;
			//��Ŵ�0��ʼ 
		}
	}
};

struct CP_set{
	vector<Poi>choose_poi;
	ll rm;
	CP_set(){
		
	}
	CP_set(vector<Poi>choose_poi){
		this->choose_poi.assign(choose_poi.begin(),choose_poi.end());
		rm=0;
		rep(i,0,this->choose_poi.size()-1){
			rm += this->choose_poi[i].r;
		}
	}
	
}w;

double get_edis(Poi a,Poi b){
	return sqrt((a.x-b.x)*(a.x-b.x)+(a.y-b.y)*(a.y-b.y));
}
Poi vq;int tot=0;
struct CP_route{
	vector<Poi>choose_poi;
	double edis;
	ll D;
	double SC;
	ll rm;
	CP_route(){
	} 
	CP_route(vector<Poi>choose_poi){
		this->choose_poi.assign(choose_poi.begin(),choose_poi.end());
		rm=0;
		D=0;
		rm+=this->choose_poi[0].r;
		rep(i,1,this->choose_poi.size()-1){
			edis += get_edis(choose_poi[i-1],choose_poi[i]);
			rm+=this->choose_poi[i].r;
		}
		if(vq.id!=choose_poi[0].id){
			edis+=get_edis(vq,choose_poi[0]);
		}
	}
	update(vector<Poi>choose_poi){
		this->choose_poi.assign(choose_poi.begin(),choose_poi.end());
		edis=0;
		if(vq.id!=choose_poi[0].id){
			edis+=get_edis(vq,choose_poi[0]);
		}rep(i,1,this->choose_poi.size()-1){
			edis += get_edis(choose_poi[i-1],choose_poi[i]);
		//	rm+=this->choose_poi[i].r;
		}
	}
	void write(){
		puts("�ؼ��ʷ���˳��"); 
		rep(i,0,this->choose_poi.size()-1){
			printf("%d %c",this->choose_poi[i].id,i==this->choose_poi.size()-1?'\n':',');
		}
		puts("��·�ܾ��룺");
		printf("%lld\n",D/1000);
		puts("��·��ϲ���̶�:");
		printf("%lld\n",rm/1000);
		puts("��·������:");
		cout<<SC/1000<<endl;
		puts("����·����");
	}
	void write_road(){
		if(choose_poi[0].id!=vq.id)printroad(vq,choose_poi[0]);
		rep(i,1,choose_poi.size()-1)printroad(choose_poi[i-1],choose_poi[i]);
		printf("%d\n",choose_poi[choose_poi.size()-1].id);
	}
};

bool operator < (const CP_route &a,const CP_route &b){
	return a.edis > b.edis;
}

	int m;
	
	vector<Poi>choose_poi;vector<Poi>P[25];vector<CP_set>CP_sets;int Isiz=0,k;
	vector<CP_route>Base_CP_route; 
	
	void get_k_CP_set(int d){
		if(Isiz>=k)return;
		
		if(d==m){
			Isiz++;
			CP_sets.pb(CP_set(choose_poi));
			return;
		}
		rep(i,0,P[d].size()-1){
			choose_poi.pb(P[d][i]);
			get_k_CP_set(d+1);
			choose_poi.pop_back();
		}
	};

	int vis2[25];vector<CP_route>Now_route;vector<Poi>Now_poi;
	void get_cp_route(CP_set CPSi,int d){
		if(d==CPSi.choose_poi.size()){
			CP_route nowr = CP_route(Now_poi);
		//	nowr.write();
			Now_route.pb(nowr);//���fg=0��˵���������ڻ�׼·��������ɸѡ 
			return;
		}
		rep(i,0,CPSi.choose_poi.size()-1){
			if(vis2[i])continue;
			Now_poi.pb(CPSi.choose_poi[i]);
			vis2[i]=1;
			get_cp_route(CPSi,d+1);
			vis2[i]=0;
			Now_poi.pop_back();
		}
	}
	map<pair<Poi,Poi>, ll>dist;
	map<CP_route,ll>ms;
	ll ddis[N];
	int bel[N],Vis[N],p=0;
	struct cmp1{
		bool operator ()(Poi &a,Poi &b)const{
			return ddis[a.id] > ddis[b.id];
		}
	};
	void init(){
		memset(Vis,0,sizeof(Vis));
		memset(ddis,0x3f,sizeof(ddis));
		p=1;
	}
	ll dijkstra(Poi s,Poi t){//return 0;
		priority_queue<Poi,vector<Poi>,cmp1>Q;
		Q.push(s);if(p==0)init();ddis[s.id]=0;
		vector<int>cg;cg.pb(s.id);
		while(!Q.empty()){
			Poi u=Q.top();
			Q.pop();
			if(Vis[u.id])continue;
			Vis[u.id]=1;
			if(u.id==t.id){ll ans=ddis[u.id];rep(i,0,cg.size()-1)ddis[cg[i]]=1e18,Vis[cg[i]]=0;return ans;}
			rep(i,0,E[u.id].size()-1)
			{
				Poi v=_Poi[E[u.id][i].first];
				if(ddis[v.id]>ddis[u.id]+E[u.id][i].second){
					cg.pb(v.id);
					ddis[v.id]=ddis[u.id]+E[u.id][i].second;
					Q.push(v);
				}
			}
		}
	}
	void initdist(){//ͬһ����ͼ�ڵ�����POI��dijkstra������˴˵ľ��� 
		
		
	}
	int tot1=0;
	struct pair_hash {
	    template <class T1, class T2>
	    std::size_t operator() (const std::pair<T1, T2>& p) const {
	        auto hash1 = std::hash<T1>{}(p.first);
	        auto hash2 = std::hash<T2>{}(p.second);
	        return hash1 ^ (hash2 << 1); // ��������ϣֵ���
	    }
		};

	unordered_map<pair<int,int>,ll,pair_hash>distt;double total_time = 0.0;
	ll get_dist(Poi u,Poi v){

    // �ں����ڲ�ͳ�Ƶ�������ʱ��
    
		if(u.id>v.id)swap(u,v);
		if(!distt.count({u.id,v.id}))distt[{u.id,v.id}]=dijkstra(u,v);
		/*if(bel[u.id]==bel[v.id]){
			return dist[{u,v}];
		}*/
		
		
		return distt[{u.id,v.id}];
		
	}
	ll get_Routedist(CP_route r){
		ll D=0;
		rep(i,1,r.choose_poi.size()-1){
			D += get_dist(r.choose_poi[i-1],r.choose_poi[i]);
		}
		if(r.choose_poi[0].id!=vq.id)D+=get_dist(vq,r.choose_poi[0]);
		return D;
	}
	CP_route get_best_CP_route(CP_set CPSi){
		priority_queue<CP_route>Qr;
		Now_route.clear();
		get_cp_route(CPSi,0);
		rep(i,0,Now_route.size()-1){
			Qr.push(Now_route[i]);
		}

		double GDs=1000000000;CP_route ans;
		while(!Qr.empty()){
			CP_route ru=Qr.top();
			Qr.pop();
			if(GDs<ru.edis){
				break;
			}
			ll Gj = get_Routedist(ru);
			ms[ru] = Gj;
			if(Gj<GDs){
				GDs=Gj;ans=ru;
			}
		}
		ans.D=GDs;
		ans.SC=-alpha*GDs+(1-alpha)*ans.rm;
		
		//cout<<endl<<ans.D<<"?"<<" "<<" "<<ans.rm<<" "<<ans.SC<<endl;
		return ans;
	}
	double SCmin;
	void get_k_CP_route(){
		SCmin=1e9;
		rep(i,0,k-1){
			CP_route nowr=get_best_CP_route(CP_sets[i]);
		//	cout<<i<<"!@!@!"<<" "<<CP_sets[i].choose_poi[1].id<<"!!"<<endl;
			Base_CP_route.pb(nowr);
			//nowr.write_road();
			//nowr.write();
			SCmin=min(SCmin,nowr.SC);
		}
	}
	double Dub;
struct cmp2{
	bool operator ()(const int &a,const int &b)const{
			if(_Poi[a].r!=_Poi[b].r)return _Poi[a].r>_Poi[b].r;
			else return a!=b;
		}
};
ll ru;
set<int,cmp2>now_Poi[25];
ll dis2[N];int Vis2[N];
struct cmp3{
	bool operator ()(const Poi &a,const Poi &b)const{
		return dis2[a.id]>dis2[b.id];
	}
};
//��ÿ���ؼ�����һ����ʼ�ĵ� 
int total;
//��ͷ��������Ҫ�Ż���ռ40%��200+ms 
	ll dijkstra2(Poi s){ 
		priority_queue<Poi,vector<Poi>,cmp3>Q;
		Q.push(s);memset(dis2,0x3f,sizeof(dis2));memset(Vis2,0,sizeof(Vis2));dis2[s.id]=0;
		//cout<<total<<"!!"<<endl;
		while(!Q.empty()){
			Poi u=Q.top();
			Q.pop();
			if(Vis2[u.id])continue;
		//	cout<<dis2[3]<<" "<<dijkstra(_Poi[1],_Poi[3])<<endl;
			Vis2[u.id]=1;
		//	if(u.id==1)cout<<"?!"<<endl;
			if(u.keyid!=-1)
			{
			//	if(u.id==1)cout<<"?!";
				total--;
			}
			if(total==0)
			{
				break;//���������еĹؼ����� 
			}
			rep(i,0,E[u.id].size()-1)
			{
				Poi v=_Poi[E[u.id][i].first];
				if(dis2[v.id]>dis2[u.id]+E[u.id][i].second){
					dis2[v.id]=dis2[u.id]+E[u.id][i].second;
					Q.push(v);
				}
			}
		}
	//	cout<<total<<"??"<<endl;
		 
	}
	const double eps=1e-5;
int del[N];
const int SGN=2005;
vector<int>SG[SGN];
ll rmaxnow[25];
int SG_num;
query nowQ;
int delSG[SGN];
double lstSCmin;
void Safe_Region_establishment(){
	int notchange=0;
	double lstDub=-1;lstSCmin=SCmin;
	//cout<<vq.id<<"??"<<dijkstra(_Poi[1],_Poi[3])<<endl;
		

	if(!Vis2[vq.id])dijkstra2(vq);	
	while(!notchange)
	{
		ru=0;	
		rep(i,0,m-1){
			auto Pnow=now_Poi[i].begin();
			ru=ru+_Poi[(*Pnow)].r;
					   
		}
	//	cout<<ru<<"!!"<<endl;
		//cout<<dis2[814]<<" "<<dis2[932]<<" "<<dis2[807]<<endl;
		//cout<<Dub<<" !"<<SCmin<<" "<<ru<<endl;
		//if(((1-alpha)*ru-SCmin)/alpha>0)
		Dub=((1-alpha)*ru-SCmin)/alpha;
	//	cout<<ru<<" "<<SCmin<<"!!!!"<<Dub<<endl<<((1-alpha)*(ru+30000)-SCmin)/alpha<<"??"<<now_Poi[0].count(1107)<<"!!!!!!!!"<<endl;
		//else break;
		//cout<<Dub<<" "<<endl;
		//cout<<Dub<<"????"<<SCmin<<endl;
		if(abs(Dub-lstDub)<eps)
		{
			break;
		}
		set<Poi,cmp2>::iterator t;
		rep(i,0,m-1){
			while(1){
				auto Pnow=now_Poi[i].begin();
				//cout<<i<<" "<<now_Poi[i].size()<<" "<<Dub<<" "<<dis2[(*Pnow)]<<" "<<(*Pnow)<<" "<<SCmin<<" "<<_Poi[(*Pnow)].r<<" "<<now_Poi[0].count(1107)<<" "<<dis2[1107]<<endl;
				if(dis2[(*Pnow)]<=Dub){//ÿ��Ԫ����౻ɾһ�� 
	//				
			//		cout<<Dub<<" "<<dis2[(*Pnow)]<<"!!";
					break;
				}//cout<<(*Pnow)<<" "<<dis2[(*Pnow)]<<" "<<Dub<<endl;
				now_Poi[i].erase(Pnow);
			}
		}//cout<<"?";
		lstDub=Dub;
	}	

						 
	cout<<Dub<<"!!"<<endl;
}

void pruning_of_SG(){
	
	ll rsub=0;
	rep(i,0,m-1){
		auto Pnow=now_Poi[i].begin();
		rmaxnow[i]=_Poi[(*Pnow)].r;
		rsub+=rmaxnow[i];
	}
	rep(i,0,SG_num-1)
	{
		ll shortest_dist=1e18;
		ll rmx=0;
		int fw=0;
		rep(j,0,SG[i].size()-1){//���Ӷ�ԼΪ�������еĵ� 
			
			Poi nw=_Poi[SG[i][j]];
			if(!nowQ.t_id.count(nw.key))continue;
			fw=1;
			shortest_dist=min(shortest_dist,dis2[SG[i][j]]);
			int t_id=nowQ.t_id[nw.key];//�ҵ��õ�Ĺؼ�����ѯ�ʽṹ����±� 
			rmx=max(rmx,rsub+nw.r-rmaxnow[t_id]);
		}
		//cout<<fw<<" "<<"?"<<endl;
		if((fw==0||-alpha*shortest_dist+rmx*(1-alpha)<=SCmin)){
			delSG[i]=1;
		}//����ͼɾ�� 
	}
}
int vis3[N],dis3[N],V[N];
vector<CP_route>all_route;
CP_set nowCPSi;
vector<Poi>nPoi;
int fg;
CP_route nr;
void shuffle_route(int d,ll edis){
	if(d==m){
		if(!fg)nr=CP_route(nPoi);
		else nr.update(nPoi);
		fg=1;
		if(nr.edis<((1-alpha)*nr.rm-SCmin)/alpha)
		Now_route.pb(nr);
		//CP_route(nPoi).write();
		return;
	}
	if(edis>((1-alpha)*nowCPSi.rm-SCmin)/alpha)return;
	rep(i,0,m-1){
		if(V[i])continue;
		V[i]=1;
		nPoi.pb(nowCPSi.choose_poi[i]);
		ll ed=0;if(d)ed=get_edis(nPoi[d-1],nowCPSi.choose_poi[i]);
		shuffle_route(d+1,edis+ed);
		nPoi.pop_back();V[i]=0;
	}
}int tott=0;
priority_queue<double>SCC;
void get_batch_CP_route(CP_set CPSi){
		priority_queue<CP_route>Qr;
		nowCPSi=CPSi;ll rm=CPSi.rm;
		Now_route.clear();
		fg=0;//cout<<tot<<endl;
		shuffle_route(0,0);//70ms!!!�����Ż�  �Ѿ��Ż� 
	//	cout<<tott<<endl;
		if(!Now_route.size())return;
		rep(i,0,Now_route.size()-1){
			Qr.push(Now_route[i]);//50~70ms!!!�����Ż�   ���Ż� 
		}	
		double GDs=1000000000;CP_route nowr;
	//	cout<<"!";
		while(!Qr.empty()){
			CP_route ru=Qr.top();
			Qr.pop();
			
			tott++;
			double Dmx=((1-alpha)*rm-SCmin)/alpha;//����Rmx�Ĳ����ܳ�Ϊ�� 
			if(ru.edis>=Dmx){
				break;
			}	
			ll Gj = get_Routedist(ru);
			ms[ru] = Gj;
			ru.D=Gj;
			ru.SC=-alpha*Gj+(1-alpha)*rm;SCC.push(-ru.SC);//���С���� 
			while(SCC.size()>k)SCC.pop();
			if(SCC.size()>=k)
			{
			//	ru.write();
			//	ru.write_road();
				SCmin=max(SCmin,-SCC.top());//��ʼ��� 
			//	cout<<ru.SC<<" "<<-SCC.top()<<" "<<SCmin<<endl<<" "<<Gj<<endl<<endl;
			}
			//cout<<Gj<<" "<<rm<<" "<<ru.SC<<" "<<CPSi.rm<<endl;
			all_route.pb(ru);
		}//28ms�����Ż� 
		
		
		if(SCmin-lstSCmin>5000)//����һ���Ż���ֵ 
		Safe_Region_establishment();
		
	
	}
	

void get_CP_set(int d,int id,Poi vf){
	if(d==m){
	++tot;
		CP_set nowset=(CP_set(choose_poi));
	
		//rep(i,0,nowset.choose_poi.size()-1){cout<<nowset.choose_poi[i].id<<"?";}
		get_batch_CP_route(nowset);

		return;
	}
	if(id==d){
		choose_poi.pb(vf);
		get_CP_set(d+1,id,vf);
		choose_poi.pop_back();
	}
	else
	rep(i,0,P[d].size()-1){
		choose_poi.pb(P[d][i]);
		get_CP_set(d+1,id,vf);
		choose_poi.pop_back();
	}
}
void get_new_route(int id,Poi vf){
  // ����ʱ��	auto end = std::chrono::high_resolution_clock::now();  // ����ʱ��
		
	Now_route.clear();choose_poi.clear();
	get_CP_set(0,id,vf);
	
}
struct cmp4{
	bool operator ()(const Poi &a,const Poi &b)const{
		return dis3[a.id]>dis3[b.id];
	}
};
void get_ans(){
	priority_queue<Poi,vector<Poi>,cmp4>Qu;
	memset(dis3,0x3f,sizeof(dis3));memset(vis3,0,sizeof(vis3));
	dis3[vq.id]=0;Qu.push(vq);
	int e=0;rep(i,0,m-1)P[i].clear();
	
	//cout<<del[3]<<" "<<delSG[bel[3]]<<" "<<bel[3]<<" "<<bel[4]<<endl;
	while(!Qu.empty()){
		Poi vf=Qu.top();
		Qu.pop();
		
		if(vis3[vf.id])continue;//������������֦�㣬��֦��ȫ���� 
		if(dis3[vf.id]>Dub)break;
		//if(vf.id==1)
		//cout<<vf.id<<"!!"<<" "<<del[vf.id]<<" "<<delSG[bel[vf.id]]<<" "<<nowQ.t_id.count(vf.key)<<" "<<vf.key<<endl;
		vis3[vf.id]=1;//�ѱ�������ɾ 
		if(nowQ.t_id.count(vf.key)&&!(del[vf.id]||delSG[bel[vf.id]])){//��ɾ�ĵ����ͼ��������Ϊƥ��� 
		//�����йؼ���ƥ�� 
			
			int id=nowQ.t_id[vf.key];//cout<<id<<"!!"<<endl;
			if(!P[id].size())e++;
			P[id].pb(vf);
			if(e==m)get_new_route(id,vf);
			
		}
		rep(i,0,E[vf.id].size()-1){
			int v = E[vf.id][i].first;
			long long w = E[vf.id][i].second;
			if(dis3[v] > dis3[vf.id] + w)dis3[v] = dis3[vf.id] + w;
			Qu.push(_Poi[v]);
		}
	}
	ll www=1;
	//priority_queue<int>
	

	//70ms,�����Ż���������ֱ���ö�װ������Ҫ��ȫ�ӽ��� 
	sort(all_route.begin(),all_route.end(),[](CP_route a,CP_route b){if(a.SC!=b.SC)return a.SC>b.SC;else return a.choose_poi[0].id<b.choose_poi[0].id;});//�����ҳ�SC�ϴ�ġ� 
	int sz=min((int)all_route.size(),max(k,5));
	rep(i,0,sz-1){
		all_route[i].write();
		all_route[i].write_road();
	}
}
void di_exploration(query Q){
	nowQ=Q;
	priority_queue<Poi>Qu;
	ru=0;
	int sz=0,findSCmin=0;Isiz=0;
	m=Q.m;k=Q.k;memset(dis3,0x3f,sizeof(dis3));memset(vis3,0,sizeof(vis3));
	vq=Q.vq;
	Qu.push(Q.vq);
	int e=0;
	
	while(!Qu.empty()){
		Poi vf=Qu.top();
		Qu.pop();
		if(vis3[vf.id])continue;//��ɾ
		vis3[vf.id]=1;//�ѱ�������ɾ 
		
		if(Q.t_id.count(vf.key)){//�����йؼ���ƥ�� 
		
			int id=Q.t_id[vf.key];
					if(!P[id].size())
						{
							e++;
							if(e==m){
								sz=1;
								for(int i=0;i<m;i++)
								{
									if(i!=id)sz=sz*P[i].size();
								}
							}
						}
					if(P[id].size()&&e==m)sz=sz*(P[id].size()+1)/P[id].size();
					P[id].push_back(vf);
					if(sz>=Q.k){
					
	
						get_k_CP_set(0);
						
						get_k_CP_route();
						
						findSCmin=1;
						pruning_of_SG();
						Safe_Region_establishment();
						
						break;
					}
		}
		rep(i,0,E[vf.id].size()-1){
			int v = E[vf.id][i].first;
			long long w = E[vf.id][i].second;
			if(dis3[v] > dis3[vf.id] + w)dis3[v] = dis3[vf.id] + w;
			Qu.push(_Poi[v]);
		}
	}
	//cout<<SCmin<<"!"<<endl;	


  
}

void get_SG() {
	string filename = "NY_2000.txt";
    ifstream file(filename);  // ���ļ�

    if (!file) {  // ����ļ���ʧ��
        cerr << "�޷����ļ���" << filename << endl;
        return;  // ���ؿյ�vector��ʾ����
    }
    int ai;
	int tot=0;
    // ���ж�ȡ�ļ�
    while (file >> ai) {
    	bel[++tot]=ai;
    	SG_num=max(SG_num,ai);
    	SG[ai].pb(tot);
    }
    file.close();  // �ر��ļ�
    return;
}
void get_Coordinates() {
    string filename = "USA-road-d.NY.co";
    ifstream file(filename);  // ���ļ�
    if (!file) {  // �ļ���ʧ��
        cerr << "�޷����ļ���" << filename << endl;
        return;
    }

    string line;
    
    // ������ͷ��ע�Ͳ���
    while (getline(file, line)) {
        if (line[0] != 'v') continue;  // ֻ������ 'v' ��ͷ����
        int id, x, y;  // id: �ڵ���, x: ����, y: γ��
        sscanf(line.c_str(), "v %d %d %d", &id, &x, &y);  // ��ȡ��ʽ���Ľڵ�����
        _Poi[id].id=id;
        _Poi[id].x=x;
        _Poi[id].y=y;
    }

    file.close();  // �ر��ļ�
}
void get_Graph() {
	string filename = "USA-road-d.NY.gr";
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
        E[u].pb({v,w});
        E[v].pb({u,w});
        //edges.push_back(make_tuple(u, v, w));  // ���ߴ洢���ߵ��б�
    }

    file.close();  // �ر��ļ�
}
void get_newGraph() {
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
        E[u].pb({v,w});
        E[v].pb({u,w});
        //edges.push_back(make_tuple(u, v, w));  // ���ߴ洢���ߵ��б�
    }

    file.close();  // �ر��ļ�
}
query que;
int lst[N];
void printroad(Poi s,Poi t){
		
		priority_queue<Poi,vector<Poi>,cmp1>Q;
		Q.push(s);if(p==0)init();ddis[s.id]=0;
		vector<int>cg;cg.pb(s.id);
		while(!Q.empty()){
			Poi u=Q.top();
			Q.pop();
			if(Vis[u.id])continue;
			Vis[u.id]=1;
			if(u.id==t.id){int q=t.id;stack<int> st;while(q){st.push(q);q=lst[q];}while(!st.empty()){if(st.top()!=t.id)printf("%d->",st.top());st.pop();}rep(i,0,cg.size()-1)ddis[cg[i]]=1e18,Vis[cg[i]]=0,lst[cg[i]]=0;return;}
			rep(i,0,E[u.id].size()-1)
			{
				Poi v=_Poi[E[u.id][i].first];
				if(ddis[v.id]>ddis[u.id]+E[u.id][i].second){
					cg.pb(v.id);
					ddis[v.id]=ddis[u.id]+E[u.id][i].second;
					Q.push(v);
					lst[v.id]=u.id;
				}
			}
		
}
}
string ss[7]={"0","1","2","3","4","5","6"};//��ǩ
void insert(int id)
{
	if(!que.t_id.count(_Poi[id].key))return;
	total++;
	now_Poi[que.t_id[_Poi[id].key]].insert(id);
	_Poi[id].keyid=que.t_id[_Poi[id].key];
	//if(_Poi[id].id==1107)cout<<"??"<<" "<<_Poi[id].keyid<<" "<<" "<<_Poi[id].r<<endl;
	
} int cnt[7];
void gettype(){
	string filename = "C:\\Users\\hp\\Desktop\\USA-road-t.NY-stripped-1000.poi";
	ifstream file(filename);  // ���ļ�
    if (!file.is_open()) {    // ����ļ��Ƿ�ɹ���
        cerr << "Error: Could not open the file!" << endl;
        return;
    }

    string line;
    vector<int>pp;
    vector<int> thirdColumn, lastColumn;
	//int totrr=0;
    // ���ж�ȡ�ļ�
	int tots=0;
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
		//	if(cnt[values[2]]>12)continue;
		//	cnt[values[2]]++;
			//if(values[1]==1107)cout<<values[2]<<" "<<ss[values[2]]<<endl;
        	pp.pb(values[1]);//����ؼ���� 
        	_Poi[values[1]].key=ss[values[2]];  //�ؼ��� 
        	//if(values[2]==2)totrr++;
            _Poi[values[1]].r=values.back()*1000;    // ���� 
        }
        
    }//cout<<tots<<"!!"<<endl;
    //cout<<totrr<<"!!"<<endl;
    file.close();  // �ر��ļ�
   // srand(time(0));


	vector<string>t;//t.pb("2");
	t.pb("1");t.pb("2");t.pb("5");t.pb("6");
	
	//t.pb("6");

	que=query(t,_Poi[810],1);
	rep(i,0,pp.size()-1){
		insert(pp[i]);
	}
	//rep(i,0,200)insert(i);//����� 
	que.vq=_Poi[810];
}
    // ����������е�ʱ��
    
int main(int argc, char** argv) {
	get_SG();
	get_Coordinates();
	//get_Graph();
	get_newGraph();
	total=0;
	gettype();//cout<<"Sd";
	
	auto start = std::chrono::high_resolution_clock::now();  // ��ʼʱ��

	di_exploration(que);
	
	get_ans(); 
	auto end = std::chrono::high_resolution_clock::now();std::chrono::duration<double, std::milli> duration = end - start;  // ������ȷ������ duration
	total_time += duration.count();
//	cout<<dijkstra(_Poi[810],_Poi[6415])<<" "<<dijkstra(_Poi[6415],_Poi[733])<<endl;
	cout<<total_time<<" "<<tot<<" "<<tott<<" "<<dijkstra(_Poi[800],_Poi[996])<<endl;
	//CP_route h= CP_route({_Poi[921] ,_Poi[932] ,_Poi[1107] ,_Poi[1103] ,_Poi[814] ,_Poi[1115]});
	//h.write();
	return 0;
}
