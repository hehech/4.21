<script setup>
import { ref, computed, onMounted, reactive } from 'vue'
import { findUser} from '@/api/user.js'
import { ElMessage, ElDialog } from 'element-plus'

// 隐私设置响应式对象
const privacySettings = reactive({
  profileVisible: 0,
  postVisible: 0
})

// 导航项配置
const navItems = [
  { key: 'security', label: '账号安全' },
  { key: 'security', label: '账号安全' },
  { key: 'privacy', label: '隐私设置' },
  { key: 'notification', label: '消息通知' }
]

// 用户数据
const isDark = ref(false)
const activeNav = ref('security')
const userData = ref({
  account: '',
  password: '',
  avatarUrl: '',
  nickname: '',
  bio: '',
  RePassword: ''
})

// 帮助相关数据
const helpDialogVisible = ref(false)
const currentHelp = ref({
  title: '',
  content: ''
})

const helpOptions = [
  {
    key: 'password',
    label: '如何修改密码？',
    title: '密码修改指南',
    content: '1. 进入账号安全页面\n2. 点击「修改密码」按钮\n3. 通过手机验证后设置新密码'
  },
  {
    key: 'login',
    label: '登录异常处理',
    title: '登录问题解决',
    content: '1. 检查网络连接\n2. 清除浏览器缓存\n3. 联系客服人员'
  },
  {
    key: 'appeal',
    label: '账号申诉指南',
    title: '账号申诉流程',
    content: '1. 准备身份证明\n2. 填写申诉表单\n3. 等待审核结果'
  },
  {
    key: 'delete',
    label: '注销账号说明',
    title: '账号注销协议',
    content: '1. 备份重要数据\n2. 确认注销操作\n3. 完成最终验证'
  }
]

// 密码修改相关数据
const passwordDialogVisible = ref(false)
const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})
const passwordRules = {
  oldPassword: [
    { required: true, message: '请输入原密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能小于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== passwordForm.newPassword) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}
const passwordFormRef = ref(null)

// 初始化加载
onMounted(() => find())

const find = async () => {
  try {
    const result = await findUser({
      account: userData.value.account
    })

    userData.value = {
      ...userData.value,
      nickname: result.data.nickname || '未命名用户',
      avatarUrl: result.data.avatar || '',
      bio: result.data.bio || '暂无简介'
    }

    ElMessage.success(result.msg || '用户信息加载完成')
  } catch (error) {
    ElMessage.error('查询失败: ' + (error.response?.data?.msg || '服务器错误'))
  }
}

// 密码修改对话框
const showPasswordDialog = () => {
  passwordDialogVisible.value = true
  passwordForm.oldPassword = ''
  passwordForm.newPassword = ''
  passwordForm.confirmPassword = ''
}

const handlePasswordChange = async () => {
  if (!passwordFormRef.value) return

  await passwordFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const result = await changePassword({
          oldPassword: passwordForm.oldPassword,
          newPassword: passwordForm.newPassword
        })
        ElMessage.success('密码修改成功')
        passwordDialogVisible.value = false
      } catch (error) {
        ElMessage.error(error.response?.data?.msg || '密码修改失败')
      }
    }
  })
}

const showHelpDialog = (helpKey) => {
  const target = helpOptions.find(item => item.key === helpKey)
  currentHelp.value = {
    title: target.title,
    content: target.content
  }
  helpDialogVisible.value = true
}
</script>

<template>
  <div class="account-container">
    <!-- 左侧导航 -->
    <aside class="nav-sidebar">
      <div class="logo-area">
        <img src="@/assets/logo.jpg" alt="Logo" style="height: 40px;">
      </div>
      <nav class="menu-group">
        <div
            v-for="item in navItems"
            :key="item.key"
            :class="['menu-item', { 'active': activeNav === item.key }]"
            @click="activeNav = item.key"
        >
          {{ item.label }}
        </div>
      </nav>
    </aside>

    <!-- 中间内容区 -->
    <main class="content-main">
      <!-- 账号安全内容 -->
      <div v-if="activeNav === 'security'" class="security-content">
        <h3 class="section-title">账号安全</h3>
        <el-card>
          <div class="security-item">
            <span>登录密码</span>
            <el-button type="text" @click="showPasswordDialog">修改密码</el-button>
          </div>
          <div class="security-item">
            <span>绑定手机</span>
            <span class="bind-info">{{ userData.phone || '未绑定' }}</span>
            <el-button type="text">更换手机</el-button>
          </div>
        </el-card>
      </div>

      <!-- 隐私设置内容 -->
      <div v-if="activeNav === 'privacy'" class="privacy-content">
        <h3 class="section-title">隐私设置</h3>
        <el-card>
          <div class="privacy-item">
            <span>个人主页可见性</span>
            <el-radio-group v-model="privacySettings.profileVisible">
              <el-radio :label="0">公开</el-radio>
              <el-radio :label="1">仅好友</el-radio>
              <el-radio :label="2">私密</el-radio>
            </el-radio-group>
          </div>
          <div class="privacy-item">
            <span>动态可见范围</span>
            <el-select v-model="privacySettings.postVisible">
              <el-option label="所有人可见" :value="0"></el-option>
              <el-option label="仅好友可见" :value="1"></el-option>
            </el-select>
          </div>
        </el-card>
      </div>
    </main>

    <!-- 右侧信息栏 -->
    <div class="user-profile">
      <!-- 头像 -->
      <div class="avatar-area" v-if="userData.avatarUrl">
        <img :src="userData.avatarUrl" alt="用户头像">
      </div>

      <!-- 基本信息 -->
      <div class="user-header">
        <h3>{{ userData.nickname }}</h3>
        <p class="nickname">ID: {{ userData.account }}</p>
      </div>

      <!-- 个性签名 -->
      <div class="user-bio">
        {{ userData.bio || '这个用户还没有签名~' }}
      </div>

      <!-- 修改后的帮助中心 -->
      <div class="help-section">
        <h4 class="help-title">帮助中心</h4>
        <div class="help-links">
          <div
              v-for="(help, index) in helpOptions"
              :key="index"
              class="help-link"
              @click="showHelpDialog(help.key)"
          >
            {{ help.label }}
          </div>
        </div>
      </div>
    </div>

    <!-- 新增帮助弹窗 -->
    <el-dialog
        v-model="helpDialogVisible"
        :title="currentHelp.title"
        width="30%"
    >
      <p>{{ currentHelp.content }}</p>
    </el-dialog>

    <!-- 密码修改弹窗 -->
    <el-dialog
        v-model="passwordDialogVisible"
        title="修改密码"
        width="400px"
        :close-on-click-modal="false"
    >
      <el-form
          ref="passwordFormRef"
          :model="passwordForm"
          :rules="passwordRules"
          label-width="100px"
      >
        <el-form-item label="原密码" prop="oldPassword">
          <el-input
              v-model="passwordForm.oldPassword"
              type="password"
              placeholder="请输入原密码"
              show-password
          />
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input
              v-model="passwordForm.newPassword"
              type="password"
              placeholder="请输入新密码"
              show-password
          />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input
              v-model="passwordForm.confirmPassword"
              type="password"
              placeholder="请再次输入新密码"
              show-password
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="passwordDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handlePasswordChange">确认修改</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.account-container {
  display: grid;
  grid-template-columns: 120px 1fr 280px;
  grid-template-areas: "nav main profile";
  min-height: 100vh;
  background: #f5f5f5;
}

.nav-sidebar {
  grid-area: nav;
  background: #fff;
  border-right: 1px solid #e8e8e8;
  padding-top: 20px;
}

.logo-area {
  padding: 20px 10px;
  display: flex;
  justify-content: center;
}

.logo-area img {
  height: 50px;
  width: 50px;
  object-fit: contain;
}

.menu-group {
  margin-top: 30px;
}

.menu-item {
  padding: 12px 8px;
  margin: 8px 0;
  border-radius: 4px;
  font-size: 13px;
  text-align: center;
  white-space: nowrap;
  cursor: pointer;
  transition: all 0.3s;
}

.menu-item:hover {
  background: #f0f0f0;
  color: #1890ff;
}

.menu-item.active {
  background: #e6f7ff;
  color: #1890ff;
  border-right: 3px solid #1890ff;
}

.content-main {
  grid-area: main;
  padding: 24px;
  background: #fff;
}

.user-profile {
  grid-area: profile;
  padding: 24px;
  background: #fff;
  border-left: 1px solid #e8e8e8;
}

.help-links {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.help-link {
  color: #1890ff;
  cursor: pointer;
  transition: color 0.3s;
}

.help-link:hover {
  color: #40a9ff;
  text-decoration: underline;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 20px;
}
</style>