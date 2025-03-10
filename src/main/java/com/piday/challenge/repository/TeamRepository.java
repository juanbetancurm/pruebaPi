package com.piday.challenge.repository;

import com.piday.challenge.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    Optional<Team> findByTeamName(String teamName);

    boolean existsByTeamName(String teamName);

    @Query("SELECT t FROM Team t WHERE t.status = 'COMPLETED' ORDER BY t.totalScore DESC, t.completionTime ASC")
    List<Team> findCompletedTeamsOrderByScoreAndTime();

    @Query("SELECT t FROM Team t WHERE t.status = 'COMPLETED' ORDER BY t.completionTime ASC, t.totalScore DESC")
    List<Team> findCompletedTeamsOrderByTimeAndScore();

    @Query("SELECT COUNT(t) FROM Team t WHERE t.status = 'IN_PROGRESS'")
    Long countActiveTeams();

    @Query("SELECT COUNT(t) FROM Team t WHERE t.status = 'COMPLETED'")
    Long countCompletedTeams();
}
