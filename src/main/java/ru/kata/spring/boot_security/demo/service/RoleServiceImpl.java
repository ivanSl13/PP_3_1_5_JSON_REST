package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.repositorie.RoleRepositories;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepositories roleRepositories;

    @Autowired
    public RoleServiceImpl(RoleRepositories roleRepositories) {
        this.roleRepositories = roleRepositories;
    }

    @Override
    @Transactional
    public void save(Role role) {
        roleRepositories.save(role);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepositories.findAll();
    }


}
