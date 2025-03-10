package com.piday.challenge.repository;

import com.piday.challenge.model.Challenge;
import com.piday.challenge.model.Submission;
import com.piday.challenge.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    List<Submission> findByTeamAndChallengeOrderBySubmissionTimeDesc(Team team, Challenge challenge);

    @Query("SELECT COUNT(s) FROM Submission s WHERE s.team.id = :teamId AND s.challenge.id = :challengeId")
    Integer countAttemptsByTeamAndChallenge(Long teamId, Long challengeId);

    @Query("SELECT COUNT(s) FROM Submission s WHERE s.team.id = :teamId AND s.isCorrect = true")
    Integer countCorrectSubmissionsByTeam(Long teamId);

    @Query("SELECT s FROM Submission s WHERE s.team.id = :teamId ORDER BY s.challenge.sequenceNumber ASC")
    List<Submission> findAllByTeamOrderBySequence(Long teamId);
}