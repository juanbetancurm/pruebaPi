package com.piday.challenge.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@Slf4j
public class TimerService {

    /**
     * Calculates the elapsed time in seconds since the given start time.
     *
     * @param startTime The start time
     * @return The elapsed time in seconds
     */
    public Integer calculateElapsedTime(LocalDateTime startTime) {
        if (startTime == null) {
            log.warn("Start time is null");
            return 0;
        }

        return (int) startTime.until(LocalDateTime.now(), ChronoUnit.SECONDS);
    }
}