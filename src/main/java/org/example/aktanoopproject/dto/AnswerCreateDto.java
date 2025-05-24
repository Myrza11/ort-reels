package org.example.aktanoopproject.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerCreateDto {
    private String answer;
    private boolean isCorrect;
}
