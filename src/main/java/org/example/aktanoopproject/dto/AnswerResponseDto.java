package org.example.aktanoopproject.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerResponseDto {
    private Long id;
    private String answer;

    public AnswerResponseDto(Long id, String answer) {
        this.id = id;
        this.answer = answer;
    }
}
