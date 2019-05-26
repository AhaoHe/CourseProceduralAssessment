package com.study.project4;

import com.study.project4.com.entity.Course;
import com.study.project4.com.entity.Course_Students;
import com.study.project4.com.entity.Teacher;
import com.study.project4.com.service.AdminService;
import com.study.project4.com.service.CourseService;
import com.study.project4.com.service.StudentService;
import com.study.project4.com.service.TeacherService;
import com.sun.deploy.util.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
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
    @Autowired
    AdminService adminService=new AdminService();

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

    //修改课程信息
    @Test
    public void UpdateCourse(){
        Course c = new Course();
        Teacher teacher;
        c.setCid(6);
        teacher=teacherService.getTeaByid(1);
        c.setTeacher(teacher);
        c.setCourse("计算机网络");
        adminService.updateCourse(c);
    }

    //查询章节信息
    @Test
    public void GetChapters(){
        Course course=courseService.getCourseByCid(1);
        String chapters=course.getChapters();
        String[] c=chapters.split("\\|\\|");
        for(int i=0;i<c.length;i++){
            System.out.println(c[i]);
        }
    }

    //添加课程
    @Test
    public void addCourse(){
        Course c = new Course();
        Teacher teacher;
        teacher=teacherService.getTeaByid(2);
        c.setTeacher(teacher);
        c.setCourse("计算机网络");
        adminService.addCourse(c);
    }

    //删除章节
    @Test
    public void updateCourse(){
        Course course=courseService.getCourseByCid(1);
        List<Course_Students> course_students=courseService.findCourse_Students(1);
        String chapters=course.getChapters();
        String[] c=chapters.split("\\|\\|");
        List<String> list = new ArrayList<String>();
        List<String> listScores = new ArrayList<String>();
        for (int i=0; i<c.length; i++) {
            list.add(c[i]);
        }
        list.remove(2);
        String sum =StringUtils.join(list,"||");
        courseService.updateChapters(sum,1);
        for (Course_Students student:course_students){
            int id=student.getStudent().getId();
            Course_Students cs=studentService.getScores(1,id);
            String scores=cs.getScores();
            String[] s=scores.split("\\|\\|");
            for (int i=0; i<s.length; i++) {
                listScores.add(s[i]);
            }
            listScores.remove(2);
            String sumScores =StringUtils.join(listScores,"||");
            courseService.updateScores(sumScores,1,id);
            listScores.clear();
        }
    }

    //添加章节
    @Test
    public void AddChapters(){
        int cid=Integer.parseInt("1");
        Course course=courseService.getCourseByCid(cid);
        String chapters=course.getChapters();
        String[] c=chapters.split("\\|\\|");
        List<String> list = new ArrayList<String>();
        for (int i=0;i<c.length;i++){
            list.add(c[i]);
        }
        list.add("第四周");
        String sum =StringUtils.join(list,"||");
        System.out.println(sum);
    }

    //查询某门课所有成绩信息
    @Test
    public void findAllScores(){
        List<Course_Students> course_students=studentService.getScoresAll(1);
        List<String[]> list=new ArrayList<String[]>();
        int aver[] = new int[10];
        for(Course_Students course_student:course_students) {
            String scores = course_student.getScores();
            String[] s = scores.split("\\|\\|");
            for (int i=0;i<s.length;i++){
               int x=Integer.parseInt(s[i]);
               aver[i]+=x;
            }
            list.add(s);
        }
        for (int i=0;i<list.size();i++){
            System.out.println(list.get(i)[0]);
        }
        System.out.println(aver[0]);

    }

    //查询签到模式
    @Test
    public void findQiandao(){
        Course course=courseService.getCourseByCid(1);
        String ifqiandao=course.getIfqiandao();//签到模式
        String[] q=ifqiandao.split(",");
        List<String> list=new ArrayList<String>();
        for (int i=0;i<q.length;i++) {
            list.add(q[i]);
        }
        list.remove(1);
        String sum=StringUtils.join(list,",");
        System.out.println("sum="+sum);
    }

    //学生签到
    @Test
    public void StudentsQiandao(){
        int x=1;
        Course_Students cs=studentService.getScores(1,2016051100);//查询签到信息
        String arrived=cs.getArrived();
        String[] a=arrived.split(",");

        Course course=courseService.getCourseByCid(1);
        String ifqiandao=course.getIfqiandao();//签到模式
        String[] q=ifqiandao.split(",");

        //x=1正常签到，x=3迟到签到
        if (x==1 &&q[1].equals("1")) {
            a[1]="1";
        }
        if (x==2 && q[1].equals("3")) {
            a[1]="3";
        }
        String sum=String.join(",",a);
        System.out.println(sum);
    }

    //某门课学生签到人数
    @Test
    public void StudentFindQiandao(){
        Course course=courseService.getCourseByCid(1);
        List<Course_Students> course_students=studentService.getScoresAll(1);
        int count=studentService.getCourse_Count(1);//这门课总人数
        int i=0,qdCount=0,cdCount=0;//qdCount签到人数，cdCount迟到人数
        List<Course_Students> qiandao=new ArrayList<Course_Students>();//签到人名单
        List<Course_Students> chidao=new ArrayList<Course_Students>();//迟到人名单
        List<Course_Students> others=new ArrayList<Course_Students>();//未签到人名单
        for(Course_Students cs:course_students){
            String arrived=cs.getArrived();
            String[] a=arrived.split(",");
            if (a[i].equals("1")||a[i].equals("2")) {
                qiandao.add(cs);
                qdCount++;
            } else if (a[i].equals("3")) {
                chidao.add(cs);
                cdCount++;
            }else{
                others.add(cs);
            }
            i++;
        }
        for (Course_Students cs:qiandao){
            System.out.println(cs.getStudent().getId());
        }

    }

}
