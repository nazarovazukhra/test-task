package com.example.task.service;

import com.example.task.dto.project.ProjectCreateDto;
import com.example.task.dto.project.ProjectUpdateDto;
import com.example.task.dto.project.UserProjectDto;
import com.example.task.dto.response.ResponseDto;
import com.example.task.entity.Project;
import com.example.task.entity.User;
import com.example.task.entity.UserProject;
import com.example.task.enums.Role;
import com.example.task.repository.ProjectRepository;
import com.example.task.repository.UserProjectRepository;
import com.example.task.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final UserProjectRepository userProjectRepository;

    public ResponseEntity<?> getAllProject(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt"));
        Page<Project> projectPage = projectRepository.findAll(pageRequest);

        PageImpl<Project> projects = new PageImpl<>(projectPage.getContent(), pageRequest, projectPage.getTotalElements());
        return ResponseDto.response(projects, HttpStatus.OK);
    }

    public ResponseEntity<?> getById(Long id) {

        Optional<Project> optionalProject = projectRepository.findById(id);
        if (optionalProject.isEmpty())
            return ResponseDto.response("Project not found", HttpStatus.NOT_FOUND);

        Project project = optionalProject.get();
        return ResponseDto.response(project, HttpStatus.OK);
    }

    public ResponseEntity<?> create(ProjectCreateDto dto) {
        String name = dto.getName();
        boolean existsByName = projectRepository.existsByName(name);
        if (existsByName)
            return ResponseDto.response("Project already exists", HttpStatus.BAD_REQUEST);

        Project project = new Project();
        project.setName(name);
        project.setDescription(dto.getDescription());
        Project savedProject = projectRepository.save(project);
        return ResponseDto.response(savedProject, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> update(ProjectUpdateDto dto) {

        Long projectId = dto.getProjectId();
        String name = dto.getName();
        String description = dto.getDescription();

        if (Objects.isNull(name) && Objects.isNull(description))
            return ResponseDto.response("Name or Description should being updated", HttpStatus.BAD_REQUEST);

        Optional<Project> optionalProject = projectRepository.findById(projectId);
        if (optionalProject.isEmpty()) {
            return ResponseDto.response("Such project not found", HttpStatus.NOT_FOUND);
        }

        Project updating = optionalProject.get();
        if (Objects.nonNull(name) && !updating.getName().equals(name)) {
            boolean exists = projectRepository.existsByName(name);
            if (exists)
                return ResponseDto.response("This project already added", HttpStatus.BAD_REQUEST);
            updating.setName(name);
        }
        if (Objects.nonNull(description)) {
            updating.setDescription(description);
        }

        Project updated = projectRepository.save(updating);
        return ResponseDto.response(updated, HttpStatus.OK);

    }

    @Transactional
    public ResponseEntity<?> delete(Long id) {
        boolean existsById = projectRepository.existsById(id);
        if (!existsById)
            return ResponseDto.response("Such project not found", HttpStatus.NOT_FOUND);

        List<UserProject> list = userProjectRepository.findByProjectId(id);
        userProjectRepository.deleteAll(list);

        projectRepository.deleteById(id);

        return ResponseDto.response("Project deleted", HttpStatus.OK);

    }

    public ResponseEntity<?> addUserToProject(UserProjectDto dto) {
        Long projectId = dto.getProjectId();
        Long userId = dto.getUserId();
        boolean existsByProjectIdAndUserId = userProjectRepository.existsByProjectIdAndUserId(projectId, userId);
        if (existsByProjectIdAndUserId)
            return ResponseDto.response("This user already exists in this project", HttpStatus.BAD_REQUEST);

        Optional<Project> optionalProject = projectRepository.findById(projectId);
        if (optionalProject.isEmpty())
            return ResponseDto.response("Such project not found", HttpStatus.NOT_FOUND);

        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty())
            return ResponseDto.response("Such user not found", HttpStatus.NOT_FOUND);

        User user = optionalUser.get();
        if (!user.isEnabled())
            return ResponseDto.response("Blocked user", HttpStatus.BAD_REQUEST);

        String roleName = dto.getRoleName();
        Role role = Role.getRole(roleName);
        if (role.equals(Role.NONE))
            return ResponseDto.response("Role NONE", HttpStatus.BAD_REQUEST);

        Project project = optionalProject.get();
        UserProject userProject = new UserProject();
        userProject.setProject(project);
        userProject.setUser(user);
        userProject.setRole(role);

        UserProject saved = userProjectRepository.save(userProject);
        project.setEmployeeCount(project.getEmployeeCount() + 1);
        projectRepository.save(project);
        return ResponseDto.response(saved, HttpStatus.OK);

    }

    public ResponseEntity<?> getAllUsersByProjectId(Long id) {
        List<UserProject> byProjectId = userProjectRepository.findByProjectId(id);
        return ResponseDto.response(byProjectId, HttpStatus.OK);
    }

    public ResponseEntity<?> getAllProjectByUserId(Long id) {
        List<UserProject> byProjectId = userProjectRepository.findByUserId(id);
        return ResponseDto.response(byProjectId, HttpStatus.OK);
    }

    public ResponseEntity<?> deleteUserFromProject(Long userId, Long projectId) {
        int result = userProjectRepository.deleteUserFromProject(userId, projectId);

        Optional<Project> optionalProject = projectRepository.findById(projectId);
        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();
            if (result == 1) {
                project.setEmployeeCount(project.getEmployeeCount() - 1);
                projectRepository.save(project);
            }
        }

        return ResponseDto.response("OK", HttpStatus.OK);

    }
}
