package com.study.project4.com.service;

import com.study.project4.com.dao.TeacherMapper;
import com.study.project4.com.entity.Course;
import com.study.project4.com.entity.Course_Students;
import com.study.project4.com.entity.Teacher;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {

    @Autowired
    private TeacherMapper teacherMapper;

    //查询所有老师
    public List<Teacher> getTeaAll(){
        return  teacherMapper.getTeaAll();
    }
    //通过老师的ID得到老师信息
    public Teacher getTeaByid(Integer id){
        return  teacherMapper.getTeaByID(id);
    }


    //根据老师ID查询上那些课
    public List<Course> findCourseByTid(Integer tid){
        return teacherMapper.getCourseByTid(tid);
    }
    //查看有哪些学生申请
    public List<Course_Students> findAddStudents(Integer tid){
        return teacherMapper.getaddStuByTid(tid);
    }


    //允许学生添加课程  //撤回删除某门课的某个学生  //管理员拒绝删除某门课的某个学生
    //将ifjoin变为2  即正常状态
    public int allowStudents(Integer cid,Integer id){
        return  teacherMapper.allowStudents(cid,id);
    }
    //拒绝学生  //管理员同意删除某个学生
    //将这行信息删除掉
    public int refuseStudents(Integer cid,Integer id){
        return  teacherMapper.refuseStudents(cid,id);
    }

    //申请删除某个学生
    public int deleteT_S(Integer cid,Integer id){
        return teacherMapper.deleteTStudents(cid,id);
    }

    //更改签到情况 ifqiandao=0为未开启签到，=1开启签到，=3开启迟到模式
    public int updateQiandao(int cid,String sum){
        return teacherMapper.updateQiandao(cid,sum);
    }


    //修改是否允许课程申请
    public int updateOpen(int cid,int x){
        return teacherMapper.updateOpen(cid,x);
    }

    //修改课程介绍
    public int editInformation(int cid,String information,String type){
        return teacherMapper.editInformation(cid,information,type);
    }
}
