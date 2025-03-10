package com.piday.challenge.repository;

import com.piday.challenge.model.TeamProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamProgressRepository extends JpaRepository<TeamProgress, Long> {
    // teamId is now the primary key, so findByTeamId isn't necessary
    // but keeping it for backward compatibility
    Optional<TeamProgress> findByTeamId(Long teamId);
}