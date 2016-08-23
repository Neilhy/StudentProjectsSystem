package com.scut.cs.service;

import com.scut.cs.domain.Admin;

import java.util.List;

/**
 * Created by NeilHY on 2016/8/21.
 */
public interface AdminsService {

    List<Admin> getAllAdmins();

    Admin addAdmin(Admin admin);

    Admin modifyAdmin(Admin admin);

    int changeRoleType(String username, String roleType);

    Admin deleteAdmin(String username);

    List<String> delelteAdminList(List<String> usernameList);
}
