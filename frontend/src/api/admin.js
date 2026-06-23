import request from './request'

export function getStudents() { return request.get('/admin/students') }
export function addStudent(data) { return request.post('/admin/students', data) }
export function updateStudent(data) { return request.put('/admin/students', data) }
export function deleteStudent(id) { return request.delete(`/admin/students/${id}`) }

export function getTeachers() { return request.get('/admin/teachers') }
export function addTeacher(data) { return request.post('/admin/teachers', data) }
export function updateTeacher(data) { return request.put('/admin/teachers', data) }
export function deleteTeacher(id) { return request.delete(`/admin/teachers/${id}`) }

export function getAdminCourses() { return request.get('/admin/courses') }
export function addCourse(data) { return request.post('/admin/courses', data) }
export function updateCourse(data) { return request.put('/admin/courses', data) }
export function deleteCourse(id) { return request.delete(`/admin/courses/${id}`) }

export function getDepartments() { return request.get('/admin/departments') }
export function addDepartment(data) { return request.post('/admin/departments', data) }
export function updateDepartment(data) { return request.put('/admin/departments', data) }
export function deleteDepartment(id) { return request.delete(`/admin/departments/${id}`) }
