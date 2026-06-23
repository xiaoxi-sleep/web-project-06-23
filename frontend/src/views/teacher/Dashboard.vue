<template>
  <div>
    <h3 style="margin-bottom:20px;color:#303133">欢迎回来，{{ store.name }}</h3>
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card shadow="hover" :body-style="{padding:'24px'}">
          <div style="display:flex;align-items:center">
            <el-icon :size="48" color="#409eff"><Notebook /></el-icon>
            <div style="margin-left:16px">
              <div style="font-size:28px;font-weight:bold">{{ stats.myCourses }}</div>
              <div style="color:#909399;font-size:14px">我的课程</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" :body-style="{padding:'24px'}">
          <div style="display:flex;align-items:center">
            <el-icon :size="48" color="#67c23a"><UserFilled /></el-icon>
            <div style="margin-left:16px">
              <div style="font-size:28px;font-weight:bold">{{ stats.totalStudents }}</div>
              <div style="color:#909399;font-size:14px">选课学生总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" :body-style="{padding:'24px'}">
          <div style="display:flex;align-items:center">
            <el-icon :size="48" color="#e6a23c"><School /></el-icon>
            <div style="margin-left:16px">
              <div style="font-size:28px;font-weight:bold">{{ stats.department }}</div>
              <div style="color:#909399;font-size:14px">所属院系</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '../../stores/user'
import { getMyCourses, getTeacherProfile } from '../../api/teacher'

const store = useUserStore()
const stats = ref({ myCourses: 0, totalStudents: 0, department: '' })

onMounted(async () => {
  try {
    const [cRes, pRes] = await Promise.all([getMyCourses(), getTeacherProfile()])
    stats.value.myCourses = cRes.data.length
    stats.value.department = pRes.data.department || '未设置'
    // 简单统计学生总数
    let total = 0
    for (const c of cRes.data) {
      total += (c.enrolledCount || 0)
    }
    stats.value.totalStudents = total
  } catch (e) {}
})
</script>
