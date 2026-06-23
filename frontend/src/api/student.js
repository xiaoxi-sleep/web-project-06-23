import request from './request'

export function getCourses() {
  return request.get('/student/courses')
}

export function selectCourse(courseId) {
  return request.post(`/student/selections/${courseId}`)
}

export function dropCourse(selectionId) {
  return request.delete(`/student/selections/${selectionId}`)
}

export function getSelections() {
  return request.get('/student/selections')
}

export function getProfile() {
  return request.get('/student/profile')
}
