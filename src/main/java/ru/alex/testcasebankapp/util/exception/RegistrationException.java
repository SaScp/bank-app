package ru.alex.testcasebankapp.util.exception;

public class RegistrationException extends RuntimeException {
    public RegistrationException(String defaultMessage) {
        super(defaultMessage);
    }
}
