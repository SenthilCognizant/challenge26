package com.gds.challenge26.controller;

import com.gds.challenge26.model.User;
import com.gds.challenge26.model.UserDto;
import com.gds.challenge26.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "Users APIs", description = "Operations related to user loaded via batch")
public class UserController {

    private final UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }


    @Operation(summary = "Get all users")
    @GetMapping("/users")
    public List<UserDto> getUsers(){
        List<User> users = repository.findAll();
        return users.stream()
                .map(user -> new UserDto(user.getName()))
                .toList();
    }
}
