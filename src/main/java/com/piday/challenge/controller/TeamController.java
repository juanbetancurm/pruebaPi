package com.piday.challenge.controller;

import com.piday.challenge.dto.TeamInfoResponse;
import com.piday.challenge.dto.TeamRegistrationRequest;
import com.piday.challenge.dto.TeamRegistrationResponse;
import com.piday.challenge.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
@Slf4j
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<TeamRegistrationResponse> registerTeam(@Valid @RequestBody TeamRegistrationRequest request) {
        log.info("Received team registration request");
        TeamRegistrationResponse response = teamService.registerTeam(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{teamId}")
    @PreAuthorize("@teamSecurityService.isTeamMember(#teamId)")
    public ResponseEntity<TeamInfoResponse> getTeamInfo(@PathVariable Long teamId) {
        log.info("Fetching team info for ID: {}", teamId);
        TeamInfoResponse response = teamService.getTeamInfo(teamId);
        return ResponseEntity.ok(response);
    }
}