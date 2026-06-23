<template>
  <div>
    <h3 style="margin-bottom:20px;color:#303133">欢迎回来，{{ store.name }}</h3>
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card shadow="hover" :body-style="{padding:'24px'}">
          <div style="display:flex;align-items:center">
            <el-icon :size="48" color="#409eff"><Collection /></el-icon>
            <div style="margin-left:16px">
              <div style="font-size:28px;font-weight:bold;color:#303133">{{ stats.availableCourses }}</div>
              <div style="color:#909399;font-size:14px">可选课程</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" :body-style="{padding:'24px'}">
          <div style="display:flex;align-items:center">
            <el-icon :size="48" color="#67c23a"><Select /></el-icon>
            <div style="margin-left:16px">
              <div style="font-size:28px;font-weight:bold;color:#303133">{{ stats.selectedCourses }}</div>
              <div style="color:#909399;font-size:14px">已选课程</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" :body-style="{padding:'24px'}">
          <div style="display:flex;align-items:center">
            <el-icon :size="48" color="#e6a23c"><Star /></el-icon>
            <div style="margin-left:16px">
              <div style="font-size:28px;font-weight:bold;color:#303133">{{ stats.totalCredits }}</div>
              <div style="color:#909399;font-size:14px">已选学分</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-card style="margin-top:20px">
      <template #header><span style="font-weight:bold">快捷入口</span></template>
      <el-row :gutter="20">
        <el-col :span="8"><el-button type="primary" style="width:100%" @click="$router.push('/student/courses')">浏览课程</el-button></el-col>
        <el-col :span="8"><el-button type="success" style="width:100%" @click="$router.push('/student/schedule')">查看课表</el-button></el-col>
        <el-col :span="8"><el-button style="width:100%" @click="$router.push('/student/selections')">已选课程</el-button></el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '../../stores/user'
import { getCourses, getSelections } from '../../api/student'

const store = useUserStore()
const stats = ref({ availableCourses: 0, selectedCourses: 0, totalCredits: 0 })

onMounted(async () => {
  try {
    const [coursesRes, selRes] = await Promise.all([getCourses(), getSelections()])
    stats.value.availableCourses = coursesRes.data.length
    stats.value.selectedCourses = selRes.data.length
    stats.value.totalCredits = coursesRes.data
      .filter(c => selRes.data.some(s => s.courseId === c.id))
      .reduce((sum, c) => sum + Number(c.credit || 0), 0)
  } catch (e) {}
})
</script>
