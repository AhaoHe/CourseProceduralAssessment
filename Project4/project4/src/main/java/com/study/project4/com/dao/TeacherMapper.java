package com.study.project4.com.dao;

import com.study.project4.com.entity.Teacher;

import java.util.List;

public interface TeacherMapper {

    public List<Teacher> getTeaAll();

    public Teacher getTeaByID(Integer id);

    public void addTeacher(Teacher teacher);

}
