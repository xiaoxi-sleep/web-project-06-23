package com.courseselection.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.courseselection.entity.Course;
import com.courseselection.mapper.CourseMapper;
import com.courseselection.service.CourseService;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {
}
