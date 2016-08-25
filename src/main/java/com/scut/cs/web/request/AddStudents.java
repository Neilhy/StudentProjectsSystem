package com.scut.cs.web.request;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.scut.cs.domain.Student;

import java.util.List;

/**
 * Created by NeilHY on 2016/8/24.
 */
public class AddStudents {
    private Long projectId;
//    @JsonManagedReference
    private List<Student> studentList;

    public AddStudents() {
    }

    public AddStudents(Long projectId, List<Student> studentList) {
        this.projectId = projectId;
        this.studentList = studentList;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public List<Student> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
    }
}
