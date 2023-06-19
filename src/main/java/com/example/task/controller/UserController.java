package com.example.task.controller;

import com.example.task.dto.project.ProjectUpdateDto;
import com.example.task.dto.user.UserUpdateDto;
import com.example.task.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "This controller for user info")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Method for getting all users", description = "This method used to get all users")
    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                    @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info("get all user");
        return userService.getAllUser(page, size);
    }

    @Operation(summary = "Method for getting user by id", description = "This method used to get one user by its id")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        log.info("get all project");
        return userService.getById(id);
    }

    @Operation(summary = "Method for updating user", description = "This method used to update user by id")
    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody UserUpdateDto dto) {
        log.info("update user  id={},email={} ", dto.getId(), dto.getEmail());
        return userService.update(dto);
    }

    @Operation(summary = "Method for updating user", description = "This method used to update user by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        log.info("delete user  id={}", id);
        return userService.delete(id);
    }
}
