package com.study.project4.com.service;

import com.study.project4.com.dao.TeacherMapper;
import com.study.project4.com.entity.Student;
import com.study.project4.com.entity.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {

    @Autowired
    private TeacherMapper teacherMapper;

    public List<Teacher> getTeaAll(){
        return  teacherMapper.getTeaAll();
    }
    public Teacher getTeaByid(Integer id){
        return  teacherMapper.getTeaByID(id);
    }

    public Teacher addTea(Teacher teacher){
        teacherMapper.addTeacher(teacher);
        return teacher;
    }
}
