package com.study.project4.com.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.study.project4.com.entity.Course;
import com.study.project4.com.entity.Course_Students;
import com.study.project4.com.service.CourseService;
import com.study.project4.com.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class StudentController {

    @Autowired
    CourseService courseService=new CourseService();
    @Autowired
    StudentService studentService=new StudentService();

    @RequestMapping("/studentMain")
    public String StudentMain(HttpSession session, Model model){
        Object stuId=session.getAttribute("loginUser");
        int id= (int) stuId;
        List<Course> courses=courseService.getCourseById(id);
        model.addAttribute("courses",courses);
        return "stumanag";
    }

    @RequestMapping("/students/AllCourse")
    public String CoursesAll(Model model,
                             @RequestParam(value="start",defaultValue="0")int start,
                             @RequestParam(value = "size", defaultValue = "4") int size,
                             HttpSession session){
        Object stuId=session.getAttribute("loginUser");
        int id= (int) stuId;
        //1. 在参数里接受当前是第几页 start ，以及每页显示多少条数据 size。 默认值分别是0和5。
        //2. 根据start,size进行分页，并且设置id 正序  如果为倒叙，则为 desc
        PageHelper.startPage(start,size,"c.cid");
        //3. 因为PageHelper的作用，这里就会返回当前分页的集合了
        List<Course> courses=courseService.getCourseAll();//查询所有课程
        List<Course> ifjoin=courseService.getIfJoin(id);
        //4. 根据返回的集合，创建PageInfo对象
        PageInfo<Course> page = new PageInfo<>(courses);
        //5. 把PageInfo对象扔进model，以供后续显示
        model.addAttribute("page", page);
        //6. 跳转到listCategory.jsp
        for (Course course:courses){
            for (Course ifj:ifjoin){
                 if(course.getCid()==ifj.getCid()){
                    course.setIfjoin(ifj.getIfjoin());
                }
            }
        }
        model.addAttribute("courses",courses);
        return "students/courses";
    }

    @RequestMapping("/students/addCourse")
    public String addCourse(@RequestParam("Cid") Integer Cid,
                            HttpSession session){
        Object stuId=session.getAttribute("loginUser");
        int id= (int) stuId;
        courseService.addIfjoin(Cid,id,1);
        return "redirect:/students/AllCourse";
    }


    //查询成绩
    @RequestMapping("/students/StuScores/{cid}")
    public String getStuScoresByCidAndId(@PathVariable("cid") Integer cid,
                             Model model,HttpSession session){
        //获得学生id
        int id=(int)session.getAttribute("loginUser");
        //查询章节数
        Course course=courseService.getCourseByCid(cid);
        String chapters=course.getChapters();
        String[] c=chapters.split("\\|\\|");
        model.addAttribute("chapters",c);

        //查询成绩
        Course_Students cs=studentService.getScores(cid,id);
        String scores=cs.getScores();
        String[] s=scores.split("\\|\\|");
        model.addAttribute("scores",s);

        //查询所有人的平时成绩
        List<Course_Students> course_students=studentService.getScoresAll(cid);
        List<String[]> list=new ArrayList<String[]>();
        int a[] = new int[c.length];//平均分的数组,代表每个章节的平均分
        int count[] = new int[c.length];
        for(Course_Students course_student:course_students) {
            String result = course_student.getScores();
            String[] rt = result.split("\\|\\|");
            for (int i=0;i<rt.length;i++){
                if(!rt[i].equals("empty")) {
                    int x = Integer.parseInt(rt[i]);
                    a[i] += x;
                    count[i]++;
                }
            }
            list.add(rt);
        }


        //个人平均成绩
        int sum=0;
        int isCount=0;
        for (int i=0;i<s.length;i++) {
            if (!s[i].equals("empty")){
                int x = Integer.parseInt(s[i]);
                sum += x;
                isCount++;
            }
            if (count[i]!=0) {
                a[i] /= count[i];//这门课每章节的平均成绩
            }else {
                a[i]=0;
            }
        }
        int aver=sum/isCount;
        model.addAttribute("aver",aver);
        model.addAttribute("CourseAver",a);
        model.addAttribute("cid",cid);

        return "students/grade";

    }

    //签到情况查询
    @GetMapping("/students/checkArrived/{cid}")
    public String checkArrived(@PathVariable("cid")int cid,
                               Model model,HttpSession session){
        int id= (int) session.getAttribute("loginUser");
        Course_Students cs=studentService.getScores(cid,id);//查询签到信息
        String arrived=cs.getArrived();
        String[] a=arrived.split(",");
        model.addAttribute("arrived",a);
        model.addAttribute("CourseID",cid);

        //得到课程名字
        Course course=courseService.getCourseByCid(cid);
        String cname=course.getCourse();
        model.addAttribute("cname",cname);

        String ifqiandao=course.getIfqiandao();//签到模式
        String[] q=ifqiandao.split(",");
        model.addAttribute("ifqiandao",q);

        return "students/theCourse";
    }

    //点击签到后
    @GetMapping("/students/clickArrived/{cid}/{index}/{x}")
    public String ClickArrived(@PathVariable("cid")int cid,
                               @PathVariable("x")int x,
                               @PathVariable("index")int index,
                               Model model,HttpSession session){
        int id= (int) session.getAttribute("loginUser");
        Course_Students cs=studentService.getScores(cid,id);//查询签到信息
        String arrived=cs.getArrived();
        String[] a=arrived.split(",");

        Course course=courseService.getCourseByCid(cid);
        String ifqiandao=course.getIfqiandao();//签到模式
        String[] q=ifqiandao.split(",");

        //x=1正常签到，x=3迟到签到
        if (x==1 && q[index].equals("1")) {
            a[index]="1";
        }
        if (x==3 && q[index].equals("3")) {
            a[index]="3";
        }
        String sum=String.join(",",a);
        studentService.updateArrived(sum,cid,id);

        return "redirect:/students/checkArrived/"+cid;
    }

}
