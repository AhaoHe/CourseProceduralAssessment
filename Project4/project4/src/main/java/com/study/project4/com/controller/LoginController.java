package com.study.project4.com.controller;

import com.study.project4.com.entity.*;
import com.study.project4.com.service.AdminService;
import com.study.project4.com.service.CourseService;
import com.study.project4.com.service.StudentService;
import com.study.project4.com.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class LoginController {

    @Autowired
    StudentService stu=new StudentService();
    @Autowired
    TeacherService tea=new TeacherService();
    @Autowired
    CourseService courseService=new CourseService();
    @Autowired
    AdminService ad=new AdminService();


    //返回主界面，作用是拦截后，在拦截器中给前端的msg赋值“没有权限”。
    @PostMapping("/toIndex")
    public  String toIndex(){
        return "index";
    }



    //学生登录
    @PostMapping("/student/login")
    public String Studentlogin(@RequestParam("stuname") String stuId,
                               @RequestParam("spsw") String spsw,
                               @RequestParam("validatecode") String validatecode,
                               Map<String,Object> map, HttpSession session){
        try {//try，catch方法：如果stuID没办法转化为，则执行catch 中的语句，防止页面报错无报错信息。
            int stuname=Integer.parseInt(stuId);
            //获得用户ID，判断是否存在
            Student loginStu = stu.getStuByid(stuname);
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

            if (loginStu!=null) {//判断用户是否存在，防止出现用户不存在而报错505
                String password = stu.getStuByid(stuname).getSpsw();
                if (!StringUtils.isEmpty(stuname) && password.equals(spsw)) {
                    //登录成功，防止表单重复提交，重定向到学生后台
                    session.setAttribute("loginUser",stuname);//学生的ID
                    //登录者的身份
                    session.setAttribute("User","student");
                    session.setAttribute("Main",loginStu);


                    //查询有无即将挂科成绩
                    List<Course> courses=courseService.getCourseById(stuname);
                    /*int len=0;判断warn的长度
                    for (Course course:courses){
                        int cid=course.getCid();
                        Course_Students cs=stu.getScores(cid,stuname);
                        String die = cs.getArrived();
                        String[] a=die.split(",");
                        len+=a.length;
                    }*/
                    String warn[]=new String[courses.size()*2];
                    int count=0;
                    int msgCount=0;//消息的数量
                    for (Course course:courses){
                        int cid=course.getCid();
                        String cname=course.getCourse();
                        //查询成绩
                        Course_Students cs=stu.getScores(cid,stuname);
                        String scores=cs.getScores();
                        String[] s=scores.split("\\|\\|");
                        //计算每门课的个人平均成绩
                        int sum=0;
                        int isCount=0;
                        for (int i=0;i<s.length;i++) {
                            if (!s[i].equals("empty")&&!s[i].equals("")){
                                int x = Integer.parseInt(s[i]);
                                sum += x;
                                isCount++;
                            }
                        }
                        int aver=100;
                        if (isCount!=0) {
                            aver=sum/isCount;//平时成绩
                        }
                        //判断成绩是否需要警告
                        if (aver<60){
                            warn[count]="亲，"+cname+"快要挂科了！！";
                            count++;
                            msgCount++;
                        }else {
                            warn[count]="empty";
                            count++;
                        }


                        //判断是否即将缺勤次数达到3次
                        int aCount=0;//签到的次数
                        String die = cs.getArrived();
                        if (!die.equals("")) {
                            String[] a=die.split(",");
                            for(int i=0;i<a.length;i++){
                                if (a[i].equals("0")) {
                                    aCount++;
                                }
                            }
                            if (aCount==2 &&a.length>=3) {
                                warn[count]=cname+"缺勤2次了，再缺1次就挂科了！";
                                count++;
                                msgCount++;
                            }else {
                                warn[count]="empty";
                                count++;
                            }
                        }

                    }
                    session.setAttribute("warn",warn);
                    session.setAttribute("msgCount",msgCount);


                    return "redirect:/";
                } else {
                    //登录失败，返回登录页面
                    map.put("msg", "用户名/密码错误");
                    return "studentlogin";//这是登录页面的地址
                }
            }else {
                //登录失败，返回登录页面
                map.put("msg", "用户名不存在");
                return "studentlogin";
            }

        } catch (NumberFormatException e) {
            map.put("msg", "用户名/密码错误");
            return "studentlogin";
        }

    }


    //教师登录
    @PostMapping("/teacher/login")
    public String Teacherlogin(@RequestParam("tname") String teacherID,
                               @RequestParam("tpsw") String tpsw,
                               @RequestParam("validatecode") String validatecode,
                               Map<String,Object> map, HttpSession session){

        try {
            int tname=Integer.parseInt(teacherID);

            Teacher loginTeacher = tea.getTeaByid(tname);

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
                    session.setAttribute("Main",loginTeacher);
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


        } catch (NumberFormatException e) {
            //登录失败
            map.put("msg", "用户名/密码错误");
            return "teacherlogin";
        }

    }


    //管理员登录
    @PostMapping("/admin/login")
    public String Adminlogin(@RequestParam("aname") String adminID,
                               @RequestParam("apsw") String apsw,
                               @RequestParam("validatecode") String validatecode,
                               Map<String,Object> map, HttpSession session){

        try {

            int aname=Integer.parseInt(adminID);

            Admin loginAdmin = ad.getAdminByid(aname);

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
                    session.setAttribute("Main",loginAdmin);
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

        } catch (NumberFormatException e) {
            //登录失败
            map.put("msg", "用户名/密码错误");
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
