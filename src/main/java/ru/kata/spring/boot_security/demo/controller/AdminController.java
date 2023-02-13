package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.List;

@Controller
public class AdminController {

    private final RoleService roleService;
    private final UserService userService;

    @Autowired
    public AdminController(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    @RequestMapping("/index")
    public List<User> getAllUsers(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("newUser", new User());
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("roles",roleService.getAllRoles());
        return userService.getAllUsers();
    }


    @DeleteMapping("/admin/{id}")
    public String delete (@PathVariable("id") int id) {
        userService.deleteUser(id);
        return "redirect:/index";
    }

    @PatchMapping("/admin/{id}")
    public String update (@ModelAttribute("user") User user, @PathVariable("id") int id) {
        userService.updatUser(id, user);
        return "redirect:/index";
    }

    @PostMapping("/add")
    public String create(@ModelAttribute("user") User user) {
        userService.register(user);
        return "redirect:/index";
    }
}
