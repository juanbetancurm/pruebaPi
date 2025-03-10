package com.piday.challenge.controller;

import com.piday.challenge.dto.LeaderboardResponse;
import com.piday.challenge.service.LeaderboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/leaderboard")
@RequiredArgsConstructor
@Slf4j
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    @GetMapping
    public ResponseEntity<LeaderboardResponse> getLeaderboard(
            @RequestParam(required = false, defaultValue = "score") String sortBy,
            @RequestParam(required = false, defaultValue = "10") Integer limit) {
        log.info("Fetching leaderboard with sortBy: {} and limit: {}", sortBy, limit);
        LeaderboardResponse response = leaderboardService.getLeaderboard(sortBy, limit);
        return ResponseEntity.ok(response);
    }
}