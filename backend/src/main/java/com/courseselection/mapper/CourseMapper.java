package com.courseselection.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.courseselection.entity.Course;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CourseMapper extends BaseMapper<Course> {
}
