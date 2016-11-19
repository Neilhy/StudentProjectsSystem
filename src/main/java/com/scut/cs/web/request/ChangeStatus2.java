package com.scut.cs.web.request;

import java.util.List;

/**
 * Created by Jack on 2016/9/6.
 */
public class ChangeStatus2 {
    List<Long> id;
    String status;
    String msgForbid;

    public ChangeStatus2() {
    }

    public ChangeStatus2(List<Long> id, String status,String msgForbid) {
        this.id = id;
        this.status = status;
        this.msgForbid = msgForbid;
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

    public String getMsgForbid() {
        return msgForbid;
    }

    public void setMsgForbid(String msgForbid) {
        this.msgForbid = msgForbid;
    }
}
