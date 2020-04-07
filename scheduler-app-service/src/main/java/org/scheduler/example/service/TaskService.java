package org.scheduler.example.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.google.common.annotations.VisibleForTesting;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.scheduler.example.configuration.PageProperty;
import org.scheduler.example.domain.Task;
import org.scheduler.example.exception.NoSuchDataFoundException;
import org.scheduler.example.model.PageRequestDTO;
import org.scheduler.example.model.TaskDTO;
import org.scheduler.example.model.TaskSearchCriteria;
import org.scheduler.example.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

import static java.util.Optional.of;
import static org.scheduler.example.utils.ConverterUtils.mapProperties;
import static org.scheduler.example.utils.PageUtils.pageable;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final PageProperty pageProperty;
    private final ObjectMapper objectMapper;
    private final TaskRepository taskRepository;


    public Page<TaskDTO> search(@NonNull final PageRequestDTO<TaskSearchCriteria> request) {
        log.debug("Getting rates by {}", request);
        Specification<Task> specification = of(request.getCriteria())
                .filter(criteria -> Objects.nonNull(criteria.getDueDateTo()))
                .map(TaskSpecification::getTaskBetweenDate)
                .orElseGet(() -> TaskSpecification.getTaskDueDateFrom(request.getCriteria()));
        return taskRepository
                .findAll(specification, pageable(request))
                .map(this::convertToTaskDTO);
    }

    public Page<TaskDTO> getLatestSet() {
        return taskRepository
                .findAll(pageable(pageProperty.getPage(), pageProperty.getSize(), pageProperty.getSort()))
                .map(this::convertToTaskDTO);
    }

    public TaskDTO patch(@NonNull UUID id, @NonNull JsonPatch patch) {
        return taskRepository.findById(id)
                .map(task -> applyPatchToTask(patch, task))
                .map(this::save)
                .map(this::convertToTaskDTO)
                .orElseThrow(() -> {
                    log.info("No Such Task found for id : {}", id);
                    return new NoSuchDataFoundException(String.format("No Such Task for id: %s ", id));
                });
    }

    public Task save(@NonNull Task task) {
        log.debug("Task received to save {}", task);
        return taskRepository.save(task);
    }

    public void delete(@NonNull UUID id) {
        log.info("Deleting Task of id: {}", id);
        taskRepository.deleteById(id);
    }

    @SneakyThrows
    private Task applyPatchToTask(@NonNull JsonPatch patch, @NonNull Task task)  {
        JsonNode patched = patch.apply(objectMapper.convertValue(task, JsonNode.class));
        return objectMapper.treeToValue(patched, Task.class);
    }

    private TaskDTO convertToTaskDTO(Task source) {
        return mapProperties(source, TaskDTO.class);
    }

    public TaskDTO getById(@NonNull UUID id) {
        return taskRepository.findById(id)
                .map(this::convertToTaskDTO)
                .orElseThrow(() -> new NoSuchDataFoundException(String.format("No such Task found for id %s", id)));
    }
}
