package ru.alex.testcasebankapp.util.exception;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(String insufficientFunds) {
        super(insufficientFunds);
    }
}
