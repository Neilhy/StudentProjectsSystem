package com.scut.cs.domain.dao;

import com.scut.cs.domain.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

/**
 * Created by NeilHY on 2016/8/9.
 */
public interface ProjectRepository extends JpaRepository<Project,Long> {
    @Query("select p from Project p where p.projectName= :name and p.rank= :rank and p.projectDate= :date")
    Project withNameAndRankAndDateQuery(@Param("name") String name, @Param("rank") String rank, @Param("date") Date date);

    Page<Project> findByCaptainCollege(String captainCollege, Pageable pageable);

    @Modifying
    @Transactional
    @Query("update Project p set p.state= ?2 where p.id= ?1")
    int setProjectState(Long id,String state);
}
