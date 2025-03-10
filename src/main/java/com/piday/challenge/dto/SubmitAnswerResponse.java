package com.piday.challenge.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmitAnswerResponse {

    private Boolean correct;
    private Integer pointsAwarded;
    private String message;
    private Integer remainingAttempts;
    private Long nextChallengeId;
    private Integer totalScore;
}