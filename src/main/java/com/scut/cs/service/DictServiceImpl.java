package com.scut.cs.service;

import com.scut.cs.domain.Dict;
import com.scut.cs.domain.dao.DictRepository;
import com.scut.cs.web.request.AddDicts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Jack on 2016/8/30.
 */
@Service("dictService")
public class DictServiceImpl implements DictService {
    @Autowired
    private DictRepository dictRepository;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public List<String> findKeywords() {
        List<String> list = dictRepository.findKeywords();
        return list;
    }

    @Override
    public void addDicts(AddDicts addDicts) {
        if(addDicts != null) {
            List<Dict> dictList = addDicts.getDictList();
            String flag = addDicts.getFlag();
            if(dictList.size() > 0) {
                String keyword = dictList.get(0).getKeyword();
                //如果是更新操作，则先删除原先的记录
                if(flag.equals("update")) {
                    List<Dict> list = dictRepository.findByKeyword(keyword);
                    for(Dict d:list) {
                        dictRepository.delete(d);
                    }
                }
                //添加记录
                for(Dict dict:dictList) {
                    dictRepository.save(dict);
                }
            }
        }
    }

    /**
     * 获取数据类型对应的数据项
     * @return List
     */
    @Override
    public List<String> getItems(String keyword) {
        List<String> items = dictRepository.findItemsByKeyword(keyword);
        return items;
    }

    /**
     * 删除当前数据类型及其对应的数据项
     * @param keyword
     */
    @Override
    public void deleteKeyword(String keyword) {
        List<Dict> items = dictRepository.findByKeyword(keyword);
        for(Dict d:items) {
            dictRepository.delete(d);
        }
    }
}
