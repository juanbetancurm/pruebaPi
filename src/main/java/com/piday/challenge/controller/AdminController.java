package com.piday.challenge.controller;

import com.piday.challenge.dto.*;
import com.piday.challenge.service.AdminService;
import com.piday.challenge.service.ChallengeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final ChallengeService challengeService;
    private final AdminService adminService;

    @PostMapping("/challenges")
    public ResponseEntity<CreateChallengeResponse> createChallenge(@Valid @RequestBody CreateChallengeRequest request) {
        log.info("Received request to create new challenge: {}", request.getTitle());
        CreateChallengeResponse response = challengeService.createChallenge(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/reset")
    public ResponseEntity<AdminResetResponse> resetCompetition(@Valid @RequestBody AdminResetRequest request) {
        log.info("Received request to reset the competition");

        if (!request.getConfirm()) {
            log.warn("Reset not confirmed");
            return ResponseEntity.badRequest().build();
        }

        AdminResetResponse response = adminService.resetCompetition();
        return ResponseEntity.ok(response);
    }
}