package com.piday.challenge.repository;

import com.piday.challenge.model.AnswerOption;
import com.piday.challenge.model.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnswerOptionRepository extends JpaRepository<AnswerOption, Long> {

    List<AnswerOption> findByChallengeOrderByOptionOrder(Challenge challenge);

    @Query("SELECT ao FROM AnswerOption ao WHERE ao.challenge.id = :challengeId AND ao.isCorrect = true")
    Optional<AnswerOption> findCorrectOptionByChallengeId(Long challengeId);
}