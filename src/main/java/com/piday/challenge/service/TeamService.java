package com.piday.challenge.service;

import com.piday.challenge.dto.TeamInfoResponse;
import com.piday.challenge.dto.TeamRegistrationRequest;
import com.piday.challenge.dto.TeamRegistrationResponse;
import com.piday.challenge.exception.ResourceNotFoundException;
import com.piday.challenge.exception.TeamAlreadyExistsException;
import com.piday.challenge.model.Team;
import com.piday.challenge.repository.TeamRepository;
import com.piday.challenge.security.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeamService {

    private final TeamRepository teamRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final TimerService timerService;

    @Transactional
    public TeamRegistrationResponse registerTeam(TeamRegistrationRequest request) {
        log.info("Registering new team with name: {}", request.getTeamName());

        // Check if team name already exists
        if (teamRepository.existsByTeamName(request.getTeamName())) {
            log.warn("Team name already exists: {}", request.getTeamName());
            throw new TeamAlreadyExistsException("Team name already exists");
        }

        // Create new team
        Team team = Team.builder()
                .teamName(request.getTeamName())
                .status(Team.TeamStatus.REGISTERED)
                .totalScore(0)
                .build();

        Team savedTeam = teamRepository.save(team);
        log.info("Team registered successfully with ID: {}", savedTeam.getId());

        // Generate JWT token for team authentication
        String token = jwtTokenUtil.generateToken(savedTeam.getId(), savedTeam.getTeamName());

        return TeamRegistrationResponse.builder()
                .id(savedTeam.getId())
                .teamName(savedTeam.getTeamName())
                .registrationTime(savedTeam.getRegistrationTime())
                .status(savedTeam.getStatus().name())
                .token(token)
                .build();
    }

    @Transactional(readOnly = true)
    public TeamInfoResponse getTeamInfo(Long teamId) {
        log.info("Fetching team info for ID: {}", teamId);

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with ID: " + teamId));

        Integer elapsedTime = null;
        if (team.getStatus() == Team.TeamStatus.IN_PROGRESS && team.getProgress() != null) {
            elapsedTime = timerService.calculateElapsedTime(team.getProgress().getStartTime());
        } else if (team.getStatus() == Team.TeamStatus.COMPLETED) {
            elapsedTime = team.getCompletionTime();
        }

        Long currentChallengeId = null;
        if (team.getProgress() != null && team.getProgress().getCurrentChallenge() != null) {
            currentChallengeId = team.getProgress().getCurrentChallenge().getId();
        }

        return TeamInfoResponse.builder()
                .id(team.getId())
                .teamName(team.getTeamName())
                .registrationTime(team.getRegistrationTime())
                .status(team.getStatus().name())
                .currentChallengeId(currentChallengeId)
                .totalScore(team.getTotalScore())
                .elapsedTime(elapsedTime)
                .build();
    }

    @Transactional
    public void updateTeamScore(Long teamId, Integer pointsToAdd) {
        log.info("Updating score for team ID: {} with points: {}", teamId, pointsToAdd);

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with ID: " + teamId));

        team.setTotalScore(team.getTotalScore() + pointsToAdd);
        teamRepository.save(team);

        log.info("Team score updated successfully. New score: {}", team.getTotalScore());
    }

    @Transactional
    public void updateTeamStatus(Long teamId, Team.TeamStatus status) {
        log.info("Updating status for team ID: {} to: {}", teamId, status);

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with ID: " + teamId));

        team.setStatus(status);
        teamRepository.save(team);

        log.info("Team status updated successfully");
    }

    @Transactional
    public void recordCompletionTime(Long teamId, LocalDateTime startTime) {
        log.info("Recording completion time for team ID: {}", teamId);

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with ID: " + teamId));

        int seconds = (int) startTime.until(LocalDateTime.now(), ChronoUnit.SECONDS);
        team.setCompletionTime(seconds);
        team.setStatus(Team.TeamStatus.COMPLETED);

        teamRepository.save(team);

        log.info("Team completion time recorded: {} seconds", seconds);
    }
}