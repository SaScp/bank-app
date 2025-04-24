package ru.alex.testcasebankapp.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.alex.testcasebankapp.model.user.User;


import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    @Override
    @EntityGraph(attributePaths = {"emails", "phones", "accounts"})
    List<User> findAllById(Iterable<UUID> uuids);

    Optional<User> findByLogin(String login);

    @Query("select user from User user join user.emails e where :email = e.email")
    Optional<User> findByEmailsIn(@Param("email") String email);

    @Query("select user from User user join user.phones p where :phone = p.phone")
    Optional<User> findByPhone(@Param("phone") String phone);

    @Query("select user.id from User user where user.fullName like :name")
    List<UUID> findAllByFullNameIsLikeId(@Param("name") String name, Pageable pageable);

    @Query("select u.id from User u where u.dataOfBirth > :localDateTime")
   List<UUID> findAllByDataOfBirthGreaterThanId(@Param("localDateTime") LocalDateTime localDateTime, Pageable pageable);


    @Query("select user.id from User user")
    List<UUID> findAllId(Pageable pageable);
}
