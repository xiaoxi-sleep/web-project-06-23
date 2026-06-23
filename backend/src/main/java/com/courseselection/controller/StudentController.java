package com.courseselection.controller;

import com.courseselection.common.Result;
import com.courseselection.entity.*;
import com.courseselection.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    private final StudentService studentService;
    private final CourseService courseService;
    private final SelectionService selectionService;
    private final UserService userService;

    public StudentController(StudentService studentService, CourseService courseService,
                            SelectionService selectionService, UserService userService) {
        this.studentService = studentService;
        this.courseService = courseService;
        this.selectionService = selectionService;
        this.userService = userService;
    }

    private Student getCurrentStudent() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getByUsername(auth.getName());
        return studentService.getByUserId(user.getId());
    }

    // 可选课程列表
    @GetMapping("/courses")
    public Result<List<Course>> listCourses() {
        List<Course> courses = courseService.list();
        courses = courses.stream()
                .filter(c -> c.getStatus() == 1)
                .collect(Collectors.toList());
        return Result.success(courses);
    }

    // 选课
    @PostMapping("/selections/{courseId}")
    public Result<?> selectCourse(@PathVariable Long courseId) {
        Student student = getCurrentStudent();
        selectionService.selectCourse(student.getId(), courseId);
        return Result.success("选课成功");
    }

    // 退课
    @DeleteMapping("/selections/{selectionId}")
    public Result<?> dropCourse(@PathVariable Long selectionId) {
        selectionService.dropCourse(selectionId);
        return Result.success("退课成功");
    }

    // 已选课程列表
    @GetMapping("/selections")
    public Result<List<Selection>> mySelections() {
        Student student = getCurrentStudent();
        return Result.success(selectionService.getByStudentId(student.getId()));
    }

    // 个人信息
    @GetMapping("/profile")
    public Result<Student> profile() {
        return Result.success(getCurrentStudent());
    }
}
