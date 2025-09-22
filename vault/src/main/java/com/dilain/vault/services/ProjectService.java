package com.dilain.vault.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dilain.vault.dtos.project.CreateProjectRequest;
import com.dilain.vault.entities.LoggedUserDetails;
import com.dilain.vault.entities.Project;
import com.dilain.vault.entities.User;
import com.dilain.vault.exceptions.UserNotFoundException;
import com.dilain.vault.repositories.ProjectRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserService userService;

    public Project create(CreateProjectRequest request, LoggedUserDetails loggedUser) {
        User user = userService.findByUsername(loggedUser.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Project project = new Project();
        project.setName(request.name());
        project.setUser(user);
        return projectRepository.save(project);
    }

    public List<Project> findProjectsByUserId(Long userId) {
        return projectRepository.findByUserId(userId);
    }
}
