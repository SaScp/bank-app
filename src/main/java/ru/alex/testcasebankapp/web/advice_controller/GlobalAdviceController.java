package ru.alex.testcasebankapp.web.advice_controller;

import ch.qos.logback.core.LogbackException;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.alex.testcasebankapp.util.exception.*;
import ru.alex.testcasebankapp.util.exception.handler.ExceptionHandlerStrategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalAdviceController {

    private Map<Class<? extends RuntimeException>, ExceptionHandlerStrategy> handlerStrategy;

    public GlobalAdviceController(List<ExceptionHandlerStrategy> strategyList) {
        handlerStrategy = new HashMap<>();
        for (var i : strategyList) {
            handlerStrategy.put(i.getExceptionClass(), i);
        }
    }

    @ExceptionHandler({
            SavedException.class,
            LoginException.class,
            RegistrationException.class,
            TransactionException.class,
            UpdateException.class,
            UsernameNotFoundException.class,
            BadCredentialsException.class,
            LastElementException.class,
            InsufficientFundsException.class
    })
    public ResponseEntity<ProblemDetail> exHandler(RuntimeException e) {
        ProblemDetail problemDetail = handlerStrategy.get(e.getClass()).execute(e);
        return ResponseEntity
                .status(problemDetail.getStatus())
                .body(problemDetail);
    }
}
