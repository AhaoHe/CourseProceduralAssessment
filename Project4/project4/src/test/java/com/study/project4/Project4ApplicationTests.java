package com.study.project4;

import com.study.project4.com.entity.Course;
import com.study.project4.com.entity.Course_Students;
import com.study.project4.com.service.CourseService;
import com.study.project4.com.service.StudentService;
import com.study.project4.com.service.TeacherService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Project4ApplicationTests {

    @Autowired
    DataSource dataSource;
    @Autowired
    StudentService studentService=new StudentService();
    @Autowired
    CourseService courseService=new CourseService();
    @Autowired
    TeacherService teacherService=new TeacherService();

    @Test
    public void contextLoads() throws SQLException {
        System.out.println(dataSource.getClass());

        Connection connection=dataSource.getConnection();
        System.out.println(connection);
        connection.close();
    }
    @Test
    public  void  login(){//登录，判断密码
        String password=studentService.getStuByid(2016051100).getSpsw();
        System.out.println(password);
    }

    @Test
    public  void  getCourseById(){//根据课程ID得到老师信息
        List<Course> courses = courseService.getCourseById(2016051100);
        for(Course course:courses){
            System.out.println(course.getCourse());
            System.out.println(course.getTeacher().getTname());
        }
    }

    @Test
    public void findCoursesAll(){//查询所有课程
        List<Course> courses=null;
        courses=courseService.getCourseAll();
        for(Course course:courses){
            System.out.println(course.getCourse());
            System.out.println(course.getIfjoin());
        }
    }

    @Test
    public void CourseFindAll(){//根据学生Id查找已经添加的课
        Object stuId=2016051100;
        int id= (int) stuId;
        List<Course> courses=null;
        courses=courseService.getIfJoin(id);
        for(Course course:courses){
            System.out.println(course.getCourse());
            System.out.println(course.getIfjoin());
        }
    }

    @Test
    public void ListIfJoin(){
        List<Course> courses=courseService.getCourseAll();//学生ID查询所有课程
        List<Course> ifjoin=courseService.getIfJoin(2016051101);
        for (Course course:courses){
            for (Course ifj:ifjoin){
                if(course.getCid()!=ifj.getCid()&&ifj.getIfjoin()!=1&&ifj.getIfjoin()!=2){
                    course.setIfjoin(3);
                    System.out.println(course.getCourse());
                    System.out.println(course.getIfjoin());
                }else if(course.getCid()==ifj.getCid()){
                    course.setIfjoin(ifj.getIfjoin());
                    System.out.println(course.getCourse());
                    System.out.println(course.getIfjoin());
                }
            }
        }
        for (Course course:courses){
            System.out.println(course.getIfjoin());
        }
    }

    @Test
    public void getAddStudents(){//老师查看已经添加课程的学生
        List<Course_Students> all=teacherService.findAddStudents(1);
        for(Course_Students a:all){
            System.out.println(a.getCourse().getCid());
            System.out.println(a.getCourse().getCourse());
            System.out.println(a.getStudent().getSname());
            System.out.println(a.getStudent().getId());
            System.out.println(a.getIfjoin());
        }
    }
    @Test
    public void CourseAll(){//查询该门课的所有学生
        List<Course_Students> cs=courseService.findCourse_Students(3);
        for(Course_Students a:cs){
            System.out.println(a.getStudent().getSname());
            System.out.println(a.getCourse().getCourse());
            System.out.println(a.getIfjoin());
        }
    }
}
