package com.courseselection.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.courseselection.entity.Course;
import com.courseselection.entity.Selection;
import com.courseselection.mapper.CourseMapper;
import com.courseselection.mapper.SelectionMapper;
import com.courseselection.service.SelectionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SelectionServiceImpl extends ServiceImpl<SelectionMapper, Selection> implements SelectionService {

    private final CourseMapper courseMapper;

    public SelectionServiceImpl(CourseMapper courseMapper) {
        this.courseMapper = courseMapper;
    }

    @Override
    @Transactional
    public void selectCourse(Long studentId, Long courseId) {
        Course course = courseMapper.selectById(courseId);
        if (course == null) {
            throw new RuntimeException("课程不存在");
        }
        if (course.getStatus() != null && course.getStatus() == 0) {
            throw new RuntimeException("该课程已停开");
        }
        if (course.getEnrolledCount() >= course.getCapacity()) {
            throw new RuntimeException("课程名额已满");
        }

        // 检查是否已选过该课程
        long count = count(new LambdaQueryWrapper<Selection>()
                .eq(Selection::getStudentId, studentId)
                .eq(Selection::getCourseId, courseId)
                .eq(Selection::getStatus, "selected"));
        if (count > 0) {
            throw new RuntimeException("你已经选过该课程");
        }

        // 检查时间冲突：获取已选课程
        List<Selection> mySelections = list(new LambdaQueryWrapper<Selection>()
                .eq(Selection::getStudentId, studentId)
                .eq(Selection::getStatus, "selected"));
        for (Selection sel : mySelections) {
            Course selected = courseMapper.selectById(sel.getCourseId());
            if (selected != null && selected.getSchedule() != null
                    && selected.getSchedule().equals(course.getSchedule())) {
                throw new RuntimeException("时间冲突：你已选择该时间段的其他课程");
            }
        }

        // 创建选课记录
        Selection selection = new Selection();
        selection.setStudentId(studentId);
        selection.setCourseId(courseId);
        selection.setSelectTime(LocalDateTime.now());
        selection.setStatus("selected");
        save(selection);

        // 更新课程已选人数
        course.setEnrolledCount(course.getEnrolledCount() + 1);
        courseMapper.updateById(course);
    }

    @Override
    @Transactional
    public void dropCourse(Long selectionId) {
        Selection selection = getById(selectionId);
        if (selection == null) {
            throw new RuntimeException("选课记录不存在");
        }
        if ("dropped".equals(selection.getStatus())) {
            throw new RuntimeException("已退课");
        }

        selection.setStatus("dropped");
        updateById(selection);

        // 恢复课程名额
        Course course = courseMapper.selectById(selection.getCourseId());
        if (course != null && course.getEnrolledCount() > 0) {
            course.setEnrolledCount(course.getEnrolledCount() - 1);
            courseMapper.updateById(course);
        }
    }

    @Override
    public List<Selection> getByStudentId(Long studentId) {
        return list(new LambdaQueryWrapper<Selection>()
                .eq(Selection::getStudentId, studentId)
                .eq(Selection::getStatus, "selected"));
    }
}
