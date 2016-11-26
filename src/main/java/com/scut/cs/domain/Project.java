package com.scut.cs.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import javax.persistence.Entity;
import java.io.Serializable;
import java.sql.Date;
import java.util.List;

/**
 * Created by NeilHY on 2016/8/7.
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
public class Project implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "projectId")
    private Long id;

    @Column(nullable = false)
    private String projectName;
    @Column(length = 20)
    private String level;               //竞赛级别
    @Column(length = 20)
    private String rank;                //获奖等级
    @Column(nullable = false)
    private String captainCollege;

    private String teacher;
    private String note;            //备注
    private Date projectDate;       //时间

    private String filePath;        //上传图片路径

    private String msgForbid; //管理员对该项目不予以通过的理由

    @Column(nullable = false,length= 10)
    private String state="未审核";

    @Column(length= 10)
    private String photoStatus = "未上传";

    @ManyToMany(fetch = FetchType.EAGER)
    @Cascade(value = {CascadeType.SAVE_UPDATE,CascadeType.PERSIST,CascadeType.DELETE,CascadeType.REMOVE,CascadeType.MERGE})
    @JoinTable(name="ProjectStudentList",
            joinColumns ={@JoinColumn(name = "projectId")},
            inverseJoinColumns = {@JoinColumn(name="studentId")})
    private List<Student> studentList;

    public Project() {
    }

    public Project(String projectName, String level, String rank,String captainCollege, String teacher, String note, String projectDate, String state, List<Student> studentList,String photoStatus) {
        this.projectName = projectName;
        this.level = level;
        this.rank = rank;
        this.captainCollege=captainCollege;
        this.teacher = teacher;
        this.note = note;
        this.projectDate = Date.valueOf(projectDate);
        this.state = state;
        this.studentList = studentList;
        this.photoStatus = photoStatus;

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

    public String getCaptainCollege() {
        return captainCollege;
    }

    public void setCaptainCollege(String captainCollege) {
        this.captainCollege = captainCollege;
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

//    @JsonManagedReference
    public void setStudentList(List<Student> studentList) {

        this.studentList = studentList;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getMsgForbid() {
        return msgForbid;
    }

    public void setMsgForbid(String msgForbid) {
        this.msgForbid = msgForbid;
    }

    public String getPhotoStatus() {
        return photoStatus;
    }

    public void setPhotoStatus(String photoStatus) {
        this.photoStatus = photoStatus;
    }

    @Override
    public String toString() {
        return "Project{" +
                "projectName='" + projectName + '\'' +
                ", level='" + level + '\'' +
                ", rank='" + rank + '\'' +
                ", captainCollege='" + captainCollege + '\'' +
                ", teacher='" + teacher + '\'' +
                ", note='" + note + '\'' +
                ", projectDate=" + projectDate +
                ", studentList=" + studentList +
                '}';
    }
}
