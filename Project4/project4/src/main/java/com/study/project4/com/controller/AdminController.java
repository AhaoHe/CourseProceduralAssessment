package com.study.project4.com.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.study.project4.com.entity.*;
import com.study.project4.com.service.AdminService;
import com.study.project4.com.service.CourseService;
import com.study.project4.com.service.StudentService;
import com.study.project4.com.service.TeacherService;
import com.sun.deploy.util.StringUtils;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public String AdminStudents(@RequestParam(value="start",defaultValue="0")int start,
                                @RequestParam(value = "size", defaultValue = "10") int size,
                                Model model){
        //1. 在参数里接受当前是第几页 start ，以及每页显示多少条数据 size。 默认值分别是0和5。
        //2. 根据start,size进行分页，并且设置id 正序  如果为倒叙，则为 desc
        PageHelper.startPage(start,size);
        //3. 因为PageHelper的作用，这里就会返回当前分页的集合了
        List<Student> students=studentService.getStuAll();//查询所有学生
        //4. 根据返回的集合，创建PageInfo对象
        PageInfo<Student> page = new PageInfo<>(students);
        //5. 把PageInfo对象扔进model，以供后续显示
        model.addAttribute("page", page);
        //6. 跳转到listCategory.jsp
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

    //(已经不是这个作用了)查询哪门课有哪些老师要删除哪些学生
    //查询这门课所有学生的状况（包括申请中，被申请删除的学生），并可以进行操作
    @RequestMapping("/Admin/TeaDelStu/{cid}")
    public String TeaDelStu(Model model,@PathVariable("cid")int cid,
                            @RequestParam(value="start",defaultValue="0")int start,
                            @RequestParam(value = "size", defaultValue = "10") int size){
        //List<Course_Students> courses=adminService.findTeaDelStuAll(id);查询ifjoin=3的学生，即申请被删除的学生。方法已经被淘汰


        //1. 在参数里接受当前是第几页 start ，以及每页显示多少条数据 size。 默认值分别是0和5。
        //2. 根据start,size进行分页，并且设置id 正序  如果为倒叙，则为 desc
        PageHelper.startPage(start,size);
        //3. 因为PageHelper的作用，这里就会返回当前分页的集合了
        List<Course_Students> cs=courseService.getStudentsAllByCid(cid);//查询该门课的所有学生（所有状态的学生）
        //4. 根据返回的集合，创建PageInfo对象
        PageInfo<Course_Students> page = new PageInfo<>(cs);
        //5. 把PageInfo对象扔进model，以供后续显示
        model.addAttribute("page", page);
        //6. 跳转到listCategory.jsp

        try {
            List<Student> otherStu=new ArrayList<>();
            List<Student> students = studentService.getStuAll();
            for (Student student : students) {
                int id = student.getId();
                Course_Students c_stu = studentService.getScores(cid, id);//判断学生是否添加过该课程
                if (c_stu != null) {//判断该学生是否存在于该课程下
                    if (c_stu.getIfjoin()!=2 && c_stu.getIfjoin()!=3){
                        //如果添加过，判断是否已经成功添加到课程,如果还没正式添加到课程，则依旧显示
                        otherStu.add(student);
                    }
                }else {
                    otherStu.add(student);
                }
            }
            model.addAttribute("others",otherStu);
        }catch (Exception e){
            e.printStackTrace();
        }


        //model.addAttribute("courses",courses);
        model.addAttribute("courseID",cid);
        model.addAttribute("course_students",cs);
        return "admin/courses";
    }

    //把未添加的学生导入
    @RequestMapping("/Admin/importStu")
    @ResponseBody
    public String importStudents(Model model,
                                 @RequestParam("cid")int cid,
                                 @RequestParam("items")String items){
        String msg;
        try {
            String []item=items.split(",");
            for(int x=0;x<item.length;x++){
                int sid=Integer.parseInt(item[x]);
                Course_Students cs=studentService.getScores(cid,sid);
                if (cs!=null) {
                    teacherService.allowStudents(cid, sid);
                }else {
                    adminService.importStudents(cid,sid);
                }

                //初始化学生成绩
                //查询章节信息
                Course course=courseService.getCourseByCid(cid);
                String chapters=course.getChapters();
                String[] c=chapters.split("\\|\\|");
                List<String> list=new ArrayList<String>();//成绩list
                if(!c[0].isEmpty()) {
                    for (int i = 0; i < c.length; i++) {//添加成绩
                        list.add("empty");
                    }
                    String sumScores = StringUtils.join(list, "||");
                    courseService.updateScores(sumScores, cid, sid);//添加成绩到数据库
                }


                //初始化学生签到情况
                String ifqiandao=course.getIfqiandao();//签到模式
                String[] q=ifqiandao.split(",");
                if(!q[0].isEmpty()) {
                    for (int i = 0; i < q.length; i++) {
                        q[i] = "2";
                    }
                    String sum = String.join(",", q);
                    studentService.updateArrived(sum, cid, sid);
                }

            }
            msg="success";
            return msg;
        }catch (Exception e){
            e.printStackTrace();
            msg="error";
            return msg;
        }

    }


                            //管理员拒绝删除
    @GetMapping("/Admin/refuseDelStu/{Cid}/{Sid}/{pageNum}")
    public String allowStu(@PathVariable("Cid") Integer Cid,
                           @PathVariable("Sid") Integer Sid,
                           @PathVariable("pageNum")int start){
        teacherService.allowStudents(Cid, Sid);
        return "redirect:/Admin/TeaDelStu/"+Cid+"?start="+start;
    }

    //管理员同意删除学生
    @GetMapping("/Admin/allowDelStu/{Cid}/{Sid}/{pageNum}")
    public String refuseStu(@PathVariable("Cid") Integer Cid,
                            @PathVariable("Sid") Integer Sid,
                            @PathVariable("pageNum")int start){
        teacherService.refuseStudents(Cid,Sid);
        return "redirect:/Admin/TeaDelStu/"+Cid+"?start="+start;
    }

    //删除学生信息
    @GetMapping("/Admin/deleteS/{id}/{pageNum}")
    public String deleteStudent(@PathVariable("id") Integer id,
                                @PathVariable("pageNum") Integer pageNum){
        adminService.deleteStu(id);
        return "redirect:/Admin/students"+"?start="+pageNum;
    }
    //删除学生men信息
    @PostMapping("/Admin/deleteSs")
    @ResponseBody
    public String deleteStudents(@RequestParam("delitems")String delitems){
        String[] strs = delitems.split(",");
        for (int i = 0; i < strs.length; i++) {
            try {
                int id = Integer.parseInt(strs[i]);
                adminService.deleteStu(id);
            } catch (Exception e) {
            }
        }
        return "成功";
        //return "redirect:/Admin/students";
    }
    //删除老师信息
    @GetMapping("/Admin/deleteT/{id}")
    public String deleteTeacher(@PathVariable("id") Integer id){
        adminService.deleteTeacher(id);
        return "redirect:/Admin/teachers";
    }
    //删除教师men信息
    @PostMapping("/Admin/deleteTs")
    @ResponseBody
    public String deleteTeachers(@RequestParam("delitems")String delitems){
        String[] strs = delitems.split(",");
        for (int i = 0; i < strs.length; i++) {
            try {
                int id = Integer.parseInt(strs[i]);
                adminService.deleteTeacher(id);
            } catch (Exception e) {
            }
        }
        return "成功";
    }
    //删除课程信息
    @GetMapping("/Admin/deleteC/{id}")
    public String deleteCourse(@PathVariable("id") Integer id){
        adminService.deleteCourse(id);
        return "redirect:/adminMain";
    }
    //删除课程men信息
    @RequestMapping("/Admin/deleteCs")
    @ResponseBody
    public Map<String,Object> deleteCourses(@RequestParam("delitems")String delitems){
        Map<String,Object> msg=new HashMap<String, Object>();
        msg.put("msg","成功");
        String[] strs = delitems.split(",");
        for (int i = 0; i < strs.length; i++) {
            try {
                int id = Integer.parseInt(strs[i]);
                adminService.deleteCourse(id);
            } catch (Exception e) {
            }
        }
        return msg;
        //return "redirect:/adminMain";
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
                               @RequestParam("information")String information,
                               @RequestParam("type")String type,
                               @RequestParam("tid") Integer tid){
        Course course = new Course();
        Teacher teacher;
        course.setCid(cid);
        teacher=teacherService.getTeaByid(tid);
        course.setTeacher(teacher);
        course.setCourse(cname);
        course.setInformation(information);
        course.setType(type);
        adminService.updateCourse(course);
        return "redirect:/adminMain";
    }
    //添加课程信息
    @RequestMapping("/Admin/addCourse")
    public String addCourse(@RequestParam("addcourse")String cname,
                            @RequestParam("addtid") Integer tid,
                            @RequestParam("addtype") String type,
                            @RequestParam("addinformation") String information){
        Course course = new Course();
        Teacher teacher;
        teacher=teacherService.getTeaByid(tid);
        course.setTeacher(teacher);
        course.setCourse(cname);
        course.setInformation(information);
        course.setType(type);
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
                                @RequestParam("classid")int classid,
                                @RequestParam("pageNum")int pageNum){
        Student student=new Student();
        ClassName className=new ClassName();
        className.setClassid(classid);

        student.setId(sid);
        student.setSname(sname);
        student.setSpsw(spswd);
        student.setSsex(ssex);
        student.setClassName(className);

        adminService.updateStu(student);
        return "redirect:/Admin/students"+"?start="+pageNum;
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


    /**
     * 导入学生清单
     * @param file
     * @param clientid
     * @return
     * @throws IOException
     */
    @RequestMapping("/Admin/importExcel")
    @ResponseBody
    public String importUsers(@RequestParam MultipartFile file, Integer clientid, HttpServletRequest request) throws IOException {
        String message = "";//返回的信息
        boolean FLAG;//身份状态

        List<Student> list = new ArrayList<Student>();
        XSSFWorkbook workbook =null;

        //把MultipartFile转化为File
       /* CommonsMultipartFile cmf= (CommonsMultipartFile)file;
        DiskFileItem dfi=(DiskFileItem) cmf.getFileItem();
        File fo=dfi.getStoreLocation();*/

        MultipartFile f = file;
        System.out.println("上传文件的key："+f.getName()); // 为上传的key值
        System.out.println("文件名："+f.getOriginalFilename()); // 上传的真实文件的文件名
        File dfile = null;
        File fo;
        try {
            dfile = File.createTempFile("prefix", "_" + f.getOriginalFilename());
            f.transferTo(dfile);
            fo = dfile;
        } catch (IOException e) {
            e.printStackTrace();
            message="文件无法转换";
            return message;
        }


        //创建Excel，读取文件内容
        workbook = new XSSFWorkbook(FileUtils.openInputStream(fo));

        //获取第一个工作表
        XSSFSheet sheet = workbook.getSheet("学生信息");

        if (sheet==null){
            message="error";
            return message;
        }

        //获取sheet中第一行行号
        int firstRowNum = sheet.getFirstRowNum();
        //获取sheet中最后一行行号
        int lastRowNum = sheet.getLastRowNum();



        try {
            //循环插入数据
            for(int i=firstRowNum+1;i<=lastRowNum;i++){
                XSSFRow row = sheet.getRow(i);

                Student users = new Student();


                XSSFCell username = row.getCell(0);//学生名称
                if(username!=null){
                    username.setCellType(Cell.CELL_TYPE_STRING);
                    users.setSname((username.getStringCellValue()));
                }

                XSSFCell password = row.getCell(1);//密码
                if(password!=null){
                    password.setCellType(Cell.CELL_TYPE_STRING);
                    users.setSpsw((password.getStringCellValue()));
                }

                XSSFCell sex = row.getCell(2);//性别
                if(sex!=null){
                    sex.setCellType(Cell.CELL_TYPE_STRING);
                    if (sex.getStringCellValue().equals("男")){
                        users.setSsex(1);
                    }else {
                        users.setSsex(0);
                    }
                }

                XSSFCell classname = row.getCell(3);//班级
                if(classname!=null){
                    classname.setCellType(Cell.CELL_TYPE_STRING);
                    String name=classname.getStringCellValue();
                    ClassName cn=new ClassName();
                    cn.setClassid(Integer.parseInt(name));
                    users.setClassName(cn);
                }
                list.add(users);
                adminService.addStu(users);
            }
            //adminService.addStu(users);//往数据库插入数据
            System.out.println("添加了学生:"+list);
            message="success";
            return message;
        } catch (Exception e) {
            message = "Error Caused By:"+e.getMessage();
            e.printStackTrace();
            return message;
        }
    }

}
