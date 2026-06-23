<template>
  <div style="min-height:100vh;display:flex;align-items:center;justify-content:center;background:linear-gradient(135deg,#667eea 0%,#764ba2 100%)">
    <el-card style="width:420px;border-radius:8px">
      <template #header>
        <div style="text-align:center">
          <h2 style="margin:0;color:#303133">学生选课管理系统</h2>
          <p style="margin:8px 0 0;color:#909399;font-size:14px">Course Selection System</p>
        </div>
      </template>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="0" size="large">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" :prefix-icon="User" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" :prefix-icon="Lock" show-password @keyup.enter="handleLogin" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" style="width:100%" @click="handleLogin">登 录</el-button>
        </el-form-item>
      </el-form>
      <div style="text-align:center;color:#909399;font-size:13px;margin-top:8px">
        <p>测试账号：admin / teacher1 / student1</p>
        <p>密码均为：123456</p>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'

const router = useRouter()
const store = useUserStore()
const formRef = ref(null)
const loading = ref(false)

const form = reactive({ username: '', password: '' })
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

async function handleLogin() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  loading.value = true
  try {
    await store.login(form)
    ElMessage.success('登录成功')
    router.push(`/${store.role}/dashboard`)
  } catch (e) {
    // error handled by interceptor
  } finally {
    loading.value = false
  }
}
</script>
