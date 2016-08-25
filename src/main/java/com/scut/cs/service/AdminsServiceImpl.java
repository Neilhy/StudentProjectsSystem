package com.scut.cs.service;

import com.scut.cs.domain.Admin;
import com.scut.cs.domain.dao.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();//TODO 分页和排序
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public Admin addAdmin(Admin admin) {
        Admin adminOld = adminRepository.findByUsername(admin.getUsername());

        if ( null == adminOld) {
            return adminRepository.save(admin);
        }
        return null;
    }

    @PreAuthorize("#admin.id == authentication.principal.id or hasRole('ROLE_ADMIN')")
    @Override
    public Admin modifyAdmin(Admin admin) {

        if ( adminRepository.exists(admin.getId()) ) {
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
}
