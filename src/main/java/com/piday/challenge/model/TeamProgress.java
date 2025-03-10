package com.piday.challenge.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "team_progress")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamProgress {

    @Id
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_challenge_id")
    private Challenge currentChallenge;

    @Column(nullable = false)
    private LocalDateTime startTime;
}