package com.scut.cs.web;

import com.scut.cs.domain.Admin;
import com.scut.cs.domain.Student;
import com.scut.cs.domain.basicEnum.RoleTypes;
import com.scut.cs.domain.Project;
import com.scut.cs.service.AdminsService;
import com.scut.cs.service.ProjectsService;
import com.scut.cs.util.ExcelFileGenerator;
import com.scut.cs.web.request.AddStudents;
import com.scut.cs.web.request.ChangeStatus;
import com.scut.cs.web.request.RequestUrls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Range;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

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

    @RequestMapping(value = RequestUrls.Excel,method = RequestMethod.GET)
    public void excel(HttpServletResponse response) {
        List<Project> projects = projectsService.getAllProjects();
        ArrayList rows = new ArrayList<>();
        int idx = 1;
        for(Project project:projects) {
            List<String> row = new ArrayList<>();
            row.add(""+idx);
            idx++;
            row.add(project.getProjectDate().toString());
            row.add(project.getProjectName());
            row.add(project.getLevel());
            row.add(project.getRank());
            List<Student> students = project.getStudentList();
            if(students.size()==1) {
                row.add("个人");
            } else {
                row.add("团体");
            }
            StringBuilder sb = new StringBuilder("");
            for(int i=0;i<students.size();i++) {
                Student s = students.get(i);
                sb.append(students.get(i).getStudentName());
                if(s.getCaptainOrNot()==1) {
                    sb.append("（队长）");
                } else {
                    sb.append(" ");
                }
            }
            row.add(sb.toString());
            row.add(project.getCaptainCollege());
            row.add(project.getTeacher());
            row.add(project.getPhotoStatus());
            row.add(project.getState());
            rows.add(row);
        }
        ArrayList<String> headers = new ArrayList<>();
        String[] titles = {"序号","竞赛时间","竞赛名称","竞赛等级","所获奖项",
                            "参赛方式","获奖者姓名","所属学院","指导老师",
                            "获奖证明","状态"};
        for(String header:titles) {
            headers.add(header);
        }
        try {
            OutputStream out = response.getOutputStream();
            response.reset();
            String fileName = produceRandomName();
            response.setHeader("Content-Disposition","attachment;filename="+fileName+".xls");
            response.setContentType("application/vnd.ms-excel");
            ExcelFileGenerator generator = new ExcelFileGenerator(headers, rows);
            generator.expordExcel(out);
            System.setOut(new PrintStream(out));
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String produceRandomName() {
        Random r = new Random();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        String str = sdf.format(new Date());
        int rn = (int)(r.nextDouble()*(99999-10000+1)) + 10000;
        return str + rn;
    }
}
