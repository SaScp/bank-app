package ru.alex.testcasebankapp.util.exception;

public class TransactionException extends RuntimeException{
    public TransactionException(String message) {
        super(message);
    }
}
