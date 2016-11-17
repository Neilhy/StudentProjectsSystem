package com.scut.cs.service;

import com.scut.cs.domain.Admin;

import java.util.List;

/**
 * Created by NeilHY on 2016/8/21.
 */
public interface AdminsService {

    List<Admin> getAdmins(String role);

    Admin addAdmin(Admin admin);

    Admin modifyAdmin(Admin admin);

    int changeRoleType(String username, String roleType);

    Admin deleteAdmin(Long id);

    List<Long> delelteAdminList(List<Long> idList);

    void changeStatus(List<Long> id,String status);

    boolean checkPwd(String pwd);

    void resetPassword(List<Long> idList);

}
