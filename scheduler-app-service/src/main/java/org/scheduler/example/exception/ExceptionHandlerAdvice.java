package org.scheduler.example.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@ControllerAdvice
@Component("ServiceExceptionHandlerAdvice")
public class ExceptionHandlerAdvice {

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(NoSuchDataFoundException.class)
    public String handleNoSuchDataFoundException(NoSuchDataFoundException e) {
        log.error("Internal server error occurred: [{}]", e.getMessage(), e);
        return e.getMessage();
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(RuntimeException e) {
        log.error("Internal server error occurred: [{}]", e.getMessage(), e);
        return e.getMessage();
    }

    @ResponseBody
    @ResponseStatus(CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public String handleDataIntegrityViolationException(HttpServletResponse response, DataIntegrityViolationException e) {
        log.error("Data integrity violation exception, responding with [{}]: {}", CONFLICT, e.getMostSpecificCause().getMessage());
        return e.getMostSpecificCause().getMessage();
    }

    @ResponseBody
    @ResponseStatus(CONFLICT)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public FieldErrorDTO methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        return processFieldErrors(fieldErrors);
    }

    private FieldErrorDTO processFieldErrors(List<FieldError> fieldErrors) {
        FieldErrorDTO error = new FieldErrorDTO(BAD_REQUEST.value(), "validation error");
        for (FieldError fieldError: fieldErrors) {
            error.addFieldError(fieldError.getObjectName(), fieldError.getField(),
                    fieldError.getDefaultMessage(), fieldError.getRejectedValue(), fieldError.getCode());
        }
        return error;
    }

}
