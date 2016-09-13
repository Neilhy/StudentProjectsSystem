package com.scut.cs.service;

import com.scut.cs.domain.Admin;
import com.scut.cs.domain.dao.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
            adminList = adminRepository.findAll();//TODO 分页和排序
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

        if ( null == adminOld) {
            admin.setPassword(passwordEncoder.encode(admin.getPassword()));
            return adminRepository.save(admin);
        }
        return null;
    }

    @PreAuthorize("#admin.id == authentication.principal.id or hasRole('ROLE_ADMIN')")
    @Override
    public Admin modifyAdmin(Admin admin) {
        Admin adminOld = adminRepository.getOne(admin.getId());
        if(null != adminOld){
            if ( adminOld.getPassword().equals(admin.getPassword())) {
                return adminRepository.save(admin);
            }else{
                admin.setPassword(passwordEncoder.encode(admin.getPassword()));
                return adminRepository.save(admin);
            }
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
    public Admin deleteAdmin(String username) {
        Admin adminOld = adminRepository.findByUsername(username);
        if (null != adminOld) {
            adminRepository.delete(adminOld);
            return adminOld;
        }
        return null;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional(rollbackFor = {IllegalArgumentException.class})
    @Override
    public List<String> delelteAdminList(List<String> usernameList) {
        List<String> usernames = new ArrayList<>();
        for (String username : usernameList) {
            if (null == deleteAdmin(username)) {
                throw new IllegalArgumentException("用户名为："+username+" 的用户不存在！！");     //TODO 可以在Controller层捕抓异常
            }
            usernames.add(username);
        }
        return usernames;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    @Override
    public void changeStatus(List<String> name, String status) {
        int priStatus = 0;
        if(status.equals("open")) {
            priStatus = 1;
        }
        for(String username:name) {
            adminRepository.changeStatus(username,priStatus);
        }
    }
}
