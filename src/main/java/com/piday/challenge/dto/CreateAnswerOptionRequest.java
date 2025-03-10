package com.piday.challenge.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAnswerOptionRequest {

    @NotBlank(message = "Option text is required")
    @Size(max = 255, message = "Option text cannot exceed 255 characters")
    private String optionText;

    @NotNull(message = "isCorrect flag is required")
    private Boolean isCorrect;

    @NotNull(message = "Option order is required")
    private Integer optionOrder;
}
