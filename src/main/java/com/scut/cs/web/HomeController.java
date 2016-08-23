package com.scut.cs.web;

import com.scut.cs.domain.Admin;
import com.scut.cs.domain.basicEnum.RoleTypes;
import com.scut.cs.domain.dao.ProjectRepository;
import com.scut.cs.domain.Project;
import com.scut.cs.service.AdminsService;
import com.scut.cs.service.ProjectsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by NeilHY on 2016/8/17.
 */
@RestController
public class HomeController {
    @Autowired
    AdminsService adminsService;
    @Autowired
    ProjectsService projectsService;

    @RequestMapping(value = RequestUrls.GetProjectsUrl,method = RequestMethod.GET)
    public List<Project> getProjects() {
        System.out.println("开始getProjects。。。");
        SecurityContext context= SecurityContextHolder.getContext();
        Authentication authentication=context.getAuthentication();
        if (authentication.getPrincipal() instanceof UserDetails) {
            Admin admin= (Admin) authentication.getPrincipal();
            String roleType=admin.getRoleType();
            List<Project> projectList = null;
            if (roleType.equals(RoleTypes.ADMIN) ||roleType.equals(RoleTypes.INNER)
                    || roleType.equals(RoleTypes.INNER_SPEC)) {
                projectList=projectsService.getAllProjects();
            } else if (roleType.equals(RoleTypes.OUTER) || roleType.equals(RoleTypes.OUTER_SPEC)) {
                projectList=projectsService.getCollegeProjects(admin.getCollege());
            }
            return projectList;
        }
        return null;
    }

    @RequestMapping(value = RequestUrls.AddProjectUrl, method = RequestMethod.POST)
    public Project addProject(Project project) {
        System.out.println("开始addProject。。。");
        return projectsService.addProject(project);
    }

    @RequestMapping(value = RequestUrls.DeleteProjectListUrl, method = RequestMethod.POST)
    public List<Long> deleteProjects(List<Long> idList) {
        System.out.println("开始deleteProjects...");
        try {
            return projectsService.deleteProjectList(idList);
        } catch (IllegalArgumentException e) {
            System.out.println("捕获到参数异常，并且返回空list");
            return null;
        }
    }
}
