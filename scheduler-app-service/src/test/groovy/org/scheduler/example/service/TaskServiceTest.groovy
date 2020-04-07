package org.scheduler.example.service

import com.github.fge.jsonpatch.JsonPatch
import org.scheduler.example.configuration.PageProperty
import org.scheduler.example.domain.Task
import org.scheduler.example.model.TaskDTO
import org.scheduler.example.repository.TaskRepository
import org.scheduler.example.util.TestDataUtil
import spock.lang.Specification

import static java.time.LocalDateTime.now
import static java.util.UUID.randomUUID
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric
import static org.apache.commons.lang3.RandomUtils.nextInt
import static org.scheduler.example.domain.TaskStatus.*
import static org.scheduler.example.util.TestDataUtil.objectMapper

class TaskServiceTest extends Specification {

    def service;
    def pageProperty = Mock(PageProperty)
    def taskRepository = Mock(TaskRepository)

    void setup() {
        service = new TaskService(pageProperty, objectMapper, taskRepository)
    }

    def "Patch"() {
        given:
        def task = Task.builder()
                .dueDate(date)
                .status(status)
                .description(description)
                .priority(priority)
                .title(title)
                .build()
        def patch = TestDataUtil.readValue("[{ \"op\":\"add\", \"path\":\"/description\", \"value\": \"" + newDescription +"\" }]", JsonPatch.class)
        def patchedTask = service.applyPatchToTask(patch, task)

        when:
        TaskDTO taskDto = service.patch(randomUUID(), patch)

        then:
        1 * taskRepository.findById(_ as UUID) >> Optional.of(task)
        1 * taskRepository.save(_ as Task) >> patchedTask

        taskDto
        taskDto.dueDate == date
        taskDto.status == status
        taskDto.description == newDescription
        taskDto.priority == priority
        taskDto.title == title

        where:
        date    | status        | description                                   | priority          | title                             | newDescription
        now()   | OPEN          | "This Task is to do " + randomAlphabetic(5)   | nextInt(1, 10)    | "Task - " + randomAlphanumeric(5) | "My New Description 1"
        now()   | IN_PROCESS    | "This Task is to do " + randomAlphabetic(5)   | nextInt(1, 10)    | "Task - " + randomAlphanumeric(5) | "My New Description 2"
        now()   | CLOSED        | "This Task is to do " + randomAlphabetic(5)   | nextInt(1, 10)    | "Task - " + randomAlphanumeric(5) | "My New Description 3"
        now()   | CANCELLED     | "This Task is to do " + randomAlphabetic(5)   | nextInt(1, 10)    | "Task - " + randomAlphanumeric(5) | "My New Description 4"
        now()   | OPEN          | "This Task is to do " + randomAlphabetic(5)   | nextInt(1, 10)    | "Task - " + randomAlphanumeric(5) | "My New Description 5"

    }
}
