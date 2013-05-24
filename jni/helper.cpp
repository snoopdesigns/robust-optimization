#include <iostream>
#include <string>
#include <vector>
#include <algorithm>

inline bool space(char c){
    return std::isspace(c);
}

inline bool notspace(char c){
    return !std::isspace(c);
}

std::vector<std::string> split(const std::string& s){
    typedef std::string::const_iterator iter;
    std::vector<std::string> ret;
    iter i = s.begin();
    while(i!=s.end()){
        i = std::find_if(i, s.end(), notspace);
        iter j= std::find_if(i, s.end(), space);
        if(i!=s.end()){
            ret.push_back(std::string(i, j));
            i = j;
        }
    }
    return ret;
}
