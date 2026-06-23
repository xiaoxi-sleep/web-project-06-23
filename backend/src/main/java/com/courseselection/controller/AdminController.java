package com.courseselection.controller;

import com.courseselection.common.Result;
import com.courseselection.entity.*;
import com.courseselection.service.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final StudentService studentService;
    private final TeacherService teacherService;
    private final CourseService courseService;
    private final DepartmentService departmentService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AdminController(StudentService studentService,
                          TeacherService teacherService,
                          CourseService courseService,
                          DepartmentService departmentService,
                          UserService userService,
                          PasswordEncoder passwordEncoder) {
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.courseService = courseService;
        this.departmentService = departmentService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // ============ 学生管理 ============
    @GetMapping("/students")
    public Result<List<Student>> listStudents() {
        List<Student> students = studentService.list();
        for (Student s : students) {
            if (s.getUserId() != null) {
                User u = userService.getById(s.getUserId());
                if (u != null) s.setName(u.getName());
            }
        }
        return Result.success(students);
    }

    @PostMapping("/students")
    @Transactional
    public Result<?> addStudent(@RequestBody Student student) {
        try {
            User user = new User();
            user.setUsername(student.getStudentNo());
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRole("student");
            user.setName(student.getName() != null && !student.getName().isEmpty() ? student.getName() : student.getStudentNo());
            user.setStatus(1);
            userService.save(user);

            student.setUserId(user.getId());
            studentService.save(student);
            return Result.success();
        } catch (Exception e) {
            return Result.error("添加失败: " + e.getMessage());
        }
    }

    @PutMapping("/students")
    public Result<?> updateStudent(@RequestBody Student student) {
        studentService.updateById(student);
        return Result.success();
    }

    @DeleteMapping("/students/{id}")
    @Transactional
    public Result<?> deleteStudent(@PathVariable Long id) {
        Student student = studentService.getById(id);
        if (student != null) {
            userService.removeById(student.getUserId());
            studentService.removeById(id);
        }
        return Result.success();
    }

    // ============ 教师管理 ============
    @GetMapping("/teachers")
    public Result<List<Teacher>> listTeachers() {
        List<Teacher> teachers = teacherService.list();
        for (Teacher t : teachers) {
            if (t.getUserId() != null) {
                User u = userService.getById(t.getUserId());
                if (u != null) t.setName(u.getName());
            }
        }
        return Result.success(teachers);
    }

    @PostMapping("/teachers")
    @Transactional
    public Result<?> addTeacher(@RequestBody Teacher teacher) {
        try {
            User user = new User();
            user.setUsername(teacher.getTeacherNo());
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRole("teacher");
            user.setName(teacher.getName() != null && !teacher.getName().isEmpty() ? teacher.getName() : teacher.getTeacherNo());
            user.setStatus(1);
            userService.save(user);

            teacher.setUserId(user.getId());
            teacherService.save(teacher);
            return Result.success();
        } catch (Exception e) {
            return Result.error("添加失败: " + e.getMessage());
        }
    }

    @PutMapping("/teachers")
    public Result<?> updateTeacher(@RequestBody Teacher teacher) {
        teacherService.updateById(teacher);
        return Result.success();
    }

    @DeleteMapping("/teachers/{id}")
    @Transactional
    public Result<?> deleteTeacher(@PathVariable Long id) {
        Teacher teacher = teacherService.getById(id);
        if (teacher != null) {
            userService.removeById(teacher.getUserId());
            teacherService.removeById(id);
        }
        return Result.success();
    }

    // ============ 课程管理 ============
    @GetMapping("/courses")
    public Result<List<Course>> listCourses() {
        return Result.success(courseService.list());
    }

    @PostMapping("/courses")
    public Result<?> addCourse(@RequestBody Course course) {
        course.setEnrolledCount(0);
        courseService.save(course);
        return Result.success();
    }

    @PutMapping("/courses")
    public Result<?> updateCourse(@RequestBody Course course) {
        courseService.updateById(course);
        return Result.success();
    }

    @DeleteMapping("/courses/{id}")
    public Result<?> deleteCourse(@PathVariable Long id) {
        courseService.removeById(id);
        return Result.success();
    }

    // ============ 院系管理 ============
    @GetMapping("/departments")
    public Result<List<Department>> listDepartments() {
        return Result.success(departmentService.list());
    }

    @PostMapping("/departments")
    public Result<?> addDepartment(@RequestBody Department department) {
        departmentService.save(department);
        return Result.success();
    }

    @PutMapping("/departments")
    public Result<?> updateDepartment(@RequestBody Department department) {
        departmentService.updateById(department);
        return Result.success();
    }

    @DeleteMapping("/departments/{id}")
    public Result<?> deleteDepartment(@PathVariable Long id) {
        departmentService.removeById(id);
        return Result.success();
    }
}
