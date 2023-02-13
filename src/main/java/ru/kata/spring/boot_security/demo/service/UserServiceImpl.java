package ru.kata.spring.boot_security.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositorie.UserRepositories;
import ru.kata.spring.boot_security.demo.util.UserNotFoundException;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepositories usersRepositories;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserServiceImpl(UserRepositories usersRepositories, @Lazy PasswordEncoder passwordEncoder) {
        this.usersRepositories = usersRepositories;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = findByUsername(username);
//        if (user == null) {
//            throw new UsernameNotFoundException(String.format("User '%s' not find", username));
//        }
//        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),
//                mapRoleToEnter(user.getRoles()));
        return usersRepositories.findByUsername(username);

    }

    private Collection<? extends GrantedAuthority> mapRoleToEnter(Collection<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());
    }


    @Override
    public User findByUsername(String username) {
        return usersRepositories.findByUsername(username);
    }

    @Override
    public List<User> getAllUsers() {
        return usersRepositories.findAll();
    }

    @Override
    public User getOneUser(int id) {

        return usersRepositories.findById(id).orElseThrow(UserNotFoundException::new);
    }

    @Override
    @Transactional
    public void updatUser(int id, User user) {
        User userUp = usersRepositories.findById(id).get();
        userUp.setId(id);
        if(user.getRoles().isEmpty()) {
            user.addRole(new Role(1,"USER_ROLE"));
        } else {
            userUp.setRoles(user.getRoles());
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userUp.setUsername(user.getUsername());
        userUp.setAge(user.getAge());
        userUp.setEmail(user.getEmail());
    }

    @Override
    @Transactional
    public void deleteUser(int id) {
        usersRepositories.deleteById(id);
    }

    @Override
    public User findById (int id) {
        return usersRepositories.findById(id).get();
    }

    @Transactional
    public void register(User user) {

        if(user.getRoles().isEmpty()) {
            user.addRole(new Role(1,"USER_ROLE"));
        }
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        usersRepositories.save(user);
        System.out.println("JSON new User: " + user.toString()+ "roles: "+user.getRoles().toString());
    }

}
