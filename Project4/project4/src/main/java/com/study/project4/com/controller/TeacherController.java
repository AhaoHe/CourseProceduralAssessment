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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;

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

        //初始化学生成绩
            //查询章节信息
        Course course=courseService.getCourseByCid(Cid);
        String chapters=course.getChapters();
        String[] c=chapters.split("\\|\\|");
        List<String> list=new ArrayList<String>();//成绩list
        if(!c[0].isEmpty()) {
            for (int i = 0; i < c.length; i++) {//添加成绩
                list.add("empty");
            }
            String sumScores = StringUtils.join(list, "||");
            courseService.updateScores(sumScores, Cid, Sid);//添加成绩到数据库
        }


        //初始化学生签到情况
        String ifqiandao=course.getIfqiandao();//签到模式
        String[] q=ifqiandao.split(",");
        if(!q[0].isEmpty()) {
            for (int i = 0; i < q.length; i++) {
                q[i] = "2";
            }
            String sum = String.join(",", q);
            studentService.updateArrived(sum, Cid, Sid);
        }


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
        //查询章节数
        Course course=courseService.getCourseByCid(cid);
        String chapters=course.getChapters();
        String[] c=chapters.split("\\|\\|");

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
        //课程id
        model.addAttribute("cid",cid);


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
                               Model model){
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
            if (BeginScore.isEmpty()){
                listScores.add("empty");
            }else {
                listScores.add(BeginScore);
            }
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


    //修改/添加学生信息学生的成绩
    @RequestMapping("/teachers/AddScores")
    @ResponseBody
    public Map<String,Object> findScores(@RequestParam("scores")String scores,
                                         @RequestParam("cid")int cid,
                                         @RequestParam("id")int id){
        Map<String,Object> msg=new HashMap<String, Object>();
        msg.put("msg","修改成功");

        courseService.updateScores(scores,cid,id);

        return msg;
    }

    //查询签到信息
    @GetMapping("/teachers/course_qiandao/{cid}")
    public String qiandao_main(@PathVariable("cid")int cid,
                               Model model){
        Course course=courseService.getCourseByCid(cid);
        String ifqiandao=course.getIfqiandao();//签到模式
        String[] q=ifqiandao.split(",");
        model.addAttribute("ifqiandao",q);
        model.addAttribute("courseID",cid);

        int count=studentService.getCourse_Count(cid);//这门课总人数
        model.addAttribute("count",count);

        return "teachers/qiandao";
    }

    //查询已经签到的人
    @RequestMapping("/teachers/getQiandao")
    @ResponseBody
    public List<Course_Students> qiandao_Already(@RequestParam("cid")int cid,
                                                 @RequestParam("index")int index){
        List<Course_Students> course_students=studentService.getScoresAll(cid);
        List<Course_Students> qiandao=new ArrayList<Course_Students>();//签到人名单
        for(Course_Students cs:course_students){
            String arrived=cs.getArrived();
            String[] a=arrived.split(",");
            if (a[index].equals("1")||a[index].equals("2")) {
                if (a[index].equals("1")) {
                    cs.setArrived("1");
                }else {
                    cs.setArrived("2");
                }
                qiandao.add(cs);
            }
        }

        return qiandao;
    }
    //查询迟到的人
    @RequestMapping("/teachers/getChidao")
    @ResponseBody
    public List<Course_Students> chidao_Already(@RequestParam("cid")int cid,
                                                @RequestParam("index")int index){
        List<Course_Students> course_students=studentService.getScoresAll(cid);
        List<Course_Students> chidao=new ArrayList<Course_Students>();//迟到人名单
        for(Course_Students cs:course_students){
            String arrived=cs.getArrived();
            String[] a=arrived.split(",");
            if (a[index].equals("3")) {
                cs.setArrived("3");
                chidao.add(cs);
            }
        }

        return chidao;
    }
    //查询旷课的人
    @RequestMapping("/teachers/getOthers")
    @ResponseBody
    public List<Course_Students> others_Already(@RequestParam("cid")int cid,
                                                @RequestParam("index")int index){
        List<Course_Students> course_students=studentService.getScoresAll(cid);
        List<Course_Students> others=new ArrayList<Course_Students>();//未签到人名单
        for(Course_Students cs:course_students){
            String arrived=cs.getArrived();
            String[] a=arrived.split(",");
            if (a[index].equals("0")) {
                cs.setArrived("0");
                others.add(cs);
            }
        }

        return others;
    }

    //更改签到情况
    @GetMapping("/teachers/UpdateQiandao/{cid}/{x}/{index}")
    public String updateQiandao(@PathVariable("cid")int cid,
                                @PathVariable("x")int x,
                                @PathVariable("index")int index){
        Course course=courseService.getCourseByCid(cid);
        String ifqiandao=course.getIfqiandao();//签到模式
        String[] q=ifqiandao.split(",");
        q[index]=String.valueOf(x);
        String sum=String.join(",",q);
        teacherService.updateQiandao(cid, sum);
        return "redirect:/teachers/course_qiandao/"+cid;
    }

    //添加签到
    @GetMapping("/teachers/AddQiandao/{cid}")
    public String addQiandao(@PathVariable("cid")int cid){
        Course course=courseService.getCourseByCid(cid);
        String ifqiandao=course.getIfqiandao();//签到模式
        String[] q=ifqiandao.split(",");
        List<String> list=new ArrayList<String>();
        for (int i=0;i<q.length;i++) {
            list.add(q[i]);
        }
        list.add("0");
        String sum=StringUtils.join(list,",");
        teacherService.updateQiandao(cid, sum);

        //给所有学生添加签到信息
        List<Course_Students> course_students=studentService.getScoresAll(cid);
        for (Course_Students cs:course_students){
            String csQiandao=cs.getArrived();
            String[] CSQ=csQiandao.split(",");
            List<String> Stulist=new ArrayList<String>();
            for (int i=0;i<q.length;i++) {
                Stulist.add(q[i]);
            }
            Stulist.add("0");
            String StuSum=StringUtils.join(list,",");
            teacherService.updateStuQiandao(cid,StuSum);
        }

        return "redirect:/teachers/course_qiandao/"+cid;
    }

    //删除签到
    @GetMapping("/teachers/DelQiandao/{cid}/{index}")
    public String delQiandao(@PathVariable("cid")int cid,
                             @PathVariable("index")int index){
        Course course=courseService.getCourseByCid(cid);
        String ifqiandao=course.getIfqiandao();//签到模式
        String[] q=ifqiandao.split(",");
        List<String> list=new ArrayList<String>();
        for (int i=0;i<q.length;i++) {
            list.add(q[i]);
        }
        list.remove(index);
        String sum=StringUtils.join(list,",");
        teacherService.updateQiandao(cid, sum);

        //给所有学生删除签到信息
        List<Course_Students> course_students=studentService.getScoresAll(cid);
        for (Course_Students cs:course_students){
            String csQiandao=cs.getArrived();
            String[] CSQ=csQiandao.split(",");
            List<String> Stulist=new ArrayList<String>();
            for (int i=0;i<q.length;i++) {
                Stulist.add(q[i]);
            }
            Stulist.remove(index);
            String StuSum=StringUtils.join(list,",");
            teacherService.updateStuQiandao(cid,StuSum);
        }

        return "redirect:/teachers/course_qiandao/"+cid;
    }

}
