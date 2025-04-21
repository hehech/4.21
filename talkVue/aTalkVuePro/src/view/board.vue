<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue';
import { useRouter } from 'vue-router'
import { useRoute } from 'vue-router'  // 必须添加这行
import { ElMessage } from 'element-plus'
const hover = ref(false)
const router = useRouter();
//==================================================================================================================================================
//搜索框变量
const activeTab = ref('bapost')
const searchText = ref('')
//=================================================================================================================================================
//我在本吧 信息展示

//=================================================================================================================================================
//帖子信息展示
const PostInfos = ref([
    {
        postId: 18, likeCount: 55, title: '你好', userId: 22, UserName: '和', showUnderline: false,
        boardId: 1
    }
]);
import { findBaPost } from '@/api/post.js'
const findBaPostApi = async (id) => {
    const rs = await findBaPost(id);
    PostInfos.value = rs.data;
};
//temp:帖子跳转
const handleTitleClick = (post) => {
    router.push({
        path: "/post",
        query: {
            boardId: post.boardId,
            postId: post.postId
        }  // 传递贴吧名称作为查询参数
    });
}
//==============================================================================================================================================
const reposts = ref([
    {
        postId: 18, likeCount: 55, title: '你好', userId: 22, UserName: '和', showUnderline: false,
        boardId: 1
    }
]);
const findrebaPost = async (id) => {
    try {
        const rs = await findBaPost(id);
        // 按点赞数降序排序
        reposts.value = rs.data.sort((a, b) => b.likeCount - a.likeCount);
    } catch (error) {
        console.error("获取帖子失败:", error);
        reposts.value = []; // 出错时清空数组
    }
};
//temp:帖子跳转
//==================================================================================================================================================
//吧信息显示
const BaInfos = ref([
    {
        boardId: 1, name: '三国杀', description: '抗压背锅', viewCount: 575,
        postCount: 222, avaterUrl: ''
    }
]);
const route = useRoute()
import { getBoardInfoById } from '@/api/board.js'
const getBoardInfoByIdApi = async (id) => {
    try {
        const result = await getBoardInfoById(id);
        if (result.data) {
            BaInfos.value = [result.data]; // 确保赋值是数组形式
        } else {
            ElMessage.error("数据加载失败");
        }
    } catch (error) {
        ElMessage.error("请求失败");
    }
};
onMounted(async () => {
    const boardId = route.query.boardId
    if (boardId) {
        await getBoardInfoByIdApi(boardId);
        await findBaPostApi(boardId);
        await findrebaPost(boardId);
        await initFollow(); // 初始化关注状态
    } else { 
        ElMessage.error("无boardId"); 
    }
})

//================================================================================================================================================

//================================================================================================================================================
//返回按钮功能实现
const goBack = () => {
    router.go(-1); // 返回上一页
};
//====================================================================================================================================
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
//=============================================
const key = ref(''); // 用于绑定搜索输入框的值
const handleSearch = () => {
    console.log("传递前的key：", key.value);
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
//======================================================================
//关注
import { doFocusBoard, cancelFocusBoard, findCurFocusB} from '@/api/board';
let isFollowed = ref(false);
const initFollow = async () => {
    try {
        // 1. 获取当前用户关注的贴吧ID列表
        const focusResponse = await findCurFocusB();

        // 检查关注列表是否成功获取
        if (focusResponse.code !== 0) {
            throw new Error(focusResponse.message || '获取关注贴吧列表失败');
        }

        // 2. 创建已关注贴吧ID的Set集合（直接使用数值数组）
        const followedBoardIds = new Set(focusResponse.data); // 直接使用返回的数值数组
        
        // 3. 检查当前贴吧是否在已关注列表中
        if (BaInfos.value.length > 0) {
            const currentBoardId = BaInfos.value[0].boardId;
            isFollowed.value = followedBoardIds.has(currentBoardId);
        }
    } catch (error) {
        console.error('初始化关注状态失败:', error);
        isFollowed.value = false; // 出错时默认设为未关注
    }
}
const handlefocus_B = async (id) => {
    try {
        console.log("点击时isFollowed状态：",isFollowed.value);
        if (isFollowed.value) {
            // 如果已关注，执行取消关注
            await cancelFocusBoard(id);
            isFollowed.value = false;
            ElMessage.success('已取消关注');
        } else {
            // 如果未关注，执行关注
            await doFocusBoard(id);
            isFollowed.value = true;
            ElMessage.success('关注成功');
        }
    } catch (error) {
        ElMessage.error('操作失败: ' + error.message);
    }
}
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
                    <el-breadcrumb-item :to="{ path: '/board' }" class="breadcrumb-item">贴吧</el-breadcrumb-item>
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
                            <input v-model="key" class="weibo-search-input" placeholder="大家都在搜：今日热门话题" />
                        </el-col>

                        <!-- 搜索按钮 -->
                        <el-col :span="4">
                            <el-button @click="handleSearch" class="weibo-search-btn" type="danger">搜索</el-button>
                        </el-col>
                    </el-row>
                </el-form>
            </div>
        </div>

        <div style="display: flex; justify-content: center; width: 100%;margin:5px 0 0 0;min-height: 800px;">
            <div class="kapian" style="max-width: 1100px;width: 100%;">
                <!-- 主信息区 -->
                <div style="">
                    <!-- 顶部背景区域 -->
                    <div
                        style="height: 130px; background: linear-gradient(60deg, #64b3f4 0%, #c2e59c 100%); position: relative;">
                        <el-avatar class="custom-avatar" :size="150" :src="BaInfos[0].avaterUrl || '@/assets/tieba.png'"
                            style="position: absolute; left: 50px; bottom: -110px; border: 4px solid white; box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);"></el-avatar>
                    </div>
                    <!-- 吧信息介绍区域 -->
                    <div style="padding: 10px 10px 40px 0">
                        <div style="display: flex;flex-direction: column;  margin:0 0 0 220px">
                            <div style="display: flex;flex-direction: row;">
                                <h2 style="margin: 0; font-size: 24px; color: #333;font-size: 25px;">{{ BaInfos[0].name
                                }}
                                </h2>
                                <el-button @click="handlefocus_B(BaInfos[0].boardId)" style="margin:5px 0 0 10px;
                padding: 8px 10px;
                font-size: 14px;
                font-weight: bold;
                cursor: pointer;
                transition: all 0.3s ease;
                display: inline-flex;
                align-items: center;
                justify-content: center;
                flex-shrink: 0;" :style="{
                    background: isFollowed ? '#cccccc' :
                        hover ? 'linear-gradient(to bottom, #5CBF60, #55b059)' :
                            'linear-gradient(to bottom, #4CAF50, #45a049)',
                    color: isFollowed ? '#666666' : 'white',
                    boxShadow: hover && !isFollowed ? '0 3px 6px rgba(0,0,0,0.25)' : '0 2px 5px rgba(0,0,0,0.2)',
                    border: isFollowed ? '1px solid #ddd' : 'none'
                }" @mouseover="hover = true" @mouseleave="hover = false">
                                    <span v-if="!isFollowed" style="margin-right: 4px">+</span>
                                    {{ isFollowed ? '已关注' : '关注' }}
                                </el-button>
                                <div style="margin:10px 0 0 20px;">
                                    <el-tag type="info" size="large">关注:{{ BaInfos[0].viewCount }}</el-tag>
                                    <el-tag style="margin:0 0 0 20px;" type="info" size="large">帖子:{{
                                        BaInfos[0].postCount }}</el-tag>
                                </div>

                            </div>

                            <span style="margin: 10px 0 0 0;font-size: 16px;">{{ BaInfos[0].description }}</span>
                        </div>

                    </div>

                    <!-- 导航部件区域 -->
                    <div class="nav-container">
                        <el-tabs v-model="activeTab" stretch>
                            <el-tab-pane label="文章" name="bapost"></el-tab-pane>
                            <el-tab-pane label="推荐" name="recommend"></el-tab-pane>
                        </el-tabs>
                    </div>

                    <!-- 主内容区 -->
                    <div style="display: flex;">
                        <!-- 帖子列表 -->
                        <div v-if="activeTab === 'bapost'"
                            style="flex: 1; background-color: white; margin-right: 4px; margin-top: 1px;">
                            <!-- 当有帖子时显示帖子列表 -->
                            <template v-if="PostInfos && PostInfos.length > 0">
                                <!-- 置顶帖1 -->
                                <div style="display: flex; padding: 15px; border-bottom: 1px solid #f0f0f0; background-color: #FFFF;"
                                    v-for="post in PostInfos" :key="post.postId">
                                    <!-- 回复数 -->
                                    <div
                                        style="width: 50px; text-align: center; color: #999; font-size: 14px; align-self: center;">
                                        {{ post.likeCount }}
                                    </div>

                                    <div style="flex: 1;">
                                        <div
                                            style="margin: 10px 0 10px 5px; display: flex; justify-content: space-between; align-items: center;">
                                            <div>
                                                <span
                                                    style="background-color: #d82100; color: white; padding: 2px 5px; border-radius: 3px; font-size: 12px; margin-right: 8px;">置顶</span>
                                                <span
                                                    style="font-size: 16px; font-weight: 500; cursor: pointer; text-decoration: none;"
                                                    @click="handleTitleClick(post)"
                                                    @mouseenter="post.showUnderline = true"
                                                    @mouseleave="post.showUnderline = false"
                                                    :style="{ textDecoration: post.showUnderline ? 'underline' : 'none' }">{{
                                                        post.title }}</span>
                                            </div>
                                            <span
                                                style="font-size: 12px; color: #d82100; position: relative; margin-right: 30px;">
                                                <img src="@/assets/zuozhe.png" alt="view icon"
                                                    style="width: 16px; height: 16px; position: absolute; left: -20px; top: 0;">
                                                {{ post.userId }}
                                            </span>
                                        </div>
                                    </div>
                                </div>
                            </template>

                            <!-- 当没有帖子时显示提示信息 -->
                            <div v-else style="padding: 40px; text-align: center; color: #999;">
                                暂时没有内容哦~
                            </div>
                        </div>

                        <!-- 推荐列表 -->
                        <div v-if="activeTab === 'recommend'"
                            style="flex: 1; background-color: white; margin-right: 4px; margin-top: 1px;">
                            <!-- 置顶帖1 -->
                            <div style="display: flex; padding: 15px; border-bottom: 1px solid #f0f0f0; background-color: #FFFF;"
                                v-for="post in reposts" :key="post.postId">
                                <!-- 回复数 -->
                                <div
                                    style="width: 50px; text-align: center; color: #999; font-size: 14px; align-self: center;">
                                    {{ post.likeCount }}
                                </div>

                                <div style="flex: 1;">
                                    <div
                                        style="margin: 10px 0 10px 5px; display: flex; justify-content: space-between; align-items: center;">
                                        <div>
                                            <span
                                                style="background-color: #d82100; color: white; padding: 2px 5px; border-radius: 3px; font-size: 12px; margin-right: 8px;">置顶</span>
                                            <span
                                                style="font-size: 16px; font-weight: 500; cursor: pointer; text-decoration: none;"
                                                @click="handleTitleClick(post)" @mouseenter="post.showUnderline = true"
                                                @mouseleave="post.showUnderline = false"
                                                :style="{ textDecoration: post.showUnderline ? 'underline' : 'none' }">{{
                                                    post.title }}</span>
                                        </div>
                                        <span
                                            style="font-size: 12px; color: #d82100; position: relative; margin-right: 30px;">
                                            <img src="@/assets/zuozhe.png" alt="view icon"
                                                style="width: 16px; height: 16px; position: absolute; left: -20px; top: 0;">
                                            {{ post.userId }}
                                        </span>
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