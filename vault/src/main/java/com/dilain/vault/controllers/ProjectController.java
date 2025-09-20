package com.dilain.vault.controllers;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dilain.vault.dtos.project.CreateProjectRequest;
import com.dilain.vault.dtos.project.ProjectDto;
import com.dilain.vault.entities.LoggedUserDetails;
import com.dilain.vault.entities.Project;
import com.dilain.vault.enums.ResponseStatus;
import com.dilain.vault.services.ProjectService;
import com.dilain.vault.utils.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    // private final UserRepository userRepository;

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<ProjectDto>>> getProjects(
            @AuthenticationPrincipal LoggedUserDetails loggedUser) {
        List<Project> projects = projectService.findProjectsByUserId(loggedUser.getId());
        List<ProjectDto> projectDtoList = ProjectDto.fromList(projects);

        return ResponseEntity.ok(new ApiResponse<>(ResponseStatus.SUCCESS, null, projectDtoList));
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<ProjectDto>> create(@Valid @RequestBody CreateProjectRequest request,
            @AuthenticationPrincipal LoggedUserDetails loggedUser) {
        try {
            Project project = projectService.create(request, loggedUser);
            return ResponseEntity.ok(new ApiResponse<>(ResponseStatus.SUCCESS, null, ProjectDto.from(project)));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse<>(ResponseStatus.ERROR, e.getMessage(), null));
        }
    }
}
