package ru.alex.testcasebankapp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.alex.testcasebankapp.model.entity.Logout;


@Repository
public interface LogoutRepository extends MongoRepository<Logout, String> {
}
