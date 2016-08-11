package com.scut.cs.web;

import com.scut.cs.dao.AdminRepository;
import com.scut.cs.dao.ProjectRepository;
import com.scut.cs.dao.RoleRepository;
import com.scut.cs.dao.StudentRepository;
import com.scut.cs.domain.Admin;
import com.scut.cs.domain.Project;
import com.scut.cs.domain.RoleType;
import com.scut.cs.domain.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.sql.Date;


/**
 * Created by NeilHY on 2016/8/9.
 */
@RestController
public class TestController {

    @Autowired
    AdminRepository adminRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    StudentRepository studentRepository;

    @RequestMapping("/saveRole")
    public RoleType saveAdmin() {
        RoleType r1 = new RoleType((short) 4,"ROLE_OUTER", new ArrayList<>());
        Admin a1 = new Admin(113L,"outer", "123456", "计算机学院", null);
        r1.getAdminList().add(a1);
        return roleRepository.save(r1);
    }
    @RequestMapping("/saveAdmin")
    public Admin saveRole() {
        RoleType r1 = new RoleType((short) 5,"ROLE_OUTER", new ArrayList<>());
        Admin a1 = new Admin(114L,"outer", "123456", "计算机学院", r1);
        return adminRepository.save(a1);
    }

    @RequestMapping("/deleteRole")
    public RoleType deleteRole(Short id) {
        RoleType r1 = roleRepository.findOne(id);
        roleRepository.delete(id);
        return r1;
    }

    @RequestMapping("/deleteAdmin")
    public Admin deleteAdmin(Long id) {
        Admin a1 = adminRepository.findOne(id);
        adminRepository.delete(id);
        return a1;
    }
//    @RequestMapping("/getRole")
//    public Admin getRole() {
//        RoleType r1 = roleRepository.getOne((short) 2);
//        Iterator<Admin> iterator= r1.getAdminList().iterator();
//        Admin a1=iterator.next();
//        a1.setRoleType(r1);
//        return adminRepository.save(a1);
//    }

    @RequestMapping("/getAdmin")
    public Admin getAdmin(Long id) {
        return adminRepository.findOne(id);
    }

    @RequestMapping("/getRole")
    public RoleType getRole(Short id) {
        return roleRepository.findOne(id);
    }

    @RequestMapping("/saveProject")
    public Project saveProject() {
        String dateStr = "2016-8-11";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = null;
        long time = 0;
        try {
             date= format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date != null) {
            time = date.getTime();
        }
        Student student1 = new Student(201430561181L, "阳阳", "计算机学院", "网络工程", (short) 2014, null);
        Student student2 = new Student(201430561182L, "阳阳", "软件学院", "网络工程", (short) 2014, null);
        Student student3 = new Student(201430561183L, "阳阳", "经贸学院", "网络工程", (short) 2014, null);
        Project project = new Project("腾讯创新大赛", "校级", "一等奖", "filepath", new Date(time), "未审核", new ArrayList<>());
        project.getStudentList().add(student1);
        project.getStudentList().add(student2);
        project.getStudentList().add(student3);

        return projectRepository.save(project);
    }

    @RequestMapping("/saveStudent")
    public Student saveStudent() {
        Student student1 = new Student(201430561180L, "阳阳", "计算机学院", "网络工程", (short) 2014, new ArrayList<>());

        Project project1 = new Project("华为创新大赛", "校级", "一等奖", "filepath", new Date(2016,8,11), "未审核", null);
        Project project2 = new Project("华为创新大赛", "校级", "二等奖", "filepath", new Date(2016,8,11), "未审核", null);
        Project project3 = new Project("华为创新大赛", "校级", "三等奖", "filepath", new Date(2016,8,11), "未审核", null);

        student1.getProjectList().add(project1);
        student1.getProjectList().add(project2);
        student1.getProjectList().add(project3);

        return studentRepository.save(student1);
    }

    @RequestMapping("/getProject")
    public Project getProject(Long id) {
        return projectRepository.findOne(id);
    }

    @RequestMapping("/getStudent")
    public Student getStudent(Long id) {
        return studentRepository.findOne(id);
    }

    @RequestMapping("/deleteProject")
    public Project deleteProject(Long id) {
        Project project = projectRepository.findOne(id);
        projectRepository.delete(id);
        return project;
    }

    @RequestMapping("/deleteStudent")
    public Student deleteStudent(Long id) {
        Student student = studentRepository.findOne(id);
        studentRepository.delete(id);
        return student;
    }
}
