<template>
  <div>
    <h3 style="margin-bottom:20px">数据统计</h3>
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card shadow="hover" :body-style="{padding:'20px'}"><div style="text-align:center"><div style="font-size:32px;color:#409eff;font-weight:bold">{{ stats.students }}</div><div style="color:#909399;margin-top:8px">学生总数</div></div></el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" :body-style="{padding:'20px'}"><div style="text-align:center"><div style="font-size:32px;color:#67c23a;font-weight:bold">{{ stats.teachers }}</div><div style="color:#909399;margin-top:8px">教师总数</div></div></el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" :body-style="{padding:'20px'}"><div style="text-align:center"><div style="font-size:32px;color:#e6a23c;font-weight:bold">{{ stats.courses }}</div><div style="color:#909399;margin-top:8px">课程总数</div></div></el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" :body-style="{padding:'20px'}"><div style="text-align:center"><div style="font-size:32px;color:#f56c6c;font-weight:bold">{{ stats.selections }}</div><div style="color:#909399;margin-top:8px">选课记录</div></div></el-card>
      </el-col>
    </el-row>
    <el-card style="margin-top:20px">
      <template #header><span style="font-weight:bold">课程选课排行</span></template>
      <el-table :data="topCourses" stripe>
        <el-table-column prop="courseName" label="课程名称" />
        <el-table-column prop="enrolledCount" label="已选人数" width="120" align="center" />
        <el-table-column prop="capacity" label="容量" width="100" align="center" />
        <el-table-column label="选课率" width="120" align="center">
          <template #default="{row}">{{ row.capacity > 0 ? Math.round(row.enrolledCount / row.capacity * 100) : 0 }}%</template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getStudents, getTeachers, getAdminCourses } from '../../api/admin'

const stats = ref({ students: 0, teachers: 0, courses: 0, selections: 0 })
const topCourses = ref([])

onMounted(async () => {
  const [sRes, tRes, cRes] = await Promise.all([getStudents(), getTeachers(), getAdminCourses()])
  stats.value.students = sRes.data.length
  stats.value.teachers = tRes.data.length
  stats.value.courses = cRes.data.length
  stats.value.selections = cRes.data.reduce((sum, c) => sum + (c.enrolledCount || 0), 0)
  topCourses.value = [...cRes.data].sort((a, b) => (b.enrolledCount || 0) - (a.enrolledCount || 0)).slice(0, 10)
})
</script>
