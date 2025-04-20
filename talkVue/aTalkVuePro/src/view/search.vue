<script setup>
import { computed, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ElMessage } from 'element-plus'

const hover = ref(false)
const router = useRouter();
const route = useRoute();
//==================================================================================================================================================
//搜索框变量
//返回按钮功能实现
const goBack = () => {
    router.go(-1); // 返回上一页
};
//==============================================================================================================================================
//===============================================================================================================================================
import { SearchUsers } from '@/api/search.js';
const SearchUsersinfo = ref([{
    userId: 1,
    avaterUrl: '',
    nickname: '',
    showUnderline: false,
    isFollowed: false
}])
const handleTitleClick_U = () => {

}
// 修改handlefocus_U函数
import { doFocusUser, cancelFocusUser } from '@/api/user';
const handlefocus_U = async (user) => {
    try {
        if (user.isFollowed) {
            // 如果已关注，执行取消关注
            await cancelFocusUser(user.userId);
            user.isFollowed = false;
            ElMessage.success('已取消关注');
        } else {
            // 如果未关注，执行关注
            await doFocusUser(user.userId);
            user.isFollowed = true;
            ElMessage.success('关注成功');
        }
    } catch (error) {
        ElMessage.error('操作失败: ' + error.message);
    }
}
// 修改searchUser函数，初始化isFollowed状态
import { findCurFocusU } from '@/api/user'
const searchUser = async () => {
    try {
        // 1. 获取搜索结果
        const searchResponse = await SearchUsers(route.query.key);

        // 检查搜索结果是否成功
        if (searchResponse.code !== 0) {
            throw new Error(searchResponse.message || '搜索用户失败');
        }

        // 2. 获取当前用户关注的用户ID列表
        const focusResponse = await findCurFocusU();

        // 检查关注列表是否成功获取
        if (focusResponse.code !== 0) {
            throw new Error(focusResponse.message || '获取关注列表失败');
        }

        // 3. 创建已关注用户ID的Set集合用于快速查找
        const followedUserIds = new Set(focusResponse.data);

        // 4. 初始化搜索结果，设置isFollowed状态
        SearchUsersinfo.value = searchResponse.data.map(user => ({
            ...user,
            isFollowed: followedUserIds.has(user.userId) // 检查用户ID是否在已关注列表中
        }));

    } catch (error) {
        console.error('用户搜索处理失败:', error);
        ElMessage.error(error.message || '用户数据加载失败');

        // 回退方案：如果出错，将所有用户设为未关注状态
        if (searchResponse?.data) {
            SearchUsersinfo.value = searchResponse.data.map(user => ({
                ...user,
                isFollowed: false
            }));
        } else {
            SearchUsersinfo.value = [];
        }
    }
}
searchUser();
//===============================================================================================================================================
import { findBoardNameById } from '@/api/post.js';
import { searchBoards, searchpost } from '@/api/search.js';
const searchBoardsinfo = ref([
    {
        boardId: 1, avaterUrl: 'avatar1', name: '抗压背锅', showUnderline: false, isFollowed: false
    }
]);
const handleTitleClick_B = (id) => {
    router.push({
        path: "/board",
        query: {
            boardId: id
        }
    });
}
import { doFocusBoard, cancelFocusBoard, findCurFocusB} from '@/api/board';
const handlefocus_B = async (board) => {
    try {
        if (board.isFollowed) {
            // 如果已关注，执行取消关注
            await cancelFocusBoard(board.boardId);
            board.isFollowed = false;
            ElMessage.success('已取消关注');
        } else {
            // 如果未关注，执行关注
            await doFocusBoard(board.boardId);
            board.isFollowed = true;
            ElMessage.success('关注成功');
        }
    } catch (error) {
        ElMessage.error('操作失败: ' + error.message);
    }
}
const searchBoard = async () => {
    try {
        // 1. 获取搜索结果
        const searchResponse = await searchBoards(route.query.key);

        // 检查搜索结果是否成功
        if (searchResponse.code !== 0) {
            throw new Error(searchResponse.message || '搜索话题失败');
        }

        // 2. 获取当前用户关注的用户ID列表
        const focusResponse = await findCurFocusB();

        // 检查关注列表是否成功获取
        if (focusResponse.code !== 0) {
            throw new Error(focusResponse.message || '获取关注话题列表失败');
        }

        // 3. 创建已关注用户ID的Set集合用于快速查找
        const followedBoardIds = new Set(focusResponse.data);

        // 4. 初始化搜索结果，设置isFollowed状态
        searchBoardsinfo.value = searchResponse.data.map(board => ({
            ...board,
            isFollowed: followedBoardIds.has(board.boardId) // 检查用户ID是否在已关注列表中
        }));

    } catch (error) {
        console.error('话题搜索处理失败:', error);
        ElMessage.error(error.message || '话题数据加载失败');

        // 回退方案：如果出错，将所有用户设为未关注状态
        if (searchResponse?.data) {
            searchBoardsinfo.value = searchResponse.data.map(board => ({
                ...board,
                isFollowed: false
            }));
        } else {
            searchBoardsinfo.value = [];
        }
    }
}
searchBoard();
//===============================================================================================================================================
const searchpostinfo = ref([
    {
        boardId: 1, postId: 1, title: '吉大常高鸣', content: '荣获年度TOP1', showUnderline: false,
        boardName: '',
        showUnderline_B: false
    }
]);
const currentPage_H = ref(1); // 当前页码
const pageSize_H = 5; // 每页显示数量
const visiblepostinfo = computed(() => {
    return searchpostinfo.value.slice(0, currentPage_H.value * pageSize_H);
});
const loadMore_H = () => {
    currentPage_H.value += 1;
    console.log('加载更多帖子，当前页:', currentPage_H.value);
};
const handleTitleClick_H_B = (id) => {
    router.push({
        path: "/board",
        query: {
            boardId: id
        }
    });
}
const handleTitleClick_H = (post) => {
    router.push({
        path: "/post",
        query: {
            boardId: post.boardId,
            postId: post.postId
        }
    });
}
const searchposts = async () => {
    try {
        // 1. 获取帖子列表
        const result = await searchpost(route.query.key);

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
        searchpostinfo.value = updatedArts;
    } catch (error) {
        console.error('数据加载失败:', error);
        searchpostinfo.value = []; // 失败时清空数据
    }
}
searchposts();
</script>

<template>
    <el-container class="suoyou" shallow="never" style="display: flex;flex-direction: column;">
        <!-- 修改导航栏样式 -->
        <el-header class="toubu" style="display: flex; justify-content: space-between; align-items: center; 
            position: fixed;
            top: 0;
            width: 100%;
            z-index: 1000;
            background-color: transparent;
            backdrop-filter: blur(10px);
            padding: 0 20px;">
            <!-- 左侧导航内容 -->
            <div>
                <el-breadcrumb style="font-size: 16px;" class="white-text">
                    <el-breadcrumb-item :to="{ path: '/' }" class="breadcrumb-item">首页</el-breadcrumb-item>
                    <el-breadcrumb-item :to="{ path: '/search' }" class="breadcrumb-item">搜索内容</el-breadcrumb-item>
                </el-breadcrumb>
            </div>

            <!-- 右侧导航组件 -->
            <div>
                <el-button-group>
                    <el-button class="anniu-wode white-text" @click="goBack" style="
                        background-color: transparent;
                        border: 1px solid rgba(255, 255, 255, 0.3);
                        color: white;
                    ">返回</el-button>
                </el-button-group>
            </div>
        </el-header>

        <!-- 搜索区 -->
        <div
            style="position: relative; height: 250px; display: flex; flex-direction: column; justify-content: center; align-items: center;">
            <!-- 视频背景 -->
            <video autoplay muted loop playsinline
                style="position: absolute;top: 0;left: 0;  width: 100% !important;height: 100% !important;object-fit: cover;z-index: 1;">
                <source src="../assets/bbg1.mp4" type="video/mp4">
            </video>
        </div>

        <div style="display: flex; justify-content: center; width: 100%;margin:5px 0 0 0;min-height: 500px;">
            <div class="kapian" style="max-width: 1200px;width: 100%;">
                <!-- 主信息区 -->
                <div style="display: flex;">
                    <!-- 相关的人-->
                    <div style="flex:0.25;margin:10px 0 0 0;background-color: #ffffff;">
                        <div type="primary"
                            style="background-color: #FFFF; padding:5px; font-weight: bold;margin:10px 0 10px 10px;">
                            相关用户
                        </div>
                        <template v-if="SearchUsersinfo.length > 0">

                            <div class="kapian" v-for="u in SearchUsersinfo" :key="u.userId" style="display: flex; align-items: center; justify-content: space-between; 
            padding: 10px; border-bottom: 1px solid #f0f0f0; 
            background-color: #FFFF; margin:0 0 0 5px">
                                <div style="display: flex; align-items: center; flex-grow: 1;">
                                    <el-avatar :src="u.avaterUrl || '@/assets/tieba.png'" class="remenba1"
                                        style="width: 55px; height: 55px; margin-right: 10px;"></el-avatar>
                                    <span @click="handleTitleClick_U(u.userId)"
                                        style="font-weight: 500; cursor: pointer; text-decoration: none;"
                                        @mouseenter="u.showUnderline = true" @mouseleave="u.showUnderline = false"
                                        :style="{ fontSize: '14px', textDecoration: u.showUnderline ? 'underline' : 'none' }">
                                        {{ u.nickname || '匿名用户' }}
                                    </span>
                                </div>
                                <el-button @click="handlefocus_U(u)" style="
        padding: 8px 10px;
        font-size: 14px;
        font-weight: bold;
        cursor: pointer;
        transition: all 0.3s ease;
        display: inline-flex;
        align-items: center;
        justify-content: center;" :style="{
            background: u.isFollowed ? '#cccccc' :
                hover ? 'linear-gradient(to bottom, #5CBF60, #55b059)' :
                    'linear-gradient(to bottom, #4CAF50, #45a049)',
            color: u.isFollowed ? '#666666' : 'white',
            boxShadow: hover && !u.isFollowed ? '0 3px 6px rgba(0,0,0,0.25)' : '0 2px 5px rgba(0,0,0,0.2)',
            border: u.isFollowed ? '1px solid #ddd' : 'none'
        }" @mouseover="hover = true" @mouseleave="hover = false">
                                    <span v-if="!u.isFollowed" style="margin-right: 4px">+</span>
                                    {{ u.isFollowed ? '已关注' : '关注' }}
                                </el-button>
                            </div>
                        </template>
                        <div v-else class="no-data" style="text-align: center; color: #999;">
                            无相关用户哦~
                        </div>
                    </div>
                    <!-- post -->
                    <div
                        style="flex:0.50;height: 400px; position: relative;margin: 10px 10px 0 10px;background-color: #ffffff;">
                        <div type="primary"
                            style="background-color: #FFFF; padding:5px; font-weight: bold;margin:10px 0 0 10px;">
                            相关文章
                        </div>
                        <!-- 使用 el-scrollbar 包裹卡片列表 -->
                        <el-scrollbar style="height: 100%;">
                            <template v-if="searchpostinfo.length > 0">
                                <!-- 遍历 arts 数组，最多显示 2 个卡片 -->
                                <el-card class="kapian" style="margin: 0 0 10px 0;"
                                    v-for="(art, index) in visiblepostinfo" :key="index">
                                    <el-list>
                                        <el-list-item style="display: flex; flex-direction: column;">
                                            <span
                                                style="margin: 0 0 10px 0;color: #580099; cursor: pointer; text-decoration: none;"
                                                @click="handleTitleClick_H_B(art.boardId)"
                                                @mouseenter="art.showUnderline_B = true"
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
                                    <el-link v-if="currentPage_H * pageSize_H < searchpostinfo.length" type="primary"
                                        @click="loadMore_H" style="font-size: 14px; cursor: pointer;">
                                        更多帖子
                                    </el-link>
                                    <span v-else style="font-size: 14px; color: #999;">
                                        没有更多了
                                    </span>
                                </div>
                            </template>
                            <div v-else class="no-data" style="text-align: center; color: #999;">
                                无相关文章哦~
                            </div>
                        </el-scrollbar>
                    </div>
                    <!-- 相关话题 -->
                    <div style="flex:0.25;margin:10px 0 0 0;background-color: #ffffff;">
                        <div type="primary"
                            style="background-color: #FFFF; padding:5px; font-weight: bold;margin:10px 0 10px 10px;">
                            相关话题
                        </div>
                        <template v-if="searchBoardsinfo.length > 0">
                            <div class="kapian" v-for="u in searchBoardsinfo" :key="u.boardId" style="display: flex; align-items: center; justify-content: space-between; 
                    padding: 10px; border-bottom: 1px solid #f0f0f0; 
                    background-color: #FFFF; margin:0 0 0 5px">
                                <div style="display: flex; align-items: center; flex-grow: 1; min-width: 0;">
                                    <el-avatar :src="u.avaterUrl || '@/assets/tieba.png'" class="remenba1"
                                        style="width: 55px; height: 55px; margin-right: 10px;"></el-avatar>
                                    <span style="font-size: 14px; font-weight: 500; cursor: pointer; text-decoration: none;
                      white-space: nowrap; overflow: hidden; text-overflow: ellipsis;"
                                        @click="handleTitleClick_B(u.boardId)" @mouseenter="u.showUnderline = true"
                                        @mouseleave="u.showUnderline = false"
                                        :style="{ textDecoration: u.showUnderline ? 'underline' : 'none' }">
                                        {{ u.name || '匿名话题' }}
                                    </span>
                                </div>
                                <el-button @click="handlefocus_B(u)" style="
                padding: 8px 10px;
                font-size: 14px;
                font-weight: bold;
                cursor: pointer;
                transition: all 0.3s ease;
                display: inline-flex;
                align-items: center;
                justify-content: center;
                flex-shrink: 0;" :style="{
                    background: u.isFollowed ? '#cccccc' :
                        hover ? 'linear-gradient(to bottom, #5CBF60, #55b059)' :
                            'linear-gradient(to bottom, #4CAF50, #45a049)',
                    color: u.isFollowed ? '#666666' : 'white',
                    boxShadow: hover && !u.isFollowed ? '0 3px 6px rgba(0,0,0,0.25)' : '0 2px 5px rgba(0,0,0,0.2)',
                    border: u.isFollowed ? '1px solid #ddd' : 'none'
                }" @mouseover="hover = true" @mouseleave="hover = false">
                                    <span v-if="!u.isFollowed" style="margin-right: 4px">+</span>
                                    {{ u.isFollowed ? '已关注' : '关注' }}
                                </el-button>
                            </div>
                        </template>
                        <div v-else class="no-data" style="padding: 20px; text-align: center; color: #999;">
                            无相关话题哦~
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
    padding-top: 0px;
    /* 根据导航栏实际高度调整 */
}

.no-data {
    padding: 20px;
    text-align: center;
    color: #999;
    font-size: 14px;
    background-color: #f9f9f9;
    border-radius: 4px;
    margin: 10px;
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

.remenba1 {
    /* 1. 禁用默认圆形裁剪 */
    border-radius: 5px;
    overflow: hidden;
    /* 防止图片超出容器 */
}

.white-text {
    color: white !important;
}

.white-text :deep(.el-breadcrumb__inner),
.white-text :deep(.el-breadcrumb__item:hover .el-breadcrumb__inner),
.white-text :deep(.el-breadcrumb__item:last-child .el-breadcrumb__inner) {
    color: white !important;
}

.white-text :deep(.el-breadcrumb__separator) {
    color: rgba(255, 255, 255, 0.7) !important;
}

/* 导航栏样式 */
.toubu {
    transition: background-color 0.3s ease;
}

/* 返回按钮样式 */
.anniu-wode {
    padding: 12px 24px !important;
    transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
}

.anniu-wode:hover {
    transform: translateY(-2px);
    background-color: rgba(255, 255, 255, 0.1) !important;
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