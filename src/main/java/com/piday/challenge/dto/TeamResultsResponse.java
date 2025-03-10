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
public class TeamResultsResponse {

    private String teamName;
    private Integer completionTime;
    private Integer totalScore;
    private Integer totalChallenges;
    private Integer correctAnswers;
    private Integer ranking;
    private Integer leaderboardPercentile;
    private List<ChallengeResultDTO> challengeBreakdown;
}