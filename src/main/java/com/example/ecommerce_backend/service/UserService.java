package com.example.ecommerce_backend.service;

import com.example.ecommerce_backend.dto.request.UserCreationRequest;
import com.example.ecommerce_backend.dto.response.UserResponse;
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
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;

    public UserResponse CreateUser(UserCreationRequest request) throws Exception {
        if (userRepository.findByUsername(request.getUsername())) {
            throw new Exception("User already existed");
        }
        User user = userMapper.toUser(request);
        List<Role> roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        String id = String.format("%s%s", user.getUsername(), user, user.getPhone());
        user.setId(id);
        return userMapper.toUserResponse(user);
    }

}
