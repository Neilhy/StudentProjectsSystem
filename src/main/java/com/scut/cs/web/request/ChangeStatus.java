package com.scut.cs.web.request;

import java.util.List;

/**
 * Created by Jack on 2016/9/6.
 */
public class ChangeStatus {
    List<Long> id;
    String status;

    public ChangeStatus() {
    }

    public ChangeStatus(List<Long> id, String status) {
        this.id = id;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Long> getId() {
        return id;
    }

    public void setId(List<Long> id) {
        this.id = id;
    }
}
