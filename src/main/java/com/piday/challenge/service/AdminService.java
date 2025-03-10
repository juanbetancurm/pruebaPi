package com.piday.challenge.service;

import com.piday.challenge.dto.AdminResetResponse;
import com.piday.challenge.repository.SubmissionRepository;
import com.piday.challenge.repository.TeamProgressRepository;
import com.piday.challenge.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final TeamRepository teamRepository;
    private final TeamProgressRepository teamProgressRepository;
    private final SubmissionRepository submissionRepository;

    /**
     * Resets the competition by clearing all team data, progress, and submissions.
     * Challenges and answer options are preserved.
     *
     * @return A response with a success message and timestamp
     */
    @Transactional
    public AdminResetResponse resetCompetition() {
        log.info("Resetting the competition...");

        // Clear all submissions
        submissionRepository.deleteAll();
        log.info("All submissions deleted");

        // Clear all team progress
        teamProgressRepository.deleteAll();
        log.info("All team progress deleted");

        // Delete all teams
        teamRepository.deleteAll();
        log.info("All teams deleted");

        log.info("Competition reset successfully");

        return AdminResetResponse.builder()
                .message("Competition reset successfully")
                .timestamp(LocalDateTime.now())
                .build();
    }
}