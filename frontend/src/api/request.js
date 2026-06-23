import axios from 'axios'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../stores/user'

const request = axios.create({
  baseURL: '/api',
  timeout: 10000
})

request.interceptors.request.use(config => {
  const store = useUserStore()
  if (store.token) {
    config.headers.Authorization = `Bearer ${store.token}`
  }
  return config
})

request.interceptors.response.use(
  response => {
    if (response.data.code !== 200) {
      ElMessage.error(response.data.message || '请求失败')
      return Promise.reject(new Error(response.data.message))
    }
    return response.data
  },
  error => {
    if (error.response) {
      const { status, data } = error.response
      if (status === 401) {
        const store = useUserStore()
        store.logout()
        ElMessage.error('登录已过期，请重新登录')
      } else if (status === 403) {
        ElMessage.error('没有访问权限')
      } else {
        ElMessage.error(data?.message || '服务器错误')
      }
    } else {
      ElMessage.error('网络错误')
    }
    return Promise.reject(error)
  }
)

export default request
