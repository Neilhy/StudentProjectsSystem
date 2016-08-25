package com.scut.cs.service;

import com.scut.cs.domain.Project;
import com.scut.cs.domain.Student;
import com.scut.cs.domain.dao.ProjectRepository;
import com.scut.cs.domain.dao.StudentRepository;
import com.scut.cs.web.request.AddStudents;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by NeilHY on 2016/8/21.
 */
@Service("projectsService")
public class ProjectsServiceImpl implements ProjectsService {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private StudentRepository studentRepository;

    @PreAuthorize("hasRole('ROLE_INNER') or hasRole('ROLE_ADMIN')")
    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();//TODO 分页和排序

//        return null;
    }

    @PreAuthorize("hasRole('ROLE_OUTER') or hasRole('ROLE_OUTER_SPEC')")
    @Override
    public List<Project> getCollegeProjects(String college) {
        if (college != null && !college.equals("")) {
            return projectRepository.findByCaptainCollege(college);
        }
        return null;
    }

    @PreAuthorize("hasRole('ROLE_OUTER')")
    @Override
    public Project addProject(Project project) {
        if (project != null) {
            Project projectOld = projectRepository.withNameAndRankAndDateQuery(project.getProjectName(), project.getRank(), project.getProjectDate());
            if ( null == projectOld) {
                return projectRepository.save(project);
            }
        }
        return null;
    }

    @PreAuthorize("hasRole('ROLE_OUTER')")
    @Override
    public Project addStudents(AddStudents addStudents) {
        if (null != addStudents && null != addStudents.getProjectId() && 0 != addStudents.getProjectId()) {
            Long projectId=addStudents.getProjectId();
            Project project=projectRepository.findOne(projectId);
            if (null!= project) {
                if (project.getId()%2==1) {
                    project.getStudentList().addAll(addStudents.getStudentList());
                    return projectRepository.save(project);
                } else {
                    List<Student> studentList = new ArrayList<>();
                    for (Student student : addStudents.getStudentList()) {
                        List<Project> projectList = new ArrayList<>();
                        projectList.add(project);
                        student.setProjectList(projectList);
                        studentList.add(student);
                    }
                    studentRepository.save(studentList);
                    return project;
                }

            }
        }
        return null;
    }

    @PreAuthorize("hasRole('ROLE_OUTER') or hasRole('ROLE_ADMIN')")
    @Override
    public Project modifyProject(Project project) {
        if (null != project && 0L != project.getId()) {
            if (projectRepository.exists(project.getId())) {
                return projectRepository.save(project);
            }
        }
        return null;
    }

    @PreAuthorize("hasRole('ROLE_INNER') or hasRole('ROLE_ADMIN')")
    @Override
    public int changeProjectState(Long id, String state) {
        if (null!=id && id > 0 && state != null && !state.equals("")) {
            if (projectRepository.exists(id)) {
                return projectRepository.setProjectState(id,state);
            }
        }
        return -1;
    }

    @PreAuthorize("hasRole('ROLE_OUTER') or hasRole('ROLE_ADMIN')")
    @Override
    public Long deleteProject(Long id) {
        if (id != null && id>0 && projectRepository.exists(id)) {
            projectRepository.delete(id);
            return id;
        }
        return -1L;
    }

    @PreAuthorize("hasRole('ROLE_OUTER') or hasRole('ROLE_ADMIN')")
    @Transactional(rollbackFor = {IllegalArgumentException.class})
    @Override
    public List<Long> deleteProjectList(List<Long> idList) {
        if (null != idList && 0!=idList.size()) {
            List<Long> projectIds = new ArrayList<>();
            for (Long id : idList) {
                if (!Objects.equals(id, deleteProject(id))) {
                    throw new IllegalArgumentException("项目id为："+id+" 的项目不存在！！");   //TODO 可以在controller捕抓异常
                }
                projectIds.add(id);
            }
            return projectIds;
        }
        return null;
    }
}
