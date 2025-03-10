package com.piday.challenge.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TeamSecurityService {

    /**
     * Checks if the authenticated user is a member of the specified team.
     *
     * @param teamId The team ID to check
     * @return true if the user is a member of the team, false otherwise
     */
    public boolean isTeamMember(Long teamId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof TeamPrincipal)) {
            log.warn("Unauthorized access attempt to team ID: {}", teamId);
            return false;
        }

        TeamPrincipal principal = (TeamPrincipal) authentication.getPrincipal();
        return principal.getTeamId().equals(teamId);
    }

    /**
     * Gets the currently authenticated team ID.
     *
     * @return The team ID, or null if not authenticated as a team
     */
    public Long getCurrentTeamId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof TeamPrincipal)) {
            return null;
        }

        TeamPrincipal principal = (TeamPrincipal) authentication.getPrincipal();
        return principal.getTeamId();
    }
}