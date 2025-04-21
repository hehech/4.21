<script setup>
import { ElMessage } from 'element-plus'
import { ref } from 'vue';
import { useRouter } from 'vue-router';
const router = useRouter();
//==================================================================================================================================================
//搜索框变量
//返回按钮功能实现
const goBack = () => {
    router.go(-1); // 返回上一页
};
//=================================================================================================================================================
const bainfo = ref(
    {
        name: "",
        description: "",
        avaterUrl: ''
    }
)
const imageUrl = ref('')
const activeTab = ref('createTopic') // 默认激活的tab

import { addba } from '@/api/board.js'
const submit = async () => {
    let rs = await addba(bainfo.value);
    ElMessage.success(rs.msg ? rs.msg : '发表成功');
    bainfo.value = {};
    imageUrl.value = ''
}
//===============================================================================================================================================
const shoutieinfo = ref(
    {
        boardId:'',
        title: "",
        content: ""
    }
)
import { addshoutie } from '@/api/post.js'
const submitshoutie = async () => {
    let rs = await addshoutie(shoutieinfo.value);
    ElMessage.success(rs.msg ? rs.msg : '发表成功');
    shoutieinfo.value = {};
}
//===============================================================================================================================================

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
                    <el-breadcrumb-item :to="{ path: '/cboard' }" class="breadcrumb-item">创建话题</el-breadcrumb-item>
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
            <div class="kapian" style="max-width: 1100px;width: 100%;">
                <!-- 主信息区 -->
                <div style="display: flex; flex-direction: column;background-color: #ffffff;">
                    <!-- 添加el-tabs -->
                    <el-tabs v-model="activeTab" style="margin: 20px 30px 0 30px;">
                        <el-tab-pane label="创建新话题" name="createTopic">
                            <h2 style="font-size: 24px; margin:20px 0 30px 0; color: #333;">创建新话题</h2>

                            <el-form label-position="top" style="max-width: 800px;">
                                <!-- 话题名称 -->
                                <el-form-item label="话题名称" required>
                                    <el-input v-model="bainfo.name" placeholder="请输入话题名称（2-10个字符）" maxlength="20"
                                        show-word-limit style="width: 100%;"></el-input>
                                </el-form-item>

                                <!-- 话题描述 -->
                                <el-form-item label="话题描述">
                                    <el-input v-model="bainfo.description" type="textarea" :rows="4"
                                        placeholder="请输入话题描述（可选，最多100字）" maxlength="200" show-word-limit
                                        style="width: 100%;"></el-input>
                                </el-form-item>

                                <!-- 图片URL输入 -->
                                <el-form-item label="话题封面图片URL">
                                    <el-input v-model="bainfo.avaterUrl" @input="imageUrl = bainfo.avaterUrl"
                                        placeholder="请输入图片URL" style="width: 100%;">
                                        <template #prepend>
                                            <span style="color: #666;">http://</span>
                                        </template>
                                    </el-input>

                                    <!-- 图片预览区域 -->
                                    <div v-if="imageUrl" style="margin-top: 15px;">
                                        <div style="font-size: 14px; color: #666; margin-bottom: 8px;">图片预览：</div>
                                        <img :src="imageUrl"
                                            style="max-width: 100%; max-height: 200px; border: 1px solid #eee; border-radius: 4px;"
                                            onerror="this.style.display='none'">
                                    </div>
                                </el-form-item>
                            </el-form>
                            <!-- 提交按钮（放在tabs外面） -->
                            <div style="margin: 0 30px 30px 0;">
                                <el-button @click="submit" type="success" size="large"
                                    style="width: 120px;">创建话题</el-button>
                            </div>
                        </el-tab-pane>

                        <el-tab-pane label="创建话题首帖" name="createFirstPost">
                            <h2 style="font-size: 24px; margin:20px 0 30px 0; color: #333;">创建话题首帖</h2>

                            <el-form label-position="top" style="max-width: 800px;">
                                <!-- 首帖话题id-->
                                <el-form-item label="首帖话题id" required>
                                    <el-input v-model="shoutieinfo.boardId" placeholder="请输入首帖话题id" maxlength="5"
                                        show-word-limit style="width: 100%;">
                                    </el-input>
                                </el-form-item>

                                <!-- 首帖标题 -->
                                <el-form-item label="首帖标题" required>
                                    <el-input v-model="shoutieinfo.title" placeholder="请输入首帖标题（5-30个字符）" maxlength="30"
                                        show-word-limit style="width: 100%;">
                                    </el-input>
                                </el-form-item>

                                <!-- 首帖内容 -->
                                <el-form-item label="首帖内容" required>
                                    <el-input v-model="shoutieinfo.content" type="textarea" :rows="8"
                                        placeholder="请输入首帖内容（10-2000字）" maxlength="2000" show-word-limit
                                        style="width: 100%;">
                                    </el-input>
                                    <div style="font-size: 12px; color: #999; margin-top: 5px;">
                                        这是话题的第一个帖子，建议详细介绍话题背景和讨论方向
                                    </div>
                                </el-form-item>
                            </el-form>
                            <!-- 提交按钮（放在tabs外面） -->
                            <div style="margin: 0 30px 30px 0;">
                                <el-button @click="submitshoutie" type="success" size="large"
                                    style="width: 120px;">创建话题首帖</el-button>
                            </div>
                        </el-tab-pane>
                    </el-tabs>


                </div>
            </div>
        </div>
    </el-container>
</template>

<style scoped>
/* 原有样式保持不变，添加以下样式 */
:deep(.el-tabs__item) {
    font-size: 16px;
    padding: 0 20px;
    height: 50px;
}

:deep(.el-tabs__header) {
    margin: 0;
}

:deep(.el-tabs__nav-wrap::after) {
    height: 1px;
    background-color: var(--el-border-color-light);
}

.suoyou {
    background-color: #eeefef;
    padding-top: 0px;
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
    border: none;
    border-right: none;
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

.weibo-search-input::placeholder {
    color: #8e8e8e;
    font-size: 16px;
}

.remenba1 {
    border-radius: 5px;
    overflow: hidden;
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

.toubu {
    transition: background-color 0.3s ease;
}

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
    border-radius: 0 !important;
    overflow: hidden;
}

.custom-avatar::before {
    content: '';
    display: block;
    width: 100%;
    height: 100%;
    background-size: contain;
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

.search-input {
    width: 260px;
    margin-left: 20px;
}

.breadcrumb-item:hover {
    text-decoration: underline;
}
</style>