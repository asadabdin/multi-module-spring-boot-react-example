package org.scheduler.example.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

import static com.fasterxml.jackson.databind.DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY;

@Configuration
@RequiredArgsConstructor
public class SchedulerAppConfiguration {

    private final ObjectMapper objectMapper;

    @PostConstruct
    public void postConstruct() {
        objectMapper.enable(ACCEPT_SINGLE_VALUE_AS_ARRAY);
        objectMapper.registerModule(new Jdk8Module());
    }
}
