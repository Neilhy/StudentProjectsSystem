package com.scut.cs.service;

import com.scut.cs.domain.Dict;
import com.scut.cs.web.request.AddDicts;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

/**
 * Created by Jack on 2016/8/30.
 */
public interface DictService {
    List<String> findKeywords();
    List<String> upDateKeywords();
    List<Dict> updateByKeyword(String keyword);
    List<Dict> addDicts(List<Dict> dictList,String flag,String keyword);
    List<Dict> getItems(String keyword);
    void deleteKeyword(String keyword);
}
