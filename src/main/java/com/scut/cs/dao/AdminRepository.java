package com.scut.cs.dao;

import com.scut.cs.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by NeilHY on 2016/8/9.
 */
public interface AdminRepository extends JpaRepository<Admin,Long> {
}
