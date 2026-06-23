<template>
  <div>
    <el-card>
      <template #header>
        <div style="display:flex;justify-content:space-between;align-items:center">
          <span style="font-weight:bold;font-size:16px">已选课程</span>
          <span style="color:#909399">共 {{ selections.length }} 门，学分合计：{{ totalCredits }}</span>
        </div>
      </template>
      <el-table :data="selections" stripe v-loading="loading">
        <el-table-column prop="course.courseNo" label="课程编号" width="120" />
        <el-table-column prop="course.courseName" label="课程名称" min-width="150" />
        <el-table-column prop="course.department" label="开课院系" width="160" />
        <el-table-column prop="course.schedule" label="上课时间" width="140" />
        <el-table-column prop="course.credit" label="学分" width="80" align="center" />
        <el-table-column label="选课时间" width="170">
          <template #default="{row}">{{ row.selectTime?.substring(0,16).replace('T',' ') }}</template>
        </el-table-column>
        <el-table-column label="操作" width="100" align="center">
          <template #default="{row}">
            <el-button type="danger" size="small" @click="doDrop(row)">退课</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getCourses, getSelections, dropCourse } from '../../api/student'
import { ElMessage, ElMessageBox } from 'element-plus'

const selections = ref([])
const loading = ref(false)
const courseMap = ref({})

const totalCredits = computed(() => 
  selections.value.reduce((sum, s) => sum + Number((s.course?.credit) || 0), 0)
)

onMounted(async () => {
  loading.value = true
  try {
    const [cRes, sRes] = await Promise.all([getCourses(), getSelections()])
    const map = {}
    cRes.data.forEach(c => { map[c.id] = c })
    courseMap.value = map
    selections.value = sRes.data.map(s => ({ ...s, course: map[s.courseId] || {} }))
  } finally { loading.value = false }
})

async function doDrop(row) {
  try {
    await ElMessageBox.confirm('确定要退选该课程吗？', '确认退课', { type: 'warning' })
    await dropCourse(row.id)
    ElMessage.success('退课成功')
    const [cRes, sRes] = await Promise.all([getCourses(), getSelections()])
    const map = {}
    cRes.data.forEach(c => { map[c.id] = c })
    courseMap.value = map
    selections.value = sRes.data.map(s => ({ ...s, course: map[s.courseId] || {} }))
  } catch (e) {}
}
</script>
