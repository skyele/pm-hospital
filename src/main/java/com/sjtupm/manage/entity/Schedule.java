package com.sjtupm.manage.entity;

import javax.persistence.*;

@Entity
@Table(name = "schedule")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name="docterID")
    private Integer docterID;

    @Column(name="docterName")
    private String docterName;

    @Column(name="day")
    private String day;

    public Integer getId() {
        return id;
    }

    public Integer getDocterID() {
        return docterID;
    }

    public void setDocterID(Integer docterID) {
        this.docterID = docterID;
    }

    public String getDocterName() {
        return docterName;
    }

    public void setDocterName(String docterName) {
        this.docterName = docterName;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
