package com.piday.challenge.repository;

import com.piday.challenge.model.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

    @Query("SELECT c FROM Challenge c WHERE c.active = true ORDER BY c.sequenceNumber ASC")
    List<Challenge> findAllActiveOrderBySequence();

    Optional<Challenge> findBySequenceNumber(Integer sequenceNumber);

    @Query("SELECT c FROM Challenge c WHERE c.sequenceNumber > :currentSequence AND c.active = true ORDER BY c.sequenceNumber ASC")
    Optional<Challenge> findNextActiveChallenge(Integer currentSequence);

    @Query("SELECT COUNT(c) FROM Challenge c WHERE c.active = true")
    Long countActiveChallenges();
}