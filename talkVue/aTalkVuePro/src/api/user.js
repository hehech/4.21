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

//手机验证码登录
export const sendMessage=(phoneNumber)=>{
    const params=new URLSearchParams();
    params.append('phoneNumber', phoneNumber)
    return request.post('/sb/user/sendMsg',params);
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
export const findinterusers=()=>{
    return request.get('sb/user/interusers');
}
export const findfans=()=>{
    return request.get('sb/user/fans');
}
export const loginByPhoneApi=(loginData)=>{
    const params=new URLSearchParams();
    for(let key in loginData){
        params.append(key,loginData[key]);
    }
    return request.post('/sb/user/loginbyphone',params);
}
export const doFocusUser= (id) =>{
    return request.post('/sb/user/addfocususer?id='+id);
}
export const cancelFocusUser= (id) =>{
    return request.post('/sb/user/cancelfocususer?id='+id);
}
export const findCurFocusU=()=>{
    return request.get('sb/user/curfocususer');
}
export const findotherpost=(id)=>{
    return request.get('sb/user/otherpost?id='+id);
}

export const findotherPostinfo= (id) =>{
    return request.get('/sb/user/otherpostinfo?id='+id);
}

export const findotherfocususers=(id)=>{
    return request.get('sb/user/otherfocususers?id='+id);
}

export const findotherfans=(id)=>{
    return request.get('sb/user/otherfans?id='+id);
}