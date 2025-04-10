//导入request.js请求工具
import request from '@/utils/request.js'

export const findCommentsByFloorId = (id) => {
    return request.get('/sb/comment/onefloor?id='+id);
}

export const Addcomment =(data)=>{
    return request.post('/sb/comment/addcomt',data);
}