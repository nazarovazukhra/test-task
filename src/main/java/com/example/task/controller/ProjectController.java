package com.example.task.controller;

import com.example.task.dto.project.ProjectCreateDto;
import com.example.task.dto.project.ProjectUpdateDto;
import com.example.task.dto.project.UserProjectDto;
import com.example.task.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api/project")
@RequiredArgsConstructor
@Tag(name = "Project Controller", description = "This controller for project")
public class ProjectController {

    private final ProjectService projectService;

    @Operation(summary = "Method for getting all projects", description = "This method used to get all projects")
    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                    @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info("get all project");
        return projectService.getAllProject(page, size);
    }

    @Operation(summary = "Method for getting project by id", description = "This method used to get one project by its id")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        log.info("get all project");
        return projectService.getById(id);
    }

    @Operation(summary = "Method for creating project", description = "This method used to create project")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody ProjectCreateDto dto) {
        log.info("create project " + dto.getName());
        return projectService.create(dto);
    }

    @Operation(summary = "Method for updating project", description = "This method used to update project by id")
    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody ProjectUpdateDto dto) {
        log.info("update project  id={},name={} ", dto.getProjectId(), dto.getName());
        return projectService.update(dto);
    }

    @Operation(summary = "Method for updating project", description = "This method used to update project by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        log.info("delete project  id={}", id);
        return projectService.delete(id);
    }

    @Operation(summary = "Method for adding users to project", description = "This method used to add users to project")
    @PostMapping("/add_user")
    public ResponseEntity<?> addUserToProject(@RequestBody UserProjectDto dto) {
        log.info("add users to project ");
        return projectService.addUserToProject(dto);
    }

    @Operation(summary = "Method for adding users to project", description = "This method used to add users to project")
    @PostMapping("/delete_user_from_project")
    public ResponseEntity<?> deleteUserFromProject(@RequestParam("user_id") Long userId, @RequestParam("project_id") Long projectId) {
        log.info("delete user from project  user_id={}, project_id={}", userId, projectId);
        return projectService.deleteUserFromProject(userId, projectId);
    }

    @Operation(summary = "Method for getting all user by project id", description = "This method used to get all users by project id")
    @GetMapping("/all_users_by_project_id/{id}")
    public ResponseEntity<?> getAllUsersByProjectId(@PathVariable Long id) {
        log.info("get all users by project id={} ", id);
        return projectService.getAllUsersByProjectId(id);
    }

    @Operation(summary = "Method for getting all project by user id", description = "This method used to get all project by user id")
    @GetMapping("/all_project_by_user_id/{id}")
    public ResponseEntity<?> getAllProjectByUserId(@PathVariable Long id) {
        log.info("get all project by user id={} ", id);
        return projectService.getAllProjectByUserId(id);
    }
}
