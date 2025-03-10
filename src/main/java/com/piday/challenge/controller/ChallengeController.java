package com.piday.challenge.controller;

import com.piday.challenge.dto.*;
import com.piday.challenge.service.ChallengeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChallengeController {

    private final ChallengeService challengeService;

    @PostMapping("/api/teams/{teamId}/start")
    @PreAuthorize("@teamSecurityService.isTeamMember(#teamId)")
    public ResponseEntity<StartCompetitionResponse> startCompetition(@PathVariable Long teamId) {
        log.info("Received request to start competition for team ID: {}", teamId);
        StartCompetitionResponse response = challengeService.startCompetition(teamId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/teams/{teamId}/challenges/current")
    @PreAuthorize("@teamSecurityService.isTeamMember(#teamId)")
    public ResponseEntity<ChallengeResponse> getCurrentChallenge(@PathVariable Long teamId) {
        log.info("Fetching current challenge for team ID: {}", teamId);
        ChallengeResponse response = challengeService.getCurrentChallenge(teamId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/teams/{teamId}/challenges/{challengeId}/submit")
    @PreAuthorize("@teamSecurityService.isTeamMember(#teamId)")
    public ResponseEntity<SubmitAnswerResponse> submitAnswer(
            @PathVariable Long teamId,
            @PathVariable Long challengeId,
            @Valid @RequestBody SubmitAnswerRequest request) {
        log.info("Received answer submission for team ID: {} and challenge ID: {}", teamId, challengeId);
        SubmitAnswerResponse response = challengeService.submitAnswer(teamId, challengeId, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/teams/{teamId}/complete")
    @PreAuthorize("@teamSecurityService.isTeamMember(#teamId)")
    public ResponseEntity<CompletionResponse> completeCompetition(@PathVariable Long teamId) {
        log.info("Received request to complete competition for team ID: {}", teamId);
        CompletionResponse response = challengeService.completeCompetition(teamId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/teams/{teamId}/results")
    @PreAuthorize("@teamSecurityService.isTeamMember(#teamId)")
    public ResponseEntity<TeamResultsResponse> getTeamResults(@PathVariable Long teamId) {
        log.info("Fetching results for team ID: {}", teamId);
        TeamResultsResponse response = challengeService.getTeamResults(teamId);
        return ResponseEntity.ok(response);
    }
}