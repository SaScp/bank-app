package ru.alex.testcasebankapp.util.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;
import ru.alex.testcasebankapp.util.exception.TransactionNotFoundException;

@Component
public class TransactionNotFoundExceptionHandler implements ExceptionHandlerStrategy{
    @Override
    public ProblemDetail execute(RuntimeException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @Override
    public Class<? extends RuntimeException> getExceptionClass() {
        return TransactionNotFoundException.class;
    }
}
