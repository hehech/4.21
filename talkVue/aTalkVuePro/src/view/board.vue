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
        getBoardInfoByIdApi(boardId);
        findBaPostApi(boardId);
    } else { ElMessage.error("无boardId"); }
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
                    <el-breadcrumb-item :to="{ path: '/board' }" class="breadcrumb-item">贴吧</el-breadcrumb-item>
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
                    <el-avatar class="custom-avatar" :size="170" :src="BaInfos[0].avaterUrl || '@/assets/tieba.png'"
                        style="position: absolute; left: 50px; bottom: -120px; border: 4px solid white; box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);"></el-avatar>
                </div>
                <!-- 吧信息介绍区域 -->
                <el-card style="padding: 0 0 40px 0">
                    <div style="display: flex;flex-direction: column;  margin:0 0 0 220px">
                        <div style="display: flex;flex-direction: row;">
                            <h2 style="margin: 0; font-size: 24px; color: #333;font-size: 30px;">{{ BaInfos[0].name }}
                            </h2>
                            <el-button style="background: linear-gradient(to bottom, #4CAF50, #45a049);color: white;
                            margin:7px 0 0 20px;
                            padding: 8px 16px;
                            font-size: 14px;
                            font-weight: bold;
                            cursor: pointer;
                            transition: all 0.3s ease;
                            display: inline-flex;
                            align-items: center;
                            justify-content: center;" @mouseover="hover = true" @mouseleave="hover = false" :style="{
                                background: hover ? 'linear-gradient(to bottom, #5CBF60, #55b059)' :
                                    'linear-gradient(to bottom, #4CAF50, #45a049)',
                                boxShadow: hover ? '0 3px 6px rgba(0,0,0,0.25)'
                                    : '0 2px 5px rgba(0,0,0,0.2)'
                            }">
                                <span style="margin-right: 4px">+</span>
                                关注
                            </el-button>
                            <div style="margin:10px 0 0 20px;">
                                <el-tag type="info" size="large">关注:{{ BaInfos[0].viewCount }}</el-tag>
                                <el-tag style="margin:0 0 0 20px;" type="info" size="large">帖子:{{
                                    BaInfos[0].postCount }}</el-tag>
                            </div>

                        </div>

                        <span style="margin: 10px 0 0 0;">{{ BaInfos[0].description }}</span>
                    </div>

                </el-card>

                <!-- 导航部件区域 -->
                <div class="nav-container">
                    <el-tabs v-model="activeTab" stretch>
                        <el-tab-pane label="看贴" name="posts"></el-tab-pane>
                        <el-tab-pane label="图片" name="images"></el-tab-pane>
                        <el-tab-pane label="吧主推荐" name="recommend"></el-tab-pane>
                        <el-tab-pane label="次元游戏" name="games"></el-tab-pane>
                    </el-tabs>

                    <el-input v-model="searchText" placeholder="吧内搜索" clearable class="search-input" />
                </div>

                <!-- 主内容区 -->
                <div style="display: flex;">
                    <!-- 帖子列表 -->
                    <!-- 帖子列表 -->
                    <div style="flex: 1; background-color: white; margin-right: 4px; margin-top: 1px;">
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

                    <!-- 右侧功能区 -->
                    <div style="display: flex;flex-direction: column;margin-top: 1px;">
                        <div style="width: 280px;margin-bottom: 3px;">
                            <div type="primary" style="background-color: #FFFF; padding:5px; font-weight: bold;">
                                我在贴吧</div>
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
                            <div type="primary" style="background-color: #FFFF; padding:5px; font-weight: bold;">本吧信息
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