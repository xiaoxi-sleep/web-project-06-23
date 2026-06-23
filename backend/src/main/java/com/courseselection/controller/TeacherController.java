package com.courseselection.controller;

import com.courseselection.common.Result;
import com.courseselection.entity.*;
import com.courseselection.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController {

    private final TeacherService teacherService;
    private final CourseService courseService;
    private final SelectionService selectionService;
    private final StudentService studentService;
    private final UserService userService;

    public TeacherController(TeacherService teacherService, CourseService courseService,
                            SelectionService selectionService, StudentService studentService,
                            UserService userService) {
        this.teacherService = teacherService;
        this.courseService = courseService;
        this.selectionService = selectionService;
        this.studentService = studentService;
        this.userService = userService;
    }

    private Teacher getCurrentTeacher() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getByUsername(auth.getName());
        return teacherService.getByUserId(user.getId());
    }

    // 我的课程
    @GetMapping("/courses")
    public Result<List<Course>> myCourses() {
        Teacher teacher = getCurrentTeacher();
        List<Course> courses = courseService.list();
        courses = courses.stream()
                .filter(c -> c.getTeacherId().equals(teacher.getId()))
                .collect(Collectors.toList());
        return Result.success(courses);
    }

    // 课程学生名单（含学生姓名学号）
    @GetMapping("/courses/{courseId}/students")
    public Result<List<Map<String, Object>>> courseStudents(@PathVariable Long courseId) {
        List<Selection> selections = selectionService.list();
        List<Map<String, Object>> result = selections.stream()
                .filter(s -> s.getCourseId().equals(courseId) && "selected".equals(s.getStatus()))
                .map(s -> {
                    Map<String, Object> m = new HashMap<>();
                    Student st = studentService.getById(s.getStudentId());
                    User u = userService.getById(st.getUserId());
                    m.put("selectionId", s.getId());
                    m.put("studentNo", st.getStudentNo());
                    m.put("studentName", u.getName());
                    m.put("department", st.getDepartment());
                    m.put("major", st.getMajor());
                    m.put("selectTime", s.getSelectTime());
                    return m;
                })
                .collect(Collectors.toList());
        return Result.success(result);
    }

    // 个人信息
    @GetMapping("/profile")
    public Result<Teacher> profile() {
        return Result.success(getCurrentTeacher());
    }
}
