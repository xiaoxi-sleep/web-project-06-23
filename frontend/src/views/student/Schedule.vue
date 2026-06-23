<template>
  <div>
    <el-card>
      <template #header><span style="font-weight:bold;font-size:16px">我的课表</span></template>
      <div v-if="scheduleData.length === 0" style="text-align:center;padding:40px;color:#909399">
        <el-empty description="暂未选课，课表为空" />
      </div>
      <el-table v-else :data="scheduleData" stripe v-loading="loading">
        <el-table-column prop="schedule" label="时间" width="150" />
        <el-table-column prop="courseName" label="课程名称" min-width="150" />
        <el-table-column prop="courseNo" label="课程编号" width="120" />
        <el-table-column prop="department" label="开课院系" width="160" />
        <el-table-column prop="classroom" label="教室" width="120" />
        <el-table-column prop="credit" label="学分" width="80" align="center" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getCourses, getSelections } from '../../api/student'

const scheduleData = ref([])
const loading = ref(false)

onMounted(async () => {
  loading.value = true
  try {
    const [cRes, sRes] = await Promise.all([getCourses(), getSelections()])
    const courseMap = {}
    cRes.data.forEach(c => { courseMap[c.id] = c })
    scheduleData.value = sRes.data.map(s => {
      const c = courseMap[s.courseId]
      return c ? {
        schedule: c.schedule || '未安排',
        courseName: c.courseName,
        courseNo: c.courseNo,
        department: c.department,
        classroom: c.classroom || '待定',
        credit: c.credit
      } : null
    }).filter(Boolean).sort((a, b) => (a.schedule || '').localeCompare(b.schedule || ''))
  } finally { loading.value = false }
})
</script>
