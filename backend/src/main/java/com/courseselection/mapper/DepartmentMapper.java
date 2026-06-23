package com.courseselection.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.courseselection.entity.Department;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {
}
