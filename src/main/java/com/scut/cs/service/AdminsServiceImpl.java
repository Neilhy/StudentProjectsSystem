package com.scut.cs.service;

import com.scut.cs.domain.Admin;
import com.scut.cs.domain.dao.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NeilHY on 2016/8/21.
 */
@Service("adminsService")
public class AdminsServiceImpl implements AdminsService {
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public List<Admin> getAdmins(String role) {
        List<Admin> adminList = null;
        if(role.equals("all")) {
            Sort sort = new Sort(Sort.Direction.ASC,"id");
            adminList = adminRepository.findAll(sort);//TODO 分页和排序
        } else {
            adminList = adminRepository.findByRoleType(role);
        }
        List<Admin> adminsNew=new ArrayList<>();
        for (Admin admin : adminList) {
            admin.setPassword(null);
            adminsNew.add(admin);
        }
        return adminsNew;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public Admin addAdmin(Admin admin) {
        Admin adminOld = adminRepository.findByUsername(admin.getUsername());
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        if ( null == adminOld) {
            return adminRepository.save(admin);
        }
        return null;
    }

    @PreAuthorize("#admin.id == authentication.principal.id")
    @Override
    public Admin modifyAdmin(Admin admin) {
        Admin a = adminRepository.findById(admin.getId());
        String password = admin.getPassword();
        if(a!=null) {
            if(password.equals("")){
                admin.setPassword(a.getPassword());
            } else {
                admin.setPassword(passwordEncoder.encode(password));
            }
            return adminRepository.save(admin);
        }
        return null;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public int changeRoleType(String username, String roleType) {
        Admin adminOld = adminRepository.findByUsername(username);
        if (null != adminOld) {
            return adminRepository.changeRoleType(username, roleType);
        }
        return -1;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public Admin deleteAdmin(Long id) {
        Admin adminOld = adminRepository.findById(id);
        if (null != adminOld) {
            adminRepository.delete(adminOld);
            return adminOld;
        }
        return null;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional(rollbackFor = {IllegalArgumentException.class})
    @Override
    public List<Long> delelteAdminList(List<Long> idList) {
        List<Long> ids = new ArrayList<>();
        for (Long id : idList) {
            if (null == deleteAdmin(id)) {
                throw new IllegalArgumentException("id为："+id+" 的用户不存在！！");     //TODO 可以在Controller层捕抓异常
            }
            ids.add(id);
        }
        return ids;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional(rollbackFor = {IllegalArgumentException.class})
    @Override
    public void changeStatus(List<Long> id, String status) {
        int priStatus = 0;
        if(status.equals("open")) {
            priStatus = 1;
        }
        for(long userid:id) {
            adminRepository.changeStatus(userid,priStatus);
        }
    }

    @Override
    public boolean checkPwd(String pwd) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Admin a = (Admin)userDetails;
        String password = a.getPassword();
        if(passwordEncoder.matches(pwd, password)) {
            return true;
        }
        return false;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public void resetPassword(List<Long> idList) {
        for(long id:idList) {
            Admin old = adminRepository.findById(id);
            if(null == old) {
                throw new IllegalArgumentException("id为："+id+" 的用户不存在！！");
            } else {
                old.setPassword(passwordEncoder.encode("000000"));
                adminRepository.save(old);
            }
        }
    }


}
