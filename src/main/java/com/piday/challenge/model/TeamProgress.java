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

    // Using team_id as primary key with @Id annotation
    @Id
    private Long teamId;  // This field will match the column name team_id

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", insertable = false, updatable = false)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_challenge_id")
    private Challenge currentChallenge;

    @Column(nullable = false)
    private LocalDateTime startTime;
}