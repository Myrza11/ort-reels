package org.example.aktanoopproject.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AnswerResponseDto {
    private String answer;

    public AnswerResponseDto(String answer) {
        this.answer = answer;
    }
}

