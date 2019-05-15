package com.study.project4.com.dao;

import com.study.project4.com.entity.Course;
import com.study.project4.com.entity.Course_Students;
import com.study.project4.com.entity.Student;
import com.study.project4.com.entity.Teacher;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface TeacherMapper {

    //查询该id老师的信息
    @Select("select * from teacher where tid=#{tid}")
    public Teacher getTeacherById(Integer tid);

    //通过id老师查询课程信息
    @Select("SELECT c.* " +
            "FROM course c,teacher t " +
            "WHERE t.tid=#{tid} AND t.tid=c.tid " +
            "GROUP BY c.cid")
    @Results({
            @Result(column = "tid", property = "teacher",javaType = Teacher.class,
                    one = @One(select = "com.study.project4.com.dao.TeacherMapper.getTeacherById"))
    })
    public List<Course> getCourseByTid(Integer tid);

    //查询哪些学生申请
    @Select("SELECT c.*,cs.ifjoin ifjoin,cs.id,s.* " +
            "FROM course c,course_students cs,teacher t,student s " +
            "WHERE t.tid=#{tid} AND cs.ifjoin=1 AND cs.cid=c.cid AND t.tid=c.tid AND s.id=cs.id")
    @Results({
            @Result(column = "id", property = "student",javaType = Student.class,
                    one = @One(select = "com.study.project4.com.dao.StudentsMapper.getStuByid")),
            @Result(column = "cid", property = "course",javaType = Course.class,
                    one = @One(select = "com.study.project4.com.dao.CourseMapper.getCourseByCid"))
    })
    public List<Course_Students> getaddStuByTid(Integer tid);

//允许学生添加课程  //撤回删除某门课的某个学生  //管理员拒绝删除某门课的某个学生
    @Update("UPDATE course_students SET ifjoin=2 WHERE cid=#{cid} AND id=#{id}")
    public int allowStudents(@Param("cid") Integer cid,
                             @Param("id") Integer id);
//拒绝学生  //管理员同意删除某个学生
    @Delete("DELETE FROM course_students WHERE cid=#{cid} AND id=#{id}")
    public  int refuseStudents(@Param("cid") Integer cid,
                               @Param("id") Integer id);

    //申请删除某门课的学生
    @Update("UPDATE course_students SET ifjoin=3 WHERE cid=#{cid} AND id=#{id}")
    public int deleteTStudents(@Param("cid") Integer cid,
                             @Param("id") Integer id);

//查询所有老师信息
    public List<Teacher> getTeaAll();
//通过ID得到老师信息
    public Teacher getTeaByID(Integer id);
//添加老师
    public void addTeacher(Teacher teacher);
    //修改老师
    @Update("UPDATE student SET tname=#{tname},tpsw=#{tpsw},tsex=#{tsex} " +
            "WHERE tid=#{tid}")
    public int updateTeacher(Teacher teacher);


}
