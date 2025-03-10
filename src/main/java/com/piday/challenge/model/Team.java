package com.piday.challenge.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "teams")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String teamName;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime registrationTime;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private TeamStatus status;

    @Column(nullable = false)
    private Integer totalScore;

    @Column
    private Integer completionTime;

    @OneToOne(mappedBy = "team", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private TeamProgress progress;

    // Enumeration for team status
    public enum TeamStatus {
        REGISTERED, IN_PROGRESS, COMPLETED
    }
}
