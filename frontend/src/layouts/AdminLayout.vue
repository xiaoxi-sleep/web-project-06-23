<template>
  <el-container style="min-height:100vh">
    <el-aside width="220px" style="background:#001529">
      <div style="height:60px;display:flex;align-items:center;justify-content:center;color:#fff;font-size:18px;font-weight:bold;border-bottom:1px solid #002140">
        <el-icon :size="22"><School /></el-icon>
        <span style="margin-left:8px">选课系统</span>
      </div>
      <el-menu :default-active="route.path" background-color="#001529" text-color="#ffffffb3" active-text-color="#fff" router>
        <el-menu-item index="/admin/dashboard"><el-icon><DataAnalysis /></el-icon><span>数据统计</span></el-menu-item>
        <el-menu-item index="/admin/students"><el-icon><UserFilled /></el-icon><span>学生管理</span></el-menu-item>
        <el-menu-item index="/admin/teachers"><el-icon><Avatar /></el-icon><span>教师管理</span></el-menu-item>
        <el-menu-item index="/admin/courses"><el-icon><Notebook /></el-icon><span>课程管理</span></el-menu-item>
        <el-menu-item index="/admin/departments"><el-icon><OfficeBuilding /></el-icon><span>院系管理</span></el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header style="background:#fff;display:flex;align-items:center;justify-content:space-between;border-bottom:1px solid #e8e8e8;padding:0 20px;height:60px">
        <span style="font-size:16px;color:#333">管理员端</span>
        <div>
          <span style="margin-right:16px;color:#666">{{ store.name }}</span>
          <el-button type="danger" size="small" @click="store.logout()">退出登录</el-button>
        </div>
      </el-header>
      <el-main style="background:#f5f7fa;padding:20px">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { useUserStore } from '../stores/user'
import { useRoute } from 'vue-router'
const store = useUserStore()
const route = useRoute()
</script>
