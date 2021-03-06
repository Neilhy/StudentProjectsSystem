package com.scut.cs.service;

import com.scut.cs.domain.Admin;
import com.scut.cs.domain.Project;
import com.scut.cs.domain.Student;
import com.scut.cs.domain.basicEnum.RoleTypes;
import com.scut.cs.domain.dao.ProjectRepository;
import com.scut.cs.domain.dao.StudentRepository;
import com.scut.cs.web.request.AddStudents;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.security.RolesAllowed;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by NeilHY on 2016/8/21.
 */
@Service("projectsService")
public class ProjectsServiceImpl implements ProjectsService {

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public List<Project> getAllProjects(String college) {
        if("无".equals(college)) {
            return projectRepository.findAll();
        }
        return projectRepository.findByCaptainCollege(college);
    }

    @PreAuthorize("hasRole('ROLE_INNER') or hasRole('ROLE_ADMIN')")
    @Override
    public Page<Project> getProjects(String keyword,String item,int page,int size) {
        Page<Project> projects = null;
        PageRequest pageRequest = null;
        if(size > 0) {
            pageRequest = new PageRequest(page,size,new Sort(Sort.Direction.ASC,"projectName"));
        }
        if("未选择".equals(keyword) || "未选择".equals(item)) {
            projects = projectRepository.findAll(pageRequest);
        } else if("学院".equals(keyword)) {
            projects =  projectRepository.findByCaptainCollege(item,pageRequest);
        } else if("年份".equals(keyword)) {
            try {
                Integer year = Integer.parseInt(item);
                projects = projectRepository.findByProjectDate(year,pageRequest);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if("竞赛等级".equals(keyword)) {
            projects = projectRepository.findByLevel(item,pageRequest);
        } else if("所获奖项".equals(keyword)) {
            projects = projectRepository.findByRank(item,pageRequest);
        } else if("获奖证明".equals(keyword)) {
            projects = projectRepository.findByPhotoStatus(item,pageRequest);
        } else if("审核状态".equals(keyword)) {
            projects = projectRepository.findByState(item,pageRequest);
        }
//        List<Project> list = projects.getContent();
//        System.out.println("共有"+list.size()+"条记录");
//        for(Project p : list) {
//            System.out.println(p.toString());
//        }
        return projects;
    }

    @Override
    public Project getProjectById(long id) {
        return projectRepository.findById(id);
    }

    @PreAuthorize("hasRole('ROLE_OUTER') or hasRole('ROLE_OUTER_SPEC')")
    @Override
    public Page<Project> getCollegeProjects(String keyword,String item,String college,int page,int size) {
        Page<Project> projects = null;
        PageRequest pageRequest = null;
        if(size > 0) {
            pageRequest = new PageRequest(page,size,new Sort(Sort.Direction.ASC,"projectName"));
        }
        if("未选择".equals(keyword) || "未选择".equals(item)) {
            projects = projectRepository.findByCaptainCollege(college,pageRequest);
        } else if("年份".equals(keyword)) {
            try {
                Integer year = Integer.parseInt(item);
                projects = projectRepository.findByProjectDateAndCaptainCollege(year,college,pageRequest);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if("竞赛等级".equals(keyword)) {
            projects = projectRepository.findByLevelAndCaptainCollege(item,college,pageRequest);
        } else if("所获奖项".equals(keyword)) {
            projects = projectRepository.findByRankAndCaptainCollege(item,college,pageRequest);
        } else if("获奖证明".equals(keyword)) {
            projects = projectRepository.findByPhotoStatusAndCaptainCollege(item,college,pageRequest);
        } else if("审核状态".equals(keyword)) {
            projects = projectRepository.findByStateAndCaptainCollege(item,college,pageRequest);
        }
        return projects;
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
                project.getStudentList().addAll(addStudents.getStudentList());
                return projectRepository.save(project);
            }
        }
        return null;
    }

//    @PreAuthorize("hasRole('ROLE_OUTER') or hasRole('ROLE_ADMIN')")
    @Override
    public Project modifyProject(Project project) {
        if (null != project && 0L != project.getId()) {
            Project old = projectRepository.findById(project.getId());
            if(null != old) {
                if(project.getPhotoStatus()=="") {
                    project.setPhotoStatus(old.getPhotoStatus());
                    project.setFilePath(old.getFilePath());
                }
                projectRepository.delete(project.getId());//自带的函数可以级联删除学生
                projectRepository.save(project);
                return project;
            }
        }
        return null;
    }

    @PreAuthorize("hasRole('ROLE_INNER') or hasRole('ROLE_ADMIN')")
    @Override
    public int changeProjectState(Long id, String state,String msgForbid) {
        if (null!=id && id > 0 && state != null && !"".equals(state)) {
            if (projectRepository.exists(id)) {
                return projectRepository.setProjectState(id,state,msgForbid);
            }
        }
        return -1;
    }

//    @PreAuthorize("hasRole('ROLE_OUTER') or hasRole('ROLE_ADMIN')")
    @Override
    public Long deleteProject(Long id) {
        if (id != null && id>0 && projectRepository.exists(id)) {
            projectRepository.delete(id);
            return id;
        }
        return -1L;
    }

//    @PreAuthorize("hasRole('ROLE_OUTER') or hasRole('ROLE_ADMIN')")
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

    @Override
    public int getTotRecords(String keyword,String item) {
        Page<Project> page = null;
        Admin a = (Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String roleType = a.getRoleType();
        if (RoleTypes.ADMIN.equals(roleType) ||RoleTypes.INNER.equals(roleType)
                || RoleTypes.INNER_SPEC.equals(roleType)) {
            page = getProjects(keyword,item,0,0);
        } else if (RoleTypes.OUTER.equals(roleType) || RoleTypes.OUTER_SPEC.equals(roleType)) {
            page = getCollegeProjects(keyword,item,a.getCollege(),0,0);
        }
        if(page == null) {
            return 0;
        }
        int size = page.getContent().size();

        return size;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_INNER')")
    @Override
    public List<Long> changeStatus(List<Long> id, String status,String msgForbid) {
        for(long pid:id) {
            projectRepository.setProjectState(pid,status,msgForbid);
        }
        return id;
    }
}
