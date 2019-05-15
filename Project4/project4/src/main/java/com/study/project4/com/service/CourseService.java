package com.study.project4.com.service;

import com.study.project4.com.dao.CourseMapper;
import com.study.project4.com.entity.Course;
import com.study.project4.com.entity.Course_Students;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseMapper courseMapper;

    //通过ID得到课程信息
    public List<Course> getCourseById(Integer id){
        return courseMapper.getCourseById(id);
    }

    //得到所有课程信息
    public List<Course> getCourseAll(){
        return courseMapper.getCoursesAll();
    }

    //通过是否课程申请来得到课程信息查看每门课申请的情况，ifjoin=2已经添加课程，=1表示正在申请，=0表示未申请，
    public List<Course> getIfJoin(Integer id){
        return courseMapper.getIfJoinById(id);
    }

    //添加课程申请
    public int addIfjoin(Integer cid,Integer id,Integer ifjoin){
        return courseMapper.addIfJoin(cid,id,ifjoin);
    }

    //查询某门课有哪些学生
    public List<Course_Students> findCourse_Students(Integer cid){
        return courseMapper.COURSE_STUDENTS(cid);
    }

}
