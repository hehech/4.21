//导入request.js请求工具
import request from '@/utils/request.js'

//文章分类列表查询
export const getAllBoard = () => {
    return request.get('/sb/board');
}

export const getBoardInfoById= (id) =>{
    return request.get('/sb/board/boardinfo?id='+id);
}

export const findinterBoards=()=>{
    return request.get('sb/board/mytopic');
}
export const doFocusBoard= (id) =>{
    return request.post('/sb/board/addfocusboard?id='+id);
}
export const cancelFocusBoard= (id) =>{
    return request.post('/sb/board/cancelfocusboard?id='+id);
}

export const findCurFocusB=()=>{
    return request.get('sb/board/curfocusboard');
}

export const addba =(data)=>{
    return request.post('/sb/board/addba',data);
}

export const findotherfocusBoards=(id)=>{
    return request.get('sb/board/otherfocusboard?id='+id);
}