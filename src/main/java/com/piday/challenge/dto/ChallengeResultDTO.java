package com.piday.challenge.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeResultDTO {

    private Long id;
    private String title;
    private String description;
    private Boolean correct;
    private Integer attemptCount;
    private Integer pointsAwarded;
    private Integer timeSpent;
}