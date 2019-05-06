package com.study.project4.com.service;

import com.study.project4.com.dao.StudentsMapper;
import com.study.project4.com.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentsMapper studentsMapper;

    public List<Student> getStuAll(){

        return  studentsMapper.getStuAll();
    }
    public Student getStuByid(Integer id){
        return  studentsMapper.getStuByid(id);
    }

    public Student addStu(Student student){
        studentsMapper.insetStu(student);
        return student;
    }
}
