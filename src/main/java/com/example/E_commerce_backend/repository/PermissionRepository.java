package com.example.E_commerce_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.E_commerce_backend.entity.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    boolean existsByPathAndMethod(String path, String method);
}
