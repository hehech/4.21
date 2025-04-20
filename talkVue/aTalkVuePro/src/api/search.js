//导入request.js请求工具
import request from '@/utils/request.js'

export const SearchUsers=(key)=>{
    return request.get('sb/search/searchuser?key='+key);
}
export const searchBoards=(key)=>{
    return request.get('sb/search/searchboard?key='+key);
}
export const searchpost=(key)=>{
    return request.get('sb/search/searchpost?key='+key);
}