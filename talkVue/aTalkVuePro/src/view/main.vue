<script setup>

import { getAllBoard } from '@/api/board';
import { findBoardNameById, findPersonalPost, findPost } from '@/api/post';
import { findPerPost, findUser } from '@/api/user.js';
import { ArrowDown } from '@element-plus/icons-vue';
import { computed, onMounted, onUnmounted, ref } from 'vue';
import { useRouter } from 'vue-router'; //导入创建路由器函数
import { useTokenStore } from '@/stores/token.js'
const tokenStore = useTokenStore()
const router = useRouter()

const handleInputFocus = () => {

};



//===========================================================================================
//热门吧展示
const items = ref([
  {
    boardId: 1, avaterUrl: 'avatar1', name: '抗压背锅', viewCount: 575,
    showUnderline: false
  }
]);
// 热门话题分页控制
const currentPage_R = ref(1) // 当前页码
const pageSize_R = 8 // 每页显示数量

// 计算可见的热门话题
const visibleItems = computed(() => {
  // 修改为：
  const start = (currentPage_R.value - 1) * pageSize_R
  const end = currentPage_R.value * pageSize_R
  return items.value.slice(start, end)
})

// 加载更多方法
const R_xiayi = () => {
  if (currentPage_R.value < items.value.length) currentPage_R.value += 1;
}
const R_shangyi = () => {
  if (currentPage_R.value > 1) currentPage_R.value -= 1;
}
const getAllBoards = async () => {
  try {
    const result = await getAllBoard();
    console.log("查询的 result:", result.data);

    // 按viewCount降序排序
    items.value = result.data.sort((a, b) => {
      return b.viewCount - a.viewCount; // 降序排列
    });
  } catch (error) {
    console.error("获取贴吧列表失败:", error);
    items.value = []; // 失败时清空数据
  }
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
const currentPage_H = ref(1); // 当前页码
const pageSize_H = 5; // 每页显示数量
const visibleArts = computed(() => {
  return arts.value.slice(0, currentPage_H.value * pageSize_H);
});
const loadMore_H = () => {
  currentPage_H.value += 1;
  console.log('加载更多帖子，当前页:', currentPage_H.value);
};
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
    showUnderline_B: false,
    lastUpdateTime: ''
  }
]);
const currentPage_P = ref(1); // 当前页码
const pageSize_P = 5; // 每页显示数量
const visibleParts = computed(() => {
  return parts.value.slice(0, currentPage_P.value * pageSize_P);
});

const loadMore_P = () => {
  currentPage_P.value += 1;
  console.log('加载更多帖子，当前页:', currentPage_P.value);
};
const getMyPosts = async () => {
  try {
    const result = await findPersonalPost();

    const updatedArts = await Promise.all(
      result.data.map(async post => {
        try {
          const boardInfo = await findBoardNameById(post.boardId);
          return {
            ...post,
            boardName: boardInfo.data || '未知板块',
            // 确保时间字段存在
            lastUpdateTime: post.lastUpdateTime || new Date(0).toISOString()
          };
        } catch (e) {
          console.error(`获取板块${post.boardId}名称失败:`, e);
          return {
            ...post,
            boardName: '未知板块'
          };
        }
      })
    );

    // 安全排序
    updatedArts.sort((a, b) => {
      try {
        return new Date(b.lastUpdateTime) - new Date(a.lastUpdateTime);
      } catch (e) {
        console.error('日期解析错误:', e);
        return 0;
      }
    });

    parts.value = updatedArts;
  } catch (error) {
    console.error('数据加载失败:', error);
    parts.value = [];
  }
}
getMyPosts();

const scrollbar_P = ref(null);


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
      // 添加排序逻辑
      HotPostInfos.value = result.data
        .sort((a, b) => b.viewCount - a.viewCount)
        .slice(0, 15);
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
//=================================================================================================================================
const headerVisible = ref(false);

// 滚动监听逻辑
const handleScroll = () => {
  const scrollTop = window.pageYOffset || document.documentElement.scrollTop;
  headerVisible.value = scrollTop > 280;
};

onMounted(() => {
  window.addEventListener('scroll', handleScroll);
});

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll);
});
//=================================================================================================================================================
import { ElMessage, ElMessageBox } from 'element-plus';

const handleLogout = async () => {
  try {
    // 添加确认对话框
    await ElMessageBox.confirm('确定要退出登录吗?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })


    // 清除用户状态
    tokenStore.removeToken()

    // 跳转到登录页
    router.push('/login')

    // 提示退出成功
    ElMessage.success('退出登录成功')
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('退出登录失败')
      console.error('退出登录错误:', error)
    }
  }
}
//================================================================================================================================================
const key = ref(''); // 用于绑定搜索输入框的值
const handleSearch = () => {
  console.log("传递前的key：",key.value);
  if (!key.value.trim()) {
    ElMessage.warning('请输入搜索内容');
    return;
  }
  
  router.push({
    path: "/search",
    query: {
      key: key.value// 传递搜索关键词
    }
  });
};
</script>


<template>
  <el-container class="suoyou" style="display: flex;flex-direction: column;">
    <!-- 头部区域 -->
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
          <el-breadcrumb-item href="#">首页</el-breadcrumb-item>
        </el-breadcrumb>
      </div>

      <!-- 右侧导航组件 -->
      <div>
          <el-dropdown>
            <el-button class="anniu-wode" type="primary">更多<el-icon><arrow-down /></el-icon></el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item>账号设置</el-dropdown-item>
                <el-dropdown-item divided>问题反馈</el-dropdown-item>
                <el-dropdown-item divided>切换账号</el-dropdown-item>
                <el-dropdown-item divided @click="handleLogout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
      </div>
    </el-header>

    <!-- 搜索区 -->
    <div
      style="position: relative; height: 350px; display: flex; flex-direction: column; justify-content: center; align-items: center;">
      <!-- 视频背景 -->
      <video autoplay muted loop playsinline
        style="position: absolute;top: 0;left: 0;  width: 100% !important;height: 100% !important;object-fit: cover;z-index: 1;">
        <source src="../assets/weibobg1.mp4" type="video/mp4">
      </video>

      <!-- 内容容器 - 添加半透明背景确保文字可读性 -->
      <div style="padding: 20px;margin-bottom: 15px;z-index: 1;">
        <link href="https://fonts.googleapis.com/css2?family=Dancing+Script:wght@700&family=Kalam:wght@700&display=swap"
          rel="stylesheet">

        <span style="font-family: 'Dancing Script', cursive; 
       font-size: 42px; 
       font-weight: bold; 
       color: white;
       text-shadow: 0 0 8px rgba(255,255,255,0.7), 
                   0 0 15px rgba(255,255,255,0.4);
       position: relative;
       display: inline-block;
       line-height: 1;">
          Talkto<span style="font-family: 'Kalam', cursive; 
                font-size: 0.7em; 
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

      <!-- 搜索主体区 -->
      <div style="width: 100%; max-width: 700px;padding: 10px;z-index: 1;">
        <el-form>
          <el-row style="display: flex; align-items: center;">
            <!-- 输入框区域 -->
            <el-col :span="20" style="position: relative;">
              <input v-model="key" class="weibo-search-input" placeholder="大家都在搜：今日热门话题" @focus="handleInputFocus" />
            </el-col>

            <!-- 搜索按钮 -->
            <el-col :span="4">
              <el-button class="weibo-search-btn" type="danger"@click="handleSearch">搜索</el-button>
            </el-col>
          </el-row>
        </el-form>
      </div>
    </div>

    <!-- 下侧区域 -->
    <div style="display: flex; justify-content: center; width: 100%;margin:10px 0 0 0">
      <div style="display: flex;max-width: 1200px;">
        <!-- 左侧内容区 -->
        <div class="gerenxinxi" style="flex: 0.20;margin: 0 10px 10px 10px;">

          <!-- 个人信息展示区 -->
          <div class="kapian" style="display: flex;flex-direction: column;">
            <!-- 用户头像区（左侧） -->
            <div style="display: flex;flex-direction: row; justify-content: center;margin: 25px 0 0 0;">
              <div style="display: flex; flex-direction: column;">
                <el-avatar :src="userinfo.avaterUrl || '@/assets/tieba.png'" class="custom-avatar"
                  style="width: 80px; height: 80px;" @click="TurnToMyinf0"></el-avatar>
                <h4 style="font-size:16px ; color:#013a3c;margin: 5px 0 0 0;">{{ userinfo.nickname }}</h4>
              </div>

              <!-- VIP信息区（右侧） -->
              <div style="display: flex; flex-direction: column;margin: 0 0 0 8px;">
                <div style="font-size:14px ;color:#013a3c;margin-bottom: 12px;">
                  VIP：{{ userinfo.vipGrade }}
                </div>
                <div style="font-size:14px ;color:#013a3c;margin-bottom: 12px;">
                  帖子：{{ userinfo.PostCount }}
                </div>
                <div style="font-size:14px ;color:#013a3c;">
                  粉丝：567
                </div>
              </div>
            </div>
            <div class="jianjiequ" :data="userinfo" style="margin: 0 0 10px 25px;">
              <p>{{ userinfo.bio }}
              </p>
            </div>

          </div>



          <!-- 最新动态区 -->

          <div style="margin: 10px 0 0 0;overflow-y: auto;">
            <h3 style="font-size:16px;margin: 0 0 10px 0;">
              最新动态
            </h3>
            <el-scrollbar ref="scrollbar_P" style="height: 100%;">
              <!-- 遍历 arts 数组，最多显示 10 个卡片 -->
              <div v-for="(art, index) in visibleParts" :key="index">
                <el-card class="kapian" style="margin: 0 0 10px 0;">
                  <el-list>
                    <el-list-item style="display: flex; flex-direction: column;">
                      <span
                        style="font-size:14px;margin: 0 0 10px 0;color: #01888D; cursor: pointer; text-decoration: none;"
                        @click="handleTitleClick_P_B(art.boardId)" @mouseenter="art.showUnderline_B = true"
                        @mouseleave="art.showUnderline_B = false"
                        :style="{ textDecoration: art.showUnderline_B ? 'underline' : 'none' }">
                        {{ art.boardName || '匿名用户' }}
                      </span>
                      <span style="font-size:14px;color: #001ea9; cursor: pointer; text-decoration: none;"
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
                <el-link v-if="currentPage_P * pageSize_P < parts.length" type="primary" @click="loadMore_P"
                  style="font-size: 14px; cursor: pointer;">
                  更多帖子
                </el-link>
                <span v-else style="font-size: 14px; color: #999;">
                  没有更多了
                </span>
              </div>
            </el-scrollbar>

          </div>
        </div>

        <!-- 中间推荐区 -->
        <div class="kapian" style="flex:0.59;margin: 0 10px 0 0;padding: 20px;">
          <!-- 热门话题 -->
          <div style="margin-left: 6px;">
            <div style="display: flex;justify-content: space-between; width: 100%;">
              <h3 style="font-size:16px;margin: 0 0 5px 0; display: flex; align-items: center;">
                <img src="@/assets/remen.png" alt="view icon" style="width: 16px; height: 16px; margin-right: 4px;">
                热门话题
              </h3>
              <div>
                <img @click="R_shangyi" src="@/assets/shangyi.png" alt="view icon"
                  style="width: 18px; height: 18px; margin-right:12px;">
                <img @click="R_xiayi" src="@/assets/xiayi.png" alt="view icon" style="width: 18px; height: 18px;">
              </div>
            </div>

            <div>
              <el-row>
                <el-col :span="6" v-for="(item, index) in visibleItems" :key="index">
                  <div style="display: flex;flex-direction: row;margin: 15px 15px 15px 0;">
                    <img :src="item.avaterUrl || '@/assets/tieba.png'" alt="icon" class="remenba">

                    <div style="display: flex;flex-direction: column;margin: 0 0 0 5px;">
                      <div style="display: flex; align-items: center;">
                        <img src="@/assets/tieba.png" alt="view icon"
                          style="width: 14px; height: 14px; margin-right: 4px;">
                        <span
                          style="color:#001164; cursor: pointer; text-decoration: none;
                          max-width:60px; display:inline-block; white-space:nowrap; overflow:hidden; text-overflow:ellipsis;"
                          @click="handleTitleClick_RB(item.boardId)" @mouseenter="item.showUnderline = true"
                          @mouseleave="item.showUnderline = false"
                          :style="{ fontSize: '14px', textDecoration: item.showUnderline ? 'underline' : 'none' }">
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
          <h3 style="font-size:16px;margin: 10px 0 15px 6px; display: flex; align-items: center;">
            <img src="@/assets/dongtai.png" alt="view icon" style="width: 16px; height: 16px; margin-right: 5px;">
            个性动态
          </h3>
          <!-- 个性动态部分 -->
          <div style="height: 400px; position: relative;margin-left: 6px;">
            <!-- 使用 el-scrollbar 包裹卡片列表 -->
            <el-scrollbar ref="scrollbar_H" style="height: 100%;">
              <!-- 遍历 arts 数组，最多显示 2 个卡片 -->
              <el-card class="kapian" style="margin: 0 0 10px 0;" v-for="(art, index) in visibleArts" :key="index">
                <el-list>
                  <el-list-item style="display: flex; flex-direction: column;">
                    <span style="margin: 0 0 10px 0;color: #580099; cursor: pointer; text-decoration: none;"
                      @click="handleTitleClick_H_B(art.boardId)" @mouseenter="art.showUnderline_B = true"
                      @mouseleave="art.showUnderline_B = false"
                      :style="{ fontSize: '14px', textDecoration: art.showUnderline_B ? 'underline' : 'none' }">
                      {{ art.boardName || '匿名用户' }}
                    </span>
                    <span style="color: #001ea9; cursor: pointer; text-decoration: none;"
                      @click="handleTitleClick_H(art)" @mouseenter="art.showUnderline = true"
                      @mouseleave="art.showUnderline = false"
                      :style="{ fontSize: '14px', textDecoration: art.showUnderline ? 'underline' : 'none' }">
                      {{ art.title }}
                    </span>
                    <p style="font-size:14px;">{{ art.content }}</p>
                  </el-list-item>
                </el-list>
              </el-card>
              <div style="display: flex; justify-content: center; width: 100%;">
                <el-link v-if="currentPage_H * pageSize_H < arts.length" type="primary" @click="loadMore_H"
                  style="font-size: 14px; cursor: pointer;">
                  更多帖子
                </el-link>
                <span v-else style="font-size: 14px; color: #999;">
                  没有更多了
                </span>
              </div>
            </el-scrollbar>
          </div>
        </div>

        <!-- 热议榜区 -->
        <div class="kapian" style="flex:0.21;margin: 0 10px 10px 0;max-width: 240px;">
          <h2 style="font-size: 16px; color: #333; margin: 20px 0 5px 15px;">
            热议榜</h2>

          <ol style="list-style: none; padding: 0; margin: 0 0 0 5px;">
            <li
              style="display: flex; align-items: center; justify-content: space-between; padding: 7px 9px 0 0; border-bottom: 1px solid #f5f5f5;"
              v-for="(post, index) in HotPostInfos" :key="post.postId">
              <div style="display: flex; align-items: center;">
                <span style="font-size: 14px; font-weight: bold; color: #f85959; width: 15px; text-align: center; ">{{
                  index
                  + 1 }} </span>
                <span style="font-size: 13px;color: #333;margin-left: 5px;cursor: pointer;text-decoration: none;
                             display: inline-block;max-width: 170px;white-space: nowrap;overflow: hidden;
                             text-overflow: ellipsis;" @mouseenter="post.showUnderline_hot = true"
                  @mouseleave="post.showUnderline_hot = false"
                  :style="{ textDecoration: post.showUnderline_hot ? 'underline' : 'none' }"
                  @click="hotpostclick(post)">
                  {{ post.title }}
                </span>
              </div>
              <span style="font-size: 12px; color: #999;">{{ post.viewCount }}</span>
            </li>
          </ol>
        </div>
      </div>
    </div>



  </el-container>
</template>



<style scoped>
:deep(.el-breadcrumb__item) {
  font-size: 18px;
}

:deep(.el-breadcrumb__inner) {
  color: #2d2d2d;
  transition: all 0.3s;
}

:deep(.el-breadcrumb__item:hover .el-breadcrumb__inner) {
  color: #ababab;
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

/* 下拉菜单优化 */
:deep(.el-dropdown-menu) {
  background: #01aa8e !important;
  border: 1px solid #016f22;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

:deep(.el-dropdown-menu__item) {
  color: #262626;
  font-weight: 500;

  &:hover {
    background: #dadada !important;
  }
}

/* 搜索框优化 */
/* 同时修改外层容器和内部输入框 */
.shurukuang {
  background: linear-gradient(120deg, #99f5ba 0%, #9cd7f5 100%);
  border: 1px solid #018e93;
  border-radius: 8px;
  padding: 12px;
}


/* 搜索框右侧按钮 */
.anniu-sousuo {
  color: #00ae97;
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
  padding: 12px 24px !important;

  background: linear-gradient(120deg, #90edd7 0%, #a4e2e9 100%);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2) !important;
}

.anniu-sousuo:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(64, 158, 255, 0.4) !important;
  transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
}

/* 个人信息区 */
.gerenxinxi {
  background-color: #eeefef;
  border: none;
  border-radius: 3px;
}

.kapian {
  background-color: #ffffff;
  border: none;
  border-radius: 3px;
}

.jianjiequ {
  background-color: #ffffff;
  border: none;
  border-radius: 3px;
  font-size: 13px;
  /* 调整字体大小 */
  color: #000000;
}

.zuixinka {
  background-color: #f5f5f5;
  border: none;
  border-radius: 8px;
}


.suoyou {
  background-color: #eeefef;
}

.toubu {
  background-color: #eeefef;
}

.gexign {
  background-image: linear-gradient(120deg, #8bebc3 0%, #6fd6ed 100%);
  border: 1px solid #018e93;
  border-radius: 8px;
}

.reyi {
  border: 1px solid #018e93;
  border-radius: 8px;
}

/* 在 style 部分添加 */
.el-header {
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  will-change: transform;
}

.weibo-search-input {
  width: 100%;
  height: 46px;
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
  height: 48px;
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

.remenba {
  /* 1. 禁用默认圆形裁剪 */
  border-radius: 5px;
  overflow: hidden;
  width: 60px;
  height: 60px;
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