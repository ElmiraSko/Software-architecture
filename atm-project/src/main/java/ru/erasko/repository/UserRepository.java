package ru.erasko.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.erasko.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String username);
}
