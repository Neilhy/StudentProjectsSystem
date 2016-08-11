package com.scut.cs.domain;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * Created by NeilHY on 2016/8/7.
 */
@Entity
public class Student {


    @Id
    @Column(name = "studentId")
    private Long id;

    @Column(nullable = false,length = 20)
    private String studentName;

    @Column(nullable = false)
    private String college;

    @Column(nullable = false,length = 20)
    private String className;

    @Column(nullable = false)
    private Short grade;

    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE},fetch = FetchType.EAGER)
//    @JoinTable(name = "project_student",
//            joinColumns =@JoinColumn(name = "student_ID",referencedColumnName = "studentId"),
//            inverseJoinColumns = @JoinColumn(name="project_ID",referencedColumnName = "projectId"))
    private List<Project> projectList;

    public Student() {
    }

    public Student(Long id, String studentName, String college, String className, Short grade, List<Project> projectList) {
        this.id = id;
        this.studentName = studentName;
        this.college = college;
        this.className = className;
        this.grade = grade;
        this.projectList = projectList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<Project> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<Project> projectList) {
        this.projectList = projectList;
    }
}
