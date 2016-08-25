package com.scut.cs.service;

import com.scut.cs.domain.Project;
import com.scut.cs.web.request.AddStudents;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

/**
 * Created by NeilHY on 2016/8/21.
 */
public interface ProjectsService {

    List<Project> getAllProjects();

    List<Project> getCollegeProjects(String college);

    Project addProject(Project project);

    Project addStudents(AddStudents addStudents);

    Project modifyProject(Project project);

    int changeProjectState(Long id,String state);

    Long deleteProject(Long id);

    List<Long> deleteProjectList(List<Long> idList);
}
