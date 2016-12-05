package com.scut.cs.service;

import com.scut.cs.domain.Project;
import com.scut.cs.web.request.AddStudents;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

/**
 * Created by NeilHY on 2016/8/21.
 */
public interface ProjectsService {
    List<Project> getAllProjects(String college);

    Page<Project> getProjects(String keyword,String item,int page,int size);

    Project getProjectById(long id);

    Page<Project> getCollegeProjects(String keyword,String item,String college,int page,int size);

    Project addProject(Project project);

    Project addStudents(AddStudents addStudents);

    Project modifyProject(Project project);

    int changeProjectState(Long id,String state,String msgForbid);

    Long deleteProject(Long id);

    List<Long> deleteProjectList(List<Long> idList);

    int getTotRecords(String keyword,String item);

    List<Long> changeStatus(List<Long> id,String status,String msgForbid);
}
