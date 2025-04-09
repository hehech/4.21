import { createRouter, createWebHistory } from 'vue-router'

// 定义动态导入的路由关系
const routes = [
  {
    path: '/login',
    component: () => import('@/view/login.vue')
  },
  {
    path: '/regist',
    component: () => import('@/view/register.vue')
  },
  {
    path: '/',
    component: () => import('@/view/main.vue')
  },
  {
    path: '/myinfo',
    component: () => import('@/view/myinfo.vue')
  },
  {
    path: '/board',
    component: () => import('@/view/board.vue')
  },
  {
    path: '/post',
    component: () => import('@/view/post.vue')
  }
]

// 创建路由器
const router = createRouter({
  history: createWebHistory(),
  routes: routes
})

// 导出路由
export default router