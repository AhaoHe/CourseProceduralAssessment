package com.study.project4.com.entity;

public class Course_Students {
    Course course;
    Student student;
    int ifjoin;
    String scores;
    String arrived;

    public String getArrived() {
        return arrived;
    }

    public void setArrived(String arrived) {
        this.arrived = arrived;
    }

    public String getScores() {
        return scores;
    }

    public void setScores(String scores) {
        this.scores = scores;
    }

    public int getIfjoin() {
        return ifjoin;
    }

    public void setIfjoin(int ifjoin) {
        this.ifjoin = ifjoin;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
