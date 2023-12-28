package com.expandapis.user.controller;

import com.expandapis.user.model.request.AuthRequest;
import com.expandapis.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/")
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @PostMapping("add")
    public ResponseEntity<String> add (@RequestBody AuthRequest authRequest){
        return userService.add(authRequest);
    }

    @PostMapping("authenticate")
    public ResponseEntity<String> authenticate (@RequestBody AuthRequest authRequest){
        return userService.authenticate(authRequest);
    }
}