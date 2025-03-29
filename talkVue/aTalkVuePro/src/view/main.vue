<script setup>

import { ref } from 'vue';
import { ArrowDown } from '@element-plus/icons-vue'
const searchForm = ref({
  query: ''
});

const handleInputFocus = () => {
  // 输入框聚焦时添加动画
};

const handleEnter = () => {
  console.log('进入贴吧', searchForm.value.query);
  // 这里可以添加路由跳转或API调用
};

const handleAllSearch = () => {
  console.log('全吧搜索', searchForm.value.query);
  // 这里可以添加搜索逻辑
};

const options = {
  fpsLimit: 60,
  interactivity: {
    detectsOn: 'canvas',
    modes: { // 配置动画效果
      bubble: {
        distance: 400,
        duration: 2,
        opacity: 0.8,
        size: 40
      },
      push: {
        quantity: 4
      },
      grab: {
        distance: 200,
        duration: 0.4
      },
    }
  },
  particles: {
    color: {
      value: '#01FCCA' // 粒子点的颜色
    },
    links: {
      color: '#03EDF6', // 线条颜色
      distance: 150,//线条距离
      enable: true,
      opacity: 0.5, // 不透明度
      width: 1.2 // 线条宽度
    },
    collisions: {
      enable: true
    },
    move: {
      attract: { enable: false, rotateX: 600, rotateY: 1200 },
      bounce: false,
      direction: 'none',
      enable: true,
      out_mode: 'out',
      random: false,
      speed: 0.5, // 移动速度
      straight: false
    },
    number: {
      density: {
        enable: true,
        value_area: 800
      },
      value: 80//粒子数
    },
    opacity: {//粒子透明度
      value: 0.7
    },
    shape: {//粒子样式
      type: 'triangle'
    },
    size: {//粒子大小
      random: true,
      value: 3
    }
  },
  detectRetina: true
}

//热门吧展示
import avatar1 from '@/assets/login.jpeg'
const items = ref([
  { icon: 'avatar1', name: '抗压背锅', viewCount: 575 },
  { icon: 'avatar1', name: '孙笑川', viewCount: 685 }
]);
import { getAllBoard } from '@/api/board';
const getAllBoards = async () => {
  const result = await getAllBoard();
  items.value = result.data;
};
getAllBoards();


//登录信息显示
const userinfo = ref({
  nickname: '',
  bio: '',
  vipGrade: ''
})
import { findUser } from '@/api/user.js';
const getUserInfo = async () => {
  const result = await findUser();
  userinfo.value = result.data;
}
getUserInfo();

import { useRouter } from 'vue-router'//导入创建路由器函数
const router = useRouter()
const TurnToMyinf0 = () => {
  router.push('/myinfo');
}



</script>


<template>
  <el-container style="height: 100vh;">
    <!-- 背景 -->
    <!-- <Particles id="tsparticles" class="bg1" :options="options" /> -->
    <!-- 头部区域 -->
    <el-header class="basicSS" style="display: flex; justify-content: space-between; align-items: center;">
      <!-- 左侧导航内容 -->
      <div>
        <el-breadcrumb style="font-size: 16px;">
          <el-breadcrumb-item href="#">首页</el-breadcrumb-item>
        </el-breadcrumb>
      </div>

      <!-- 右侧导航组件 -->
      <div>
        <el-button-group>

          <el-dropdown>
            <el-button type="primary" icon="search">我的<el-icon
                class="el-icon--right"><arrow-down /></el-icon></el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item>我的关注</el-dropdown-item>
                <el-dropdown-item>我的贴吧</el-dropdown-item>
                <el-dropdown-item>我的主页</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
          
          <el-dropdown>
            <el-button type="primary" icon="search">会员<el-icon
                class="el-icon--right"><arrow-down /></el-icon></el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item>会员升级</el-dropdown-item>
                <el-dropdown-item>会员权限</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>




          <el-dropdown>
            <el-button type="primary" icon="user">更多<el-icon class="el-icon--right"><arrow-down /></el-icon></el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item>账号设置</el-dropdown-item>
                <el-dropdown-item>问题反馈</el-dropdown-item>
                <el-dropdown-item  divided>切换账号</el-dropdown-item>
                <el-dropdown-item>退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </el-button-group>
      </div>
    </el-header>

    <!-- 主要内容区 -->
    <el-main style="display: flex;flex-direction: column;">
      <!-- 搜索区 -->
      <div class="basicSS" style="display: flex; justify-content: center; margin: 0 20px 20px 20px;">
        <!-- 品牌标识区 -->
        <div style="display: flex;">
          <span>
            <span style="font-size: 24px; color: #FF3B30; font-weight: bold;">科技</span>
            <span style="font-size: 20px; color: #1E90FF; vertical-align: sub;">贴吧</span>
          </span>
        </div>

        <!-- 搜索主体区-->
        <div style="display: flex;">
          <el-form>
            <el-row :gutter="10" style="display: flex;">
              <!-- 搜索框列（响应式宽度） -->
              <el-col :xs="22" :sm="18" :md="16">
                <el-input placeholder="请输入关键词..." clearable style="width: 100%;" @focus="handleInputFocus" />
              </el-col>

              <!-- 操作按钮列（固定宽度） -->
              <el-col :xs="4" :sm="6" :md="8" style="display: flex; flex-direction: row;">
                <el-button type="primary" size-type="medium" style="width: 100%;">
                  进入贴吧
                </el-button>
                <el-button type="success" size-type="medium" style="width: 100%;">
                  全吧搜索
                </el-button>
              </el-col>
            </el-row>
          </el-form>
        </div>
      </div>

      <!-- 下侧区域 -->
      <div style="display: flex">
        <!-- 左侧内容区 -->
        <el-card style="flex: 0.8;margin: 0 20px 20px 20px;">
          <!-- 个人信息展示区 -->
          <el-card :data="userinfo" class="basicSS" style="display: flex; flex-direction: column;margin: 0 0 15px 0;">

            <div style="display: flex;">
              <!-- 用户头像区（左侧） -->
              <div style="display: flex; flex-direction: column;" @click="TurnToMyinf0">
                <el-avatar class="custom-avatar" style="width: 90px; height: 90px;"></el-avatar>
                <h4 style=" margin: 10px 0 0 0;">{{ userinfo.nickname }}</h4>
              </div>

              <!-- VIP信息区（右侧） -->
              <div style="display: flex; flex-direction: column;margin: 0 0 0 15px;">
                <div style="margin-bottom: 12px;">
                  VIP：{{ userinfo.vipGrade }}
                </div>
                <div style="margin-bottom: 12px;">
                  帖子：1234
                </div>
                <div>
                  粉丝：567
                </div>
              </div>
            </div>
            <div class="profile-content" style="width: 100%;">
              <p>{{ userinfo.bio }}
              </p>
            </div>
            <!-- 优化后的功能按钮组
            <el-button-group style="display: flex;">
              左侧按钮：占满50%宽度并左对齐 -->
            <!-- <el-button> -->
            <!-- 编辑资料 -->
            <!-- </el-button> -->

            <!-- 右侧按钮：自动填充剩余空间 -->
            <!-- <el-button> -->
            <!-- 退出登录 -->
            <!-- </el-button> -->
            <!-- </el-button-group> -->
          </el-card>

          <!-- 最新动态区 -->
          <h3 style="margin: 0 0 15px 0;">
            最新动态
          </h3>
          <div style="margin: 10px 0 0 0;height: 200px;overflow-y: auto;border-radius: 4px;">
            <div class="post-card" style="margin:0 0 15px 0;">
              <h4 style="margin: 0 0 12px 0;">
                post信息
              </h4>
              <!-- 帖子信息 -->
              <div style="display: flex; justify-content: space-between; 
                   color: #999; font-size: 12px;">
                <span style="color: #ff4d4f; font-weight: bold;">
                  post.hot
                </span>
                <span>
                  post.time
                </span>
              </div>
            </div>
            <div class="post-card" style="margin:0 0 15px 0;">
              <h4 style="margin: 0 0 12px 0;">
                post信息
              </h4>
              <!-- 帖子信息 -->
              <div style="display: flex; justify-content: space-between; 
                   color: #999; font-size: 12px;">
                <span style="color: #ff4d4f; font-weight: bold;">
                  post.hot
                </span>
                <span>
                  post.time
                </span>
              </div>
            </div>

          </div>
        </el-card>

        <!-- 右侧推荐区 -->
        <el-card style="flex: 3;margin: 0 20px 20px 0;">
          <!-- 热门话题 -->
          <div style="">
            <h3 style="margin: 0 0 15px 0;">热门话题</h3>
            <div>
              <el-row>
                <el-col :span="4" v-for="(item, index) in items" :key="index">
                  <div style="display: flex;flex-direction: row;margin: 15px 15px 15px 0;">
                    <img :src="avatar1" alt="icon" class="baIcon">
                    <div style="display: flex;flex-direction: column;margin: 0 0 0 5px;">
                      <div>{{ item.name }}</div>
                      <div>{{ item.viewCount }}</div>
                    </div>
                  </div>
                </el-col>
              </el-row>
            </div>
          </div>

          <!-- 热门动态 -->
          <h3 style="color: #333;">热门动态</h3>
          <div style="box-shadow: 0 2px 10px rgba(0,0,0,0.1);padding:20px;height: 200px;
        overflow-y: auto;
        border-radius: 4px;">

            <el-card style="background: #f8fafc;margin: 0 0 15px 0;">
              <el-list>
                <el-list-item style="display: flex; flex-direction: column;">
                  <span style="margin: 0 0 15px 0;">发布人名称</span>
                  <span style="color: #ff4d4f;">文章标题</span>
                </el-list-item>
              </el-list>
            </el-card>

            <el-card style="background: #f8fafc;">
              <el-list>
                <el-list-item style="display: flex; flex-direction: column;">
                  <span style="margin: 0 0 15px 0;">发布人名称</span>
                  <span style="color: #ff4d4f;">文章标题</span>
                </el-list-item>
              </el-list>
            </el-card>
          </div>

        </el-card>

      </div>

    </el-main>

    <!-- 底部功能区 -->
    <el-footer style="background: #fff; box-shadow: 0 -2px 10px rgba(0,0,0,0.1);">

    </el-footer>
  </el-container>
</template>



<style scoped>
.profile-content p {
  font-size: 13px;
  /* 调整字体大小 */
  color: #333;
}

.icon {
  width: 50px;
  height: 50px;
}





.basicSS {
  background: #ffffff;
}



/* 动态卡片样式优化 */
.post-card {
  background: #ffffff;
  border: 1px solid #eee;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.post-card h4 {
  color: #2a2a2a;
  font-weight: 600;
  margin-bottom: 15px;
}

.baIcon {
  /* 1. 禁用默认圆形裁剪 */
  border-radius: 0 !important;
  overflow: hidden;
  width: 80px;
  height: 80px;
  /* 防止图片超出容器 */
}

.custom-avatar {
  /* 1. 禁用默认圆形裁剪 */
  border-radius: 0 !important;
  overflow: hidden;
  width: 90px;
  height: 90px;
  /* 防止图片超出容器 */
}

.custom-avatar::before {
  /* 2. 伪元素占满容器 */
  content: '';
  display: block;
  width: 100%;
  height: 100%;

  /* 3. 图片填充策略：保持宽高比并完整显示 */
  background-image: url('@/assets/bg1.jpg');
  background-size: contain;
  /* 图片等比缩放填满容器 */
  background-repeat: no-repeat;
  background-position: center;
}
</style>