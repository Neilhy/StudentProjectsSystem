package com.scut.cs.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Date;

/**
 * Created by Jack on 2016/8/29.
 */
@Entity
public class Dict implements Serializable {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false, length = 20)
    private String keyword;
    @Column(nullable = false, length = 100, unique = true)
    private String itemName;

    public Dict() {
    }

    public Dict(String keyword, String itemName, String code, Date date) {
        this.keyword = keyword;
        this.itemName = itemName;
        this.code = code;
        this.date = date;
    }

    @Column(nullable = false, length = 20)

    private String code;
    private Date date;


    public String getCode() {
        return code;
    }

    public Date getDate() {
        return date;
    }

    public Integer getId() {
        return id;
    }

    public String getItemName() {
        return itemName;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public String toString() {
        return "Dict{" +
                "id=" + id +
                ", keyword='" + keyword + '\'' +
                ", itemName='" + itemName + '\'' +
                ", code='" + code + '\'' +
                ", date=" + date +
                '}';
    }
}
