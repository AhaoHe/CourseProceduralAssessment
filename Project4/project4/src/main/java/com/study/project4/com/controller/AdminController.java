package com.study.project4.com.controller;


import com.study.project4.com.entity.Course;
import com.study.project4.com.entity.Course_Students;
import com.study.project4.com.entity.Student;
import com.study.project4.com.entity.Teacher;
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

import javax.servlet.http.HttpSession;
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
        model.addAttribute("students",students);
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
        model.addAttribute("courses",courses);
        return "manag";
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

}
