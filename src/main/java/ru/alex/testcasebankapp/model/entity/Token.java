package ru.alex.testcasebankapp.model.entity;

import java.time.Instant;
import java.util.List;

public record Token(String id, String subject, List<String> authorities, Instant createAt, Instant expireAt) {
}
