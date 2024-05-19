package ru.alex.testcasebankapp.util.exception.handler;

import io.lettuce.core.dynamic.annotation.Command;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;
import ru.alex.testcasebankapp.util.exception.InsufficientFundsException;

@Component
public class InsufficientFundsExceptionHandler implements ExceptionHandlerStrategy{
    @Override
    public ProblemDetail execute(RuntimeException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @Override
    public Class<? extends RuntimeException> getExceptionClass() {
        return InsufficientFundsException.class;
    }
}
