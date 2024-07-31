package com.example.ecommerce_backend.service;

import com.example.ecommerce_backend.dto.request.UserCreationRequest;
import com.example.ecommerce_backend.dto.request.UserUpdateRequest;
import com.example.ecommerce_backend.dto.response.UserResponse;
import com.example.ecommerce_backend.exception.AppException;
import com.example.ecommerce_backend.mapper.UserMapper;
import com.example.ecommerce_backend.models.Role;
import com.example.ecommerce_backend.models.User;
import com.example.ecommerce_backend.repository.RoleRepository;
import com.example.ecommerce_backend.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
    public UserResponse CreateUser(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException("User already existed");
        }
        User user = userMapper.toUser(request);
        List<Role> roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        String id = String.format("%s%s", user.getUsername(), user, user.getPhone());
        user.setId(id);
        return userMapper.toUserResponse(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException("User not existed"));

        return updateUser(request, user);
    }

    public UserResponse updateUser(UserUpdateRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(() -> new AppException("User not existed"));

        return updateUser(request, user);
    }

    private UserResponse updateUser(UserUpdateRequest request, User user) {
        if (request.getRoles() != null) {
            List<Role> roles = roleRepository.findAllById(request.getRoles());
            user.setRoles(new HashSet<>(roles));
        }
//        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
        if (request.getPassword() != null) {
            request.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        userMapper.updateUser(user, request);

        return userMapper.toUserResponse(userRepository.save(user));
    }


    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException("User not existed"));

        return userMapper.toUserResponse(user);
    }


    public UserResponse getMyInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new AppException("User not existed"));

        return userMapper.toUserResponse(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

}
