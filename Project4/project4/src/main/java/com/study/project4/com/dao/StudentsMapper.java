package com.study.project4.com.dao;

import com.study.project4.com.entity.ClassName;
import com.study.project4.com.entity.Course_Students;
import com.study.project4.com.entity.Student;
import org.apache.ibatis.annotations.*;

import java.util.List;

//必须设置@Mapper在cProject4Application中设置@MapperScan扫描mapper文件
//在Mapper文件中用注解写SQL语句

public interface StudentsMapper {

    //查询所有学生
    @Select("select * from Student")
    @Results(
            @Result(column = "classid", property = "className",javaType = ClassName.class,
                    one = @One(select = "com.study.project4.com.dao.StudentsMapper.getClassNameById"))

    )
    public List<Student> getStuAll();

    //查询班级信息
    @Select("select * from classname where classid=#{classid}")
    public ClassName getClassNameById(int classid);

    //根据学生id查询学生信息
    @Select("select * from student where id = #{id}")
    @Results(
            @Result(column = "classid", property = "className",javaType = ClassName.class,
                    one = @One(select = "com.study.project4.com.dao.StudentsMapper.getClassNameById"))

    )
    public  Student getStuByid(Integer id);



    //查询班级信息
    @Select("SELECT * FROM classname GROUP BY classid")
    public  List<ClassName> getClassName();

    //查询某个人某门课成绩
    @Select("SELECT * FROM course_students WHERE cid=#{cid} AND id=#{id}")
    public Course_Students getScoresByCidandId(@Param("cid")int cid,
                                               @Param("id")int id);
    //查询某门课成绩
    @Select("SELECT * FROM course_students WHERE cid=#{cid} ORDER By id asc")
    public List<Course_Students> getScoresByCid(int cid);
}
