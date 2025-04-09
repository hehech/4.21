<script setup>

import { ref, computed } from 'vue';
import { ArrowDown } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'//导入创建路由器函数
const router = useRouter()
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

//===========================================================================================
//热门吧展示
const items = ref([
  {
    boardId:1,avaterUrl: 'avatar1', name: '抗压背锅', viewCount: 575,
    showUnderline: false
  }
]);
import { getAllBoard } from '@/api/board';
const getAllBoards = async () => {
  const result = await getAllBoard();
  console.log("查询的 result:", result.data);
  items.value = result.data;
};
getAllBoards();
//点击事件
const handleTitleClick_RB = (id) => {
  router.push({
        path:"/board",
        query: {
            boardId: id
        } 
    });
}
//===========================================================================================
//登录信息显示
const userinfo = ref({
  avaterUrl:'',
  nickname: '',
  bio: '',
  vipGrade: '',
  PostCount:1
})
import { findUser,findPerPost } from '@/api/user.js';
const getUserInfo = async () => {
  try {
    // 1. 获取用户基本信息
    const userResult = await findUser();
    // 2. 获取用户帖子数
    const postResult = await findPerPost();
    // 更新用户信息
    userinfo.value = {
      ...userResult.data,
      PostCount: postResult.data || 0
    };
  } catch (error) {
    console.error('获取用户信息失败:', error);
  }
};
getUserInfo();

//=============================================================================================

const TurnToMyinf0 = () => {
  router.push('/myinfo');
}

//====================================================================================================

//热门动态显示
//文章内容
const arts = ref([
  {
    boardId: 1, postId:1,title: '吉大常高鸣', content: '荣获年度TOP1', showUnderline: false,
    boardName: '',
    showUnderline_B: false
  }
]);
import { findPost, findBoardNameById } from '@/api/post';
const getAllPost = async () => {
  try {
    // 1. 获取帖子列表
    const result = await findPost();

    // 2. 并行获取每个帖子的boardName
    const updatedArts = await Promise.all(
      result.data.map(async post => {
        const boardInfo = await findBoardNameById(post.boardId);
        return {
          ...post,
          boardName: boardInfo.data || '未知板块'
        };
      })
    );

    // 3. 更新响应式数据
    arts.value = updatedArts;
  } catch (error) {
    console.error('数据加载失败:', error);
    arts.value = []; // 失败时清空数据
  }
}
getAllPost();
const scrollbar_H = ref(null);
// 计算属性：只显示前 2 个卡片
const visibleArts = computed(() => {
  return arts.value.slice(0, 3);
});
// 加载更多帖子
const loadMore_H = () => {
  // 这里可以加载更多数据或展开所有卡片
  console.log('加载更多帖子');
};
//title点击处理
const handleTitleClick_H = (post) => {
  router.push({
        path:"/post",
        query: {
            boardId: post.boardId,
            postId:post.postId
        } 
    });
}
const handleTitleClick_H_B = (id) => {
  router.push({
        path:"/board",
        query: {
            boardId: id
        } 
    });
}
//===================================================================================================
//个人最新动态
//文章内容
const parts = ref([
  {
    boardId: 1, title: '吉大常高鸣', content: '荣获年度TOP1', showUnderline: false,
    boardName: '',
    showUnderline_B: false
  }
]);
import { findPersonalPost } from '@/api/post';
const getMyPosts = async () => {
  try {
    // 1. 获取帖子列表
    const result = await findPersonalPost();

    // 2. 并行获取每个帖子的boardName
    const updatedArts = await Promise.all(
      result.data.map(async post => {
        const boardInfo = await findBoardNameById(post.boardId);
        return {
          ...post,
          boardName: boardInfo.data || '未知板块'
        };
      })
    );

    // 3. 更新响应式数据
    parts.value = updatedArts;
  } catch (error) {
    console.error('数据加载失败:', error);
    parts.value = []; // 失败时清空数据
  }
}
getMyPosts();

const scrollbar_P = ref(null);
const visibleParts = computed(() => {
  return parts.value.slice(0, 3);
});
const loadMore_P = () => {
  // 这里可以加载更多数据或展开所有卡片
  console.log('加载更多帖子');
};
//title点击处理
const handleTitleClick_P = (post) => {
  router.push({
        path:"/post",
        query: {
            boardId: post.boardId,
            postId:post.postId
        } 
    });
}
const handleTitleClick_P_B = (id) => {
  router.push({
        path:"/board",
        query: {
            boardId: id
        } 
    });
}
//======================================================================================================
const goToMyInfo=()=>{
  router.push("/myinfo");
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
                <el-dropdown-item @click="goToMyInfo">我的主页</el-dropdown-item>
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
                <el-dropdown-item divided>切换账号</el-dropdown-item>
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
              <div style="display: flex; flex-direction: column;">
                <el-avatar :src="userinfo.avaterUrl|| '@/assets/tieba.png'"class="custom-avatar" style="width: 110px; height: 110px;" @click="TurnToMyinf0"></el-avatar>
                <h4 style=" margin: 10px 0 0 0;">{{ userinfo.nickname }}</h4>
              </div>

              <!-- VIP信息区（右侧） -->
              <div style="display: flex; flex-direction: column;margin: 0 0 0 15px;">
                <div style="margin-bottom: 12px;">
                  VIP：{{ userinfo.vipGrade }}
                </div>
                <div style="margin-bottom: 12px;">
                  帖子：{{ userinfo.PostCount }}
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

          </el-card>

          <!-- 最新动态区 -->
          <h3 style="margin: 0 0 15px 0;">
            最新动态
          </h3>
          <div style="margin: 10px 0 0 0;overflow-y: auto;border-radius: 4px;">
            <el-scrollbar ref="scrollbar_P" style="height: 100%;">
              <!-- 遍历 arts 数组，最多显示 2 个卡片 -->
              <div v-for="(art, index) in visibleParts" :key="index">
                <el-card style="background: #f8fafc; margin: 0 0 15px 0;">
                  <el-list>
                    <el-list-item style="display: flex; flex-direction: column;">
                      <span style="margin: 0 0 15px 0;color: #01888D; cursor: pointer; text-decoration: none;"
                        @click="handleTitleClick_P_B(art.boardId)" @mouseenter="art.showUnderline_B = true"
                        @mouseleave="art.showUnderline_B = false"
                        :style="{ textDecoration: art.showUnderline_B ? 'underline' : 'none' }">
                        {{ art.boardName || '匿名用户' }}
                      </span>
                      <span style="color: #001ea9; cursor: pointer; text-decoration: none;" @click="handleTitleClick_P(art)"
                        @mouseenter="art.showUnderline = true" @mouseleave="art.showUnderline = false"
                        :style="{ textDecoration: art.showUnderline ? 'underline' : 'none' }">
                        {{ art.title }}
                      </span>
                    </el-list-item>
                  </el-list>
                </el-card>
              </div>
              <div style="display: flex; justify-content: center; width: 100%;">
                <el-link type="primary" @click="loadMore_H" style="font-size: 14px; cursor: pointer;">
                  更多帖子
                </el-link>
              </div>
            </el-scrollbar>

          </div>
        </el-card>

        <!-- 右侧推荐区 -->
        <el-card style="flex: 3;margin: 0 20px 20px 0;">
          <!-- 热门话题 -->
          <div style="">
            <h3 style="margin: 0 0 5px 0; display: flex; align-items: center;">
              <img src="@/assets/remen.png" alt="view icon" style="width: 18px; height: 18px; margin-right: 4px;">
              热门话题
            </h3>
            <div>
              <el-row>
                <el-col :span="6" v-for="(item, index) in items" :key="index">
                  <div style="display: flex;flex-direction: row;margin: 15px 15px 15px 0;">
                    <img :src="item.avaterUrl|| '@/assets/tieba.png'" alt="icon" class="baIcon">
                    <div style="display: flex;flex-direction: column;margin: 0 0 0 5px;">
                      <div style="display: flex; align-items: center;">
                        <img src="@/assets/tieba.png" alt="view icon"
                          style="width: 16px; height: 16px; margin-right: 4px;">
                        <span style="color: #d82100; cursor: pointer; text-decoration: none;"
                          @click="handleTitleClick_RB(item.boardId)" @mouseenter="item.showUnderline = true"
                          @mouseleave="item.showUnderline = false"
                          :style="{ textDecoration: item.showUnderline ? 'underline' : 'none', fontSize: '18px' }">
                          {{ item.name }}
                        </span>
                      </div>
                      <div style="display: flex; align-items: center;">
                        <img src="@/assets/renqi.png" alt="view icon"
                          style="width: 16px; height: 16px; margin-right: 4px;">
                        <span>{{ item.viewCount }}</span>
                      </div>
                    </div>
                  </div>
                </el-col>
              </el-row>
            </div>
          </div>

          <!-- 热门动态 -->
          <h3 style="margin: 10px 0 15px 0; display: flex; align-items: center;">
              <img src="@/assets/dongtai.png" alt="view icon" style="width: 16px; height: 16px; margin-right: 5px;">
              热门动态
          </h3>

          <div style="height: 400px; position: relative;">
            <!-- 使用 el-scrollbar 包裹卡片列表 -->
            <el-scrollbar ref="scrollbar_H" style="height: 100%;">
              <!-- 遍历 arts 数组，最多显示 2 个卡片 -->
              <div v-for="(art, index) in visibleArts" :key="index">
                <el-card style="background: #f8fafc; margin: 0 0 15px 0;">
                  <el-list>
                    <el-list-item style="display: flex; flex-direction: column;">
                      <span style="margin: 0 0 15px 0;color: #d82100; cursor: pointer; text-decoration: none;"
                        @click="handleTitleClick_H_B(art.boardId)" @mouseenter="art.showUnderline_B = true"
                        @mouseleave="art.showUnderline_B = false"
                        :style="{ textDecoration: art.showUnderline_B ? 'underline' : 'none' }">
                        {{ art.boardName || '匿名用户' }}
                      </span>
                      <span style="color: #001ea9; cursor: pointer; text-decoration: none;" @click="handleTitleClick_H(art)"
                        @mouseenter="art.showUnderline = true" @mouseleave="art.showUnderline = false"
                        :style="{ textDecoration: art.showUnderline ? 'underline' : 'none' }">
                        {{ art.title }}
                      </span>
                      <p>{{ art.content }}</p>
                    </el-list-item>
                  </el-list>
                </el-card>
              </div>
              <div style="display: flex; justify-content: center; width: 100%;">
                <el-link type="primary" @click="loadMore_P" style="font-size: 14px; cursor: pointer;">
                  更多帖子
                </el-link>
              </div>
            </el-scrollbar>
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
  border-radius: 5px;
  overflow: hidden;
  width: 100px;
  height: 100px;
  /* 防止图片超出容器 */
}

.custom-avatar {
  /* 1. 禁用默认圆形裁剪 */
  border-radius: 0 !important;
  overflow: hidden;
  /* 防止图片超出容器 */
}

.custom-avatar::before {
  /* 2. 伪元素占满容器 */
  content: '';
  display: block;
  width: 100%;
  height: 100%;

  /* 3. 图片填充策略：保持宽高比并完整显示 */
  background-size: contain;
  /* 图片等比缩放填满容器 */
  background-repeat: no-repeat;
  background-position: center;
}
</style>