package com.study.project4.com.entity;

public class Teacher {
    int tid;
    String tname;
    String tpsw;
    int tsex;//0为女，1为男

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getTpsw() {
        return tpsw;
    }

    public void setTpsw(String tpsw) {
        this.tpsw = tpsw;
    }

    public int getTsex() {
        return tsex;
    }

    public void setTsex(int tsex) {
        this.tsex = tsex;
    }
}
