package com.piday.challenge.service;

import com.piday.challenge.dto.LeaderboardEntryDTO;
import com.piday.challenge.dto.LeaderboardResponse;
import com.piday.challenge.model.Team;
import com.piday.challenge.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LeaderboardService {

    private final TeamRepository teamRepository;

    @Transactional(readOnly = true)
    public LeaderboardResponse getLeaderboard(String sortBy, Integer limit) {
        log.info("Fetching leaderboard with sortBy: {} and limit: {}", sortBy, limit);

        List<Team> teams;
        if ("time".equals(sortBy)) {
            teams = teamRepository.findCompletedTeamsOrderByTimeAndScore();
        } else {
            teams = teamRepository.findCompletedTeamsOrderByScoreAndTime();
        }

        // Apply limit
        if (limit != null && limit > 0 && limit < teams.size()) {
            teams = teams.subList(0, limit);
        }

        List<LeaderboardEntryDTO> leaderboardEntries = teams.stream()
                .map(team -> LeaderboardEntryDTO.builder()
                        .rank(teams.indexOf(team) + 1)
                        .teamName(team.getTeamName())
                        .totalScore(team.getTotalScore())
                        .completionTime(team.getCompletionTime())
                        .status(team.getStatus().name())
                        .build())
                .collect(Collectors.toList());

        // Get team statistics
        Long totalTeams = (long) teamRepository.count();
        Long activeTeams = teamRepository.countActiveTeams();
        Long completedTeams = teamRepository.countCompletedTeams();

        return LeaderboardResponse.builder()
                .leaderboard(leaderboardEntries)
                .totalTeams(totalTeams.intValue())
                .activeTeams(activeTeams.intValue())
                .completedTeams(completedTeams.intValue())
                .build();
    }
}