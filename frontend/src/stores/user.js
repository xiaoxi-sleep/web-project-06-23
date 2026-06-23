import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { login as loginApi } from '../api/auth'
import router from '../router'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userId = ref(Number(localStorage.getItem('userId')) || 0)
  const username = ref(localStorage.getItem('username') || '')
  const role = ref(localStorage.getItem('role') || '')
  const name = ref(localStorage.getItem('name') || '')

  const isLoggedIn = computed(() => !!token.value)

  async function login(form) {
    const res = await loginApi(form)
    token.value = res.data.token
    userId.value = res.data.userId
    username.value = res.data.username
    role.value = res.data.role
    name.value = res.data.name
    localStorage.setItem('token', res.data.token)
    localStorage.setItem('userId', res.data.userId)
    localStorage.setItem('username', res.data.username)
    localStorage.setItem('role', res.data.role)
    localStorage.setItem('name', res.data.name)
  }

  function logout() {
    token.value = ''
    userId.value = 0
    username.value = ''
    role.value = ''
    name.value = ''
    localStorage.clear()
    router.push('/login')
  }

  return { token, userId, username, role, name, isLoggedIn, login, logout }
})
