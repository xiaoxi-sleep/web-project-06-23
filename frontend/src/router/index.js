import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '../stores/user'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/student',
    component: () => import('../layouts/StudentLayout.vue'),
    meta: { role: 'student' },
    children: [
      { path: '', redirect: '/student/dashboard' },
      { path: 'dashboard', name: 'StudentDashboard', component: () => import('../views/student/Dashboard.vue'), meta: { title: '学生首页' } },
      { path: 'courses', name: 'StudentCourses', component: () => import('../views/student/Courses.vue'), meta: { title: '课程浏览' } },
      { path: 'schedule', name: 'StudentSchedule', component: () => import('../views/student/Schedule.vue'), meta: { title: '我的课表' } },
      { path: 'selections', name: 'StudentSelections', component: () => import('../views/student/Selections.vue'), meta: { title: '已选课程' } },
      { path: 'profile', name: 'StudentProfile', component: () => import('../views/student/Profile.vue'), meta: { title: '个人信息' } }
    ]
  },
  {
    path: '/teacher',
    component: () => import('../layouts/TeacherLayout.vue'),
    meta: { role: 'teacher' },
    children: [
      { path: '', redirect: '/teacher/dashboard' },
      { path: 'dashboard', name: 'TeacherDashboard', component: () => import('../views/teacher/Dashboard.vue'), meta: { title: '教师首页' } },
      { path: 'courses', name: 'TeacherCourses', component: () => import('../views/teacher/Courses.vue'), meta: { title: '我的课程' } },
      { path: 'profile', name: 'TeacherProfile', component: () => import('../views/teacher/Profile.vue'), meta: { title: '个人信息' } }
    ]
  },
  {
    path: '/admin',
    component: () => import('../layouts/AdminLayout.vue'),
    meta: { role: 'admin' },
    children: [
      { path: '', redirect: '/admin/dashboard' },
      { path: 'dashboard', name: 'AdminDashboard', component: () => import('../views/admin/Dashboard.vue'), meta: { title: '管理首页' } },
      { path: 'students', name: 'AdminStudents', component: () => import('../views/admin/Students.vue'), meta: { title: '学生管理' } },
      { path: 'teachers', name: 'AdminTeachers', component: () => import('../views/admin/Teachers.vue'), meta: { title: '教师管理' } },
      { path: 'courses', name: 'AdminCourses', component: () => import('../views/admin/Courses.vue'), meta: { title: '课程管理' } },
      { path: 'departments', name: 'AdminDepartments', component: () => import('../views/admin/Departments.vue'), meta: { title: '院系管理' } }
    ]
  },
  { path: '/:pathMatch(.*)*', redirect: '/login' }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const store = useUserStore()
  if (to.path === '/login') {
    next()
  } else if (!store.token) {
    next('/login')
  } else if (to.meta.role && to.meta.role !== store.role) {
    next(`/${store.role}/dashboard`)
  } else {
    document.title = to.meta.title ? to.meta.title + ' - 选课系统' : '选课系统'
    next()
  }
})

export default router
