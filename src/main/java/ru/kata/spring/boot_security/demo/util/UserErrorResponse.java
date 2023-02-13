package ru.kata.spring.boot_security.demo.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserErrorResponse {
    private  String massage;
    private long time;
}
