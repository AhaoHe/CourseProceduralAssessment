package com.study.project4.com.dao;

import com.study.project4.com.entity.Course;
import com.study.project4.com.entity.Course_Students;
import com.study.project4.com.entity.Student;
import com.study.project4.com.entity.Teacher;
import org.apache.ibatis.annotations.*;

//必须设置@Mapper在cProject4Application中设置@MapperScan扫描mapper文件
//在Mapper文件中用注解写SQL语句

import java.util.List;

public interface CourseMapper {


    //通过id学生查询课程信息
    @Select("SELECT c.* " +
            "FROM course c,course_students cs,teacher t " +
            "WHERE cs.id=#{id} AND cs.ifjoin=2 AND cs.cid=c.cid AND t.tid=c.tid " +
            "ORDER BY c.cid ASC")
    @Results({
            @Result(column = "tid", property = "teacher",javaType = Teacher.class,
                    one = @One(select = "com.study.project4.com.dao.TeacherMapper.getTeacherById"))
    })
    public List<Course> getCourseById(Integer id);

    //查询所有存在的课程信息
    @Select("SELECT c.* " +
            "FROM course c,teacher t " +
            "WHERE t.tid=c.tid "+
            "GROUP BY c.cid")
    @Results({
            @Result(column = "tid", property = "teacher",javaType = Teacher.class,
                    one = @One(select = "com.study.project4.com.dao.TeacherMapper.getTeacherById")),
            })
    public List<Course> getCoursesAll();


    //查看每门课申请的情况，ifjoin=2已经添加课程，=1表示正在申请，=0表示未申请,3表示申请删除
    @Select("SELECT c.*,cs.ifjoin ifjoin " +
            "FROM course c,course_students cs " +
            "WHERE cs.id=#{id} AND cs.cid=c.cid " +
            "GROUP BY ifjoin,c.cid")
    public List<Course> getIfJoinById(Integer id);

    //通过课程id查询课程信息
    //end=0表示在上课，end=2表示申请结课，end=3表示申请恢复课程，end=1表示结课
    //socres组成：a,b,c,d   ——a表示迟到扣分情况，b表示缺勤扣分情况，c表示
    @Select("SELECT * FROM course WHERE cid=#{cid}")
    public Course getCourseByCid(Integer cid);


    //添加申请
    @Insert("INSERT INTO course_students(cid,id,ifjoin) VALUES (#{cid},#{id},#{ifjoin})")
    public int addIfJoin(@Param("cid")Integer cid,
                         @Param("id")Integer id,
                         @Param("ifjoin")Integer ifjoin);

    //查询某门课有哪些学生
    @Select("SELECT s.*,c.cid,cs.ifjoin " +
            "FROM course c,course_students cs,student s " +
            "WHERE cs.ifjoin IN (2,3) AND cs.cid=c.cid AND s.id=cs.id AND cs.cid=#{cid} " +
            "ORDER By s.id asc")
    @Results({
            @Result(column = "id", property = "student",javaType = Student.class,
                    one = @One(select = "com.study.project4.com.dao.StudentsMapper.getStuByid")),
            @Result(column = "cid", property = "course",javaType = Course.class,
                    one = @One(select = "com.study.project4.com.dao.CourseMapper.getCourseByCid"))
    })
    public List<Course_Students> COURSE_STUDENTS(Integer cid);


    //真·查询所有学生，不管是申请中还是请求删除的
    @Select("SELECT * FROM course_students WHERE cid=#{cid} ORDER By id asc")
    @Results({
            @Result(column = "id", property = "student",javaType = Student.class,
                    one = @One(select = "com.study.project4.com.dao.StudentsMapper.getStuByid")),
            @Result(column = "cid", property = "course",javaType = Course.class,
                    one = @One(select = "com.study.project4.com.dao.CourseMapper.getCourseByCid"))
    })
    public List<Course_Students> getStudentsAllByCid(int cid);

    //修改或者删除章节
    @Update("UPDATE course SET chapters=#{chapters} WHERE cid=#{cid}")
    public int UpdateandDelChapters(@Param("chapters") String chapters,
                                       @Param("cid") int cid);

    //修改或者删除成绩
    @Update("UPDATE course_students SET scores=#{scores} WHERE cid=#{cid} AND id=#{id}")
    public int UpdateandDelScores(@Param("scores") String scores,
                                  @Param("cid") int cid,
                                  @Param("id") int id);

    //修改或者删除难度
    @Update("UPDATE course SET hardness=#{hardness} WHERE cid=#{cid}")
    public int UpdateandDelHardness(@Param("hardness") String hardness,
                                    @Param("cid") int cid);


}
