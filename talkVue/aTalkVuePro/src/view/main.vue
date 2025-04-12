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
    boardId: 1, avaterUrl: 'avatar1', name: '抗压背锅', viewCount: 575,
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
    path: "/board",
    query: {
      boardId: id
    }
  });
}
//===========================================================================================
//登录信息显示
const userinfo = ref({
  avaterUrl: '',
  nickname: '',
  bio: '',
  vipGrade: '',
  PostCount: 1
})
import { findUser, findPerPost } from '@/api/user.js';
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
    boardId: 1, postId: 1, title: '吉大常高鸣', content: '荣获年度TOP1', showUnderline: false,
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
    path: "/post",
    query: {
      boardId: post.boardId,
      postId: post.postId
    }
  });
}
const handleTitleClick_H_B = (id) => {
  router.push({
    path: "/board",
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
    path: "/post",
    query: {
      boardId: post.boardId,
      postId: post.postId
    }
  });
}
const handleTitleClick_P_B = (id) => {
  router.push({
    path: "/board",
    query: {
      boardId: id
    }
  });
}
//======================================================================================================
const goToMyInfo = () => {
  router.push("/myinfo");
}
//======================================================================================================================================================
//热议信息显示
const HotPostInfos = ref([
  {
    postId: 18, title: '你好', viewCount: 1,
    showUnderline_hot: false, boardId: 2
  }
]);
const getHotPosts = async () => {
  try {
    const result = await findPost();
    if (result.data) {
      console.log("API返回数据:", result.data); // 调试输出数据结构
      HotPostInfos.value = result.data;
      console.log("改变后返回数据:", HotPostInfos.value); // 调试输出数据结构
    } else {
      ElMessage.error("数据加载失败");
    }
  } catch (error) {
    ElMessage.error("请求失败");
  }
}
getHotPosts();
const hotpostclick = (post) => {
  router.push({
    path: "/post",
    query: {
      boardId: post.boardId,
      postId: post.postId
    }  // 传递贴吧名称作为查询参数
  });
}






</script>


<template>
  <el-container class="toubu"style="height: 100vh;">
    <!-- 头部区域 -->
    <el-header
      style="display: flex; justify-content: space-between; align-items: center;">
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
            <el-button class="anniu-wode" type="primary">我的<el-icon><arrow-down /></el-icon></el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item>我的关注</el-dropdown-item>
                <el-dropdown-item divided>我的贴吧</el-dropdown-item>
                <el-dropdown-item divided @click="goToMyInfo">我的主页</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>


          <el-dropdown>
            <el-button class="anniu-wode" type="primary">更多<el-icon><arrow-down /></el-icon></el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item>账号设置</el-dropdown-item>
                <el-dropdown-item divided>问题反馈</el-dropdown-item>
                <el-dropdown-item divided>切换账号</el-dropdown-item>
                <el-dropdown-item divided>退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </el-button-group>
      </div>
    </el-header>

    <!-- 主要内容区 -->
    <el-main 
      style="display: flex;flex-direction: column;">
      <!-- 搜索区 -->
      <div class="Sousuo"
        style="display: flex; justify-content: center; margin: 0 20px 20px 20px;">
        <!-- 品牌标识区 -->
        <div style="display: flex;margin-top:">
          <span>
            <span style="font-size: 24px; color: #ff0041; font-weight: bold;">Talkto</span>
            <span style="font-size: 20px; color: #ffc700; vertical-align: sub;">World</span>
          </span>
        </div>

        <!-- 搜索主体区-->
        <div style="display: flex;">
          <el-form>
            <el-row style="display: flex;">
              <!-- 搜索框列（响应式宽度） -->
              <el-col :xs="20" :sm="18" :md="16">
                <input class="shurukuang" placeholder="请输入关键词..." @focus="handleInputFocus" />
              </el-col>

              <!-- 操作按钮列（固定宽度） -->
              <el-col :xs="4" :sm="6" :md="8" style="display: flex; flex-direction: row;margin-left: -45px;">
                <el-button class="anniu-sousuo"size-type="medium" style="width: 100%;">
                  进入贴吧
                </el-button>
                <el-button class="anniu-sousuo"size-type="medium" style="width: 100%;">
                  全吧搜索
                </el-button>
              </el-col>
            </el-row>
          </el-form>
        </div>
      </div>

      <!-- 下侧区域 -->
      <div style="display: flex;">
        <!-- 左侧内容区 -->
        <el-card class="gerenxinxi"style="flex: 0.21;margin: 0 20px 20px 20px;">
          <!-- 个人信息展示区 -->
          <el-card class="touxiang" :data="userinfo" style="display: flex; flex-direction: column;margin: 0 0 15px 0;">

            <div style="display: flex;">
              <!-- 用户头像区（左侧） -->
              <div style="display: flex; flex-direction: column;">
                <el-avatar :src="userinfo.avaterUrl || '@/assets/tieba.png'" class="custom-avatar"
                  style="width: 95px; height: 95px;" @click="TurnToMyinf0"></el-avatar>
                <h4 style="color:#013a3c;margin: 10px 0 0 0;">{{ userinfo.nickname }}</h4>
              </div>

              <!-- VIP信息区（右侧） -->
              <div style="display: flex; flex-direction: column;margin: 0 0 0 15px;">
                <div style="color:#013a3c;margin-bottom: 12px;">
                  VIP：{{ userinfo.vipGrade }}
                </div>
                <div style="color:#013a3c;margin-bottom: 12px;">
                  帖子：{{ userinfo.PostCount }}
                </div>
                <div style="color:#013a3c;">
                  粉丝：567
                </div>
              </div>
            </div>
            <div class="jianjieziti" style="width: 100%;">
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
                <el-card  class="zuixinka"style="margin: 0 0 15px 0;">
                  <el-list>
                    <el-list-item style="display: flex; flex-direction: column;">
                      <span style="margin: 0 0 15px 0;color: #01888D; cursor: pointer; text-decoration: none;"
                        @click="handleTitleClick_P_B(art.boardId)" @mouseenter="art.showUnderline_B = true"
                        @mouseleave="art.showUnderline_B = false"
                        :style="{ textDecoration: art.showUnderline_B ? 'underline' : 'none' }">
                        {{ art.boardName || '匿名用户' }}
                      </span>
                      <span style="color: #001ea9; cursor: pointer; text-decoration: none;"
                        @click="handleTitleClick_P(art)" @mouseenter="art.showUnderline = true"
                        @mouseleave="art.showUnderline = false"
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

        <!-- 中间推荐区 -->
        <div class="zhongjiantuijian"style="flex:0.57;margin: 0 20px 20px 0;padding: 20px;">
          <!-- 热门话题 -->
          <div style="margin-left: 6px;">
            <h3 style="margin: 0 0 5px 0; display: flex; align-items: center;">
              <img src="@/assets/remen.png" alt="view icon" style="width: 18px; height: 18px; margin-right: 4px;">
              热门话题
            </h3>
            <div>
              <el-row>
                <el-col :span="6" v-for="(item, index) in items" :key="index">
                  <div style="display: flex;flex-direction: row;margin: 15px 15px 15px 0;">
                    <img :src="item.avaterUrl || '@/assets/tieba.png'" alt="icon" class="baIcon">

                    <div style="display: flex;flex-direction: column;margin: 0 0 0 5px;">
                      <div style="display: flex; align-items: center;">
                        <img src="@/assets/tieba.png" alt="view icon"
                          style="width: 16px; height: 16px; margin-right: 4px;">
                        <span style="color:#001164; cursor: pointer; text-decoration: none;"
                          @click="handleTitleClick_RB(item.boardId)" @mouseenter="item.showUnderline = true"
                          @mouseleave="item.showUnderline = false"
                          :style="{ fontSize: '16px', textDecoration: item.showUnderline ? 'underline' : 'none' }">
                          {{ item.name }}
                        </span>
                      </div>
                      <div style="display: flex; align-items: center;">
                        <img src="@/assets/renqi.png" alt="view icon"
                          style="width: 16px; height: 16px; margin-right: 4px;">
                        <span style="font-size: 12px;">{{ item.viewCount }}</span>
                      </div>
                    </div>
                  </div>
                </el-col>
              </el-row>
            </div>
          </div>

          <!-- 个性动态 -->
          <h3 style="margin: 10px 0 15px 6px; display: flex; align-items: center;">
            <img src="@/assets/dongtai.png" alt="view icon" style="width: 16px; height: 16px; margin-right: 5px;">
            个性动态
          </h3>
          <!-- 个性动态部分 -->
          <div style="height: 400px; position: relative;margin-left: 6px;">
            <!-- 使用 el-scrollbar 包裹卡片列表 -->
            <el-scrollbar ref="scrollbar_H" style="height: 100%;">
              <!-- 遍历 arts 数组，最多显示 2 个卡片 -->
              <div v-for="(art, index) in visibleArts" :key="index">
                <el-card class="gexign"style="margin: 0 0 15px 0;">
                  <el-list>
                    <el-list-item style="display: flex; flex-direction: column;">
                      <span style="margin: 0 0 15px 0;color: #580099; cursor: pointer; text-decoration: none;"
                        @click="handleTitleClick_H_B(art.boardId)" @mouseenter="art.showUnderline_B = true"
                        @mouseleave="art.showUnderline_B = false"
                        :style="{ textDecoration: art.showUnderline_B ? 'underline' : 'none' }">
                        {{ art.boardName || '匿名用户' }}
                      </span>
                      <span style="color: #001ea9; cursor: pointer; text-decoration: none;"
                        @click="handleTitleClick_H(art)" @mouseenter="art.showUnderline = true"
                        @mouseleave="art.showUnderline = false"
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
        </div>

        <!-- 热议榜区 -->
        <div class="reyi"style="flex:0.22;box-shadow: 0 1px 3px rgba(0,0,0,0.1);margin: 0 20px 20px 0;padding:20px">
          <h2
            style="font-size: 18px; color: #333; margin: 0 0 10px 0; padding-bottom: 10px;">
            热议榜</h2>

          <ol style="list-style: none; padding: 0; margin: 0;">
            <li
              style="display: flex; align-items: center; justify-content: space-between; padding: 10px 0; border-bottom: 1px solid #f5f5f5;"
              v-for="post in HotPostInfos" :key="post.postId">
              <div style="display: flex; align-items: center;">
                <span
                  style="font-size: 16px; font-weight: bold; color: #f85959; width: 20px; text-align: center; ">1</span>
                <span style="font-size: 15px;color: #333;margin-left: 4px;cursor: pointer;text-decoration: none;
                             display: inline-block;max-width: 220px;white-space: nowrap;overflow: hidden;
                             text-overflow: ellipsis;" @mouseenter="post.showUnderline_hot = true"
                  @mouseleave="post.showUnderline_hot = false"
                  :style="{ textDecoration: post.showUnderline_hot ? 'underline' : 'none' }"
                  @click="hotpostclick(post)">
                  {{ post.title }}
                </span>
              </div>
              <span style="font-size: 13px; color: #999;">{{ post.viewCount }}</span>
            </li>

          </ol>
        </div>
      </div>

    </el-main>

  </el-container>
</template>



<style scoped>
.Sousuo {
  padding: 10px;
  border: 2px solid #018e93;
  /* 绿色边框 */
  border-radius: 12px;
  /* 圆角 */
  box-shadow: 0 4px 8px rgba(2, 133, 156, 0.2);
  /* 阴影 */
}

:deep(.el-breadcrumb__item) {
  font-size: 18px;
}

:deep(.el-breadcrumb__inner) {
  color: #0881fb;
  transition: all 0.3s;
}

:deep(.el-breadcrumb__item:hover .el-breadcrumb__inner) {
  color: #adfcad;
}

.anniu-wode {
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
  padding: 12px 24px !important;

  background: linear-gradient(135deg, #84e2f5 0%, #8fd3f4 100%) !important;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2) !important;
}

.anniu-wode:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(64, 158, 255, 0.4) !important;
  transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
}

/* 下拉菜单优化 */
:deep(.el-dropdown-menu) {
  background: #06eab8 !important;
  border: 1px solid #016f22;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

:deep(.el-dropdown-menu__item) {
  color: #0080cf;
  font-weight: 500;

  &:hover {
    background: #c0fef9 !important;
  }
}

/* 搜索框优化 */
/* 同时修改外层容器和内部输入框 */
.shurukuang {
  margin:0 0 0 8px;
  background:linear-gradient(120deg, #99f5ba 0%, #9cd7f5 100%);
  border: 1px solid #018e93;
  border-radius: 8px;
  padding: 8px;
}


/* 搜索框右侧按钮 */
.anniu-sousuo {
  color: #00ae97 ;
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
  padding: 12px 24px !important;

  background:linear-gradient(120deg, #90edd7 0%, #a4e2e9 100%);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2) !important;
}

.anniu-sousuo:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(64, 158, 255, 0.4) !important;
  transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
}

/* 个人信息区 */
.gerenxinxi{
  background-image: linear-gradient(120deg, #84fab0 0%, #76d7bc 100%);
  border: 2px solid #018e93;
  border-radius: 8px;
}

/* 头像区*/
.touxiang{
  background-image: linear-gradient(120deg, #b6ffd1 0%, #7ae1a7 100%);
  border: 1px solid #018e93;
  border-radius: 8px;
}


.jianjieziti{
  font-size: 13px;
  /* 调整字体大小 */
  color: #013e39;
}

.zuixinka{
  background-image: linear-gradient(120deg, #b6ffd1 0%, #7ae1a7 100%);
  border: 1px solid #018e93;
  border-radius: 8px;
}

.zhongjiantuijian{
  /* background-image: linear-gradient(120deg, #8bebc3 0%, #6fd6ed 100%); */
  border: 1px solid #018e93;
  border-radius: 8px;
}

.toubu{
  background-image: linear-gradient(120deg, #83f9ae 0%, #6fc3ed 100%);
    /* 自定义滚动条样式 */
  scrollbar-width: thin;
  scrollbar-color: #6fd6ed #6fd6ed;
}

.gexign{
  background-image: linear-gradient(120deg, #8bebc3 0%, #6fd6ed 100%);
  border: 1px solid #018e93;
  border-radius: 8px;
}
.reyi{
  border: 1px solid #018e93;
  border-radius: 8px;
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
  width: 70px;
  height: 70px;
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