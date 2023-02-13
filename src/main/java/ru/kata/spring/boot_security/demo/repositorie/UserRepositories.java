package ru.kata.spring.boot_security.demo.repositorie;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.User;


@Repository
public interface UserRepositories extends JpaRepository<User,Integer> {
    User findByUsername (String username);

}
