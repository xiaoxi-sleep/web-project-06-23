package com.courseselection.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.courseselection.entity.Selection;
import java.util.List;

public interface SelectionService extends IService<Selection> {
    void selectCourse(Long studentId, Long courseId);
    void dropCourse(Long selectionId);
    List<Selection> getByStudentId(Long studentId);
}
