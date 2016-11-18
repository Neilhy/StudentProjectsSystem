package com.scut.cs.domain.dao;

import com.scut.cs.domain.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by NeilHY on 2016/8/9.
 */
public interface ProjectRepository extends JpaRepository<Project,Long> {
    @Query("select p from Project p where p.projectName= :name and p.rank= :rank and p.projectDate= :date")
    Project withNameAndRankAndDateQuery(@Param("name") String name, @Param("rank") String rank, @Param("date") Date date);

    Page<Project> findByCaptainCollege(String captainCollege, Pageable pageable);
    @Query("select p from Project p where year(p.projectDate) = ?1")
    Page<Project> findByProjectDate(int year, Pageable pageable);

    Page<Project> findByLevel(String level, Pageable pageable);

    Page<Project> findByRank(String rank, Pageable pageable);

    Page<Project> findByState(String state, Pageable pageable);

    Page<Project> findByPhotoStatus(String photoStatus, Pageable pageable);
    @Query("select p from Project p where year(p.projectDate) = ?1 and p.captainCollege = ?2")
    Page<Project> findByProjectDateAndCaptainCollege(int year,String captainCollege,Pageable pageable);

    Page<Project> findByLevelAndCaptainCollege(String level,String captainCollege, Pageable pageable);

    Page<Project> findByRankAndCaptainCollege(String rank,String captainCollege, Pageable pageable);

    Page<Project> findByStateAndCaptainCollege(String state,String captainCollege, Pageable pageable);

    Page<Project> findByPhotoStatusAndCaptainCollege(String photoStatus,String captainCollege, Pageable pageable);
    @Modifying
    @Transactional
    @Query("update Project p set p.state= ?2,p.msgForbid = ?3 where p.id= ?1")
    int setProjectState(Long id,String state,String msgForbid);
}
