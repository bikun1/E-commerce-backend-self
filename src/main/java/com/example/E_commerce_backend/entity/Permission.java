package com.example.E_commerce_backend.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "permissions")
@NoArgsConstructor
@Getter
public class Permission extends BaseEntity {

    private String path;

    private String method;

    public Permission(String path, String method) {
        this.path = path;
        this.method = method;
    }

    public static Permission of(String path, String method) {
        return new Permission(path, method);
    }

}
