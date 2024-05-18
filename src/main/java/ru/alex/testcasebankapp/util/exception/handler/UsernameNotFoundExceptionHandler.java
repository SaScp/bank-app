package ru.alex.testcasebankapp.util.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UsernameNotFoundExceptionHandler implements ExceptionHandlerStrategy{
    @Override
    public ProblemDetail execute(RuntimeException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @Override
    public Class<? extends RuntimeException> getExceptionClass() {
        return UsernameNotFoundException.class;
    }
}
