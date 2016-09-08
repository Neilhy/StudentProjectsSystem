package com.scut.cs.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

/**
 * Created by NeilHY on 2016/8/9.
 */
@Entity
public class Admin implements UserDetails{
    private static final long serialVersionUID=1L;
    @Id
    @GeneratedValue
    @Column(name = "adminId")
    private Long id;

    @Column(nullable = false,length = 20,unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String college;

//    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.PERSIST},fetch = FetchType.EAGER)
//    @JoinColumn(name = "roleID", referencedColumnName = "roleId")
//    private RoleType roleType;
    @Column(nullable = false)
    private String roleType;

    @Column(nullable = false,columnDefinition = "TINYINT UNSIGNED default 1")
    private Integer status=1;

    public Admin() {
    }

    public Admin(Long id, String username, String password, String college, String roleType) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.college = college;
        this.roleType = roleType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> auths = new ArrayList<>();
        String role=this.getRoleType();
        auths.add(new SimpleGrantedAuthority(role));
        return auths;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
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

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
