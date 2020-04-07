package org.scheduler.example.schedular;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.scheduler.example.domain.Task;
import org.scheduler.example.domain.TaskStatus;
import org.scheduler.example.repository.TaskRepository;
import org.scheduler.example.service.TaskService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static java.lang.String.format;
import static java.lang.String.join;
import static java.time.LocalDateTime.now;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.scheduler.example.domain.TaskStatus.OPEN;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "org.task.schedule.enabled")
public class TaskSchedulerService {

    private final TaskService taskService;

    @Scheduled(initialDelayString = "${org.task.schedule.initial-delay}",
            fixedDelayString = "${org.task.schedule.fixed-delay}")
    public void spawnNewTask() {
        Task task = Task.builder()
                .dueDate(now())
                .status(OPEN)
                .description("This Task is to do " + randomAlphabetic(5))
                .priority(nextInt(1, 10))
                .title("Task - " + randomAlphanumeric(5))
                .build();
        task = taskService.save(task);
        log.debug("New Task has created {}", task);
    }

}
