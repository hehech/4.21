<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue';
import { useRouter } from 'vue-router'
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
import { findPersonalPost } from '@/api/post.js'
const PostInfos = ref([
  {
    postId: 18, likeCount: 55, title: '你好', userId: 22, UserName: '和', showUnderline: false,
    boardId: 1, viewCount: 2
  }
]);
const getPersonalPosts = async () => {
  const rs = await findPersonalPost();
  PostInfos.value = rs.data;
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

//===================================================================================================================================
const headerVisible = ref(false);

// 滚动监听逻辑
const handleScroll = () => {
  const scrollTop = window.pageYOffset || document.documentElement.scrollTop;
  headerVisible.value = scrollTop > 100;
};

onMounted(() => {
  window.addEventListener('scroll', handleScroll);
});

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll);
});
</script>

<template>
  <el-container class="suoyou" shallow="never" style="display: flex;flex-direction: column;">
    <el-header class="toubu" style="display: flex; justify-content: space-between; align-items: center;" :style="{
      position: 'fixed',
      top: 0,
      width: '100%',
      zIndex: 1000,
      transform: headerVisible ? 'translateY(0)' : 'translateY(-100%)',
      transition: 'transform 0.3s ease-in-out',
      backgroundColor: 'rgba(255, 255, 255, 0.9)',
      backdropFilter: 'blur(10px)'
    }">
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
          <el-button class="anniu-wode" @click="goBack">返回</el-button>
        </el-button-group>
      </div>
    </el-header>


    <!-- 搜索区 -->
    <div
      style="position: relative; height: 130px; display: flex; flex-direction: column; justify-content: center; align-items: center;">
      <!-- 视频背景 -->
      <video autoplay muted loop playsinline
        style="position: absolute;top: 0;left: 0;  width: 100% !important;height: 100% !important;object-fit: cover;z-index: 1;">
        <source src="../assets/bbg1.mp4" type="video/mp4">
      </video>



      <!-- 搜索主体区 -->
      <div style="width: 100%; max-width: 700px;padding: 10px;z-index: 1;">
        <el-form>
          <el-row :span="5" style="display: flex; align-items: center;">
            <!-- 输入框区域 -->
            <!-- 内容容器 - 添加半透明背景确保文字可读性 -->
            <div style="padding: 20px;margin-bottom: 15px;z-index: 1;">
              <link
                href="https://fonts.googleapis.com/css2?family=Dancing+Script:wght@700&family=Kalam:wght@700&display=swap"
                rel="stylesheet">

              <span style="font-family: 'Dancing Script', cursive; 
       font-size: 31px; 
       font-weight: bold; 
       color: white;
       text-shadow: 0 0 8px rgba(255,255,255,0.7), 
                   0 0 15px rgba(255,255,255,0.4);
       position: relative;
       display: inline-block;
       line-height: 1;">
                Talkto<span style="font-family: 'Kalam', cursive; 
                font-size: 0.6em; 
                vertical-align: sub;
                text-shadow: 0 0 5px rgba(255,255,255,0.7);">
                  World
                </span>
                <!-- 增强光效的装饰线 -->
                <span style="position: absolute; 
              bottom: -5px; 
              left: 0; 
              width: 100%; 
              height: 1px; 
              background: rgba(255,255,255,0.3);
              box-shadow: 0 0 10px 2px rgba(255,255,255,0.4);">
                </span>
              </span>
            </div>
            <el-col :span="14" style="position: relative;">
              <input class="weibo-search-input" placeholder="大家都在搜：今日热门话题" />
            </el-col>

            <!-- 搜索按钮 -->
            <el-col :span="4">
              <el-button class="weibo-search-btn" type="danger">搜索</el-button>
            </el-col>
          </el-row>
        </el-form>
      </div>
    </div>


    <!-- 下侧内容区 -->
    <div style="display: flex; justify-content: center; width: 100%;margin:5px 0 0 0">
      <!-- 主信息区 -->
      <div class="kapian" style="max-width: 1200px;width: 100%;">
        <!-- 顶部背景区域 -->
        <div style="height: 130px;background: linear-gradient(60deg, #64b3f4 0%, #c2e59c 100%); position: relative;">
          <el-button class="anniu-wode"
            style="position: absolute; left: 230px; bottom: 10px;font-size: 14px;">编辑资料</el-button>
          <el-avatar class="custom-avatar" :size="150" :src="userinfo.avaterUrl || '@/assets/tieba.png'"
            style="position: absolute; left: 50px; bottom: -110px; border: 4px solid white;"></el-avatar>
        </div>
        <!-- 个人信息介绍区域 -->
        <div shallow="never" style="padding:10px">
          <div style="display: flex;flex-direction: column;  margin:0 0 0 210px">
            <h2 style="margin: 0; color: #333;font-size: 25px;">{{ userinfo.nickname }}
            </h2>
            <div style="margin:10px 0 0 0;">
              <el-tag type="info" size="large">用户名：{{ userinfo.nickname }}</el-tag>
              <el-tag style="margin:0 0 0 20px;" type="info" size="large">vip等级：{{ userinfo.vipGrade }}</el-tag>
              <el-tag style="margin:0 0 0 20px;" type="info" size="large">吧龄：{{ userinfo.vipGrade }}年</el-tag>
              <el-tag style="margin:0 0 0 20px;" type="info" size="large">发帖：{{ userinfo.PostCount }}</el-tag>
              <el-tag style="margin:0 0 0 20px;" type="info" size="large">地址：{{ userinfo.address }}</el-tag>
            </div>
            <span style="margin: 10px 0 0 0;font-size: 14px;">个性签名：{{ userinfo.bio }}</span>
          </div>

        </div>

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
            <h3 style="margin: 15px 0 0 30px;font-size: 16px;">
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

    </div>

  </el-container>




</template>

<style scoped>
.suoyou {
  background-color: #eeefef;
}

.toubu {
  background-color: #ffffff;
}

:deep(.el-breadcrumb__item) {
  font-size: 16px;
}

:deep(.el-breadcrumb__inner) {
  color: #2d2d2d;
  transition: all 0.3s;
}

:deep(.el-breadcrumb__item:hover .el-breadcrumb__inner) {
  color: #ababab;
}

.breadcrumb-item:hover {
  text-decoration: underline;
}

.anniu-wode {
  padding: 12px 24px !important;

  background-color: #01aa8e;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2) !important;
}

.anniu-wode:hover {
  transform: translateY(-2px);
  transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
}

.kapian {
  background-color: #ffffff;
  border: none;
  border-radius: 3px;
}

.nav-container {
  display: flex;
  align-items: center;
  background-color: white;
  padding: 0 20px;
  border-bottom: 1px solid var(--el-border-color-light);
}

.weibo-search-input {
  width: 100%;
  height: 36px;
  padding-left: 20px;
  /* 字体向右移动 */
  border: none;
  border-right: none;
  /* 避免边框重叠 */
  border-radius: 30px 0 0 30px;
  font-size: 16px;
  outline: none;
  transition: all 0.3s;
}

.weibo-search-input:focus {
  box-shadow: 0 2px 8px rgba(255, 130, 0, 0.2);
}

.weibo-search-btn {
  font-size: 18px;
  width: 100%;
  height: 38px;
  border: none;
  background: #01aa8e;
  border-radius: 0 30px 30px 0;
  margin-left: 0;
}

.weibo-search-btn:hover {
  background: #02ffd5 !important;
}

/* placeholder 样式 */
.weibo-search-input::placeholder {
  color: #8e8e8e;
  font-size: 16px;
}





































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
</style>