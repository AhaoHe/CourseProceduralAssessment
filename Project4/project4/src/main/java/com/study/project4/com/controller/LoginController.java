package com.study.project4.com.controller;

import com.study.project4.com.entity.Admin;
import com.study.project4.com.entity.Student;
import com.study.project4.com.entity.Teacher;
import com.study.project4.com.service.AdminService;
import com.study.project4.com.service.StudentService;
import com.study.project4.com.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
public class LoginController {

    @Autowired
    StudentService stu=new StudentService();
    @Autowired
    TeacherService tea=new TeacherService();
    @Autowired
    AdminService ad=new AdminService();


    //返回主界面，作用是拦截后，在拦截器中给前端的msg赋值“没有权限”。
    @PostMapping("/toIndex")
    public  String toIndex(){
        return "index";
    }



    //学生登录
    @PostMapping("/student/login")
    public String Studentlogin(@RequestParam("stuname") int stuname,
                               @RequestParam("spsw") String spsw,
                               @RequestParam("validatecode") String validatecode,
                               Map<String,Object> map, HttpSession session){
        Student loginStu = null;
        Student Stu = null;
        //获得用户ID，判断是否存在
        loginStu = stu.getStuByid(stuname);
        //获得该ID的学生基本信息

        //登录之前首先判断一下用户输入的验证码是否正确
        String verifyCodeExpected = (String)session.getAttribute(
                com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
        //这里相当于 request.getParameter("verifyCodeActual");
        //String verifyCodeActual = HttpServletRequestUtil.getString(request, "verifyCodeActual");
        String verifyCodeActual = validatecode;
        if(verifyCodeActual == null || !verifyCodeActual.equals(verifyCodeExpected)){
            //验证码错误，登录失败
            map.put("msg", "验证码错误");
            return "studentlogin";
        }

        if (loginStu!=null) {//判断用户是否存在，防止出现密码不存在而报错505
            String password = stu.getStuByid(stuname).getSpsw();
            if (!StringUtils.isEmpty(stuname) && password.equals(spsw)) {
                //登录成功，防止表单重复提交，重定向到学生后台
                session.setAttribute("loginUser",stuname);
                session.setAttribute("User","student");
                session.setAttribute("Main",loginStu);
                return "redirect:/";
            } else {
                //登录失败
                map.put("msg", "用户名/密码错误");
                return "studentlogin";
            }
        }else {
            //登录失败
            map.put("msg", "用户名不存在");
            return "studentlogin";
        }
    }


    //教师登录
    @PostMapping("/teacher/login")
    public String Teacherlogin(@RequestParam("tname") int tname,
                               @RequestParam("tpsw") String tpsw,
                               @RequestParam("validatecode") String validatecode,
                               Map<String,Object> map, HttpSession session){
        Teacher loginTeacher = null;
        loginTeacher = tea.getTeaByid(tname);

        //登录之前首先判断一下用户输入的验证码是否正确
        String verifyCodeExpected = (String)session.getAttribute(
                com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
        //这里相当于 request.getParameter("verifyCodeActual");
        //String verifyCodeActual = HttpServletRequestUtil.getString(request, "verifyCodeActual");
        String verifyCodeActual = validatecode;
        if(verifyCodeActual == null || !verifyCodeActual.equals(verifyCodeExpected)){
            //验证码错误，登录失败
            map.put("msg", "验证码错误");
            return "teacherlogin";
        }

        if (loginTeacher!=null) {//判断用户是否存在，防止出现密码不存在而报错505
            String password = tea.getTeaByid(tname).getTpsw();
            if (!StringUtils.isEmpty(tname) && password.equals(tpsw)) {
                //登录成功，防止表单重复提交，重定向到学生后台
                session.setAttribute("loginUser",tname);
                session.setAttribute("User","teacher");
                return "redirect:/";
            } else {
                //登录失败
                map.put("msg", "用户名/密码错误");
                return "teacherlogin";
            }
        }else {
            //登录失败
            map.put("msg", "用户名不存在");
            return "teacherlogin";
        }
    }


    //管理员登录
    @PostMapping("/admin/login")
    public String Adminlogin(@RequestParam("aname") int aname,
                               @RequestParam("apsw") String apsw,
                               @RequestParam("validatecode") String validatecode,
                               Map<String,Object> map, HttpSession session){
        Admin loginAdmin = null;
        loginAdmin =ad.getAdminByid(aname);

        //登录之前首先判断一下用户输入的验证码是否正确
        String verifyCodeExpected = (String)session.getAttribute(
                com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
        //这里相当于 request.getParameter("verifyCodeActual");
        //String verifyCodeActual = HttpServletRequestUtil.getString(request, "verifyCodeActual");
        String verifyCodeActual = validatecode;
        if(verifyCodeActual == null || !verifyCodeActual.equals(verifyCodeExpected)){
            //验证码错误，登录失败
            map.put("msg", "验证码错误");
            return "managementlogin";
        }

        if (loginAdmin!=null) {//判断用户是否存在，防止出现密码不存在而报错505
            String password = ad.getAdminByid(aname).getPassword();
            if (!StringUtils.isEmpty(aname) && password.equals(apsw)) {
                //登录成功，防止表单重复提交，重定向到管理员后台
                session.setAttribute("loginUser",aname);
                session.setAttribute("User","admin");
                return "redirect:/";
            } else {
                //登录失败
                map.put("msg", "用户名/密码错误");
                return "managementlogin";
            }
        }else {
            //登录失败
            map.put("msg", "用户名不存在");
            return "managementlogin";
        }
    }


    //注销
    @RequestMapping("/logout")
    public String logout(HttpSession session){
        if(session.getAttribute("loginUser")!=null){
            session.removeAttribute("loginUser");
            session.removeAttribute("User");
        }
        return "redirect:/";
    }

}
