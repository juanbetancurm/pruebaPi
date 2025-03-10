package com.piday.challenge.repository;

import com.piday.challenge.model.TeamProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamProgressRepository extends JpaRepository<TeamProgress, Long> {

    Optional<TeamProgress> findByTeamId(Long teamId);
}