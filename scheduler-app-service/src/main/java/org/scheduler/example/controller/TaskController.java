package org.scheduler.example.controller;

import com.github.fge.jsonpatch.JsonPatch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.scheduler.example.model.PageRequestDTO;
import org.scheduler.example.model.TaskDTO;
import org.scheduler.example.model.TaskSearchCriteria;
import org.scheduler.example.service.TaskService;
import org.scheduler.example.validator.TaskSearchCriteriaValidator;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/v1/tasks")
public class TaskController {

    private final TaskService taskService;
    private final TaskSearchCriteriaValidator taskSearchCriteriaValidator;

    @InitBinder
    @SuppressWarnings("rawtypes")
    public void initBinder(WebDataBinder webDataBinder) {
        if (webDataBinder.getTarget() instanceof PageRequestDTO &&
                ((PageRequestDTO) webDataBinder.getTarget()).getCriteria() instanceof TaskSearchCriteria) {
            webDataBinder.addValidators(taskSearchCriteriaValidator);
        }
    }

    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    public TaskDTO get(@PathVariable("id") UUID id) {
        return taskService.getById(id);
    }

    @PostMapping(path = "/search", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public Page<TaskDTO> search(@Valid @RequestBody PageRequestDTO<TaskSearchCriteria> request) {
        log.info("getting Task with {}", request);
        return taskService.search(request);
    }

    @GetMapping(path = "/latest", produces = APPLICATION_JSON_VALUE)
    public Page<TaskDTO> latest() {
        log.info("getting latest Task ");
        return taskService.getLatestSet();
    }

    @PatchMapping(path = "/{id}", consumes = "application/json-patch+json", produces = APPLICATION_JSON_VALUE)
    public TaskDTO patch(@PathVariable("id") UUID id, @Valid @RequestBody final JsonPatch patch) {
        return taskService.patch(id, patch);
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable("id") UUID id) {
        taskService.delete(id);
    }
}
