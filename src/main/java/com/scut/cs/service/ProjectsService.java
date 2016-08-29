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

    Page<Project> getAllProjects(int page,int size);

    Page<Project> getCollegeProjects(String college,int page,int size);

    Project addProject(Project project);

    Project addStudents(AddStudents addStudents);

    Project modifyProject(Project project);

    int changeProjectState(Long id,String state);

    Long deleteProject(Long id);

    List<Long> deleteProjectList(List<Long> idList);
}
