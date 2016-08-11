package com.scut.cs.dao;

import com.scut.cs.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by NeilHY on 2016/8/9.
 */
public interface StudentRepository extends JpaRepository<Student,Long> {
}
