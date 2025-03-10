package com.piday.challenge.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamRegistrationResponse {

    private Long id;
    private String teamName;
    private LocalDateTime registrationTime;
    private String status;
    private String token;
}
