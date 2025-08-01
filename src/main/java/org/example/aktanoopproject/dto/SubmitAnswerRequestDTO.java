package org.example.aktanoopproject.dto;

import lombok.Data;

@Data
public class SubmitAnswerRequestDTO {
    private Long sessionId;
    private Long questionId;
    private Long answerId;
}