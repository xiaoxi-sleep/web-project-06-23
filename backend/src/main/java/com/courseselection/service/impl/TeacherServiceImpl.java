package com.courseselection.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.courseselection.entity.Teacher;
import com.courseselection.mapper.TeacherMapper;
import com.courseselection.service.TeacherService;
import org.springframework.stereotype.Service;

@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {

    @Override
    public Teacher getByUserId(Long userId) {
        return getOne(new LambdaQueryWrapper<Teacher>().eq(Teacher::getUserId, userId));
    }
}
