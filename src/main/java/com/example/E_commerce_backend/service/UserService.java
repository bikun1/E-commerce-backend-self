package com.example.E_commerce_backend.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.E_commerce_backend.dto.request.UpdateUserRequest;
import com.example.E_commerce_backend.dto.response.UserResponse;
import com.example.E_commerce_backend.entity.User;
import com.example.E_commerce_backend.exception.ResourceDuplicationException;
import com.example.E_commerce_backend.exception.ResourceNotFoundException;
import com.example.E_commerce_backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id = " + id));

        return UserResponse.fromEntity(user);
    }

    @Transactional
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id = " + id));

        if (request.email() != null && !request.email().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.email())) {
                throw new ResourceDuplicationException("User already exists with email = " + request.email());
            }
            user.setEmail(request.email());
        }

        if (request.password() != null) {
            user.setPassword(passwordEncoder.encode(request.password()));
        }

        User savedUser = userRepository.save(user);
        return UserResponse.fromEntity(savedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id = " + id);
        }

        userRepository.deleteById(id);
    }
}
