package org.scheduler.example.exception;

import lombok.Data;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

@Data
public class FieldErrorDTO {

    private int status;
    private String message;
    private List<FieldError> fieldErrors;

    FieldErrorDTO(int status, String message) {
        this.status = status;
        this.message = message;
        fieldErrors = new ArrayList<>();
    }

    public void addFieldError(String objectName, String path, String message, Object rejectedValue, String errorCode) {
        FieldError error = new FieldError(objectName, path, rejectedValue, true,
                new String[]{errorCode}, null, message);
        fieldErrors.add(error);
    }

}
