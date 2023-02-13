package ru.kata.spring.boot_security.demo.service;





import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {

    User findByUsername (String username);
    public void register(User user);

    List<User> getAllUsers();
//    void addUser(User user);
    User getOneUser(int id);
    void updatUser(int id, User user);
    void deleteUser(int id);
    public User findById (int id);


}
