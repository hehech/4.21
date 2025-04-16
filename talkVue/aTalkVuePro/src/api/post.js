//导入request.js请求工具
import request from '@/utils/request.js'

export const findPost = () => {
    return request.get('/sb/post');
}

export const findPersonalPost = () =>{
    return request.get('/sb/post/perpost');
}

export const findBoardNameById=(id)=>{
    return request.get('/sb/board/boardname?id='+id);
}

export const findBaPost = (id) =>{
    return request.get('/sb/post/bapost?id='+id);
}

export const findPostById= (id) =>{
    return request.get('/sb/post/onepost?id='+id);
}

export const findBaHotPosts= (id) =>{
    return request.get('/sb/post/hotpost?id='+id);
}

export const findPersonalCollectPost = () =>{
    return request.get('/sb/post/percollect');
}