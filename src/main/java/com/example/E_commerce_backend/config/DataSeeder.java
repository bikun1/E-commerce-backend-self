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
        // clearData();
        seedData();
    }

    private void clearData() {
        logger.info("Clearing old data");
        userRepository.deleteAll();
        roleRepository.deleteAll();
        permissionRepository.deleteAll();
    }

    private void seedData() {
        logger.info("Seeding permissions");
        Permission listUsers = Permission.of("/api/users", "GET");
        Permission getUserById = Permission.of("/api/users/{id}", "GET");
        Permission updateUserById = Permission.of("/api/users/{id}", "PUT");
        Permission deleteUserById = Permission.of("/api/users/{id}", "DELETE");

        if (!permissionRepository.existsByPathAndMethod(listUsers.getPath(), listUsers.getMethod())) {
            permissionRepository.save(listUsers);
        }
        if (!permissionRepository.existsByPathAndMethod(getUserById.getPath(), getUserById.getMethod())) {
            permissionRepository.save(getUserById);
        }
        if (!permissionRepository.existsByPathAndMethod(updateUserById.getPath(), updateUserById.getMethod())) {
            permissionRepository.save(updateUserById);
        }
        if (!permissionRepository.existsByPathAndMethod(deleteUserById.getPath(), deleteUserById.getMethod())) {
            permissionRepository.save(deleteUserById);
        }

        List<Permission> allPermissions = permissionRepository.findAll();
        List<Permission> userPermissions = allPermissions.stream()
                .filter(permission -> permission.getPath().equals("/api/users") && permission.getMethod().equals("GET")
                        || permission.getPath().equals("/api/users/{id}") && permission.getMethod().equals("GET"))
                .toList();
        List<Permission> adminPermissions = allPermissions.stream()
                .filter(permission -> permission.getPath().equals("/api/users") && permission.getMethod().equals("GET")
                        || permission.getPath().equals("/api/users/{id}") && (permission.getMethod().equals("GET")
                                || permission.getMethod().equals("PUT")
                                || permission.getMethod().equals("DELETE")))
                .toList();

        if (roleRepository.count() == 0) {
            logger.info("Seeding roles");
            Role adminRole = Role.of("Admin Role", Set.copyOf(adminPermissions));
            Role norRole = Role.of("User Role", Set.copyOf(userPermissions));
            roleRepository.saveAll(List.of(adminRole, norRole));
        }

        if (userRepository.count() == 0) {
            logger.info("Seeding users");
            Role adminRole = roleRepository.findByName("Admin Role").orElseThrow(
                    () -> new IllegalStateException("Admin role not exists"));
            Role normalRole = roleRepository.findByName("User Role").orElseThrow(
                    () -> new IllegalStateException("User role not exists"));

            User admin = User.of("Admin", "admin@test.com", passwordEncoder.encode("123456"), Set.of(adminRole));
            User user = User.of("User", "user@test.com", passwordEncoder.encode("123456"), Set.of(normalRole));

            userRepository.saveAll(List.of(admin, user));
        }
    }
}
