package com.shopme.admin.controller;

import com.shopme.admin.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRestController {

    @Autowired
    public UserService userService;

    @PostMapping("/users/check_email")
    public String checkDuplicateEmail(@Param("email") String email, @Param("id") Integer id) {
        return userService.isEmailUnique(id, email) ? "OK" : "Duplicated";
    }

}
