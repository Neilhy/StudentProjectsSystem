package com.scut.cs.domain.dao;

import com.scut.cs.domain.Dict;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Jack on 2016/8/29.
 */
public interface DictRepository extends JpaRepository<Dict,Long> {

    Dict findByKeywordAndItemName(String keyword, String itemName);

    @Transactional
    @Query("select distinct keyword from Dict")
    List<String> findKeywords();

    @Transactional
    @Query("select itemName from Dict where keyword = ?1 order by code asc")
    List<String> findItemsByKeyword(String keyword);

    List<Dict> findByKeyword(String keyword,Sort sort);

    @Modifying
    @Transactional
    @Query("update Dict set itemName = ?1 where keyword =?2")
    void update(String itemName, String keyword, String code);

    @Transactional
    List<Dict> removeByKeyword(String keyword);
}
