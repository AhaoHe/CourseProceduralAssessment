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
    @RequestMapping("/students/StuScores/{id}/{cid}")
    public String getStuScoresByCidAndId(@PathVariable("cid") Integer cid,
                             @PathVariable("id") Integer id,
                             Model model){
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
        for(Course_Students course_student:course_students) {
            String result = course_student.getScores();
            String[] rt = result.split("\\|\\|");
            for (int i=0;i<rt.length;i++){
                int x=Integer.parseInt(rt[i]);
                a[i]+=x;
            }
            list.add(rt);
        }


        //个人平均成绩
        int sum=0;
        for (int i=0;i<s.length;i++) {
            int x = Integer.parseInt(s[i]);
            sum+=x;
            a[i] /= list.size();//这门课每章节的平均成绩
        }
        int aver=sum/s.length;
        model.addAttribute("aver",aver);
        model.addAttribute("CourseAver",a);

        return "students/grade";

    }
}
