package ru.alex.testcasebankapp.util.exception.handler;

import org.springframework.http.ProblemDetail;
import ru.alex.testcasebankapp.model.response.ErrorResponse;

public interface ExceptionHandlerStrategy {
     ProblemDetail execute(RuntimeException e);
     Class<? extends RuntimeException> getExceptionClass();
}
