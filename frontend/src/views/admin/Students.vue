<template>
  <div>
    <el-card>
      <template #header>
        <div style="display:flex;justify-content:space-between;align-items:center">
          <span style="font-weight:bold;font-size:16px">学生管理</span>
          <el-button type="primary" @click="openDialog()">添加学生</el-button>
        </div>
      </template>
      <el-table :data="list" stripe v-loading="loading">
        <el-table-column prop="studentNo" label="学号" width="140" />
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column prop="department" label="院系" width="160" />
        <el-table-column prop="major" label="专业" width="150" />
        <el-table-column prop="grade" label="年级" width="100" />
        <el-table-column prop="className" label="班级" width="130" />
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{row}">
            <el-button type="primary" size="small" @click="openDialog(row)">编辑</el-button>
            <el-button type="danger" size="small" @click="doDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog :title="isEdit ? '编辑学生' : '添加学生'" v-model="dialogVisible" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="学号"><el-input v-model="form.studentNo" /></el-form-item>
        <el-form-item label="姓名"><el-input v-model="form.name" placeholder="请输入姓名" /></el-form-item>
        <el-form-item label="院系"><el-input v-model="form.department" /></el-form-item>
        <el-form-item label="专业"><el-input v-model="form.major" /></el-form-item>
        <el-form-item label="年级"><el-input v-model="form.grade" /></el-form-item>
        <el-form-item label="班级"><el-input v-model="form.className" /></el-form-item>
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
import { getStudents, addStudent, updateStudent, deleteStudent } from '../../api/admin'
import { ElMessage, ElMessageBox } from 'element-plus'

const list = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const saving = ref(false)
const form = ref({ studentNo: '', name: '', department: '', major: '', grade: '', className: '' })

onMounted(async () => { await loadData() })

async function loadData() {
  loading.value = true
  try { const res = await getStudents(); list.value = res.data } finally { loading.value = false }
}

function openDialog(row) {
  if (row) { isEdit.value = true; form.value = { ...row } }
  else { isEdit.value = false; form.value = { studentNo: '', name: '', department: '', major: '', grade: '', className: '' } }
  dialogVisible.value = true
}

async function doSave() {
  saving.value = true
  try {
    if (isEdit.value) { await updateStudent(form.value); ElMessage.success('更新成功') }
    else { await addStudent(form.value); ElMessage.success('添加成功，默认密码 123456') }
    dialogVisible.value = false
    await loadData()
  } finally { saving.value = false }
}

async function doDelete(id) {
  try {
    await ElMessageBox.confirm('确定删除该学生吗？', '确认', { type: 'warning' })
    await deleteStudent(id)
    ElMessage.success('删除成功')
    await loadData()
  } catch (e) {}
}
</script>
