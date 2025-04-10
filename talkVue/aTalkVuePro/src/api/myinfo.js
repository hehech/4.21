//导入request.js请求工具
import request from '@/utils/request.js'

//查找当前登录用户信息
export const findUser=()=>{
    return request.get('sb/user/currentuser');
}
