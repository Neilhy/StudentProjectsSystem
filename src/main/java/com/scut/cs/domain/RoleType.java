package com.scut.cs.domain;

import javax.persistence.*;
import java.util.List;
import java.util.Set;


/**
 * Created by NeilHY on 2016/8/7.
 */
@Entity
public class RoleType {

    @Id
    @Column(name = "roleId")
    private Short id;

    @Column(nullable = false,length = 20)
    private String name;

    @OneToMany(cascade = {CascadeType.ALL},fetch = FetchType.EAGER)
    //@JoinColumn(name = "roleID", referencedColumnName = "roleId")
    private List<Admin> adminList;

    public RoleType() {
    }

    public RoleType(Short id, String name, List<Admin> adminList) {
        this.id = id;
        this.name = name;
        this.adminList = adminList;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Admin> getAdminList() {
        return adminList;
    }

    public void setAdminList(List<Admin> adminList) {
        this.adminList = adminList;
    }
}
