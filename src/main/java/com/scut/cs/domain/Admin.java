package com.scut.cs.domain;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by NeilHY on 2016/8/9.
 */
@Entity
public class Admin {
    @Id
    @Column(name = "adminId")
    private Long id;

    @Column(nullable = false,length = 20)
    private String userName;

    @Column(nullable = false,length = 20)
    private String password;

    @Column(nullable = false)
    private String college;

    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.PERSIST},fetch = FetchType.EAGER)
    @JoinColumn(name = "roleID", referencedColumnName = "roleId")
    private RoleType roleType;

    public Admin() {
    }

    public Admin(Long id,String userName, String password, String college, RoleType role) {
        this.id=id;
        this.userName = userName;
        this.password = password;
        this.college = college;
        this.roleType = role;
        Date date = new Date();
        date=Calendar.getInstance().getTime();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }
}
