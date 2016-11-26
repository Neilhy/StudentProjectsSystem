package com.scut.cs.domain.dao;

import com.scut.cs.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by NeilHY on 2016/8/9.
 */
public interface StudentRepository extends JpaRepository<Student,Long> {
    //Student findByRegisterId(Long registerId);//因为学号不唯一了

    List<Student> findByStudentName(String studentName);

    List<Student> findByCollege(String college);

    List<Student> findByClassName(String ClassName);

    List<Student> findByGrade(Short grade);

}
