package com.scut.cs.web.request;

import java.util.List;

/**
 * Created by Jack on 2016/9/6.
 */
public class ChangeStatus {
    List<String> name;
    String status;

    public ChangeStatus() {
    }

    public ChangeStatus(List<String> name, String status) {
        this.name = name;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getName() {
        return name;
    }

    public void setName(List<String> name) {
        this.name = name;
    }
}
