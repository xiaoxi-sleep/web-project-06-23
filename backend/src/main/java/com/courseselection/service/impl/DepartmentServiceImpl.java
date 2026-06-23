package com.courseselection.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.courseselection.entity.Department;
import com.courseselection.mapper.DepartmentMapper;
import com.courseselection.service.DepartmentService;
import org.springframework.stereotype.Service;

@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {
}
