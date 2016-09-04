package com.scut.cs.service;

import com.scut.cs.web.request.AddDicts;

import java.util.List;

/**
 * Created by Jack on 2016/8/30.
 */
public interface DictService {
    List findKeywords();
    void addDicts(AddDicts addDicts);
    List getItems(String keyword);
    void deleteKeyword(String keyword);
}
