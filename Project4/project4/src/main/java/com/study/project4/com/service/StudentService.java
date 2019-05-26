package com.study.project4.com.service;

import com.study.project4.com.dao.AdminMapper;
import com.study.project4.com.dao.StudentsMapper;
import com.study.project4.com.entity.ClassName;
import com.study.project4.com.entity.Course_Students;
import com.study.project4.com.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentsMapper studentsMapper;

    //查询所有学生
    public List<Student> getStuAll(){
        return  studentsMapper.getStuAll();
    }
    public Student getStuByid(Integer id){
        return  studentsMapper.getStuByid(id);
    }

    //查询所有班级
    public  List<ClassName> getSclass(){
        return studentsMapper.getClassName();
    }

    //查询某个人某门课成绩
    //查询某个人某门课签到信息
    public Course_Students getScores(int cid,int id){
        return studentsMapper.getScoresByCidandId(cid,id);
    }

    //查询某门课的成绩
    public List<Course_Students> getScoresAll(int cid){
        return  studentsMapper.getScoresByCid(cid);
    }

    //得到某门课总人数
    public int getCourse_Count(int cid){
        return studentsMapper.getCourse_CountByCid(cid);
    }

    //修改签到信息
    public int updateArrived(String sum,int cid,int id){
        return studentsMapper.updateArrived(sum, cid, id);
    }
}
