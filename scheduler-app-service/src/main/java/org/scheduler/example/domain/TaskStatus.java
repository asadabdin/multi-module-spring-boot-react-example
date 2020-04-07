package org.scheduler.example.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static java.util.Optional.ofNullable;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toMap;

public enum TaskStatus {
    OPEN,
    IN_PROCESS,
    CLOSED,
    CANCELLED;

    @Getter(onMethod = @__(@JsonValue))
    private String description;

    TaskStatus() {
        this.description = description(this);
    }

    private static Map<String, TaskStatus> descriptionMap =
            Arrays.stream(values())
                    .collect(
                            collectingAndThen(
                                    toMap(TaskStatus::description, identity())
                                    , Collections::unmodifiableMap
                            )
                    );

    private static String description(TaskStatus value) {
        return value.name().toLowerCase().replaceAll("_", "-");
    }

    @JsonCreator
    public static TaskStatus fromDescription(String description) {
        return ofNullable(description)
                .map(descriptionMap::get)
                .orElseThrow(() -> new IllegalArgumentException("Invalid TaskStatus: " + description));
    }
}
