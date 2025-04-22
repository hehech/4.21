<script setup>
import { ElMessage } from 'element-plus'
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useTokenStore } from '@/stores/token.js'
const tokenStore = useTokenStore()
const router = useRouter();
//==================================================================================================================================================
//搜索框变量
//返回按钮功能实现
const goBack = () => {
  router.go(-1); // 返回上一页
};
//=================================================================================================================================================
const imageUrl = ref('')
const activeTab = ref('accountSettings') // 默认激活的tab
// 表单数据
const accountForm = ref({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})
import { Cpassword } from '@/api/user';
const changepassword = async () => {  
  try {
    // 前端验证
    if (!accountForm.value.oldPassword) {
      ElMessage.warning('请输入原密码');
      return;
    }

    if (!accountForm.value.newPassword) {
      ElMessage.warning('请输入新密码');
      return;
    }

    if (accountForm.value.newPassword.length < 8 || accountForm.value.newPassword.length > 16) {
      ElMessage.warning('新密码长度需在8-16位之间');
      return;
    }

    if (!accountForm.value.confirmPassword) {
      ElMessage.warning('请确认新密码');
      return;
    }

    if (accountForm.value.newPassword !== accountForm.value.confirmPassword) {
      ElMessage.warning('两次输入的新密码不一致');
      return;
    }

    if (accountForm.value.oldPassword === accountForm.value.newPassword) {
      ElMessage.warning('新密码不能与原密码相同');
      return;
    }
    // 调用API
    const rs = await Cpassword(accountForm.value);
    // 处理响应
    if (rs.code === 0) { // 假设200表示成功
      ElMessage.success(rs.msg || '密码修改成功，请重新登录');
      tokenStore.removeToken();
      router.push({
        path: "/login"
      });
      // 清空表单
      accountForm.value = {
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      };
    } else {
      ElMessage.error(rs.msg || '密码修改失败');
    }
  } catch (error) {
    // 错误处理
    console.error('修改密码出错:', error);
    ElMessage.error('修改密码时发生错误，请稍后再试');
  }
}
const profileForm = ref({
  userId:'',
  nickname: '',
  avaterUrl: '',
  bio: '',
  address: ''
})
import {findUser,saveinfo} from '@/api/user.js'
const showinfo=async()=>{
  const rs=await findUser();
  profileForm.value=rs.data;
}
showinfo();
const changeinfo=async()=>{
  const rs=await saveinfo(profileForm.value);
  ElMessage.success(rs.msg?rs.msg:"修改成功");
  showinfo();
}
//===============================================================================================================================================

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
          <el-breadcrumb-item :to="{ path: '/accountinfo' }" class="breadcrumb-item">账号设置</el-breadcrumb-item>
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
            <el-tab-pane label="账号信息设置" name="accountSettings">
              <h2 style="font-size: 24px; margin:20px 0 30px 0; color: #333;">修改密码</h2>

              <el-form label-position="top" style="max-width: 800px;">
                <!-- 原密码 -->
                <el-form-item label="原密码" required>
                  <el-input v-model="accountForm.oldPassword" type="password" placeholder="请输入当前密码" show-password
                    style="width: 100%;"></el-input>
                </el-form-item>

                <!-- 新密码 -->
                <el-form-item label="新密码" required>
                  <el-input v-model="accountForm.newPassword" type="password" placeholder="请输入新密码（8-16位字符）"
                    show-password style="width: 100%;"></el-input>
                  <div style="font-size: 12px; color: #999; margin-top: 5px;">
                    建议使用字母、数字和符号的组合
                  </div>
                </el-form-item>

                <!-- 确认密码 -->
                <el-form-item label="确认新密码" required>
                  <el-input v-model="accountForm.confirmPassword" type="password" placeholder="请再次输入新密码" show-password
                    style="width: 100%;"></el-input>
                </el-form-item>
              </el-form>
              <!-- 提交按钮 -->
              <div style="margin: 0 30px 30px 0;">
                <el-button type="primary" size="large" @click="changepassword" style="width: 120px;">修改密码</el-button>
              </div>
            </el-tab-pane>

            <el-tab-pane label="个人信息设置" name="profileSettings">
              <h2 style="font-size: 24px; margin:20px 0 30px 0; color: #333;">个人信息设置</h2>

              <el-form label-position="top" style="max-width: 800px;">
                <!-- 昵称 -->
                <el-form-item label="昵称" required>
                  <el-input v-model="profileForm.nickname" placeholder="请输入昵称（2-12个字符）" maxlength="12" show-word-limit
                    style="width: 100%;">
                  </el-input>
                </el-form-item>

                <!-- 头像URL -->
                <el-form-item label="头像URL">
                  <el-input v-model="profileForm.avaterUrl" @input="imageUrl = profileForm.avatarUrl"
                    placeholder="请输入头像图片URL" style="width: 100%;">
                  </el-input>

                  <!-- 头像预览 -->
                  <div v-if="imageUrl" style="margin-top: 15px;">
                    <div style="font-size: 14px; color: #666; margin-bottom: 8px;">头像预览：</div>
                    <img :src="imageUrl"
                      style="width: 100px; height: 100px; border: 1px solid #eee; border-radius: 50%; object-fit: cover;"
                      onerror="this.style.display='none'">
                  </div>
                </el-form-item>

                <!-- 个人简介 -->
                <el-form-item label="个人简介">
                  <el-input v-model="profileForm.bio" type="textarea" :rows="4" placeholder="介绍一下你自己吧（最多100字）"
                    maxlength="200" show-word-limit style="width: 100%;">
                  </el-input>
                </el-form-item>

                <!-- 所在地 -->
                <el-form-item label="所在地">
                  <el-input v-model="profileForm.address" placeholder="请输入你的所在地" maxlength="30" style="width: 100%;">
                  </el-input>
                </el-form-item>
              </el-form>
              <!-- 提交按钮 -->
              <div style="margin: 0 30px 30px 0;">
                <el-button @click="changeinfo"     type="primary" size="large" style="width: 120px;">保存信息</el-button>
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