package ru.alex.testcasebankapp.util.exception;

public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException(String notFound) {
        super(notFound);
    }
}
