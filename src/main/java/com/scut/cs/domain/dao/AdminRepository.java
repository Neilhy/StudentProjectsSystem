package com.scut.cs.domain.dao;

import com.scut.cs.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by NeilHY on 2016/8/9.
 */
public interface AdminRepository extends JpaRepository<Admin,Long> {
    Admin findByUsername(String username);

    List<Admin> findByCollege(String college);

    List<Admin> findByRoleType(String roleType);

    List<Admin> findByStatus(Integer status);

    @Modifying
    @Transactional
    @Query("update Admin a set a.roleType= ?2 where a.username= ?1")
    int changeRoleType(String username, String roleType);

    @Modifying
    @Transactional
    @Query("update Admin a set a.status = ?2 where a.username = ?1")
    int changeStatus(String username,int status);
}
