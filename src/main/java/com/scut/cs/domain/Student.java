package com.scut.cs.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Created by NeilHY on 2016/8/7.
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
public class Student implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "studentId")
    private Long id;

    @Column(nullable = false,unique = false)
    private Long registerId;//学号,因为这个人可以在不同的项目中，担任队长或者队员

    @Column(nullable = false,length = 20)
    private String studentName;

    @Column(nullable = false)
    private String college;

    @Column(nullable = false,length = 20)
    private String className;

    @Column(nullable = false)
    private Short grade;

    @Column(nullable = false,columnDefinition = "TINYINT UNSIGNED")
    private Integer captainOrNot=0; //判断是否是队长,0表示队员，1表示队长。

//    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE},fetch = FetchType.LAZY)
    @JoinTable(name="ProjectStudentList",
            joinColumns ={@JoinColumn(name = "studentId")},
            inverseJoinColumns = {@JoinColumn(name="projectId")})
    private List<Project> projectList;

    public Student() {
    }

    public Student(Long registerId, String studentName, String college, String className, Short grade,Integer captainOrNot, List<Project> projectList) {
        this.registerId = registerId;
        this.studentName = studentName;
        this.college = college;
        this.className = className;
        this.grade = grade;
        this.captainOrNot=captainOrNot;
        this.projectList = projectList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRegisterId() {
        return registerId;
    }

    public void setRegisterId(Long registerId) {
        this.registerId = registerId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Short getGrade() {
        return grade;
    }

    public void setGrade(Short grade) {
        this.grade = grade;
    }

    public Integer getCaptainOrNot() {
        return captainOrNot;
    }

    public void setCaptainOrNot(Integer captainOrNot) {
        this.captainOrNot = captainOrNot;
    }

//    @JsonBackReference
    public List<Project> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<Project> projectList) {
        this.projectList = projectList;
    }
}
