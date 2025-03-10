package com.piday.challenge.service;

import com.piday.challenge.dto.*;
import com.piday.challenge.exception.InvalidOperationException;
import com.piday.challenge.exception.ResourceNotFoundException;
import com.piday.challenge.model.*;
import com.piday.challenge.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final AnswerOptionRepository answerOptionRepository;
    private final TeamRepository teamRepository;
    private final TeamProgressRepository teamProgressRepository;
    private final SubmissionRepository submissionRepository;
    private final TeamService teamService;

    private static final int MAX_ATTEMPTS = 3;
    private static final int MAX_POINTS_PER_CHALLENGE = 3;

    @Transactional
    public StartCompetitionResponse startCompetition(Long teamId) {
        log.info("Starting competition for team ID: {}", teamId);

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with ID: " + teamId));

        if (team.getStatus() == Team.TeamStatus.IN_PROGRESS) {
            log.warn("Competition already started for team ID: {}", teamId);
            throw new InvalidOperationException("Competition already started for this team");
        }

        // Get first challenge
        Challenge firstChallenge = challengeRepository.findBySequenceNumber(1)
                .orElseThrow(() -> new ResourceNotFoundException("No challenges found"));

        // Create or update team progress
        TeamProgress progress = team.getProgress();
        if (progress == null) {
            progress = new TeamProgress();
            progress.setTeam(team);
        }

        LocalDateTime now = LocalDateTime.now();
        progress.setStartTime(now);
        progress.setCurrentChallenge(firstChallenge);

        // Update team status
        team.setStatus(Team.TeamStatus.IN_PROGRESS);
        teamRepository.save(team);
        teamProgressRepository.save(progress);

        log.info("Competition started successfully for team ID: {}", teamId);

        return StartCompetitionResponse.builder()
                .message("Competition started")
                .startTime(now)
                .firstChallengeId(firstChallenge.getId())
                .build();
    }

    @Transactional(readOnly = true)
    public ChallengeResponse getCurrentChallenge(Long teamId) {
        log.info("Fetching current challenge for team ID: {}", teamId);

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with ID: " + teamId));

        if (team.getStatus() != Team.TeamStatus.IN_PROGRESS) {
            log.warn("Team is not in progress: {}", teamId);
            throw new InvalidOperationException("Team is not in an active competition");
        }

        TeamProgress progress = teamProgressRepository.findByTeamId(teamId)
                .orElseThrow(() -> new InvalidOperationException("Team has not started the competition"));

        Challenge challenge = progress.getCurrentChallenge();
        if (challenge == null) {
            log.warn("No current challenge found for team ID: {}", teamId);
            throw new ResourceNotFoundException("No current challenge found for team");
        }

        List<AnswerOption> options = answerOptionRepository.findByChallengeOrderByOptionOrder(challenge);

        // Count previous attempts
        Integer attempts = submissionRepository.countAttemptsByTeamAndChallenge(teamId, challenge.getId());
        Integer remainingAttempts = MAX_ATTEMPTS - attempts;

        List<AnswerOptionDTO> optionDTOs = options.stream()
                .map(option -> AnswerOptionDTO.builder()
                        .id(option.getId())
                        .optionText(option.getOptionText())
                        .optionOrder(option.getOptionOrder())
                        .build())
                .collect(Collectors.toList());

        return ChallengeResponse.builder()
                .id(challenge.getId())
                .title(challenge.getTitle())
                .description(challenge.getDescription())
                .sequenceNumber(challenge.getSequenceNumber())
                .answerOptions(optionDTOs)
                .remainingAttempts(remainingAttempts)
                .maxPoints(calculateMaxPoints(remainingAttempts))
                .build();
    }

    @Transactional
    public SubmitAnswerResponse submitAnswer(Long teamId, Long challengeId, SubmitAnswerRequest request) {
        log.info("Processing answer submission for team ID: {} and challenge ID: {}", teamId, challengeId);

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with ID: " + teamId));

        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new ResourceNotFoundException("Challenge not found with ID: " + challengeId));

        TeamProgress progress = teamProgressRepository.findByTeamId(teamId)
                .orElseThrow(() -> new InvalidOperationException("Team has not started the competition"));

        if (!challenge.getId().equals(progress.getCurrentChallenge().getId())) {
            log.warn("Submitted answer for incorrect challenge");
            throw new InvalidOperationException("This is not the current challenge for the team");
        }

        AnswerOption selectedOption = answerOptionRepository.findById(request.getSelectedOptionId())
                .orElseThrow(() -> new ResourceNotFoundException("Answer option not found"));

        // Check if option belongs to the current challenge
        if (!selectedOption.getChallenge().getId().equals(challengeId)) {
            log.warn("Selected option does not belong to the current challenge");
            throw new InvalidOperationException("Selected option does not belong to the current challenge");
        }

        // Count previous attempts
        Integer attempts = submissionRepository.countAttemptsByTeamAndChallenge(teamId, challengeId);
        Integer attemptNumber = attempts + 1;

        if (attemptNumber > MAX_ATTEMPTS) {
            log.warn("Maximum attempts exceeded for team ID: {} on challenge ID: {}", teamId, challengeId);
            throw new InvalidOperationException("Maximum attempts exceeded for this challenge");
        }

        boolean isCorrect = selectedOption.getIsCorrect();
        Integer pointsAwarded = 0;

        if (isCorrect) {
            pointsAwarded = calculateMaxPoints(MAX_ATTEMPTS - attempts);
            teamService.updateTeamScore(teamId, pointsAwarded);
            log.info("Correct answer submitted. Points awarded: {}", pointsAwarded);
        } else {
            log.info("Incorrect answer submitted. No points awarded.");
        }

        // Record submission
        Submission submission = Submission.builder()
                .team(team)
                .challenge(challenge)
                .selectedOption(selectedOption)
                .isCorrect(isCorrect)
                .attemptNumber(attemptNumber)
                .pointsAwarded(pointsAwarded)
                .build();

        submissionRepository.save(submission);

        SubmitAnswerResponse.SubmitAnswerResponseBuilder responseBuilder = SubmitAnswerResponse.builder()
                .correct(isCorrect)
                .pointsAwarded(pointsAwarded)
                .totalScore(team.getTotalScore());

        // If answer is correct, move to next challenge or complete
        if (isCorrect) {
            Optional<Challenge> nextChallenge = challengeRepository.findNextActiveChallenge(challenge.getSequenceNumber());

            if (nextChallenge.isPresent()) {
                progress.setCurrentChallenge(nextChallenge.get());
                teamProgressRepository.save(progress);

                responseBuilder
                        .message("Correct answer! Moving to next challenge.")
                        .nextChallengeId(nextChallenge.get().getId())
                        .remainingAttempts(MAX_ATTEMPTS);
            } else {
                // No more challenges - complete the competition
                progress.setCurrentChallenge(null);
                teamProgressRepository.save(progress);

                teamService.recordCompletionTime(teamId, progress.getStartTime());

                responseBuilder
                        .message("Correct answer! You have completed all challenges.")
                        .nextChallengeId(null)
                        .remainingAttempts(0);
            }
        } else {
            responseBuilder
                    .message("Incorrect answer. Try again.")
                    .nextChallengeId(null)
                    .remainingAttempts(MAX_ATTEMPTS - attemptNumber);
        }

        return responseBuilder.build();
    }

    @Transactional
    public CompletionResponse completeCompetition(Long teamId) {
        log.info("Completing competition for team ID: {}", teamId);

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with ID: " + teamId));

        if (team.getStatus() != Team.TeamStatus.IN_PROGRESS) {
            log.warn("Team is not in progress: {}", teamId);
            throw new InvalidOperationException("Team is not in an active competition");
        }

        TeamProgress progress = teamProgressRepository.findByTeamId(teamId)
                .orElseThrow(() -> new InvalidOperationException("Team has not started the competition"));

        // Check if team has completed all challenges
        if (progress.getCurrentChallenge() != null) {
            log.warn("Team has not completed all challenges");
            throw new InvalidOperationException("Team has not completed all challenges");
        }

        // Already completed earlier, just return the results
        if (team.getStatus() == Team.TeamStatus.COMPLETED && team.getCompletionTime() != null) {
            return buildCompletionResponse(team);
        }

        // Record completion time if not already done
        teamService.recordCompletionTime(teamId, progress.getStartTime());

        return buildCompletionResponse(team);
    }

    private CompletionResponse buildCompletionResponse(Team team) {
        // Count total challenges and correct answers
        Long totalChallenges = challengeRepository.countActiveChallenges();
        Integer correctAnswers = submissionRepository.countCorrectSubmissionsByTeam(team.getId());

        // Calculate ranking (assumes leaderboard is sorted by score desc, time asc)
        List<Team> rankedTeams = teamRepository.findCompletedTeamsOrderByScoreAndTime();
        Integer ranking = 0;
        for (int i = 0; i < rankedTeams.size(); i++) {
            if (rankedTeams.get(i).getId().equals(team.getId())) {
                ranking = i + 1;
                break;
            }
        }

        // Calculate percentile
        Integer percentile = ranking > 0 && !rankedTeams.isEmpty()
                ? (int)(100 - ((double)ranking / rankedTeams.size() * 100))
                : null;

        return CompletionResponse.builder()
                .teamName(team.getTeamName())
                .completionTime(team.getCompletionTime())
                .totalScore(team.getTotalScore())
                .totalChallenges(totalChallenges.intValue())
                .correctAnswers(correctAnswers)
                .ranking(ranking > 0 ? ranking : null)
                .leaderboardPercentile(percentile)
                .build();
    }

    @Transactional
    public TeamResultsResponse getTeamResults(Long teamId) {
        log.info("Fetching results for team ID: {}", teamId);

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with ID: " + teamId));

        if (team.getStatus() != Team.TeamStatus.COMPLETED) {
            log.warn("Team has not completed the competition: {}", teamId);
            throw new InvalidOperationException("Team has not completed the competition");
        }

        // Get completion response
        CompletionResponse completion = buildCompletionResponse(team);

        // Get detailed challenge results
        List<Submission> submissions = submissionRepository.findAllByTeamOrderBySequence(teamId);
        TeamProgress progress = teamProgressRepository.findByTeamId(teamId)
                .orElseThrow(() -> new InvalidOperationException("Team progress not found"));

        // Group submissions by challenge to get attempts and time spent
        Map<Long, List<Submission>> submissionsByChallenge = submissions.stream()
                .collect(Collectors.groupingBy(s -> s.getChallenge().getId()));

        List<ChallengeResultDTO> challengeResults = new ArrayList<>();
        List<Challenge> allChallenges = challengeRepository.findAllActiveOrderBySequence();

        for (Challenge challenge : allChallenges) {
            List<Submission> challengeSubmissions = submissionsByChallenge.getOrDefault(challenge.getId(), new ArrayList<>());

            boolean correct = challengeSubmissions.stream().anyMatch(Submission::getIsCorrect);
            int attempts = challengeSubmissions.size();
            int points = challengeSubmissions.stream()
                    .mapToInt(Submission::getPointsAwarded)
                    .sum();

            // Estimate time spent (difference between first and last submission)
            int timeSpent = 0;
            if (!challengeSubmissions.isEmpty()) {
                Submission first = challengeSubmissions.get(0);
                Submission last = challengeSubmissions.get(challengeSubmissions.size() - 1);
                timeSpent = (int) first.getSubmissionTime().until(last.getSubmissionTime(), ChronoUnit.SECONDS);

                // Add a default time if only one submission or time too short
                if (timeSpent < 10) timeSpent = 30;
            }

            challengeResults.add(ChallengeResultDTO.builder()
                    .id(challenge.getId())
                    .title(challenge.getTitle())
                    .description(challenge.getDescription())
                    .correct(correct)
                    .attemptCount(attempts)
                    .pointsAwarded(points)
                    .timeSpent(timeSpent)
                    .build());
        }

        return TeamResultsResponse.builder()
                .teamName(completion.getTeamName())
                .completionTime(completion.getCompletionTime())
                .totalScore(completion.getTotalScore())
                .totalChallenges(completion.getTotalChallenges())
                .correctAnswers(completion.getCorrectAnswers())
                .ranking(completion.getRanking())
                .leaderboardPercentile(completion.getLeaderboardPercentile())
                .challengeBreakdown(challengeResults)
                .build();
    }

    @Transactional
    public CreateChallengeResponse createChallenge(CreateChallengeRequest request) {
        log.info("Creating new challenge: {}", request.getTitle());

        Challenge challenge = Challenge.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .sequenceNumber(request.getSequenceNumber())
                .difficulty(request.getDifficulty())
                .active(true)
                .build();

        Challenge savedChallenge = challengeRepository.save(challenge);

        // Validate that at least one option is marked as correct
        boolean hasCorrectOption = request.getAnswerOptions().stream()
                .anyMatch(CreateAnswerOptionRequest::getIsCorrect);

        if (!hasCorrectOption) {
            challengeRepository.delete(savedChallenge);
            throw new InvalidOperationException("At least one answer option must be marked as correct");
        }

        // Create answer options
        for (CreateAnswerOptionRequest optionRequest : request.getAnswerOptions()) {
            AnswerOption option = AnswerOption.builder()
                    .challenge(savedChallenge)
                    .optionText(optionRequest.getOptionText())
                    .isCorrect(optionRequest.getIsCorrect())
                    .optionOrder(optionRequest.getOptionOrder())
                    .build();

            answerOptionRepository.save(option);
        }

        log.info("Challenge created successfully with ID: {}", savedChallenge.getId());

        return CreateChallengeResponse.builder()
                .id(savedChallenge.getId())
                .title(savedChallenge.getTitle())
                .message("Challenge created successfully")
                .build();
    }

    private Integer calculateMaxPoints(Integer remainingAttempts) {
        // 3 points for first attempt, 2 for second, 1 for third
        return Math.min(remainingAttempts, MAX_POINTS_PER_CHALLENGE);
    }
}