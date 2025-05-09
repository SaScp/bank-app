package ru.alex.testcasebankapp.util.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;
import ru.alex.testcasebankapp.util.exception.UpdateException;

import java.time.LocalDateTime;

@Slf4j
@Component
public class UpdateExceptionHandler implements ExceptionHandlerStrategy{
    @Override
    public ProblemDetail execute(RuntimeException e) {
        log.error("update error, date error: {}", LocalDateTime.now());
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @Override
    public Class<? extends RuntimeException> getExceptionClass() {
        return UpdateException.class;
    }
}
