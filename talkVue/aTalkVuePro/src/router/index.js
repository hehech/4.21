import {createRouter,createWebHistory} from 'vue-router'

//导入组件
import loginVue from '@/view/login.vue'
import registVue from '@/view/register.vue'
import mainVue from '@/view/main.vue'
import myinfoVue from '@/view/myinfo.vue'
import boardVue from '@/view/board.vue'
import postVue from '@/view/post.vue'

//定义路由关系
const routes=[
    {path:'/login',component:loginVue},
    {path:'/regist',component:registVue},
    {path:'/',component:mainVue},
    {path:'/myinfo',component:myinfoVue},
    {path:'/board',component:boardVue},
    {path:'/post',component:postVue}
]

//创建路由器
const router =createRouter({
    history:createWebHistory(),
    routes:routes
})

//导出路由
export default router