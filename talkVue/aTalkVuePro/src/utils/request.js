//定制请求的实例

//导入axios  npm install axios
import axios from 'axios';
import { ElMessage } from 'element-plus';
import router from '@/router'
import { useTokenStore } from '@/stores/token.js';
//定义一个变量,记录公共的前缀  ,  baseURL

const baseURL = '';
const instance = axios.create({baseURL})

// 添加请求拦截器
instance.interceptors.request.use(
    (config)=>{
        //请求前的回调
        //添加token
        const tokenStore=useTokenStore()
        //判断有没有token
        if(tokenStore.token){
            config.headers.Authorization=tokenStore.token
        }
        return config;
    },
    (err)=>{
        //请求错误的回调
        ElMessage.error('请求发生错误')
        Promise.reject(err)
    }

)

// 添加响应拦截器

instance.interceptors.response.use(
    result=>{
        //判断业务状态码
        if(result.data.code===0){
            return result.data;
        }
        //操作失败
        //alert(result.data.msg?result.data.msg:'服务异常')
        ElMessage.error(result.data);
        //异步操作的状态转换为失败
        return Promise.reject(result.data)
    },
    err=>{
        //判断响应状态码，如果为401，则证明未登录，提示登录，并跳转登陆页面
        if(err.response.status === 401){
            ElMessage.error('请先登录')
            router.push('/login')
        }else{
            ElMessage.error('服务异常');
        }
        return Promise.reject(err);
    }
)

export default instance;