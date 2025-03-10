package com.piday.challenge.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TeamPrincipal {
    private Long teamId;
    private String teamName;
}