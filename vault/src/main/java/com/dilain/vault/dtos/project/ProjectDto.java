package com.dilain.vault.dtos.project;

import java.util.List;
import java.util.stream.Collectors;

import com.dilain.vault.entities.Project;

public record ProjectDto(Long id, String name, Long userId, String userName) {
    public static ProjectDto from(Project project) {
        return new ProjectDto(project.getId(), project.getName(), project.getUser().getId(),
                project.getUser().getUsername());
    }

    public static List<ProjectDto> fromList(List<Project> projects) {
        return projects.stream().map(ProjectDto::from).collect(Collectors.toList());
    }
}
