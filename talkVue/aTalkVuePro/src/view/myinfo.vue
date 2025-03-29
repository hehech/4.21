<script setup>



</script>


<template>
  <el-container style="height: 100vh;">
    <el-header class="basicSS" style="display: flex; justify-content: space-between; align-items: center;">
      <!-- 左侧导航内容 -->
      <div>
        <el-breadcrumb style="font-size: 16px;">
          <el-breadcrumb-item href="#">首页</el-breadcrumb-item>
          <el-breadcrumb-item href="#">我的信息</el-breadcrumb-item>
        </el-breadcrumb>
      </div>

      <!-- 右侧导航组件 -->
      <div>
        <el-button-group>
          <el-button icon="search">返回</el-button>
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
        <div
          style="height: 250px; background: linear-gradient(135deg, #6e8efb, #a777e3); position: relative; border-radius: 4px;">
          <div
            style="position: absolute; top: 0; left: 0; right: 0; bottom: 0; background: rgba(0, 0, 0, 0.2); border-radius: 4px;">
          </div>
          <el-avatar :size="125" :src="avatar1"
            style="position: absolute; left: 50px; bottom: -80px; border: 4px solid white; box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);"></el-avatar>
          <el-button type="primary"  style="margin:210px 0 10px 185px">
            编辑资料
          </el-button>
        </div>

        <!-- 个人信息卡片 -->
        <el-card style="">
          <div style="display: flex; align-items: center; gap: 15px; margin:0 0 0 170px">
            <h2 style="margin: 0; font-size: 24px; color: #333;">用户名</h2>
            <el-tag type="info" size="small">性别: 男</el-tag>
            <el-tag type="info" size="small">吧龄: 3年</el-tag>
            <el-tag type="info" size="small">发贴: 256</el-tag>
            <el-tag type="info" size="small">地区: 长春</el-tag>
            <el-tag type="info" size="small">生日: 2013-09-02</el-tag>
          </div>

          <el-divider style="margin: 40px 0 0 0;"></el-divider>

          <div style="margin:20px 0 0 50px">
            <h3 style="margin-bottom: 10px; color: #333;">个人简介</h3>
            <p style="color: #666; line-height: 1.6;">这里是用户的个人简介内容，可以包含兴趣爱好、个性签名等信息。</p>
          </div>

        </el-card>

        <!-- 发帖记录 -->
        <el-card style="margin: 5px 0 0 0;">
          <template #header>
            <div style="display: flex; justify-content: space-between; align-items: center;">
              <span>我的动态</span>   
              <el-button type="text">查看更多</el-button>
            </div>
          </template>
          <el-timeline>
            <el-timeline-item v-for="(post, index) in 3" :key="index" :timestamp="'2025-03-2' + index">
              <el-card>
                <h4 style="margin: 0 0 10px 0; color: #333;">帖子标题 {{ index + 1 }}</h4>
                <p style="margin: 0; color: #666;">这里是帖子内容的简要描述...</p>
              </el-card>
            </el-timeline-item>
          </el-timeline>
        </el-card>
      </div>
    </el-main>
  </el-container>
</template>



<style scoped>
.basicSS {
  background: #ffffff;
}
</style>