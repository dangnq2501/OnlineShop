package com.example.ecommerce_backend.controller;

import com.example.ecommerce_backend.dto.request.UserCreationRequest;
import com.example.ecommerce_backend.dto.request.UserUpdateRequest;
import com.example.ecommerce_backend.dto.response.ApiResponse;
import com.example.ecommerce_backend.dto.response.UserResponse;
import com.example.ecommerce_backend.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request)  {
        UserResponse userResponse = userService.CreateUser(request);
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userResponse);
        return apiResponse;
    }

    @PutMapping("/{id}")
    ApiResponse<UserResponse> updateUser(@PathVariable("id") String id, @RequestBody @Valid UserUpdateRequest request)  {
        UserResponse userResponse = userService.updateUser(id, request);
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userResponse);
        return apiResponse;
    }
    @PutMapping
    ApiResponse<UserResponse> updateUser( @RequestBody @Valid UserUpdateRequest request)  {
        UserResponse userResponse = userService.updateUser(request);
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userResponse);
        return apiResponse;
    }
    @GetMapping("/{id}")
    ApiResponse<UserResponse> getUser(@PathVariable("id") String id)  {
        UserResponse userResponse = userService.getUserById(id);
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userResponse);
        return apiResponse;
    }

    @GetMapping("/my-info")
    ApiResponse<UserResponse> getMyInfo (){
        UserResponse userResponse = userService.getMyInfo();
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userResponse);
        return apiResponse;
    }
    @DeleteMapping("/{id}")
    ApiResponse<Object> detekeUser(@PathVariable("id") String id)  {
        userService.deleteUser(id);
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("User has been deleted!");
        return apiResponse;
    }
}
