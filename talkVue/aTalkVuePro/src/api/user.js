//导入request.js请求工具
import request from '@/utils/request.js'

//调用登录接口
export const userLoginService=(loginData)=>{
    const params=new URLSearchParams();
    for(let key in loginData){
        params.append(key,loginData[key]);
    }
    return request.post('/sb/user/login',params)
}

//提供调用注册接口的函数
export const userRegisterService =(registerData)=>{
    //借助于UrlSearchParams完成传递
    return request.post('/sb/user/regist',registerData);
}

//查找当前登录用户信息
export const findUser=()=>{
    return request.get('sb/user/currentuser');
}

export const findUserinfoByUserId= (id) =>{
    return request.get('/sb/user/userinfo?id='+id);
}
export const findPerPost=()=>{
    return request.get('sb/user/perpostcount');
}