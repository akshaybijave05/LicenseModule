package com.license.admin;


import java.sql.Date;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.license.entity.Permission;
import com.license.entity.Role;
import com.license.entity.User;
import com.license.repository.PermissionRepository;
import com.license.repository.RoleRepository;
import com.license.repository.UserRepository;


@Component
public class SuperAdminInitializer implements CommandLineRunner {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PermissionRepository permissionRepo;

	private BCryptPasswordEncoder passwordEncoder;

	public SuperAdminInitializer(UserRepository userRepository, RoleRepository roleRepository,
			BCryptPasswordEncoder passwordEncoder, PermissionRepository permissionRepo) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.permissionRepo = permissionRepo;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	@Transactional
	public void run(String... args) {

		if (!userRepository.existsByEmail("superAdmin@gmail.com")) {

			// create new role ADMIN
			Role superAdminRole = roleRepository.findByName("ADMIN");
			if (superAdminRole == null) {
				superAdminRole = new Role();
				superAdminRole.setName("ADMIN");
				superAdminRole = roleRepository.save(superAdminRole);

			}
				//create super admin 
				User superAdmin = new User();
				superAdmin.setEmail("superAdmin@gmail.com");
				superAdmin.setFirstName("Super");
				superAdmin.setLastName("Admin");
				String encodedPssword = passwordEncoder.encode("admin@1234");
				superAdmin.setPassword(encodedPssword);
				superAdmin.setRole(superAdminRole);
				superAdmin.setStatus(1);
				java.util.Date utilDate = new java.util.Date();
				Date date = new Date(utilDate.getTime());
				superAdmin.setCreationDate(null);
				superAdmin.setPasswordUpdatedAt(date);
				userRepository.save(superAdmin);

				// create permission with Role_admin
				if (!permissionRepo.existsByRoleName("ROLE_ADMIN")) {
					Permission permission = new Permission();
					permission.setName("ROLE_ADMIN");
					permission.setRole(superAdminRole);
					permissionRepo.save(permission);
				}
			
		}
	}
}
