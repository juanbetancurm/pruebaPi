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
public class TeamInfoResponse {

    private Long id;
    private String teamName;
    private LocalDateTime registrationTime;
    private String status;
    private Long currentChallengeId;
    private Integer totalScore;
    private Integer elapsedTime;
}