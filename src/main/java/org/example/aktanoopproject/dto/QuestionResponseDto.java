package org.example.aktanoopproject.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QuestionResponseDto {
    private Long id;
    private String question; // base64
    private List<AnswerResponseDto> answers;
}
