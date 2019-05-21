package com.study.project4.com.entity;


public class Course {
    int cid;
    String course;
    int ifjoin;
    Teacher teacher;
    String chapters;

    public String getChapters() {
        return chapters;
    }

    public void setChapters(String chapters) {
        this.chapters = chapters;
    }

    public int getIfjoin() {
        return ifjoin;
    }

    public void setIfjoin(int ifjoin) {
        this.ifjoin = ifjoin;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
}
