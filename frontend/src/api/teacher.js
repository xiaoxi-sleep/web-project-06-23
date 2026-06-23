import request from './request'

export function getMyCourses() {
  return request.get('/teacher/courses')
}

export function getCourseStudents(courseId) {
  return request.get(`/teacher/courses/${courseId}/students`)
}

export function getTeacherProfile() {
  return request.get('/teacher/profile')
}
