<script setup>
import avatar1 from '@/assets/login.jpeg'
</script>

<template>









  <div style="max-width: 1000px; margin: 0 auto; padding: 20px;">
    <!-- 顶部背景区域 -->
    <div style="height: 200px; background: linear-gradient(135deg, #6e8efb, #a777e3); position: relative; border-radius: 4px; margin-bottom: 80px;">
      <div style="position: absolute; top: 0; left: 0; right: 0; bottom: 0; background: rgba(0, 0, 0, 0.2); border-radius: 4px;"></div>
      <el-avatar :size="100" :src="avatar1" style="position: absolute; left: 30px; bottom: -50px; border: 4px solid white; box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);"></el-avatar>
    </div>

    <!-- 个人信息卡片 -->
    <el-card style="margin-bottom: 20px;">
      <div style="display: flex; align-items: center; gap: 15px; margin-bottom: 10px;">
        <h2 style="margin: 0; font-size: 24px; color: #333;">用户名</h2>
        <el-tag type="info" size="small">吧龄: 3年</el-tag>
        <el-tag type="info" size="small">发贴: 256</el-tag>
      </div>

      <el-divider></el-divider>

      <div style="padding: 0 20px;">
        <el-row :gutter="20">
          <el-col :span="8">
            <div style="display: flex; align-items: center; gap: 8px; margin-bottom: 15px; color: #666;">
              <el-icon><User /></el-icon>
              <span>性别: 男</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div style="display: flex; align-items: center; gap: 8px; margin-bottom: 15px; color: #666;">
              <el-icon><Location /></el-icon>
              <span>地区: 北京</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div style="display: flex; align-items: center; gap: 8px; margin-bottom: 15px; color: #666;">
              <el-icon><Calendar /></el-icon>
              <span>生日: 1990-01-01</span>
            </div>
          </el-col>
        </el-row>

        <div style="margin-top: 20px;">
          <h3 style="margin-bottom: 10px; color: #333;">个人简介</h3>
          <p style="color: #666; line-height: 1.6;">这里是用户的个人简介内容，可以包含兴趣爱好、个性签名等信息。</p>
        </div>
      </div>
    </el-card>

    <!-- 发帖记录 -->
    <el-card style="margin-top: 20px;">
      <template #header>
        <div style="display: flex; justify-content: space-between; align-items: center;">
          <span>我的发帖</span>
          <el-button type="text">查看更多</el-button>
        </div>
      </template>
      <el-timeline>
        <el-timeline-item
          v-for="(post, index) in 3"
          :key="index"
          :timestamp="'2025-03-2' + index"
        >
          <el-card>
            <h4 style="margin: 0 0 10px 0; color: #333;">帖子标题 {{ index + 1 }}</h4>
            <p style="margin: 0; color: #666;">这里是帖子内容的简要描述...</p>
          </el-card>
        </el-timeline-item>
      </el-timeline>
    </el-card>
  </div>
</template>