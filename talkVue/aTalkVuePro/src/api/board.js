//导入request.js请求工具
import request from '@/utils/request.js'

//文章分类列表查询
export const getAllBoard = () => {
    return request.get('/sb/board');
}

export const getBoardInfoById= (id) =>{
    return request.get('/sb/board/boardinfo?id='+id);
}