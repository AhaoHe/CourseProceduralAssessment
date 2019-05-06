package com.study.project4.com.controller;

import com.study.project4.com.dao.TeacherMapper;
import com.study.project4.com.entity.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {
    @Autowired
    TeacherMapper teacherMapper;

    @GetMapping("/findTeaByID/{id}")
    public Teacher getTeaByID(@PathVariable("id") Integer id){
        return teacherMapper.getTeaByID(id);
    }

    @GetMapping("/addTeacher")
    public Teacher getTeaByID(Teacher teacher){
        teacherMapper.addTeacher(teacher);
        return  teacher;
    }
}
