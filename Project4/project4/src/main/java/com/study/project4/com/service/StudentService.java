package com.study.project4.com.service;

import com.study.project4.com.dao.AdminMapper;
import com.study.project4.com.dao.StudentsMapper;
import com.study.project4.com.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentsMapper studentsMapper;
    @Autowired
    private AdminMapper adminMapper;

    //查询所有学生
    public List<Student> getStuAll(){
        return  studentsMapper.getStuAll();
    }
    public Student getStuByid(Integer id){
        return  studentsMapper.getStuByid(id);
    }

    //添加学生
    public Student addStu(Student student){
        studentsMapper.insetStu(student);
        return student;
    }

    //修改学生
    public  Student updateStu(Student student){
        adminMapper.updateStu(student);
        return student;
    }
}
