package ru.alex.testcasebankapp.util.exception.handler;

import org.springframework.http.ProblemDetail;


public interface ExceptionHandlerStrategy {
     ProblemDetail execute(RuntimeException e);
     Class<? extends RuntimeException> getExceptionClass();
}
