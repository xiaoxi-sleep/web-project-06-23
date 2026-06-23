package com.courseselection.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.courseselection.entity.Teacher;

public interface TeacherService extends IService<Teacher> {
    Teacher getByUserId(Long userId);
}
