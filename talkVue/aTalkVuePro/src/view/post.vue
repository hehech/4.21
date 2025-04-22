<script setup>
import { ref, onMounted, onUnmounted } from 'vue';
import { useRoute } from 'vue-router'  // 必须添加这行
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'//导入创建路由器函数
import { QuillEditor } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css'

import EmojiPicker from "vue3-emoji-picker";
import "vue3-emoji-picker/css";

const router = useRouter()
const route = useRoute()

const goBack = () => {
    router.go(-1); // 返回上一页
};
//================================================================================================================================================
//吧信息显示
const BaInfos = ref([
    {
        boardId:'',name: '三国杀', description: '抗压背锅', avaterUrl: ''
    }
]);
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
//================================================================================================================================================
//主贴信息展示
import { findPostById } from '@/api/post.js'
import { findUserinfoByUserId } from '@/api/user'
const PostInfos = ref(
    {
        postId: 18, title: '你好', content: '', createTime: '',
        userId: 22, UserName: '和', UserAvater: '', address: '',
    }
);
const getOnePost = async (id) => {
    try {
        const result = await findPostById(id);
        if (!result.data) throw new Error("数据为空");

        const updatedPostInfos = await Promise.all(
            (Array.isArray(result.data) ? result.data : [result.data]).map(async post => {
                const userinfo = await findUserinfoByUserId(post.userId);
                return {
                    ...post,
                    UserName: userinfo.data?.nickname || '未知名字',
                    UserAvater: userinfo.data?.avaterUrl || '无头像url',
                    address: userinfo.data?.address || '无地址'
                };
            })
        );

        PostInfos.value = updatedPostInfos;
    } catch (error) {
        console.error('数据加载失败:', error);
        PostInfos.value = [];
    }
};
// 定义响应式变量
const showReply_main = ref(false)
const toggleReply = () => {
    showReply_main.value = !showReply_main.value;
}
//===============================================================================================================================================
//漏贴数据模型
import { Addfloor } from '@/api/floor.js'
const floorModel = ref(
    {
        postId: '', content: ''
    }
);
const issue = async (post) => {
    floorModel.value.postId = post.postId;
    // 去除HTML标签
    //floorModel.value.content = floorModel.value.content.replace(/<[^>]*>/g, '');
    let rs = await Addfloor(floorModel.value);
    ElMessage.success(rs.msg ? rs.msg : '发表成功');
    await getlposts(post.postId);
};
//===============================================================================================================================================
//热议信息显示
import { findBaHotPosts } from '@/api/post.js'
const HotPostInfos = ref([
    {
        postId: 18, title: '你好', viewCount: 1,
        showUnderline_hot: false
    }
]);
const getBaHotPosts = async (id) => {
    try {
        const result = await findBaHotPosts(id);
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
const hotpostclick = (id) => {
    const boardId = route.query.boardId;
    router.replace({
        path: "/post",
        query: {
            boardId: boardId,
            postId: id
        }
    }).then(() => {
        // 触发数据重新加载
        getOnePost(id);
        getLposts(id);
    });
}
//================================================================================================================================================
//楼贴信息展示
const Lpostinfos = ref([
    {
        floorId: 1, postId: 1, floorNumber: 2, content: '', createTime: '',
        userId: 1, UserName: '', UserAvater: '', address: '',

        showReply: false, // 新增：控制是否显示回复框
        replyContent: '', // 新增：存储回复内容
        deviceInfo: 'iPhone客户端', // 设备信息

        EmojiChooseShow: false,
        showUnderline_Comment: false,

        placeholder: ''
    }
]);
const showEmojiPicker = (floorId) => {
    const post = Lpostinfos.value.find(p => p.floorId === floorId);
    if (post) {
        post.EmojiChooseShow = !post.EmojiChooseShow;
    }
}
// 修改后的方法
const onVue3EmojiPicker = (emoji, floorId) => {
    const post = Lpostinfos.value.find(p => p.floorId === floorId);
    if (post) {
        post.replyContent = (post.replyContent || '') + emoji.i; // 双重保障
    }
};


//================================================================================================================================================
//评论显示
const Commentinfo = ref([
    {
        commentId: 1,
        floorId: 1,
        userId: 1,
        parentCommentId: 1,
        content: '',
        createTime: '',

        UserName: '', UserAvaterUrl: '', parentCommentUserName: '',
        showUnderline_Answer: false
    }
])
import { findCommentsByFloorId } from '@/api/comment.js'
const ShowComment = async (floorId) => {
    const post = Lpostinfos.value.find(p => p.floorId === floorId);
    if (post) {
        // 关闭其他已打开的回复框
        Lpostinfos.value
            .filter(item => item.showReply && item.floorId !== floorId)
            .forEach(item => {
                item.showReply = false;
                item.EmojiChooseShow = false;
            });
        // 切换当前帖子的状态
        post.showReply = !post.showReply;
        post.EmojiChooseShow = false;
        post.placeholder = "请输入内容..."
    }
    try {
        const result = await findCommentsByFloorId(floorId);
        if (!result.data) throw new Error("数据为空");
        console.log("评论返回的数据：", result.data);

        const updatedCommentinfo = await Promise.all(
            result.data.map(async comt => {
                const userinfo = await findUserinfoByUserId(comt.userId);
                return {
                    ...comt,
                    UserName: userinfo.data?.nickname || '未知名字',
                    UserAvaterUrl: userinfo.data?.avaterUrl || '无头像url',
                };
            })
        );

        // 对评论按 createTime 升序排序
        updatedCommentinfo.sort((a, b) => {
            return new Date(a.createTime) - new Date(b.createTime);
        });
        // 新增：处理父评论用户名
        updatedCommentinfo.forEach(comment => {
            if (comment.parentCommentId) {
                const parentComment = updatedCommentinfo.find(c =>
                    c.commentId === comment.parentCommentId
                );
                if (parentComment) {
                    comment.parentCommentUserName = parentComment.UserName;
                }
            }
        });

        Commentinfo.value = updatedCommentinfo;
        console.log("加载的评论：", Commentinfo.value);

    } catch (error) {
        console.error('数据加载失败:', error);
        Commentinfo.value = [];
    }
}
//=================================================================================================================================================
//评论模型
const CommentModel = ref(
    {
        floorId: 1,
        parentCommentId: null,
        content: ''
    }
)

import { Addcomment } from '@/api/comment.js'
const issueComment = async (post) => {
    CommentModel.value = { ...CommentModel.value, floorId: post.floorId };


    let rs = await Addcomment(CommentModel.value);
    ElMessage.success(rs.msg ? rs.msg : '发表成功');
    await ShowComment(post.floorId);
    CommentModel.value = [];
    post.placeholder = "请输入内容..."
};
const CommentAnswer = (comt, post) => {
    CommentModel.value = { ...CommentModel.value, parentCommentId: comt.commentId };
    post.placeholder = `回复 ${comt.UserName}：`;
}
//======================================================================================================================================================
//分页器设置
//分页条数据模型
const pageNum = ref(1)//当前页
const total = ref(10)//总条数
const pageSize = ref(2)//每页条数
// 修改分页处理函数
const onSizeChange = (size) => {
    pageSize.value = size;
    const postId = route.query.postId
    getLposts(postId);  // 添加数据加载
}

const onCurrentChange = (num) => {
    pageNum.value = num;
    const postId = route.query.postId
    getLposts(postId);  // 添加数据加载
}
import { getLpostsPage } from '@/api/floor.js'
const getLposts = async (id) => {
    try {
        let params = {
            pageNum: pageNum.value,
            pageSize: pageSize.value,
            Id: id  // 修正引用方式
        }

        const result = await getLpostsPage(params);
        console.log("返回的分页数据：", result.data)
        if (!result?.data) throw new Error("数据为空");

        const updatedLpostinfos = await Promise.all(
            result.data.items.map(async post => {
                const userinfo = await findUserinfoByUserId(post.userId);
                return {
                    ...post,
                    UserName: userinfo.data?.nickname || '未知名字',
                    UserAvater: userinfo.data?.avaterUrl || '无头像url',
                    address: userinfo.data?.address || '无地址'
                };
            })
        );

        Lpostinfos.value = updatedLpostinfos.sort((a, b) => a.floorNumber - b.floorNumber);
        total.value = result.data.total;  // 移到try块内
    } catch (error) {
        console.error('数据加载失败:', error);
        Lpostinfos.value = [];
        total.value = 0;  // 出错时重置总数
    }
}












//======================================================================================================================================================

//接受外界面变量；
onMounted(async () => {
    await initFollow();
    //吧信息展示
    const boardId = route.query.boardId
    console.log("查询的 board_id:", boardId);
    if (boardId) {
        getBoardInfoByIdApi(boardId);
        getBaHotPosts(boardId);
    } else { ElMessage.error("无boardId"); }
    //帖子信息展示
    const postId = route.query.postId
    console.log("查询的 post_id:", postId);
    if (postId) {
        getOnePost(postId);
        getLposts(postId);
    } else { ElMessage.error("无boardId"); }
})

//===================================================================================
const headerVisible = ref(false);

// 滚动监听逻辑
const handleScroll = () => {
    const scrollTop = window.pageYOffset || document.documentElement.scrollTop;
    headerVisible.value = scrollTop > 210;
};

onMounted(() => {
    window.addEventListener('scroll', handleScroll);
});

onUnmounted(() => {
    window.removeEventListener('scroll', handleScroll);
});
//============================================================================================
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


//============================================================================================================================================
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
                    <el-breadcrumb-item :to="{ path: '/' }" href="#" class="breadcrumb-item">首页</el-breadcrumb-item>
                    <el-breadcrumb-item :to="{ path: '/post' }" href="#" class="breadcrumb-item">帖子</el-breadcrumb-item>
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

        <div style="display: flex; justify-content: center; width: 100%;margin:5px 0 0 0">
            <div class="kapian" style="max-width: 1100px;width: 100%;">
                <!-- 主信息区 -->
                <!-- 吧区 -->
                <div
                    style="display: flex; justify-content: space-between; align-items: center; background-color: white; border-bottom: 1px solid #e4e4e4; padding: 10px 15px;">
                    <!-- 左侧头像 -->
                    <el-avatar class="custom-avatar" :size="70" style="flex-shrink: 0;"
                        :src="BaInfos[0].avaterUrl || '@/assets/tieba.png'">
                    </el-avatar>

                    <!-- 中间标题和关注按钮 -->
                    <div style="display: flex; align-items: center; flex-grow: 1; margin-left: 15px;">
                        <h2 style="font-size: 18px; color: #333; margin: 0 12px 0 0;">{{ BaInfos[0].name }}</h2>
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
                    </div>
                </div>
                <!-- 分页器部分 -->
                <div
                    style="font-family: 'PingFang SC', 'Microsoft YaHei', sans-serif; padding: 10px; background: white; box-shadow: 0 1px 3px rgba(0,0,0,0.1);">
                    <!-- 分页主体 -->
                    <div style="display: flex; align-items: center; flex-wrap: wrap; justify-content: space-between;">
                        <!-- 左侧分页内容 -->
                        <el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize"
                            :page-sizes="[2, 4, 6, 8]" layout="jumper, total, sizes, prev, pager, next" background
                            :total="total" @size-change="onSizeChange" @current-change="onCurrentChange"
                            style="justify-content: flex-end" />
                        <!-- 右侧返回按钮 -->
                        <div>
                            <a style="color: #06aedc; text-decoration: none; font-size: 14px;" @click="goBack">
                                <span style="margin-right: 5px;">←</span>返回{{ BaInfos[0].name }}
                            </a>
                        </div>
                    </div>
                </div>

                <!-- 帖子+排行部分 -->
                <div style="display: flex;flex-direction: row;">
                    <div style="flex:0.7">
                        <!-- 帖子内容区域 -->
                        <div style="display: flex;margin-top: 1px; background-color: white;position: relative;"
                            v-for="post in PostInfos" :key="post.postId">
                            <!-- 帖子标题和作者信息 -->
                            <div
                                style="flex:0.14; display: flex; flex-direction: column; align-items: center;background-color: #f5ffff;">
                                <div class="badge-triangle">
                                    <span class="badge-text1">主</span>
                                    <span class="badge-text2">贴</span>
                                </div>
                                <div style="display: flex; justify-content: center;margin:20px 20px 0 20px">
                                    <img :src="post.UserAvater || '@/assets/tieba.png'" alt="作者头像"
                                        style="width: 90px; height: 90px;">
                                </div>
                                <div style="margin-top: 10px; text-align: center;">
                                    <div style="font-weight: bold; font-size: 16px;">{{ post.UserName }}</div>
                                    <div style="font-size: 12px; color: #999; display: flex; justify-content: center;">
                                        <span>普通用户</span>
                                    </div>
                                </div>
                            </div>
                            <div style="flex:0.86;margin:20px 20px 20px 20px">
                                <!-- 帖子标题 -->
                                <div style="margin-bottom: 15px; font-size: 17px; line-height: 2;">
                                    {{ post.title }}
                                </div>
                                <!-- 帖子正文 -->
                                <div style="margin-bottom: 15px; font-size: 16px; line-height: 1.6;">
                                    {{ post.content }}
                                </div>


                                <!-- 底部信息栏和互动按钮组（水平布局） -->
                                <div
                                    style="display: flex; justify-content: space-between; align-items: center; margin-top: 15px;">
                                    <!-- 底部信息栏 -->
                                    <div></div>

                                    <!-- 互动按钮组 -->
                                    <div style="display: flex; align-items: center;">
                                        <div
                                            style="font-size: 12px; color: #999; display: flex; align-items: center;margin:0 15px 0 0">
                                            <span style="margin: 0 10px 0 0; font-weight: bold;">1楼</span>
                                            <span>地址：{{ post.address }}</span>
                                            <span style="margin: 0 5px;"></span>

                                        </div>
                                        <a href="#"
                                            style="font-size: 12px; color: #999; margin-right: 15px; text-decoration: none;">{{
                                                post.createTime }}</a>
                                        <button
                                            style="background: none; border: none; color: #06aedc; font-size: 14px; cursor: pointer;"
                                            @click="toggleReply">回复</button>
                                    </div>
                                </div>
                                <!-- 回复输入框（默认隐藏） -->
                                <div v-if="showReply_main" class="editor" style="margin-top: 15px;" :model="floorModel">
                                    <h3 style="margin: 10px 0 10px 10px;">
                                        发表回复
                                    </h3>
                                    <quill-editor theme="snow" contentType="html" v-model:content="floorModel.content">
                                    </quill-editor>
                                    <div style="display: flex; justify-content: flex-end;">
                                        <el-button style="margin: 10px 10px;" @click="issue(post)">立刻发表</el-button>
                                    </div>
                                </div>
                            </div>

                        </div>
                        <!-- 楼层帖子区域 -->
                        <div style="display: flex;margin-top: 1px; background-color: white;position: relative;"
                            v-for="post in Lpostinfos" :key="post.floorId">
                            <!-- 帖子标题和作者信息 -->
                            <div
                                style="flex:0.14; display: flex; flex-direction: column; align-items: center;background-color: #f5ffff;">
                                <div style="display: flex; justify-content: center;margin:20px 20px 0 20px">
                                    <img :src="post.UserAvater || '@/assets/tieba.png'" alt="作者头像"
                                        style="width: 90px; height: 90px;">
                                </div>
                                <div style="margin-top: 10px; text-align: center;">
                                    <div style="font-weight: bold; font-size: 16px;">{{ post.UserName }}</div>
                                    <div style="font-size: 12px; color: #999; display: flex; justify-content: center;">
                                        <span>普通用户</span>
                                    </div>
                                </div>
                            </div>
                            <div style="flex:0.86;margin:20px 20px 20px 20px">
                                <!-- 帖子正文 -->
                                <div style="margin-bottom: 15px; font-size: 16px; line-height: 1.6;"
                                    v-html="post.content">
                                </div>


                                <!-- 底部信息栏和互动按钮组（水平布局） -->
                                <div
                                    style="display: flex; justify-content: space-between; align-items: center; margin-top: 15px;">
                                    <div></div>
                                    <!-- 互动按钮组 -->
                                    <div style="display: flex; align-items: center;">
                                        <div
                                            style="font-size: 12px; color: #999; display: flex; align-items: center;margin:0 15px 0 0">
                                            <span style="margin:0 10px 0 0; font-weight: bold;">{{ post.floorNumber
                                                }}楼</span>
                                            <span>地址：{{ post.address }}</span>
                                            <span style="margin: 0 5px;"></span>

                                        </div>
                                        <a href="#"
                                            style="font-size: 12px; color: #999; margin-right: 15px; text-decoration: none;">{{
                                                post.createTime }}</a>
                                        <button style="background: none; border: none; color: #ff2d00; font-size: 14px; cursor: pointer;
                                        text-decoration: none;" @mouseenter="post.showUnderline_Comment = true"
                                            @mouseleave="post.showUnderline_Comment = false"
                                            :style="{ textDecoration: post.showUnderline_Comment ? 'underline' : 'none' }"
                                            @click="ShowComment(post.floorId)">评论</button>
                                        <div v-if="post.EmojiChooseShow"
                                            style="position: absolute; z-index: 9999; right: 0; top: 100%;">
                                            <EmojiPicker :native="true"
                                                @select="(emoji) => onVue3EmojiPicker(emoji, post.floorId)" />
                                        </div>
                                    </div>
                                </div>
                                <!-- 评论显示 -->
                                <div v-if="post.showReply" v-for="comt in Commentinfo" :key="comt.commentId"
                                    style=" background: #f5f5f5; border-radius: 4px; padding: 12px;">
                                    <div style="display: flex;">
                                        <!-- 头像 -->
                                        <el-avatar style="width: 50px; height: 50px; margin-right: 12px;" shape="square"
                                            :src="comt.UserAvaterUrl || '@/assets/tieba.png'" />

                                        <!-- 评论内容 -->
                                        <div style="flex: 1;">
                                            <!-- 用户名和评论内容 -->
                                            <div style="display: flex; margin:10px 0 0 0; align-items: flex-start;">
                                                <span
                                                    style="font-size: 16px; font-weight: 600; color: #008080; margin-right: 5px; white-space: nowrap;">
                                                    {{ comt.UserName }}
                                                </span>
                                                <div v-if="comt.parentCommentId"
                                                    style="font-size: 14px; color: #333333; line-height: 1.5; margin-bottom: 8px; word-wrap: break-word;">
                                                    回复
                                                </div>
                                                <span v-if="comt.parentCommentId"
                                                    style="font-size: 16px; font-weight: 600; color: #008080; margin-right: 5px; white-space: nowrap;">
                                                    {{ comt.parentCommentUserName }}
                                                </span>
                                                <div
                                                    style="font-size: 14px; color: #333333; line-height: 1.5; margin-bottom: 8px; word-wrap: break-word;">
                                                    :{{ comt.content }}
                                                </div>
                                            </div>

                                            <!-- 操作按钮和时间（靠右对齐） -->
                                            <div
                                                style="display: flex; justify-content: flex-end; align-items: center; gap: 8px;">
                                                <span style="font-size: 12px; color: #999999;">{{ comt.createTime
                                                    }}</span>
                                                <el-button style="font-size: 12px; color: #666666; padding: 0;text-decoration: none;
                                                 cursor: pointer;" link @mouseenter="comt.showUnderline_Answer = true"
                                                    @mouseleave="comt.showUnderline_Answer = false"
                                                    :style="{ textDecoration: comt.showUnderline_Answer ? 'underline' : 'none' }"
                                                    @click="CommentAnswer(comt, post)">
                                                    回复
                                                </el-button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <!-- 回复输入框（默认隐藏） -->
                                <div v-if="post.showReply"
                                    style="background: #f5f5f5; padding: 10px; border-radius: 4px;">
                                    <!-- 回复头部信息 -->

                                    <!-- 回复输入区域 -->
                                    <div style="position: relative;">
                                        <textarea v-model="CommentModel.content"
                                            :placeholder="post.placeholder || '输入回复内容...'"
                                            style="width: 98%; min-height: 30px; padding: 10px; border: 1px solid #ddd; border-radius: 4px;"></textarea>

                                        <!-- 表情和发表按钮 -->
                                        <div
                                            style="display: flex; justify-content: flex-end; align-items: center; margin-top: 10px;">
                                            <button @click="issueComment(post)"
                                                style="margin-left: 15px; background: #06aedc; color: white; border: none; padding: 5px 15px; border-radius: 4px; cursor: pointer;">
                                                发表
                                            </button>
                                        </div>
                                    </div>
                                </div>


                            </div>

                        </div>
                        <!-- 分页器部分 -->
                        <div
                            style="font-family: 'PingFang SC', 'Microsoft YaHei', sans-serif; padding: 10px; background: white; box-shadow: 0 1px 3px rgba(0,0,0,0.1);">
                            <!-- 分页主体 -->
                            <div
                                style="display: flex; align-items: center; flex-wrap: wrap; justify-content: space-between;">
                                <!-- 左侧分页内容 -->
                                <el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize"
                                    :page-sizes="[2, 4, 6, 8]" layout="jumper, total, sizes, prev, pager, next"
                                    background :total="total" @size-change="onSizeChange"
                                    @current-change="onCurrentChange" style="justify-content: flex-end" />
                            </div>
                        </div>
                    </div>

                    <!-- 热度排行榜区域 -->
                    <div style="flex:0.3">
                        <div
                            style="font-family: 'PingFang SC', 'Microsoft YaHei', sans-serif;background: white; box-shadow: 0 1px 3px rgba(0,0,0,0.1); padding: 15px;margin:1px 0 0 1px">
                            <h2
                                style="font-size: 18px; color: #333; margin: 0 0 15px 0; padding-bottom: 10px; border-bottom: 1px solid #f0f0f0;">
                                热议榜</h2>

                            <ol style="list-style: none; padding: 0; margin: 0;">
                                <li style="display: flex; align-items: center; justify-content: space-between; padding: 10px 0; border-bottom: 1px solid #f5f5f5;"
                                    v-for="(post, index) in HotPostInfos" :key="post.postId">
                                    <div style="display: flex; align-items: center;">
                                        <span
                                            style="font-size: 14px; font-weight: bold; color: #f85959; width: 15px; text-align: center; ">{{
                                                index
                                                + 1 }} </span>
                                        <span style="font-size: 14px; color: #333; margin-left: 8px;
                                            cursor: pointer;text-decoration: none;
                                            display: inline-block;max-width: 230px;white-space: nowrap;overflow: hidden;
                                            text-overflow: ellipsis;" 
                                            @mouseenter="post.showUnderline_hot = true"
                                            @mouseleave="post.showUnderline_hot = false"
                                            :style="{ textDecoration: post.showUnderline_hot ? 'underline' : 'none' }"
                                            @click="hotpostclick(post.postId)">{{
                                                post.title
                                            }}</span>
                                    </div>
                                    <span style="font-size: 13px; color: #999;">{{ post.viewCount }}</span>
                                </li>

                            </ol>
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
    padding: 12px 12px !important;

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

.breadcrumb-item:hover {
    text-decoration: underline;
}

.editor {
    width: 100%;
    height: 300px;
    /* 固定高度 */
    position: relative;
    display: flex;
    flex-direction: column;
    border: 1px solid #ddd;
    overflow: hidden;
    /* 隐藏外部滚动条 */
}

:deep(.ql-toolbar.ql-snow) {
    position: sticky;
    top: 0;
    z-index: 10;
    background: white;
    border-bottom: 1px solid #ddd;
}

:deep(.ql-container.ql-snow) {
    flex: 1;
    overflow-y: auto;
    /* 内部滚动 */
    border: #ddd;
}

:deep(.ql-editor) {
    min-height: 150px;
    /* 最小高度 */
}

.editor>div:last-child {
    /* 发表按钮容器 */
    position: sticky;
    bottom: 0;
    background: white;
    border-top: 1px solid #ddd;
    z-index: 5;
}

.badge-triangle {
    position: absolute;
    top: 0;
    right: 0;
    border-style: solid;
    border-width: 0 60px 60px 0;
    border-color: transparent #1E90FF transparent transparent;
}

.badge-text1 {
    position: absolute;
    left: 28px;
    top: -3px;
    right: 0;
    color: white;
    font-size: 15px;
    font-weight: bold;
    transform: rotate(45deg);
    text-shadow: 0 0 2px rgba(0, 0, 0, 0.3);
}

.badge-text2 {
    position: absolute;
    left: 41px;
    top: 11px;
    right: 0;
    color: white;
    font-size: 15px;
    font-weight: bold;
    transform: rotate(45deg);
    text-shadow: 0 0 2px rgba(0, 0, 0, 0.3);
}
</style>
