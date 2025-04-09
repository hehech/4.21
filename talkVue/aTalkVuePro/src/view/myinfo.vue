<script setup>
import { ref } from 'vue'
import { onMounted } from 'vue' // 必须添加这行
import { useRouter } from 'vue-router'
import { useRoute } from 'vue-router'  // 必须添加这行
import { ElMessage } from 'element-plus'
const hover = ref(false)
const router = useRouter();
//==================================================================================================================================================
//搜索框变量
const activeTab = ref('posts')
const searchText = ref('')
//=================================================================================================================================================
//显示个人信息
import { findUser, findPerPost } from '@/api/user.js';
const userinfo = ref({
  userId: 11,
  avaterUrl: '',
  nickname: '',
  bio: '',
  vipGrade: '',
  address: '',
  registrationTime: '',
  lastLoginTime: '',
  BaAge: '',
  PostCount: 0
});

const getUserInfo = async () => {
  try {
    // 1. 获取用户基本信息
    const userResult = await findUser();
    // 2. 获取用户帖子数
    const postResult = await findPerPost();
    // 3. 计算注册年限
    const calculateBaAge = (registrationTime) => {
      if (!registrationTime) return 0;

      const regDate = new Date(registrationTime);
      const currentDate = new Date();
      const diffYears = currentDate.getFullYear() - regDate.getFullYear();

      // 考虑月份差异
      if (currentDate.getMonth() < regDate.getMonth() ||
        (currentDate.getMonth() === regDate.getMonth() &&
          currentDate.getDate() < regDate.getDate())) {
        return diffYears - 1;
      }
      return diffYears;
    };

    // 更新用户信息
    userinfo.value = {
      ...userResult.data,
      PostCount: postResult.data || 0,
      BaAge: calculateBaAge(userResult.data.registrationTime)
    };
  } catch (error) {
    console.error('获取用户信息失败:', error);
  }
};

getUserInfo();

//=================================================================================================================================================
//帖子信息展示
import {findPersonalPost} from '@/api/post.js'
const PostInfos = ref([
    {
        postId: 18, likeCount: 55, title: '你好', userId: 22, UserName: '和', showUnderline: false,
        boardId: 1,viewCount:2
    }
]);
const getPersonalPosts = async () => {
    const rs= await findPersonalPost();
    PostInfos.value=rs.data;
};
getPersonalPosts();
const handleTitleClick = (post) => {
    router.push({
        path: "/post",
        query: {
            boardId: post.boardId,
            postId: post.postId
        }  // 传递贴吧名称作为查询参数
    });
}
//==================================================================================================================================================
//帖子信息显示

onMounted(async () => {

})

//================================================================================================================================================

//================================================================================================================================================
//返回按钮功能实现
const goBack = () => {
  router.go(-1); // 返回上一页
};

</script>

<template>
  <el-container style="height: 100vh;">
    <el-header class="basicSS" style="display: flex; justify-content: space-between; align-items: center;">
      <!-- 左侧导航内容 -->
      <div>
        <el-breadcrumb style="font-size: 16px;">
          <el-breadcrumb-item :to="{ path: '/' }" class="breadcrumb-item">首页</el-breadcrumb-item>
          <el-breadcrumb-item :to="{ path: '/myinfo' }" class="breadcrumb-item">个人信息</el-breadcrumb-item>
        </el-breadcrumb>
      </div>

      <!-- 右侧导航组件 -->
      <div>
        <el-button-group>
          <el-button @click="goBack">返回</el-button>
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
                <el-input placeholder="请输入关键词..." clearable style="width: 100%;" />
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

      <!-- 主信息区 -->
      <div style="margin:0 20px 0 20px">
        <!-- 顶部背景区域 -->
        <div style="height: 150px; background: linear-gradient(135deg, #6e8efb, #a777e3); position: relative;">
          <el-button style="position: absolute; left: 240px; bottom: 10px;">编辑资料</el-button>
          <el-avatar class="custom-avatar" :size="170" :src="userinfo.avaterUrl || '@/assets/tieba.png'"
            style="position: absolute; left: 50px; bottom: -120px; border: 4px solid white; box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);"></el-avatar>
        </div>
        <!-- 个人信息介绍区域 -->
        <el-card style="">
          <div style="display: flex;flex-direction: column;  margin:0 0 0 220px">
            <h2 style="margin: 0; font-size: 24px; color: #333;font-size: 30px;">{{ userinfo.nickname }}
            </h2>
            <div style="margin:10px 0 0 0;">
              <el-tag type="info" size="large">用户名：{{ userinfo.nickname }}</el-tag>
              <el-tag style="margin:0 0 0 20px;" type="info" size="large">vip等级：{{ userinfo.vipGrade }}</el-tag>
              <el-tag style="margin:0 0 0 20px;" type="info" size="large">吧龄：{{ userinfo.vipGrade }}年</el-tag>
              <el-tag style="margin:0 0 0 20px;" type="info" size="large">发帖：{{ userinfo.PostCount }}</el-tag>
              <el-tag style="margin:0 0 0 20px;" type="info" size="large">地址：{{ userinfo.address }}</el-tag>
            </div>
            <span style="margin: 10px 0 0 0;">个性签名：{{ userinfo.bio }}</span>
          </div>

        </el-card>

        <!-- 导航部件区域 -->
        <div class="nav-container">
          <el-tabs v-model="activeTab" stretch>
            <el-tab-pane label="我的主页" name="posts"></el-tab-pane>
            <el-tab-pane label="我的收藏" name="images"></el-tab-pane>
            <el-tab-pane label="关注的吧" name="recommend"></el-tab-pane>
            <!-- <el-tab-pane label="我的粉丝" name="games"></el-tab-pane> -->
          </el-tabs>

          <el-input v-model="searchText" placeholder="用户搜索" clearable class="search-input" />
        </div>

        <!-- 主内容区 -->
        <div style="display: flex;">
          <!-- 帖子列表 -->
          <div style="flex: 1; background-color: white; margin-right: 4px; margin-top: 1px;">
            <h3 style="margin: 15px 0 0 30px;">
              最新动态
            </h3>
            <!-- 置顶帖1 -->
            <div style="display: flex; padding: 15px; border-bottom: 1px solid #f0f0f0; background-color: #FFFF;"
              v-for="post in PostInfos" :key="post.postId">
              <!-- 回复数 -->
              <div style="width: 50px; text-align: center; color: #999; font-size: 14px; align-self: center;">
                {{ post.viewCount }}
              </div>

              <div style="flex: 1;">
                <div
                  style="margin: 10px 0 10px 5px; display: flex; justify-content: space-between; align-items: center;">
                  <div>
                    <span style="font-size: 16px; font-weight: 500; cursor: pointer; text-decoration: none;"
                      @click="handleTitleClick(post)" @mouseenter="post.showUnderline = true"
                      @mouseleave="post.showUnderline = false"
                      :style="{ textDecoration: post.showUnderline ? 'underline' : 'none' }">{{
                        post.title }}</span>
                  </div>
                  <span style="font-size: 12px; color: #d82100; position: relative; margin-right: 30px;">
                    <img src="@/assets/zuozhe.png" alt="view icon"
                      style="width: 16px; height: 16px; position: absolute; left: -20px; top: 0;">
                    {{ post.likeCount }}
                  </span>
                </div>
              </div>
            </div>
          </div>

          <!-- 右侧功能区 -->
          <div style="display: flex;flex-direction: column;margin-top: 1px;">
            <div style="width: 280px;margin-bottom: 3px;">
              <div type="primary" style="background-color: #FFFF; padding:5px; font-weight: bold;">
                关注的人</div>
              <div
                style="background-color: white; border-radius: 4px; padding: 15px;display:flex; flex-direction: row;">
                <div style="display: flex; flex-direction: column;">
                  <el-avatar class="custom-avatar" style="width: 110px; height: 110px;">
                  </el-avatar>
                </div>

                <!-- VIP信息区（右侧） -->
                <div style="display: flex; flex-direction: column;margin: 0 0 0 15px;">
                  <div style="margin-bottom: 12px;">
                    用户名
                  </div>
                  <div style="margin-bottom: 12px;">
                    帖子：1234
                  </div>
                  <div>
                    粉丝：567
                  </div>
                </div>
              </div>
            </div>
            <div style="width: 280px;">
              <div type="primary" style="background-color: #FFFF; padding:5px; font-weight: bold;">我的粉丝
              </div>
              <div
                style="background-color: white; border-radius: 4px; padding: 15px;display:flex; flex-direction: column;">
                <div style="display: flex; flex-direction: column;">
                  <el-avatar class="custom-avatar" style="width: 110px; height: 110px;">
                  </el-avatar>
                  <div style="margin: 10px 0 10px 15px;">
                    吧主：
                  </div>
                </div>

                <!-- VIP信息区（右侧） -->
                <div style="display: flex; flex-direction: column;">
                  <div style="margin-bottom: 10px;">
                    帖子：22
                  </div>
                  <div>
                    粉丝：567
                  </div>
                </div>
              </div>
            </div>
          </div>

        </div>
      </div>


    </el-main>
  </el-container>




</template>

<style scoped>
.basicSS {
  background: #ffffff;
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

.nav-container {
  display: flex;
  align-items: center;
  background-color: white;
  padding: 0 20px;
  border-bottom: 1px solid var(--el-border-color-light);
}

:deep(.el-tabs) {
  flex: 1;
}

:deep(.el-tabs__header) {
  margin: 0;
}

:deep(.el-tabs__item) {
  padding: 0 20px;
  height: 50px;
  line-height: 50px;
}

.search-input {
  width: 260px;
  margin-left: 20px;
}

.breadcrumb-item:hover {
  text-decoration: underline;
}
</style>