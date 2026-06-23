<template>
  <div>
    <el-card style="max-width:600px">
      <template #header><span style="font-weight:bold;font-size:16px">个人信息</span></template>
      <el-descriptions :column="1" border v-if="profile" v-loading="loading">
        <el-descriptions-item label="用户名">{{ store.username }}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ store.name }}</el-descriptions-item>
        <el-descriptions-item label="工号">{{ profile.teacherNo }}</el-descriptions-item>
        <el-descriptions-item label="院系">{{ profile.department }}</el-descriptions-item>
        <el-descriptions-item label="职称">{{ profile.title }}</el-descriptions-item>
      </el-descriptions>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '../../stores/user'
import { getTeacherProfile } from '../../api/teacher'

const store = useUserStore()
const profile = ref(null)
const loading = ref(false)

onMounted(async () => {
  loading.value = true
  try {
    const res = await getTeacherProfile()
    profile.value = res.data
  } finally { loading.value = false }
})
</script>
