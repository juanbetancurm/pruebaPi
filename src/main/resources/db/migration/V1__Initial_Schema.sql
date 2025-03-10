-- Initial Schema for Pi Day ChallengeModel Application

-- Teams table to store information about competing teams
CREATE TABLE teams (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       team_name VARCHAR(100) NOT NULL UNIQUE,
                       registration_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       status VARCHAR(20) NOT NULL,
                       total_score INT NOT NULL DEFAULT 0,
                       completion_time INT NULL
);

-- Challenges table to store Pi-themed questions
CREATE TABLE challenges (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            title VARCHAR(100) NOT NULL,
                            description TEXT NOT NULL,
                            difficulty TINYINT NOT NULL,
                            sequence_number INT NOT NULL,
                            active BOOLEAN NOT NULL DEFAULT TRUE,
                            UNIQUE (sequence_number)
);

-- Answer options for multiple-choice questions
CREATE TABLE answer_options (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                challenge_id BIGINT NOT NULL,
                                option_text VARCHAR(255) NOT NULL,
                                is_correct BOOLEAN NOT NULL,
                                option_order TINYINT NOT NULL,
                                FOREIGN KEY (challenge_id) REFERENCES challenges(id) ON DELETE CASCADE
);

-- Submissions to track team answers and scoring
CREATE TABLE submissions (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             team_id BIGINT NOT NULL,
                             challenge_id BIGINT NOT NULL,
                             selected_option_id BIGINT NOT NULL,
                             is_correct BOOLEAN NOT NULL,
                             attempt_number TINYINT NOT NULL,
                             points_awarded INT NOT NULL,
                             submission_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             FOREIGN KEY (team_id) REFERENCES teams(id) ON DELETE CASCADE,
                             FOREIGN KEY (challenge_id) REFERENCES challenges(id) ON DELETE CASCADE,
                             FOREIGN KEY (selected_option_id) REFERENCES answer_options(id) ON DELETE CASCADE
);

-- TeamModel progress to track which challenge a team is currently on
CREATE TABLE team_progress (
                               team_id BIGINT PRIMARY KEY,
                               current_challenge_id BIGINT NULL,
                               start_time TIMESTAMP NOT NULL,
                               FOREIGN KEY (team_id) REFERENCES teams(id) ON DELETE CASCADE,
                               FOREIGN KEY (current_challenge_id) REFERENCES challenges(id) ON DELETE SET NULL
);

-- Create indexes for better query performance
CREATE INDEX idx_teams_status ON teams(status);
CREATE INDEX idx_challenges_active ON challenges(active);
CREATE INDEX idx_challenges_sequence ON challenges(sequence_number);
CREATE INDEX idx_submissions_team_challenge ON submissions(team_id, challenge_id);
CREATE INDEX idx_answer_options_challenge ON answer_options(challenge_id);