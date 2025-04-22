<!-- 1. 在view文件夹下创建Login文件夹，Login文件夹下创建login.vue文件
2.结合html+element构建页面 -->
<script setup lang="ts">
import { ref, computed } from 'vue'// Vue 3 响应式声明

//注册数据模型
const registerData = ref({
  account: '',
  password: '',
})
//数据校验规则
const rules = {
  account: [
    { required: true, message: "请输入账号", trigger: 'blur' },
    { min: 8, max: 16, message: "长度为8-16的非空字符", trigger: 'blur' }
  ],
  password: [
    { required: true, message: "请输入密码", trigger: 'blur' },
    { min: 5, max: 16, message: "长度为5-16的非空字符", trigger: 'blur' }
  ]
}
//调用登录接口
//登录，复用注册的数据模型
//表单数据校验
//登录按钮函数
import { useTokenStore } from '@/stores/token.js'
import { userLoginService } from '@/api/user'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'//导入创建路由器函数
const tokenStore = useTokenStore()
const router = useRouter()
const login = async () => {
  try {
    const result = await userLoginService(registerData.value);
    ElMessage.success(result.msg || '登录成功');
    tokenStore.setToken(result.data);
    router.push('/');
  } catch (error) {
    // 处理错误
    const errMsg = error.response?.status
      ? `登录失败（状态码：${error.response.status}）`
      : error.message || '登录失败';
    ElMessage.error(errMsg);
    console.error('登录错误详情:', error);
  }
};
const TurnToRegister = () => {
  router.push('/regist');
}
//样式
const options = {
  fpsLimit: 60,
  interactivity: {
    detectsOn: 'canvas',
    events: {
      onClick: { // 开启鼠标点击的效果
        enable: true,
        mode: 'push'
      },
      onHover: { // 开启鼠标悬浮的效果(线条跟着鼠标移动)
        enable: true,
        mode: 'grab'
      },
      resize: true
    },
    modes: { // 配置动画效果
      bubble: {
        distance: 400,
        duration: 2,
        opacity: 0.8,
        size: 40
      },
      push: {
        quantity: 4
      },
      grab: {
        distance: 200,
        duration: 0.4
      },
      attract: { // 鼠标悬浮时，集中于一点，鼠标移开时释放产生涟漪效果
        distance: 200,
        duration: 0.4,
        factor: 5
      }
    }
  },
  particles: {
    color: {
      value: '#00ffb1' // 粒子点的颜色
    },
    links: {
      color: '#00ffb1', // 线条颜色
      distance: 150,//线条距离
      enable: true,
      opacity: 0.5, // 不透明度
      width: 1.2 // 线条宽度
    },
    collisions: {
      enable: true
    },
    move: {
      attract: { enable: false, rotateX: 600, rotateY: 1200 },
      bounce: false,
      direction: 'none',
      enable: true,
      out_mode: 'out',
      random: false,
      speed: 0.5, // 移动速度
      straight: false
    },
    number: {
      density: {
        enable: true,
        value_area: 800
      },
      value: 80//粒子数
    },
    opacity: {//粒子透明度
      value: 0.7
    },
    shape: {//粒子样式
      type: 'triangle'
    },
    size: {//粒子大小
      random: true,
      value: 3
    }
  },
  detectRetina: true
}
//=================================================================================================================================
const loginMethod = ref<'account' | 'phone'>('account'); // 默认显示账号密码登录

const toggleLoginMethod = () => {
  loginMethod.value = loginMethod.value === 'account' ? 'phone' : 'account';
};
//===================================================================================================================================
//手机验证码登录
const phonelogindata = ref({
  phoneNumber: '',
  code: ''
})

const countdown = ref(0)
const isCountingDown = computed(() => countdown.value > 0)

// 发送验证码
import { sendMessage } from '@/api/user.js'
const sendVerificationCode = async () => {
  countdown.value=0;
  if (!phonelogindata.value.phoneNumber) {
    ElMessage.warning('请输入手机号码')
    return
  }

  if (isCountingDown.value) return

  try {
    // 调用后端发送验证码接口
    const rs = await sendMessage(phonelogindata.value.phoneNumber);
    ElMessage.success('验证码已发送')
    // 开始倒计时
    countdown.value = 60
    const timer = setInterval(() => {
      countdown.value--
      if (countdown.value <= 0) {
        clearInterval(timer)
      }
    }, 1000)
  } catch (error) {
    ElMessage.error('发送验证码失败: ' + (error.response?.data?.message || error.message))
  }
}

// 手机验证码登录
import {loginByPhoneApi} from '@/api/user.js'
const loginByPhone = async () => {
  if (!phonelogindata.value.phoneNumber) {
    ElMessage.warning('请输入手机号码')
    return
  }
  
  if (!phonelogindata.value.code) {
    ElMessage.warning('请输入验证码')
    return
  }

  try {
    // 调用手机验证码登录接口
    const result = await loginByPhoneApi(phonelogindata.value);
    
    ElMessage.success(result.msg || '登录成功');
    tokenStore.setToken(result.data);
    router.push('/');
  } catch (error) {
    const errMsg = error.response?.data?.message || error.message || '登录失败';
    ElMessage.error(errMsg);
    console.error('手机登录错误详情:', error);
  }
}
</script>



<template>
  <div class="login">
    <!-- 背景 -->
    <Particles id="tsparticles" class="login__particles" :options="options" />
    <!-- 登录表单 -->
    <div class="loginPart" v-if="loginMethod === 'account'">
      <h2>用户登录</h2>
      <el-form status-icon label-width="100px" style="transform:translate(-30px);" :model="registerData" :rules="rules">
        <el-form-item label="账号：" prop="account">
          <el-input placeholder="请输入账号" v-model="registerData.account" maxlength="20" clearable />
        </el-form-item>
        <el-form-item label="密码：" prop="password">
          <el-input type="password" placeholder="请输入密码" v-model="registerData.password" maxlength="20" show-password
            clearable />
        </el-form-item>
        <!-- <el-form-item label="验证码：">
          <el-input  placeholder="请输入验证码" maxlength="20" clearable />
          <img class="verifyCodeImg">
        </el-form-item> -->
        <el-button class="btn" type="primary" @click="login">登录</el-button>
        <!-- 修改表单部分的代码 -->
        <el-form-item>
          <div class="form-links">
            <el-link type="warning" @click="toggleLoginMethod">手机验证码登录</el-link>
            <el-link type="warning" @click="TurnToRegister">去注册</el-link>
          </div>
        </el-form-item>
      </el-form>
    </div>
    <!-- 手机验证码登录表单 -->
    <div class="loginPart" v-else>
      <h2>手机验证码登录</h2>
      <el-form status-icon label-width="100px" style="transform:translate(-30px);">
        <el-form-item label="手机号码：">

          <el-input v-model="phonelogindata.phoneNumber" placeholder="请输入手机号码" maxlength="11" clearable
            class="phone-input" />
        </el-form-item>

        <el-form-item label="验证码：">
          <div class="phone-input-container">
            <el-input v-model="phonelogindata.code" placeholder="请输入验证码" maxlength="6" clearable class="code-input" />
            <el-link type="primary" @click="sendVerificationCode" :disabled="isCountingDown" class="get-code-link">
              {{ countdown > 0 ? `${countdown}s后重新获取` : '获取验证码' }}
            </el-link>
          </div>


        </el-form-item>

        <el-button class="btn" type="primary" @click="loginByPhone">登录</el-button>
        <el-button class="btn1" type="primary" @click="toggleLoginMethod">返回</el-button>

        <el-form-item>
          <div class="form-links-single">
            <el-link type="warning" @click="TurnToRegister">去注册</el-link>
          </div>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<style scoped>
.login {
  height: 100%;
  width: 100%;
  overflow: hidden;
}

.login__particles {
  height: 100%;
  width: 100%;
  background-size: cover;
  background-repeat: no-repeat;
  background-image: url('@/assets/allbg.jpg');
  opacity: 0.9;
  position: fixed;
  pointer-events: none;
}

.loginPart {
  position: absolute;
  /*定位方式绝对定位absolute*/
  top: 50%;
  left: 80%;
  /*顶和高同时设置50%实现的是同时水平垂直居中效果*/
  transform: translate(-50%, -50%);
  /*实现块元素百分比下居中*/
  width: 450px;
  padding: 50px;
  background: rgba(85, 255, 224, 0.418);
  /*背景颜色为黑色，透明度为0.8*/
  box-sizing: border-box;
  /*box-sizing设置盒子模型的解析模式为怪异盒模型，
    将border和padding划归到width范围内*/
  box-shadow: 0px 10px 25px rgba(0, 16, 16, 0.5);
  /*边框阴影  水平阴影0 垂直阴影15px 模糊25px 颜色黑色透明度0.5*/
  border-radius: 15px;
  /*边框圆角，四个角均为15px*/
}

h2 {
  margin: 0 0 30px;
  padding: 0;
  color: #339887;
  text-align: center;
  /*文字居中*/
}

.btn {
  /* 1. 布局优化 */
  position: relative;
  left: 100px;
  /* 替代 transform，更符合直觉的定位方式 */
  width: 250px;
  height: 30px;

  /* 2. 视觉优化 */
  background: #00d6af;
  /* 明确按钮背景色 */
  border: 2px solid #00aab3;
  /* 深色边框增强对比 */
  border-radius: 8px;
  /* 更柔和的圆角 */
  color: white;
  /* 高对比度文字 */
  font-size: 16px;
  /* 改善可读性 */
  line-height: 40px;
  /* 垂直居中文字 */
  padding: 0 15px;
  /* 内边距避免文字溢出 */
  text-align: center;
  /* 水平居中（冗余但保险） */

  /* 3. 交互优化 */
  transition:
    background 0.3s ease,
    transform 0.2s ease;
  /* 平滑过渡效果 */
  cursor: pointer;

  /* 悬停状态 */
  &:hover:not(:disabled) {
    background: #00695e;
    transform: translateY(-2px);
    /* 微妙上浮效果 */
    box-shadow: 0 2px 8px rgba(0, 86, 187, 0.2);
    /* 投影增强立体感 */
  }

  /* 点击状态 */
  &:active:not(:disabled) {
    transform: translateY(0);
  }

  /* 焦点状态（提升可访问性） */
  &:focus:not(:disabled) {
    outline: 2px solid #fff;
    outline-offset: 2px;
  }

  /* 禁用状态 */
  &disabled {
    background: #cccccc;
    border-color: #aaa;
    cursor: not-allowed;
    opacity: 0.8;
  }
}

.btn1 {
  /* 1. 布局优化 */
  margin: 10px 0 0 100px;
  /* 替代 transform，更符合直觉的定位方式 */
  width: 250px;
  height: 30px;

  /* 2. 视觉优化 */
  background: #00d6af;
  /* 明确按钮背景色 */
  border: 2px solid #00aab3;
  /* 深色边框增强对比 */
  border-radius: 8px;
  /* 更柔和的圆角 */
  color: white;
  /* 高对比度文字 */
  font-size: 16px;
  /* 改善可读性 */
  line-height: 40px;
  /* 垂直居中文字 */
  padding: 0 15px;
  /* 内边距避免文字溢出 */
  text-align: center;
  /* 水平居中（冗余但保险） */

  /* 3. 交互优化 */
  transition:
    background 0.3s ease,
    transform 0.2s ease;
  /* 平滑过渡效果 */
  cursor: pointer;

  /* 悬停状态 */
  &:hover:not(:disabled) {
    background: #00695e;
    transform: translateY(-2px);
    /* 微妙上浮效果 */
    box-shadow: 0 2px 8px rgba(0, 86, 187, 0.2);
    /* 投影增强立体感 */
  }

  /* 点击状态 */
  &:active:not(:disabled) {
    transform: translateY(0);
  }

  /* 焦点状态（提升可访问性） */
  &:focus:not(:disabled) {
    outline: 2px solid #fff;
    outline-offset: 2px;
  }

  /* 禁用状态 */
  &disabled {
    background: #cccccc;
    border-color: #aaa;
    cursor: not-allowed;
    opacity: 0.8;
  }
}

/* 4. 响应式优化（可选） */
@media (max-width: 768px) {
  .btn {
    left: 50%;
    /* 居中显示 */
    transform: translateX(-50%);
    width: 150px;
    /* 适应移动端屏幕 */
  }
}

.form-links {
  display: flex;
  justify-content: space-between;
  width: 100%;
  margin-top: 10px;
}


.form-links .el-link {
  font-size: 14px;
}

.form-links .el-link:hover {
  color: #00d6af;
  text-decoration: underline;
}

.form-links-single {
  display: flex;
  justify-content: flex-end;
  /* 将内容靠右对齐 */
  width: 100%;
  margin-top: 10px;
}

/* 保持原有链接样式 */
.form-links-single .el-link {
  font-size: 14px;
}

.form-links-single .el-link:hover {
  color: #00d6af;
  text-decoration: underline;
}

/* 手机号码输入框容器 */
.phone-input-container {
  display: flex;
  align-items: center;
  gap: 10px;
}

/* 手机号码输入框 */
.phone-input {
  flex: 1;
}

/* 获取验证码链接 */
.get-code-link {
  width: 120px;
  text-align: center;
  white-space: nowrap;
}

/* 验证码输入框 */
.code-input {
  width: 100%;
}

/* 禁用状态的获取验证码链接 */
.get-code-link.is-disabled {
  color: #c0c4cc;
  cursor: not-allowed;
}
</style>