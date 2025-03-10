package com.piday.challenge.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamRegistrationRequest {

    @NotBlank(message = "Team name is required")
    @Size(min = 3, max = 50, message = "Team name must be between 3 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9 ._-]+$", message = "Team name can only contain alphanumeric characters, spaces, dots, underscores, and hyphens")
    private String teamName;
}