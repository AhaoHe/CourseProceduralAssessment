package com.study.project4.com.service;

import com.study.project4.com.dao.AdminMapper;
import com.study.project4.com.dao.StudentsMapper;
import com.study.project4.com.dao.TeacherMapper;
import com.study.project4.com.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private StudentsMapper studentsMapper;

//获得管理员账户密码，用于登录
    public Admin getAdminByid(Integer id){
        return  adminMapper.getAdminByid(id);
    }

    //删除学生
    public int deleteStu(Integer id){
        return adminMapper.deleteStudent(id);
    }

    //删除老师
    public int deleteTeacher(Integer id){
        return adminMapper.deleteTeacher(id);
    }

    //删除课程
    public int deleteCourse(Integer id){
        return adminMapper.deleteCourse(id);
    }

    //查询哪门课有哪些老师要删除哪些学生
    public List<Course_Students> findTeaDelStuAll(Integer cid){
        return adminMapper.findTeaDelStuAll(cid);
    }


    //添加老师
    public int addTea(Teacher teacher){
        return teacherMapper.addTeacher(teacher);
    }
    //修改老师信息
    public int updateTeahcer(Teacher teacher){
        return adminMapper.updateTeacher(teacher);
    }
    //添加学生
    public int addStu(Student student){
        return adminMapper.insetStu(student);
    }

    //修改学生
    public  int updateStu(Student student){
        return adminMapper.updateStu(student);
    }

    //添加课程信息
    public  int addCourse(Course course){
        return  adminMapper.insetCourse(course);
    }

    //修改课程信息
    public int updateCourse(Course course){
        return adminMapper.updateCourse(course);
    }


    //班级信息
    //删除班级信息
    public int deleteClass(int classid){
        return adminMapper.deleteClass(classid);
    }
    //添加班级信息
    public int addClassname(ClassName className){
        return adminMapper.insetClass(className);
    }
    //修改班级信息
    public int updateClass(ClassName className){
        return adminMapper.updateClass(className);
    }

}
