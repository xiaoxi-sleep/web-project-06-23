<template>
  <div>
    <el-card>
      <template #header>
        <div style="display:flex;justify-content:space-between;align-items:center">
          <span style="font-weight:bold;font-size:16px">教师管理</span>
          <el-button type="primary" @click="openDialog()">添加教师</el-button>
        </div>
      </template>
      <el-table :data="list" stripe v-loading="loading">
        <el-table-column prop="teacherNo" label="工号" width="140" />
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column prop="department" label="院系" width="160" />
        <el-table-column prop="title" label="职称" width="120" />
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{row}">
            <el-button type="primary" size="small" @click="openDialog(row)">编辑</el-button>
            <el-button type="danger" size="small" @click="doDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog :title="isEdit ? '编辑教师' : '添加教师'" v-model="dialogVisible" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="工号"><el-input v-model="form.teacherNo" /></el-form-item>
        <el-form-item label="姓名"><el-input v-model="form.name" placeholder="请输入姓名" /></el-form-item>
        <el-form-item label="院系"><el-input v-model="form.department" /></el-form-item>
        <el-form-item label="职称"><el-input v-model="form.title" /></el-form-item>
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
import { getTeachers, addTeacher, updateTeacher, deleteTeacher } from '../../api/admin'
import { ElMessage, ElMessageBox } from 'element-plus'

const list = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const saving = ref(false)
const form = ref({ teacherNo: '', name: '', department: '', title: '' })

onMounted(async () => { await loadData() })

async function loadData() {
  loading.value = true
  try { const res = await getTeachers(); list.value = res.data } finally { loading.value = false }
}

function openDialog(row) {
  if (row) { isEdit.value = true; form.value = { ...row } }
  else { isEdit.value = false; form.value = { teacherNo: '', name: '', department: '', title: '' } }
  dialogVisible.value = true
}

async function doSave() {
  saving.value = true
  try {
    if (isEdit.value) { await updateTeacher(form.value); ElMessage.success('更新成功') }
    else { await addTeacher(form.value); ElMessage.success('添加成功，默认密码 123456') }
    dialogVisible.value = false
    await loadData()
  } finally { saving.value = false }
}

async function doDelete(id) {
  try {
    await ElMessageBox.confirm('确定删除该教师吗？', '确认', { type: 'warning' })
    await deleteTeacher(id)
    ElMessage.success('删除成功')
    await loadData()
  } catch (e) {}
}
</script>
