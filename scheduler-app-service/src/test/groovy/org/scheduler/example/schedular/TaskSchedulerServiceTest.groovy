package org.scheduler.example.schedular

import org.scheduler.example.domain.Task
import org.scheduler.example.service.TaskService
import spock.lang.Specification

import static java.time.LocalDateTime.now
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric
import static org.apache.commons.lang3.RandomUtils.nextInt
import static org.scheduler.example.domain.TaskStatus.CANCELLED
import static org.scheduler.example.domain.TaskStatus.CLOSED
import static org.scheduler.example.domain.TaskStatus.IN_PROCESS
import static org.scheduler.example.domain.TaskStatus.OPEN

class TaskSchedulerServiceTest extends Specification {

    def service
    def taskService = Mock(TaskService)

    void setup() {
        service = new TaskSchedulerService(taskService)
    }

    def "SpawnNewTask"() {
        given:
        def task = Task.builder()
                .dueDate(date)
                .status(status)
                .description(description)
                .priority(priority)
                .title(title)
                .build()

        when:
        service.spawnNewTask()

        then:
        1 * taskService.save(_ as Task) >> task

        where:
        date    | status        | description                                   | priority          | title
        now()   | OPEN          | "This Task is to do " + randomAlphabetic(5)   | nextInt(1, 10)    | "Task - " + randomAlphanumeric(5)
        now()   | IN_PROCESS    | "This Task is to do " + randomAlphabetic(5)   | nextInt(1, 10)    | "Task - " + randomAlphanumeric(5)
        now()   | CLOSED        | "This Task is to do " + randomAlphabetic(5)   | nextInt(1, 10)    | "Task - " + randomAlphanumeric(5)
        now()   | CANCELLED     | "This Task is to do " + randomAlphabetic(5)   | nextInt(1, 10)    | "Task - " + randomAlphanumeric(5)
        now()   | OPEN          | "This Task is to do " + randomAlphabetic(5)   | nextInt(1, 10)    | "Task - " + randomAlphanumeric(5)
    }
}
