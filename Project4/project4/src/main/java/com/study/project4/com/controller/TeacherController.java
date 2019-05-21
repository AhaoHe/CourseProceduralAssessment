package com.study.project4.com.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.study.project4.com.entity.Course;
import com.study.project4.com.entity.Course_Students;
import com.study.project4.com.service.CourseService;
import com.study.project4.com.service.StudentService;
import com.study.project4.com.service.TeacherService;
import com.sun.deploy.util.StringUtils;
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
public class TeacherController {

    @Autowired
    public TeacherService teacherService=new TeacherService();
    @Autowired
    CourseService courseService=new CourseService();
    @Autowired
    StudentService studentService=new StudentService();


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



        //查询章节数
        Course course=courseService.getCourseByCid(cid);
        String chapters=course.getChapters();
        String[] c=chapters.split("\\|\\|");
        model.addAttribute("chapters",c);

        //查询成绩
        List<Course_Students> course_students=studentService.getScoresAll(cid);
        List<String[]> list=new ArrayList<String[]>();
        for(Course_Students course_student:course_students) {
            String scores = course_student.getScores();
            String[] s = scores.split("\\|\\|");
            list.add(s);
        }
        model.addAttribute("scores",list);

        return "teachers/course_students";
    }

    //查询某门课程有哪些章节
    @GetMapping("/teachers/course/{cid}")
    public String courses_main(@PathVariable("cid")int cid,
                               Model model,
                               HttpSession session){
        Course course=courseService.getCourseByCid(cid);
        String chapters=course.getChapters();
        String[] c=chapters.split("\\|\\|");
        model.addAttribute("chapters",c);
        model.addAttribute("courseID",cid);
        return "teachers/courses";
    }

    //删除某门课的某个章节
    @RequestMapping("/teachers/deleteChapter/{cid}/{id}")
    public String deleteChapter(@PathVariable("cid")int cid,
                                @PathVariable("id")int id){
        Course course=courseService.getCourseByCid(cid);
        String chapters=course.getChapters();
        String[] c=chapters.split("\\|\\|");
        List<String> list = new ArrayList<String>();
        for (int i=0; i<c.length; i++) {
            list.add(c[i]);
        }
        list.remove(id);
        String sum = StringUtils.join(list,"||");
        courseService.updateChapters(sum,cid);

        //删除学生成绩
        List<String> listScores = new ArrayList<String>();
        List<Course_Students> course_students=courseService.findCourse_Students(cid);
        for (Course_Students student:course_students){
            int Stuid=student.getStudent().getId();
            Course_Students cs=studentService.getScores(cid,Stuid);
            String scores=cs.getScores();
            String[] s=scores.split("\\|\\|");
            for (int i=0; i<s.length; i++) {
                listScores.add(s[i]);
            }
            listScores.remove(id);
            String sumScores =StringUtils.join(listScores,"||");
            courseService.updateScores(sumScores,cid,Stuid);
            listScores.clear();
        }
        return "redirect:/teachers/course/"+cid;
    }

    //添加某门课的某个章节
    @RequestMapping("/teachers/addChapters/{cid}")
    public String addChapter(@RequestParam("chapterName")String chapterName,
                             @RequestParam("scores")String BeginScore,
                             @PathVariable("cid")int cid){
        Course course=courseService.getCourseByCid(cid);
        String chapters=course.getChapters();
        String[] c=chapters.split("\\|\\|");
        List<String> list = new ArrayList<String>();
        for (int i=0;i<c.length;i++){
            list.add(c[i]);
        }
        list.add(chapterName);
        String sum =StringUtils.join(list,"||");
        courseService.updateChapters(sum,cid);

        //添加学生成绩
        List<String> listScores = new ArrayList<String>();
        List<Course_Students> course_students=courseService.findCourse_Students(cid);
        for (Course_Students student:course_students){
            int Stuid=student.getStudent().getId();
            Course_Students cs=studentService.getScores(cid,Stuid);
            String scores=cs.getScores();
            String[] s=scores.split("\\|\\|");
            for (int i=0; i<s.length; i++) {
                listScores.add(s[i]);
            }
            listScores.add(BeginScore);
            String sumScores =StringUtils.join(listScores,"||");
            courseService.updateScores(sumScores,cid,Stuid);
            listScores.clear();
        }

        return "redirect:/teachers/course/"+cid;
    }

    //修改章节名称
    @RequestMapping("/teachers/updateChapter/{cid}")
    public String UpdateChapter(@PathVariable("cid")int cid,
                                @RequestParam("chapterId")int chapterId,
                                @RequestParam("chapterName")String chapterName){

        Course course=courseService.getCourseByCid(cid);
        String chapters=course.getChapters();
        String[] c=chapters.split("\\|\\|");
        List<String> list = new ArrayList<String>();
        for (int i=0;i<c.length;i++){
            if (i!=chapterId){
                list.add(c[i]);
            }else {
                list.add(chapterName);
            }
        }
        String sum =StringUtils.join(list,"||");
        courseService.updateChapters(sum,cid);
        return "redirect:/teachers/course/"+cid;
    }


    //查询某个学生的成绩
    @RequestMapping("/students/scores/{cid}/{id}")
    public String findScores(@PathVariable("cid") Integer cid,
                             @PathVariable("id") Integer id,
                             Model model){

return "";
    }

}
