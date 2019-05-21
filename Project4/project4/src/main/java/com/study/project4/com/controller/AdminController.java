package com.study.project4.com.controller;


import com.study.project4.com.entity.*;
import com.study.project4.com.service.AdminService;
import com.study.project4.com.service.CourseService;
import com.study.project4.com.service.StudentService;
import com.study.project4.com.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AdminController {

    @Autowired
    StudentService studentService;
    @Autowired
    CourseService courseService;
    @Autowired
    TeacherService teacherService;
    @Autowired
    AdminService adminService;

    //显示所有学生信息
    @RequestMapping("/Admin/students")
    public String AdminStudents(Model model){
        List<Student> students=studentService.getStuAll();
        List<ClassName> classNames=studentService.getSclass();
        model.addAttribute("students",students);
        model.addAttribute("classNames",classNames);
        return "admin/students";
    }

    //显示所有老师信息
    @RequestMapping("/Admin/teachers")
    public String AdminTeachers(Model model){
        List<Teacher> teachers=teacherService.getTeaAll();
        model.addAttribute("teachers",teachers);
        return "admin/teachers";
    }

    //显示所有课程信息
    @RequestMapping("/adminMain")
    public String AdminMain(Model model){
        List<Course> courses=courseService.getCourseAll();
        List<Teacher> teachers=teacherService.getTeaAll();
        model.addAttribute("courses",courses);
        model.addAttribute("teachers",teachers);
        return "manag";
    }

    //显示所有班级信息
    @RequestMapping("/Admin/classname")
    public String ClassNameMain(Model model){
        List<ClassName> classNames=studentService.getSclass();
        model.addAttribute("classnames",classNames);
        return "admin/classname";
    }

    //查询哪门课有哪些老师要删除哪些学生
    @RequestMapping("/Admin/TeaDelStu/{cid}")
    public String TeaDelStu(Model model,@PathVariable("cid")int id){
        List<Course_Students> courses=adminService.findTeaDelStuAll(id);
        model.addAttribute("courses",courses);
        return "admin/courses";
    }
    //管理员拒绝删除
    @GetMapping("/Admin/refuseDelStu/{Cid}/{Sid}")
    public String allowStu(@PathVariable("Cid") Integer Cid,
                           @PathVariable("Sid") Integer Sid){
        teacherService.allowStudents(Cid, Sid);
        return "redirect:/Admin/TeaDelStu/"+Cid;
    }

    //管理员同意删除学生
    @GetMapping("/Admin/allowDelStu/{Cid}/{Sid}")
    public String refuseStu(@PathVariable("Cid") Integer Cid,
                            @PathVariable("Sid") Integer Sid){
        teacherService.refuseStudents(Cid,Sid);
        return "redirect:/Admin/TeaDelStu/"+Cid;
    }

    //删除学生信息
    @GetMapping("/Admin/deleteS/{id}")
    public String deleteStudent(@PathVariable("id") Integer id){
        adminService.deleteStu(id);
        return "redirect:/Admin/students";
    }
    //删除老师信息
    @GetMapping("/Admin/deleteT/{id}")
    public String deleteTeacher(@PathVariable("id") Integer id){
        adminService.deleteTeacher(id);
        return "redirect:/Admin/teachers";
    }
    //删除课程信息
    @GetMapping("/Admin/deleteC/{id}")
    public String deleteCourse(@PathVariable("id") Integer id){
        adminService.deleteCourse(id);
        return "redirect:/adminMain";
    }

    //删除班级信息
    @GetMapping("/Admin/deleteClass/{id}")
    public String deleteClass(@PathVariable("id") int id){
        adminService.deleteClass(id);
        return "redirect:/Admin/classname";
    }


    //修改课程信息
    @RequestMapping("/Admin/updateC")
    public String updateCourse(@RequestParam("cid") Integer cid,
                               @RequestParam("course")String cname,
                               @RequestParam("tid") Integer tid){
        Course course = new Course();
        Teacher teacher;
        course.setCid(cid);
        teacher=teacherService.getTeaByid(tid);
        course.setTeacher(teacher);
        course.setCourse(cname);
        adminService.updateCourse(course);
        return "redirect:/adminMain";
    }
    //添加课程信息
    @RequestMapping("/Admin/addCourse")
    public String addCourse(@RequestParam("addcourse")String cname,
                            @RequestParam("addtid") Integer tid){
        Course course = new Course();
        Teacher teacher;
        teacher=teacherService.getTeaByid(tid);
        course.setTeacher(teacher);
        course.setCourse(cname);
        adminService.addCourse(course);
        return "redirect:/adminMain";
    }


    //修改教师信息
    @RequestMapping("/Admin/updateT")
    public String updateTeacher(@RequestParam("teacherId") Integer tid,
                               @RequestParam("tpswd")String tpswd,
                               @RequestParam("tsex") int tsex,
                                @RequestParam("tname")String tname){
        Teacher teacher=new Teacher();

        teacher.setTid(tid);
        teacher.setTname(tname);
        teacher.setTpsw(tpswd);
        teacher.setTsex(tsex);

        adminService.updateTeahcer(teacher);
        return "redirect:/Admin/teachers";
    }

    //添加教师信息
    @RequestMapping("/Admin/addTeacher")
    public String addTeacher(@RequestParam("addtpswd")String tpswd,
                             @RequestParam("addtsex") int tsex,
                             @RequestParam("addtname")String tname){
        Teacher teacher=new Teacher();
        if (tpswd.isEmpty()){
            tpswd="123456";
        }
        teacher.setTname(tname);
        teacher.setTpsw(tpswd);
        teacher.setTsex(tsex);

        adminService.addTea(teacher);
        return "redirect:/Admin/teachers";
    }

    //修改学生信息
    @RequestMapping("/Admin/updateStu")
    public String updateStudent(@RequestParam("StuId")int sid,
                                @RequestParam("spswd")String spswd,
                                @RequestParam("sname")String sname,
                                @RequestParam("ssex")int ssex,
                                @RequestParam("classid")int classid){
        Student student=new Student();
        ClassName className=new ClassName();
        className.setClassid(classid);

        student.setId(sid);
        student.setSname(sname);
        student.setSpsw(spswd);
        student.setSsex(ssex);
        student.setClassName(className);

        adminService.updateStu(student);
        return "redirect:/Admin/students";
    }

    //添加学生信息
    @RequestMapping("/Admin/addStudent")
    public String updateStudent(@RequestParam("spswd")String spswd,
                                @RequestParam("sname")String sname,
                                @RequestParam("ssex")int ssex,
                                @RequestParam("classid")int classid){
        Student student=new Student();
        ClassName className=new ClassName();
        className.setClassid(classid);

        student.setSname(sname);
        if (spswd.isEmpty()){
            spswd="123456";
        }
        student.setSpsw(spswd);
        student.setSsex(ssex);
        student.setClassName(className);

        adminService.addStu(student);
        return "redirect:/Admin/students";
    }


    //修改班级信息
    @RequestMapping("/Admin/updateClass")
    public String updateClass(@RequestParam("classId")int classid,
                              @RequestParam("classname")String classname){
        ClassName className=new ClassName();
        className.setClassid(classid);
        className.setClassname(classname);
        adminService.updateClass(className);
        return "redirect:/Admin/classname";
    }
    //添加班级信息
    @RequestMapping("/Admin/addClass")
    public String addClass(@RequestParam("classname")String classname){
        ClassName className=new ClassName();
        className.setClassname(classname);
        adminService.addClassname(className);
        return "redirect:/Admin/classname";
    }

}
