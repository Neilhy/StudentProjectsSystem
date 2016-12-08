package com.scut.cs.service;

import com.scut.cs.domain.Dict;
import com.scut.cs.domain.dao.DictRepository;
import com.scut.cs.web.request.AddDicts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jack on 2016/8/30.
 */
@Service("dictService")
public class DictServiceImpl implements DictService {
    @Autowired
    private DictRepository dictRepository;

    @Cacheable(value = "keywordList")
    @Override
    public List<String> findKeywords() {
        return dictRepository.findKeywords();
    }

    @CachePut(value = "keywordList")
    @Override
    public List<String> upDateKeywords() {
        return dictRepository.findKeywords();
    }

    @CachePut(value = "dictList",key = "#keyword")
    @Override
    public List<Dict> updateByKeyword(String keyword) {
        return dictRepository.findByKeyword(keyword,new Sort(Sort.Direction.ASC,"code"));
    }

  //  @Transactional
   // @CachePut(value = "dictList",key = "#keyword")
//    @Transactional
    @CacheEvict(value="dictList",key="#keyword",beforeInvocation=true)
    @Override
    public List<Dict> addDicts(List<Dict>dictList,String flag,String keyword) {
        //如果是更新操作，则先删除原先的记录
//        System.out.println(flag + " " + keyword);
        if(flag.equals("update")) {
            deleteKeyword(keyword);
        }
        List<Dict> dicts=dictRepository.save(dictList);

        return dicts;
    }

    /**
     * 获取数据类型对应的数据项
     * @return List
     */
    @Cacheable(value = "dictList",key = "#keyword")
    @Override
    public List<Dict> getItems(String keyword) {
        return dictRepository.findByKeyword(keyword,new Sort(Sort.Direction.ASC,"code"));
    }
    /**
     * 删除当前数据类型及其对应的数据项
     * @param keyword
     */
    @Transactional
    @CacheEvict(value = "dictList")
    @Override
    public void deleteKeyword(String keyword) {
        dictRepository.removeByKeyword(keyword);
    }
}
