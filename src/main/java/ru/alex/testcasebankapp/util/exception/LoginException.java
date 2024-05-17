package ru.alex.testcasebankapp.util.exception;

public class LoginException extends RuntimeException{
    public LoginException(String message) {
        super(message);
    }
}
