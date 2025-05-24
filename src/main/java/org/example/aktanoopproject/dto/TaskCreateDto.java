package org.example.aktanoopproject.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class TaskCreateDto {
    private MultipartFile question;
    private List<AnswerCreateDto> answers;
}
