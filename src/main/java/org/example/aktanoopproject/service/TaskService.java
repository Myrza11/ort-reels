package org.example.aktanoopproject.service;

import org.example.aktanoopproject.dto.*;
import org.example.aktanoopproject.model.Answer;
import org.example.aktanoopproject.model.Task;
import org.example.aktanoopproject.model.User;
import org.example.aktanoopproject.repository.AnswerRepository;
import org.example.aktanoopproject.repository.TaskRepository;
import org.example.aktanoopproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable; // ✅

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AnswerRepository answerRepository;



    public void createTask(TaskCreateDto dto) {
        if (dto.getAnswers().size() != 4) {
            throw new IllegalArgumentException("Должно быть ровно 4 ответа!");
        }

        // Проверка, что ровно 1 правильный ответ
        long correctCount = dto.getAnswers().stream()
                .filter(answer -> answer.isCorrect() == true)
                .count();
        System.out.println(correctCount);
        System.out.println("вот сколько правильных ответов");
        if (correctCount != 1) {
            throw new IllegalArgumentException("Должен быть ровно 1 правильный ответ!");
        }

        // Создаем новый таск
        Task task = new Task();
        try {
            task.setQuestion(dto.getQuestion().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Добавляем ответы
        for (AnswerCreateDto answerDto : dto.getAnswers()) {
            Answer answer = new Answer();
            answer.setAnswer(answerDto.getAnswer());
            answer.setCorrect(answerDto.isCorrect());
            answer.setTask(task);
            task.getAnswers().add(answer);
        }

        // Сохраняем в репозиторий
        taskRepository.save(task);
    }


    public List<TaskResponseDto> getNewTasksForUser(User user, int limit) {

        Set<Task> usedTasks = user.getUsedTasks();
        if (usedTasks == null) {
            usedTasks = Collections.emptySet();
        }
        List<Task> newTasks = taskRepository.findNewTasksForUser(usedTasks, (Pageable) PageRequest.of(0, limit));

        if (newTasks.isEmpty()) {
            List<Task> allTasks = taskRepository.findAll(PageRequest.of(0, limit)).getContent();

            user.getUsedTasks().clear();

            newTasks = allTasks;
        }

        user.getUsedTasks().addAll(newTasks);
        userRepository.save(user);

        return newTasks.stream()
                .map(task -> {
                    TaskResponseDto dto = new TaskResponseDto();
                    dto.setId(task.getId());
                    dto.setQuestion(Base64.getEncoder().encodeToString(task.getQuestion()));
                    dto.setAnswers(
                            task.getAnswers().stream()
                                    .map(a -> new AnswerResponseDto(a.getAnswer()))
                                    .collect(Collectors.toList())
                    );
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public boolean checkAnswer(Long id) {
        Optional<Answer> answer = answerRepository.getAnswerById(id);
        if (!answer.isPresent()) {
            throw new RuntimeException("There is no such answer");
        }
        return answer.get().isCorrect();
    }

}
