package com.example.E_commerce_backend.config;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.E_commerce_backend.entity.Permission;
import com.example.E_commerce_backend.entity.Role;
import com.example.E_commerce_backend.entity.User;
import com.example.E_commerce_backend.repository.PermissionRepository;
import com.example.E_commerce_backend.repository.RoleRepository;
import com.example.E_commerce_backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataSeeder.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        seedData();
    }

    private void seedData() {
        if (permissionRepository.count() > 0) {
            logger.info("permission already exists");
            return;
        }

        logger.info("Seeding permissions");
        Permission test1 = Permission.of("/api/test1", "GET");
        Permission test2 = Permission.of("/api/test2", "GET");

        permissionRepository.saveAll(List.of(test1, test2));

        if (roleRepository.count() > 0) {
            logger.info("Role already exists");
            return;
        }

        logger.info("Seeding roles");
        Role adminRole = Role.of("Admin Role", Set.of(test1, test2));
        Role norRole = Role.of("User Role", Set.of(test1));

        roleRepository.saveAll(List.of(adminRole, norRole));

        if (userRepository.count() > 0) {
            logger.info("User already exists");
            return;
        }

        logger.info("Seeding users");
        User admin = User.of("Admin", "admin@test.com", passwordEncoder.encode("123456"), Set.of(adminRole));
        User user = User.of("User", "user@test.com", passwordEncoder.encode("123456"), Set.of(norRole));

        userRepository.saveAll(List.of(admin, user));
    }
}
