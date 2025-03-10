package com.piday.challenge.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeResponse {

    private Long id;
    private String title;
    private String description;
    private Integer sequenceNumber;
    private List<AnswerOptionDTO> answerOptions;
    private Integer remainingAttempts;
    private Integer maxPoints;
}