package ru.alex.testcasebankapp.util.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class BadCredentialsExceptionHandler implements ExceptionHandlerStrategy{
    @Override
    public ProblemDetail execute(RuntimeException e) {
        log.error("password incorrect error, date error: {}", LocalDateTime.now());
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @Override
    public Class<? extends RuntimeException> getExceptionClass() {
        return BadCredentialsException.class;
    }
}
