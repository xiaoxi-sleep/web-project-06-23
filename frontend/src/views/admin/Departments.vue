<template>
  <div>
    <el-card>
      <template #header>
        <div style="display:flex;justify-content:space-between;align-items:center">
          <span style="font-weight:bold;font-size:16px">院系管理</span>
          <el-button type="primary" @click="openDialog()">添加院系</el-button>
        </div>
      </template>
      <el-table :data="list" stripe v-loading="loading">
        <el-table-column prop="name" label="院系名称" min-width="200" />
        <el-table-column prop="description" label="描述" min-width="300" />
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{row}">
            <el-button type="primary" size="small" @click="openDialog(row)">编辑</el-button>
            <el-button type="danger" size="small" @click="doDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog :title="isEdit ? '编辑院系' : '添加院系'" v-model="dialogVisible" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="名称"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="描述"><el-input v-model="form.description" type="textarea" :rows="3" /></el-form-item>
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
import { getDepartments, addDepartment, updateDepartment, deleteDepartment } from '../../api/admin'
import { ElMessage, ElMessageBox } from 'element-plus'

const list = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const saving = ref(false)
const form = ref({ name: '', description: '' })

onMounted(async () => { await loadData() })

async function loadData() {
  loading.value = true
  try { const res = await getDepartments(); list.value = res.data } finally { loading.value = false }
}

function openDialog(row) {
  if (row) { isEdit.value = true; form.value = { ...row } }
  else { isEdit.value = false; form.value = { name: '', description: '' } }
  dialogVisible.value = true
}

async function doSave() {
  saving.value = true
  try {
    if (isEdit.value) { await updateDepartment(form.value); ElMessage.success('更新成功') }
    else { await addDepartment(form.value); ElMessage.success('添加成功') }
    dialogVisible.value = false
    await loadData()
  } finally { saving.value = false }
}

async function doDelete(id) {
  try {
    await ElMessageBox.confirm('确定删除该院系吗？', '确认', { type: 'warning' })
    await deleteDepartment(id)
    ElMessage.success('删除成功')
    await loadData()
  } catch (e) {}
}
</script>
