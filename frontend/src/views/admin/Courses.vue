<template>
  <div>
    <el-card>
      <template #header>
        <div style="display:flex;justify-content:space-between;align-items:center">
          <span style="font-weight:bold;font-size:16px">课程管理</span>
          <el-button type="primary" @click="openDialog()">添加课程</el-button>
        </div>
      </template>
      <el-table :data="list" stripe v-loading="loading">
        <el-table-column prop="courseNo" label="课程编号" width="120" />
        <el-table-column prop="courseName" label="课程名称" min-width="150" />
        <el-table-column label="授课教师" width="120">
          <template #default="{row}">{{ getTeacherName(row.teacherId) }}</template>
        </el-table-column>
        <el-table-column prop="schedule" label="上课时间" width="140" />
        <el-table-column prop="classroom" label="教室" width="120" />
        <el-table-column prop="credit" label="学分" width="80" align="center" />
        <el-table-column label="容量" width="100" align="center">
          <template #default="{row}">{{ row.enrolledCount }}/{{ row.capacity }}</template>
        </el-table-column>
        <el-table-column label="状态" width="80" align="center">
          <template #default="{row}">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">{{ row.status === 1 ? '可选' : '停开' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{row}">
            <el-button type="primary" size="small" @click="openDialog(row)">编辑</el-button>
            <el-button type="danger" size="small" @click="doDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog :title="isEdit ? '编辑课程' : '添加课程'" v-model="dialogVisible" width="650px">
      <el-form :model="form" label-width="80px">
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="课程编号"><el-input v-model="form.courseNo" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="课程名称"><el-input v-model="form.courseName" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="授课教师">
              <el-select v-model="form.teacherId" placeholder="请选择教师" style="width:100%" filterable>
                <el-option v-for="t in teacherOptions" :key="t.id" :label="t.name + ' - ' + t.department" :value="t.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12"><el-form-item label="学分"><el-input-number v-model="form.credit" :min="0.5" :step="0.5" :precision="1" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="容量"><el-input-number v-model="form.capacity" :min="1" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="学期"><el-input v-model="form.semester" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="教室"><el-input v-model="form.classroom" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="上课时间"><el-input v-model="form.schedule" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="开课院系">
              <el-select v-model="form.department" placeholder="请选择院系" style="width:100%" filterable allow-create>
                <el-option v-for="d in deptOptions" :key="d.name" :label="d.name" :value="d.name" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12"><el-form-item label="状态">
            <el-switch v-model="form.status" :active-value="1" :inactive-value="0" active-text="可选" inactive-text="停开" />
          </el-form-item></el-col>
        </el-row>
        <el-form-item label="描述"><el-input v-model="form.description" type="textarea" :rows="2" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="doSave" :loading="saving">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getAdminCourses, addCourse, updateCourse, deleteCourse, getTeachers, getDepartments } from '../../api/admin'
import { ElMessage, ElMessageBox } from 'element-plus'

const list = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const saving = ref(false)
const teacherOptions = ref([])
const deptOptions = ref([])

const form = ref({ courseNo: '', courseName: '', teacherId: null, credit: 2, capacity: 60, semester: '', classroom: '', schedule: '', department: '', description: '', status: 1 })

onMounted(async () => { await loadData() })

async function loadData() {
  loading.value = true
  try {
    const [cRes, tRes, dRes] = await Promise.all([getAdminCourses(), getTeachers(), getDepartments()])
    list.value = cRes.data
    teacherOptions.value = tRes.data
    deptOptions.value = dRes.data
  } finally { loading.value = false }
}

function getTeacherName(id) {
  const t = teacherOptions.value.find(t => t.id === id)
  return t ? (t.name || t.teacherNo) : id
}

function openDialog(row) {
  if (row) {
    isEdit.value = true
    form.value = { ...row }
  } else {
    isEdit.value = false
    form.value = { courseNo: '', courseName: '', teacherId: null, credit: 2, capacity: 60, semester: '', classroom: '', schedule: '', department: '', description: '', status: 1 }
  }
  dialogVisible.value = true
}

async function doSave() {
  saving.value = true
  try {
    if (isEdit.value) { await updateCourse(form.value); ElMessage.success('更新成功') }
    else { await addCourse(form.value); ElMessage.success('添加成功') }
    dialogVisible.value = false
    await loadData()
  } finally { saving.value = false }
}

async function doDelete(id) {
  try {
    await ElMessageBox.confirm('确定删除该课程吗？', '确认', { type: 'warning' })
    await deleteCourse(id)
    ElMessage.success('删除成功')
    await loadData()
  } catch (e) {}
}
</script>
