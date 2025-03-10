package com.piday.challenge.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerOptionDTO {

    private Long id;
    private String optionText;
    private Integer optionOrder;
    // isCorrect is not included to prevent cheating
}