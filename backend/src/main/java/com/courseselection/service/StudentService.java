package com.courseselection.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.courseselection.entity.Student;

public interface StudentService extends IService<Student> {
    Student getByUserId(Long userId);
}
