package com.scut.cs;

import com.scut.cs.domain.Admin;
import com.scut.cs.domain.dao.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class StudentProjectsApplication extends SpringBootServletInitializer implements CommandLineRunner{

	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(StudentProjectsApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(StudentProjectsApplication.class);
	}

	@Override
	public void run(String... strings) throws Exception {
		if(adminRepository.findByUsername("admin") != null) {
			return;
		}
		adminRepository.deleteAll();
		Admin admin = new Admin(1L, "admin", passwordEncoder.encode("123"), "", "ROLE_ADMIN");
		Admin outer = new Admin(2L, "outer", passwordEncoder.encode("123"), "计算机科学与工程学院", "ROLE_OUTER");
		Admin inner = new Admin(3L, "inner", passwordEncoder.encode("123"), "", "ROLE_INNER");
		adminRepository.save(admin);
		adminRepository.save(outer);
		adminRepository.save(inner);
	}
}
