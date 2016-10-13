package com.scut.cs.web;

import com.scut.cs.domain.Admin;
import com.scut.cs.domain.basicEnum.RoleTypes;
import com.scut.cs.domain.Project;
import com.scut.cs.service.AdminsService;
import com.scut.cs.service.ProjectsService;
import com.scut.cs.web.request.AddStudents;
import com.scut.cs.web.request.ChangeStatus;
import com.scut.cs.web.request.RequestUrls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static javafx.scene.input.KeyCode.R;

/**
 * Created by NeilHY on 2016/8/17.
 */
@RestController
public class ProjectController {

    @Autowired
    ProjectsService projectsService;

    @RequestMapping(value = RequestUrls.GetProjectsUrl,method = RequestMethod.GET)
    public Page<Project> getProjects(@PathVariable String keyword,@PathVariable String item,
                                     @PathVariable int page, @PathVariable int size) {
        System.out.println("开始getProjects。。。关键字为"+keyword);
        SecurityContext context= SecurityContextHolder.getContext();
        Authentication authentication=context.getAuthentication();
        if (authentication.getPrincipal() instanceof UserDetails) {
            Admin admin= (Admin) authentication.getPrincipal();
            String roleType=admin.getRoleType();
            Page<Project> projectPage = null;
            if (roleType.equals(RoleTypes.ADMIN) ||roleType.equals(RoleTypes.INNER)
                    || roleType.equals(RoleTypes.INNER_SPEC)) {
                projectPage=projectsService.getProjects(keyword,item,page,size);
            } else if (roleType.equals(RoleTypes.OUTER) || roleType.equals(RoleTypes.OUTER_SPEC)) {
                projectPage=projectsService.getCollegeProjects(keyword,item,admin.getCollege(),page,size);
            }
            System.out.println("要返回数据了");
            return projectPage;
        }
        return null;
    }



    @RequestMapping(value = RequestUrls.AddProjectUrl, method = RequestMethod.POST,consumes = "application/json",produces = "application/json")
    public Project addProject(@RequestBody Project project) {
        System.out.println("开始addProject。。。"+project.toString());
        return projectsService.addProject(project);
    }

    @RequestMapping(value = RequestUrls.AddStudentsUrl, method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public Project addStudents(@RequestBody AddStudents addStudents) {
        System.out.println("开始addStudents。。。");
        Project project=projectsService.addStudents(addStudents);
        return project;
    }

    @RequestMapping(value = RequestUrls.DeleteProjectListUrl, method = RequestMethod.POST,consumes = "application/json",produces = "application/json")
    public List<Long> deleteProjects(@RequestBody List<Long> idList) {
        System.out.println("开始deleteProjects..."+"id为："+idList);
        try {
            return projectsService.deleteProjectList(idList);
        } catch (IllegalArgumentException e) {
            System.out.println("捕获到参数异常，并且返回空list  "+e.getMessage());
            return null;
        }
    }

    @RequestMapping(value = RequestUrls.GetTotPages, method = RequestMethod.GET)
    public Integer getTotPages(@PathVariable String size,
                               @PathVariable String keyword,
                               @PathVariable String item) {
        int sz = Integer.parseInt(size);
        int tot = projectsService.getTotRecords(keyword,item);
        int pages = tot/sz;
        if(tot % sz != 0) {
            pages++;
        }
        return pages;
    }

    @RequestMapping(value = RequestUrls.ChangeProjectsStatus, method = RequestMethod.POST,consumes = "application/json",produces = "application/json")
    public List<Long> changeProjectStatus(@RequestBody ChangeStatus changeStatus) {
        List<Long> id = changeStatus.getId();
        String status = changeStatus.getStatus();
        return projectsService.changeStatus(id,status);
    }
}
