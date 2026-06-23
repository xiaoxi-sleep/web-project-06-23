package com.courseselection.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.courseselection.entity.Student;
import com.courseselection.mapper.StudentMapper;
import com.courseselection.service.StudentService;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    @Override
    public Student getByUserId(Long userId) {
        return getOne(new LambdaQueryWrapper<Student>().eq(Student::getUserId, userId));
    }
}
