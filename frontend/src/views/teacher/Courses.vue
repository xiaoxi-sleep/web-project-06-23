<template>
  <div>
    <el-card>
      <template #header><span style="font-weight:bold;font-size:16px">我的课程</span></template>
      <el-table :data="courses" stripe v-loading="loading">
        <el-table-column prop="courseNo" label="课程编号" width="120" />
        <el-table-column prop="courseName" label="课程名称" min-width="150" />
        <el-table-column prop="schedule" label="上课时间" width="140" />
        <el-table-column prop="classroom" label="教室" width="120" />
        <el-table-column prop="credit" label="学分" width="80" align="center" />
        <el-table-column label="选课人数" width="120" align="center">
          <template #default="{row}">{{ row.enrolledCount }}/{{ row.capacity }}</template>
        </el-table-column>
        <el-table-column label="操作" width="120" align="center">
          <template #default="{row}">
            <el-button type="primary" size="small" @click="showStudents(row)">查看学生</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="'选修「' + currentCourse.courseName + '」的学生'" width="700px">
      <el-table :data="students" stripe>
        <el-table-column prop="studentNo" label="学号" width="130" />
        <el-table-column prop="studentName" label="姓名" width="100" />
        <el-table-column prop="department" label="院系" width="160" />
        <el-table-column prop="major" label="专业" width="150" />
        <el-table-column label="选课时间" width="170">
          <template #default="{row}">{{ row.selectTime?.substring(0,16).replace('T',' ') }}</template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getMyCourses, getCourseStudents } from '../../api/teacher'

const courses = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const currentCourse = ref({})
const students = ref([])

onMounted(async () => {
  loading.value = true
  try {
    const res = await getMyCourses()
    courses.value = res.data
  } finally { loading.value = false }
})

async function showStudents(course) {
  currentCourse.value = course
  dialogVisible.value = true
  const res = await getCourseStudents(course.id)
  students.value = res.data
}
</script>
