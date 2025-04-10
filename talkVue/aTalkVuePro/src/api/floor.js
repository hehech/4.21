//导入request.js请求工具
import request from '@/utils/request.js'

export const findFloorByPostId= (id) =>{
    return request.get('/sb/floor/pfloor?id='+id);
}

export const Addfloor =(data)=>{
    return request.post('/sb/floor/addfloor',data);
}