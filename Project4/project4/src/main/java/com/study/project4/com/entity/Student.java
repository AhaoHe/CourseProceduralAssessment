package com.study.project4.com.entity;

public class Student {
    int id;
    String sname;
    String spsw;
    int ssex;
    ClassName className;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getSpsw() {
        return spsw;
    }

    public void setSpsw(String spsw) {
        this.spsw = spsw;
    }

    public int getSsex() {
        return ssex;
    }

    public void setSsex(int ssex) {
        this.ssex = ssex;
    }

    public ClassName getClassName() {
        return className;
    }

    public void setClassName(ClassName className) {
        this.className = className;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", 学生姓名='" + sname + '\'' +
                ", 密码='" + spsw + '\'' +
                ", 性别=" + ssex +
                ", classID=" + className.getClassid() +
                '}';
    }
}
