package com.dilain.vault.dtos.project;

import jakarta.validation.constraints.NotBlank;

public record CreateProjectRequest(@NotBlank String name) {
}
