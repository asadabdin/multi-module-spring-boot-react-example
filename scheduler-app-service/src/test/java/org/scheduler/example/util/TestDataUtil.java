package org.scheduler.example.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.List;

import static java.lang.ClassLoader.getSystemResourceAsStream;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;

@UtilityClass
public class TestDataUtil {

    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(new JavaTimeModule());
    }

    public static String readResourceToString(String resourceName) {
        try {
            return IOUtils.toString(requireNonNull(getSystemResourceAsStream(resourceName)), UTF_8);
        } catch (NullPointerException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T convertJsonFileTo(String jsonPath, Class<T> type) {
        return readValue(readResourceToString(jsonPath), type);
    }

    public static <T> T readValue(String json, Class<T> type) {
        try {
            return objectMapper.readValue(json, type);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> List<T> convertJsonFileToList(String jsonPath, Class<T> type) {
        try {
            return objectMapper.readValue(readResourceToString(jsonPath), objectMapper.getTypeFactory().constructCollectionType(List.class, type));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
