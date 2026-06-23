<template>
  <div>
    <el-card>
      <template #header>
        <div style="display:flex;justify-content:space-between;align-items:center">
          <span style="font-weight:bold;font-size:16px">课程浏览</span>
          <el-input v-model="search" placeholder="搜索课程名称" style="width:240px" clearable :prefix-icon="Search" />
        </div>
      </template>
      <el-table :data="filteredCourses" stripe style="width:100%" v-loading="loading">
        <el-table-column prop="courseNo" label="课程编号" width="120" />
        <el-table-column prop="courseName" label="课程名称" min-width="150" />
        <el-table-column prop="department" label="开课院系" width="160" />
        <el-table-column prop="schedule" label="上课时间" width="140" />
        <el-table-column prop="classroom" label="教室" width="120" />
        <el-table-column label="学分" width="70" align="center">
          <template #default="{row}">{{ row.credit }}</template>
        </el-table-column>
        <el-table-column label="容量" width="100" align="center">
          <template #default="{row}">{{ row.enrolledCount }}/{{ row.capacity }}</template>
        </el-table-column>
        <el-table-column label="操作" width="100" align="center" fixed="right">
          <template #default="{row}">
            <el-button v-if="!isSelected(row.id)" type="primary" size="small" @click="doSelect(row)" :disabled="row.enrolledCount >= row.capacity">选课</el-button>
            <el-tag v-else type="success">已选</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getCourses, selectCourse, getSelections } from '../../api/student'
import { ElMessage } from 'element-plus'
import { Search } from '@element-plus/icons-vue'

const courses = ref([])
const selections = ref([])
const search = ref('')
const loading = ref(false)

const selectedIds = computed(() => selections.value.map(s => s.courseId))
const isSelected = (id) => selectedIds.value.includes(id)
const filteredCourses = computed(() => {
  if (!search.value) return courses.value
  return courses.value.filter(c => c.courseName.includes(search.value) || c.courseNo.includes(search.value))
})

onMounted(async () => {
  loading.value = true
  try {
    const [cRes, sRes] = await Promise.all([getCourses(), getSelections()])
    courses.value = cRes.data
    selections.value = sRes.data
  } finally { loading.value = false }
})

async function doSelect(course) {
  try {
    await selectCourse(course.id)
    ElMessage.success('选课成功')
    const [cRes, sRes] = await Promise.all([getCourses(), getSelections()])
    courses.value = cRes.data
    selections.value = sRes.data
  } catch (e) {}
}
</script>
