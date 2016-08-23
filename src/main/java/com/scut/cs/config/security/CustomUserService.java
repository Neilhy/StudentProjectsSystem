package com.scut.cs.config.security;

import com.scut.cs.domain.dao.AdminRepository;
import com.scut.cs.domain.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Created by NeilHY on 2016/8/17.
 */
public class CustomUserService  implements UserDetailsService{

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByUsername(s);
        if (admin == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        return admin;
    }
}
