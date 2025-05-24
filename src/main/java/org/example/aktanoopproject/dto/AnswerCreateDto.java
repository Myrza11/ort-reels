package org.example.aktanoopproject.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerCreateDto {
    private String answer;
    @JsonProperty("isCorrect")

    private boolean isCorrect;

}
