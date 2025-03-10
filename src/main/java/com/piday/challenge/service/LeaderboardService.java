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
import java.util.stream.IntStream;

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

        // Create a final copy of teams for use in the lambda
        final List<Team> finalTeams = teams;

        List<LeaderboardEntryDTO> leaderboardEntries = IntStream.range(0, finalTeams.size())
                .mapToObj(i -> LeaderboardEntryDTO.builder()
                        .rank(i + 1)
                        .teamName(finalTeams.get(i).getTeamName())
                        .totalScore(finalTeams.get(i).getTotalScore())
                        .completionTime(finalTeams.get(i).getCompletionTime())
                        .status(finalTeams.get(i).getStatus().name())
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