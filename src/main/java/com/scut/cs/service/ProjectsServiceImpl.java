package com.scut.cs.service;

import com.scut.cs.domain.Project;
import com.scut.cs.domain.dao.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @PreAuthorize("hasRole('ROLE_INNER') or hasRole('ROLE_ADMIN')")
    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();//TODO 分页和排序
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
            Project projectOle = projectRepository.withNameAndRankAndDateQuery(project.getProjectName(), project.getRank(), project.getProjectDate());
            if ( null == projectOle) {
                return projectRepository.save(project);
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
        if (id > 0 && state != null && !state.equals("")) {
            if (projectRepository.exists(id)) {
                return projectRepository.setProjectState(id,state);
            }
        }
        return -1;
    }

    @PreAuthorize("hasRole('ROLE_OUTER') or hasRole('ROLE_ADMIN')")
    @Override
    public Long deleteProject(Long id) {
        if (id>0 && projectRepository.exists(id)) {
            projectRepository.delete(id);
            return id;
        }
        return -1L;
    }

    @PreAuthorize("hasRole('ROLE_OUTER') or hasRole('ROLE_ADMIN')")
    @Transactional(rollbackFor = {IllegalArgumentException.class})
    @Override
    public List<Long> deleteProjectList(List<Long> idList) {
        if (null != idList && !idList.isEmpty()) {
            List<Long> projectIds = new ArrayList<>();
            for (Long id : idList) {
                if (!Objects.equals(id, deleteProject(id))) {
                    throw new IllegalArgumentException("项目id为："+id+" 的项目不存在！！");   //TODO 验证能不能继续正常执行程序
                }
                projectIds.add(id);
            }
            return projectIds;
        }
        return null;
    }
}
