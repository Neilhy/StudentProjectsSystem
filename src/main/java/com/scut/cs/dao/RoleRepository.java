package com.scut.cs.dao;

import com.scut.cs.domain.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by NeilHY on 2016/8/9.
 */
public interface RoleRepository extends JpaRepository<RoleType,Short> {
}
