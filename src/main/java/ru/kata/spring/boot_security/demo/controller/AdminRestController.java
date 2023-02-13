package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.util.UserErrorResponse;
import ru.kata.spring.boot_security.demo.util.UserNotCreatedException;
import ru.kata.spring.boot_security.demo.util.UserNotFoundException;
import ru.kata.spring.boot_security.demo.util.UserNotUpdateException;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/users")
public class AdminRestController {

    private final UserService userService;
    @Autowired
    public AdminRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> response = userService.getAllUsers();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public User getRestUserById (@PathVariable("id") int id) {
        return userService.getOneUser(id);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError f : fieldErrors) {
                errorMsg.append(f.getField())
                        .append(" - ")
                        .append(f.getDefaultMessage())
                        .append(";");
            }
            throw new UserNotCreatedException(errorMsg.toString());
        }

        userService.register(user);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }
    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid User user,
                                             @PathVariable("id") int id, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError f : fieldErrors) {
                errorMsg.append(f.getField())
                        .append(" - ")
                        .append(f.getDefaultMessage())
                        .append(";");
            }
            throw new UserNotUpdateException(errorMsg.toString());
        }
        userService.updatUser(id,user);
        return ResponseEntity.ok(HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable("id") int id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException (UserNotFoundException userNotFoundException) {
        UserErrorResponse userErrorResponse = new UserErrorResponse("User with this id not found",
                System.currentTimeMillis());
        return new ResponseEntity<>(userErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException (UserNotCreatedException userNotCreatedException) {
        UserErrorResponse userErrorResponse = new UserErrorResponse(userNotCreatedException.getMessage(),
                System.currentTimeMillis());
        return new ResponseEntity<>(userErrorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException (UserNotUpdateException userNotUpdateException) {
        UserErrorResponse userErrorResponse = new UserErrorResponse(userNotUpdateException.getMessage(),
                System.currentTimeMillis());
        return new ResponseEntity<>(userErrorResponse, HttpStatus.BAD_REQUEST);
    }

}
