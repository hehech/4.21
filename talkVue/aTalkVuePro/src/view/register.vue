<!-- 1. 在view文件夹下创建Login文件夹，Login文件夹下创建login.vue文件
2.结合html+element构建页面 -->
<script setup lang="ts">
import { ref } from 'vue'// Vue 3 响应式声明

//注册数据模型
const registerData=ref({
    account:'',
    password:'',
    nickname:'',
    RePassword:''
})

//密码校验函数
const checkRePassword=(rule,value,callback)=>{
    if(value===''){
        callback(new Error('请再次确认密码'))
    }else if(value !==registerData.value.password){
        callback(new Error('请确保两次输入的密码相同'))
    }else{
        callback()
    }
}

//数据校验规则
const rules={
    nickname:[
        {required:true,message:"请输入昵称",trigger:'blur'},
        {trigger:'blur'}
    ],
    account:[
        {required:true,message:"请输入账号",trigger:'blur'},
        {min:8,max:16,message:"长度为8-16的非空字符",trigger:'blur'}
    ],
    password:[
        {required:true,message:"请输入密码",trigger:'blur'},
        {min:5,max:16,message:"长度为5-16的非空字符",trigger:'blur'}
    ],
    rePassword:[
        {validator:checkRePassword,trigger:'blur'}
    ]
}

//调用后台接口完成注册
import {userRegisterService} from '@/api/user.js'
import { ElMessage } from 'element-plus'
import router from '@/router'
const register =async()=>{
     let result=await userRegisterService(registerData.value);
    ElMessage.success(result.msg?result.msg:'注册成功');
    router.push('/login');
}

const ReturnToLogin = () => {
    router.push('/login');
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
      value: '#01FCCA' // 粒子点的颜色
    },
    links: {
      color: '#03EDF6', // 线条颜色
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
</script>

<template>
  <div class="login">
    <!-- 背景 -->
    <Particles id="tsparticles" class="login__particles" :options="options" />
    <!-- 注册表单 -->
    <div class="loginPart">
      <h2>注册</h2>
      <el-form status-icon label-width="100px" class="demo-ruleForm" style="transform:translate(-30px);" :model="registerData" :rules="rules">
        <el-form-item  prop="nickname">
          <el-input placeholder="请输入昵称"v-model="registerData.nickname" maxlength="20" clearable />
        </el-form-item>
        <el-form-item  prop="account">
          <el-input placeholder="请输入账号"v-model="registerData.account" maxlength="20" clearable />
        </el-form-item>
        <el-form-item  prop="password">
          <el-input type="password" placeholder="请输入密码"v-model="registerData.password" maxlength="20" show-password clearable />
        </el-form-item>
        <el-form-item  prop="RePassword">
          <el-input type="password" placeholder="再次输入密码"v-model="registerData.RePassword" maxlength="20" show-password clearable />
        </el-form-item>
        <el-button class="btn" type="primary" @click="register">注册</el-button>
        <div style="text-align: right;">
          <el-link @click="ReturnToLogin">返回</el-link>
        </div>
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
  background: rgba(0, 26, 27, 0.418);
  /*背景颜色为黑色，透明度为0.8*/
  box-sizing: border-box;
  /*box-sizing设置盒子模型的解析模式为怪异盒模型，
    将border和padding划归到width范围内*/
  box-shadow: 0px 10px 25px rgba(0, 130, 97, 0.5);
  /*边框阴影  水平阴影0 垂直阴影15px 模糊25px 颜色黑色透明度0.5*/
  border-radius: 15px;
  /*边框圆角，四个角均为15px*/
}

h2 {
  margin: 0 0 30px;
  padding: 0;
  color: #79f9db;
  text-align: center;
  /*文字居中*/
}

.btn {
  /* 1. 布局优化 */
  position: relative;
  left: 100px; /* 替代 transform，更符合直觉的定位方式 */
  width: 250px;
  height: 30px;
  
  /* 2. 视觉优化 */
  background: #00d6af; /* 明确按钮背景色 */
  border: 2px solid #00aab3; /* 深色边框增强对比 */
  border-radius: 8px; /* 更柔和的圆角 */
  color: white; /* 高对比度文字 */
  font-size: 16px; /* 改善可读性 */
  line-height: 40px; /* 垂直居中文字 */
  padding: 0 15px; /* 内边距避免文字溢出 */
  text-align: center; /* 水平居中（冗余但保险） */
  
  /* 3. 交互优化 */
  transition: 
    background 0.3s ease,
    transform 0.2s ease; /* 平滑过渡效果 */
  cursor: pointer;
  
  /* 悬停状态 */
  &:hover:not(:disabled) {
    background: #00695e;
    transform: translateY(-2px); /* 微妙上浮效果 */
    box-shadow: 0 2px 8px rgba(0, 86, 187, 0.2); /* 投影增强立体感 */
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
    left: 50%; /* 居中显示 */
    transform: translateX(-50%);
    width: 150px; /* 适应移动端屏幕 */
  }
}
</style>