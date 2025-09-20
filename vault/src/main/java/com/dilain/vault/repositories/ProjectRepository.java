package com.dilain.vault.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dilain.vault.entities.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByUserId(Long userId);
}
