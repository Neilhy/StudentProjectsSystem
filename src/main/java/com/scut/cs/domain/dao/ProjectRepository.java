package com.scut.cs.domain.dao;

import com.scut.cs.domain.Project;
import com.scut.cs.domain.Student;
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
    Project findById(Long id);
    List<Project> findByCaptainCollege(String captainCollege);
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
    @Query("delete from Project p where p.id=?1")
    void removeById(Long id);//如果用自己的delete语句，则不会级联删除学生；用jpa自带的delete函数，就可以有级联删除

    @Modifying
    @Transactional
    @Query("update Project p set p.state= ?2,p.msgForbid = ?3 where p.id= ?1")
    int setProjectState(Long id,String state,String msgForbid);
//    @Modifying
//    @Transactional
//    @Query("update Project p set p.projectName=?1, p.level=?2, p.rank=?3, " +
//            "p.captainCollege=?4, p.teacher=?5, p.note=?6, p.projectDate=?7," +
//            "p.photoStatus=?8, p.filePath=?9, p.state=?10, p.msgForbid=?11" +
//            " where p.id = ?12")
//    void update(String projectName, String level, String rank, String captainCollege,
//                String teacher, String note, Date projectDate, String photoStatus,
//                String filePath, String state, String msgForbid, long id);
}
