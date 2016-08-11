package com.scut.cs.domain;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by NeilHY on 2016/8/7.
 */
@Entity
public class Project {
    @Id
    @GeneratedValue
    @Column(name = "projectId")
    private Long id;

    @Column(nullable = false)
    private String projectName;
    @Column(length = 20)
    private String level;
    @Column(length = 20)
    private String rank;
    private String filePath;
    private Date projectDate;
    @Column(nullable = false)
    private String state;
    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE},fetch = FetchType.EAGER)
    @JoinTable(name = "Project_student",
            joinColumns =@JoinColumn(name = "project_ID",referencedColumnName = "projectId"),
            inverseJoinColumns = @JoinColumn(name="student_ID",referencedColumnName = "studentId"))
    private List<Student> studentList;

    public Project() {
    }

    public Project(String projectName, String level, String rank, String filePath, Date projectDate, String state, List<Student> studentList) {
        this.projectName = projectName;
        this.level = level;
        this.rank = rank;
        this.filePath = filePath;
        this.projectDate = projectDate;
        this.state = state;
        this.studentList = studentList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Date getProjectDate() {
        return projectDate;
    }

    public void setProjectDate(Date projectDate) {
        this.projectDate = projectDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<Student> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
    }
}
