package com.study.project4.com.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.study.project4.com.entity.Course;
import com.study.project4.com.entity.Course_Students;
import com.study.project4.com.service.CourseService;
import com.study.project4.com.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class TeacherController {

    @Autowired
    public TeacherService teacherService=new TeacherService();
    @Autowired
    CourseService courseService=new CourseService();

    @RequestMapping("/teacherMain")
    public String StudentMain(HttpSession session, Model model){
        Object Tid=session.getAttribute("loginUser");
        int tid= (int) Tid;
        List<Course> courses=teacherService.findCourseByTid(tid);
        model.addAttribute("courses",courses);
        return "teachermanag";
    }


    //查询哪些学生申请了课程
    @RequestMapping("/teachers/AllCourse")
    public String AddCourseAll(Model model,
                             @RequestParam(value="start",defaultValue="0")int start,
                             @RequestParam(value = "size", defaultValue = "5") int size,
                             HttpSession session){
        Object Tid=session.getAttribute("loginUser");
        int id= (int) Tid;
        //1. 在参数里接受当前是第几页 start ，以及每页显示多少条数据 size。 默认值分别是0和5。
        //2. 根据start,size进行分页，并且设置id 倒排序
        PageHelper.startPage(start,size,"c.cid");
        //3. 因为PageHelper的作用，这里就会返回当前分页的集合了
        List<Course_Students> StuAdd=teacherService.findAddStudents(id);//查询所有课程
        //4. 根据返回的集合，创建PageInfo对象
        PageInfo<Course_Students> page = new PageInfo<>(StuAdd);
        //5. 把PageInfo对象扔进model，以供后续显示
        model.addAttribute("page", page);
        //6. 跳转到listCategory.jsp
        model.addAttribute("stuadd",StuAdd);
        return "teachers/addStudents";
    }

    //允许学生添加课程，x是判断重定向到哪个页面  //教师撤销删除
    @GetMapping("/teachers/allowStu/{Cid}/{Sid}/{x}")
    public String allowStu(@PathVariable("Cid") Integer Cid,
                           @PathVariable("Sid") Integer Sid,
                           @PathVariable("x") Integer x){
        teacherService.allowStudents(Cid, Sid);
        if(x==1){
            return "redirect:/teachers/AllCourse";
        }else {
            return "redirect:/teachers/course_students/"+Cid;
        }
    }

    //丑拒学生添加课程,x是判断重定向到哪个页面 ,删除申请信息
    @GetMapping("/teachers/refuseStu/{Cid}/{Sid}")
    public String refuseStu(@PathVariable("Cid") Integer Cid,
                            @PathVariable("Sid") Integer Sid){
        teacherService.refuseStudents(Cid,Sid);
        return "redirect:/teachers/AllCourse";
    }

    //申请删除学生
    @GetMapping("/teachers/deleteStu/{id}/{cid}")
    public String deleteStu(@PathVariable("id")Integer id,
                            @PathVariable("cid")Integer cid){
        teacherService.deleteT_S(cid,id);
        return "redirect:/teachers/course_students/"+cid;
    }

    //查询某门课有哪些学生
    @GetMapping("/teachers/course_students/{cid}")
    public String course_students(@PathVariable("cid")Integer cid,
                                  @RequestParam(value="start",defaultValue="0")int start,
                                  @RequestParam(value = "size", defaultValue = "5") int size,
                                  Model model){

        //1. 在参数里接受当前是第几页 start ，以及每页显示多少条数据 size。 默认值分别是0和5。
        //2. 根据start,size进行分页，并且设置id 倒排序
        PageHelper.startPage(start,size);
        //3. 因为PageHelper的作用，这里就会返回当前分页的集合了
        List<Course_Students> cs=courseService.findCourse_Students(cid);
        //4. 根据返回的集合，创建PageInfo对象
        PageInfo<Course_Students> page = new PageInfo<>(cs);
        //5. 把PageInfo对象扔进model，以供后续显示
        model.addAttribute("page", page);
        model.addAttribute("cs",cs);
        return "teachers/course_students";
    }

}
