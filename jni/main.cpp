#include "dijkstra.h"
#include <cstdlib>
#include <iostream>
#include <fstream>
#include <string>
#include <string.h>
#include <time.h> 
#include "helper.h"
#include "com_home_ui_RobustNativeWrapper.h"

using namespace std;

typedef int vertex_t;
typedef double weight_t;
typedef std::vector<std::vector<neighbor> > adjacency_list_t;
std::vector<std::vector<int> > range;
std::vector< int > linRange;
adjacency_list_t adjacency_list;
adjacency_list_t sim;
std::list<vertex_t> min_path;

int num; 
int n; // Number of nodes
double epsilon = 0.05;

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class Gamma_ {
public:
	Gamma_(double G_ = 1 - epsilon) {G = G_; };
	void set(double G_) { G = G_; };
	double get() { return G; };

	double G;
};

class GammaX_ {
public:
	GammaX_(vector<double> G_ = (vector<double> )(1 - epsilon, 1) ) { G = G_; };
	void set(vector<double> G_) { G = G_; };
	vector<double> get() { return G; };

	vector<double> G;
};

Gamma_ Gamma;
GammaX_ GammaX;

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


string itos(int i) {
	std::string s;
	char a[100];

	itoa(i, a, 10);
	s = (const char*) a;

	return s;
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

void networkGen(int V, double prob, int meanOfCostMean, int meanOfCostRange, int rangeOfCostMean, int rangeOfCostRange ) {
	ofstream f("../network.txt");
    ofstream vNum("../num.txt");
	int stat = 1;

	srand( time(NULL) );

	for (int i = 0; i < V; i++) {
		for(int j = 0; j < V; j++) {
			if( ((double) rand() / (double) RAND_MAX >= prob) && (i != j) ) {
				f << i << ' ' << j << ' ';
				f << (int) ( ( (double)rand() / (double) RAND_MAX - 1/2 ) * (double) rangeOfCostMean ) + meanOfCostMean;
				f << ' ';
				f << (int) ( ( (double)rand() / (double) RAND_MAX - 1/2 ) * (double) rangeOfCostRange ) + meanOfCostRange;
				f << endl;
				stat++;
			}
		}
	}
	vNum << stat << endl << V;
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

void loadGraph( string pathToGraph, string pathToNum ) {
	int k, stat;
	string line;
	string temp;
	int t[4];

	ifstream f(pathToGraph.c_str());
	ifstream vNum(pathToNum.c_str());

	getline(vNum, line);
	temp.clear();
	k = 0;
	while(k < line.length()) {
		temp = temp + line.at(k);
		k++;
	}
	num = atoi(temp.c_str());

	getline(vNum, line);
	temp.clear();
	k = 0;
	while(k < line.length()) {
		temp = temp + line.at(k);
		k++;
	}
	n = atoi(temp.c_str());

	adjacency_list.resize(num);
	range.resize(num);

	if(f.is_open()) {                 //	—читывание графа из файла
		while(f.good()) {
			getline(f, line);
			k = 0;
			stat = 0;

			while (k < line.length()) {
				temp.clear();

				while((k < line.length()) && (line.at(k) != ' ')) {
					temp = temp + line.at(k);
					k++;
				}

				k++;
				t[stat] = atoi(temp.c_str());
				stat++;
			}

			adjacency_list[ t[0] ].push_back(neighbor(t[1], t[2])); 
			range[ t[0] ].push_back(t[3]);
		}
	}

	int l = 0;
	for (int i = 0; i < range.size(); i++) {
		for (int j = 0; j < range.at(i).size(); j++) {
			linRange.push_back(range.at(i).at(j));
			l++;
		}
	}

	num = l;
	f.close();
	vNum.close();
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

void printGraph(adjacency_list_t adjacency_list) {
	for (int i = 0; i < adjacency_list.size(); i++) {
		for (int j = 0; j < adjacency_list.at(i).size(); j++) {
			cout << i << " " << adjacency_list.at(i).at(j).target << " " << adjacency_list.at(i).at(j).weight << " " << range.at(i).at(j) << endl;
		}
	}
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



double pathCost(list<vertex_t> path_list, adjacency_list_t adjacency_list)
{
	int cost=0;
	int j=0;
	vector<vertex_t> path(path_list.begin(),path_list.end());
	for (int i = 1; i < path_list.size(); i++) {
		j = 0;
		while( j < adjacency_list[ path[i-1] ].size() && adjacency_list[ path[i-1] ].at(j).target != path[i] && j < adjacency_list.at(path[i-1]).size())
			j++;
		cost += adjacency_list.at(path[i-1]).at(j).weight;
	}
	
	return cost;
}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

adjacency_list_t simulateCost(adjacency_list_t adjacency_list, vector<std::vector<int> > range, double par = 1) {
	adjacency_list_t sim = adjacency_list;

	srand( time(NULL) );
	for (int i = 0; i < sim.size(); i++)
		for (int j = 0; j < sim.at(i).size(); j++)
			sim.at(i).at(j).weight += (int) ((double) range.at(i).at(j) * (double) rand() / (double) RAND_MAX * par);

	return sim;
}



///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

int findPathDijkstra(int start, int finish, double epsilon ) {
	vector<weight_t> min_distance;
	vector<vertex_t> previous;

	DijkstraComputePaths(start, adjacency_list, min_distance, previous);
	min_path = DijkstraGetShortestPathTo(finish, previous);

	return min_distance[finish];
	
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

int findPathGamma(int start, int finish, double Gamma) {
	adjacency_list_t adjacency_list_temp;
	vector<weight_t> min_distance;
	vector<vertex_t> previous;
	list<vertex_t> path;
	int min_dist = -1;

	int stat = 0;

	adjacency_list_temp = adjacency_list;
	int k = 0;
	for (int l = 0; l < num; l++) {
		k = 0;
		for (int i = 0; i < adjacency_list.size(); i++)
			if(k < l) {
				for (int j = 0; j < adjacency_list.at(i).size(); j++) {
					if(k < l) {
						adjacency_list.at(i).at(j).weight += range.at(i).at(j) - linRange.at(l);
						k++;

						stat++;
					}
					else
						break;
				}
			}
			else
				break;
		DijkstraComputePaths(start, adjacency_list, min_distance, previous);
		path = DijkstraGetShortestPathTo(finish, previous);
		min_distance[finish] += Gamma * linRange.at(l);
		if (-1 == min_dist) {
			min_dist = min_distance[finish];
			min_path = path;
		}
		if (min_dist > min_distance[finish]) {
			min_dist = min_distance[finish];
			min_path = path;
		}
		adjacency_list = adjacency_list_temp;
	}


	adjacency_list_temp.clear();
	min_distance.clear();
	previous.clear();
	path.clear();

	return min_dist;
	
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

int findPathGammaX(int start, int finish, vector<double> Gamma) {
	adjacency_list_t adjacency_list_temp;
	std::vector<weight_t> min_distance;
	std::vector<vertex_t> previous;
	std::list<vertex_t> path;
	int min_dist = -1;
	int k, temp;

	adjacency_list_temp = adjacency_list;
	for (int l = 0; l < num; l++) {
		k = 0;
		temp = 0;
		for (int i = 0; i < adjacency_list.size(); i++)
			if(k < l) {
				for (int j = 0; j < adjacency_list.at(i).size(); j++) {
					adjacency_list.at(i).at(j).weight += Gamma[temp] * linRange.at(l);
					temp++;
					if(k < l) {
						adjacency_list.at(i).at(j).weight += range.at(i).at(j) - linRange.at(l);
						k++;
					}
				}
			}		
			DijkstraComputePaths(start, adjacency_list, min_distance, previous);
			path = DijkstraGetShortestPathTo(finish, previous);
			min_distance[finish] += Gamma[0] * linRange.at(l);
			if (-1 == min_dist) {
				min_dist = min_distance[finish];
				min_path = path;
			}

			if (min_dist > min_distance[finish]) {
				min_dist = min_distance[finish];
				min_path = path;
			}			
			adjacency_list = adjacency_list_temp;		
		}

	adjacency_list_temp.clear();
	min_distance.clear();
	previous.clear();
	path.clear();

		
	return min_dist;
	
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

double findPath(string networkPath, string numPath, int start, int finish, int type, bool show, double epsilon) {
	adjacency_list = adjacency_list_t();
	linRange = std::vector< int > ();
	loadGraph(networkPath, numPath);
	type = 2;

	double ans = 0;

	if (1 == type) {
		Gamma.set( (num + 1) * (1 - epsilon) * 0 );
		ans = findPathGamma(start, finish, Gamma.get());
	}

	if (2 == type) {
	    vector<double> gamma(num + 1, 1 - epsilon);
		GammaX.set(gamma);
		ans = findPathGammaX(start, finish, GammaX.G); 
	}

	if (3 == type) {
		ans = findPathDijkstra(start, finish, epsilon); 
	}

	if(ans >= 0)
	{
		sim = simulateCost(adjacency_list, range, 1 - epsilon);
		ans = pathCost(min_path, sim);
	}
	if (show) {
		cout << "Distance from " << start << " to " << finish << ": " << ans << endl;
		cout << "Path : ";
		copy(min_path.begin(), min_path.end(), ostream_iterator<vertex_t>(cout, " "));
		vector<vertex_t> vc(min_path.begin(), min_path.end());
		vc.push_back(ans);
		for(int i=0;i<vc.size();i++)
		{
			cout << vc.at(i) << " ";
		}
		cout << endl;
	}

	return ans;
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

double getMeanCost(int type, string pathToGraph, string pathToNum, bool show, double epsilon) {
	double mean = 0;
	double temp = 0;
	int stat = 0;

	adjacency_list = adjacency_list_t();
	linRange = std::vector< int > ();
	loadGraph(pathToGraph, pathToNum);
	if (type != 1)
		sim = simulateCost(adjacency_list, range, 1 - epsilon);

	for(int i = 0; i < n; i++)
		for(int j = 0; j < n; j++) {
			if (i != j)
				temp = findPath(pathToGraph, pathToNum, i, j, type, show, epsilon);
			if (show)
				cout << endl;
			if(temp > 0) {
				mean += temp;
				stat++;
			}
		}

	mean /= stat;

	return mean;
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

void test(string newtworkPath, string numPath, double epsilonStart, double epsilonFinish, double epsilonStep, bool show) {	
	ofstream fg("../out_g.txt");
	ofstream fgx("../out_gx.txt");
	loadGraph("../network.txt", "../num.txt");

	for (double i = epsilonStart; i < epsilonFinish; i += epsilonStep) {
		sim = simulateCost(adjacency_list, range, 2 - 2 * i);
		double mean1 = getMeanCost(1, newtworkPath, numPath, show, i);
		double mean2 = getMeanCost(2, newtworkPath, numPath, show, i);
		if (show)
			cout << mean1 << endl << mean2 << endl << endl;
		fg << mean1 << endl;
		fgx << mean2 << endl;
	}

	cout << "FIN";
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


/*int main()
{
	//networkGen(10, 0.7, 100, 15, 15, 30);
	int ans = findPath("../network.txt", "../num.txt", 0, 1, 2, 1, 0.1);
	cout<<ans;
	//test("../network.txt", "../num.txt", 0.1, 0.2, 0.01, true);

	cin.get();

    return 0;
}*/

vector<vertex_t> beginParse(const char * str)
{
	std::string s(str);
	std::vector<std::string> vc = split(s);
	for(int i=0;i<vc.size();i++)
	{
		std::cout << "Input: " << vc.at(i).c_str() << std::endl;
	}
	std::cout << std::endl;
	int ans = findPath(vc.at(0).c_str(), "e:/num.txt", atoi(vc.at(1).c_str()), atoi(vc.at(2).c_str()),
			atoi(vc.at(4).c_str()), 1, atof(vc.at(3).c_str()));
	vector<vertex_t> result(min_path.begin(),min_path.end());
	result.push_back(ans);
	cout<<ans;
	return result;
}

JNIEXPORT jobjectArray JNICALL Java_com_home_ui_RobustNativeWrapper_startWork
  (JNIEnv * env, jclass clazz, jobjectArray vc)
{
	int stringCount = env->GetArrayLength(vc);
	jobjectArray ret;

	char data[80];
	for (int i=0; i<stringCount; i++) {
		jstring string = (jstring) env->GetObjectArrayElement(vc, i);
		const char *rawString = env->GetStringUTFChars(string, 0);
		if(i==0)
		{
			strcpy(data,rawString);
		} else {
			strcat(data,rawString);
		}
		strcat(data," ");
		//beginParse(rawString);
		//env->SetObjectArrayElement(ret,i,env->NewStringUTF(rawString));
	}

	vector<vertex_t> result = beginParse(data);
	char resItem[20];
	cout << "results size: " << result.size() << endl;
	ret= (jobjectArray)env->NewObjectArray(result.size(),env->FindClass("java/lang/String"),
			env->NewStringUTF(""));
	for(int i=0;i<result.size();i++)
	{
		itoa(result.at(i),resItem,10);
		env->SetObjectArrayElement(ret,i,env->NewStringUTF(resItem));
	}
	return (ret);
}
