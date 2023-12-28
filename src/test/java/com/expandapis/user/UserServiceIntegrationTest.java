package com.expandapis.user;

import com.expandapis.handler.exceptions.ForbiddenRequestException;
import com.expandapis.handler.exceptions.UserAlreadyExistsException;
import com.expandapis.handler.exceptions.UserNotFoundException;
import com.expandapis.user.model.request.AuthRequest;
import com.expandapis.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Test
    void testAddUser() {
        AuthRequest authRequest = new AuthRequest("newUser", "password123");
        ResponseEntity<String> response = userService.add(authRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testAddExistingUser() {
        AuthRequest existingUser = new AuthRequest("existingUser", "password456");
        userService.add(existingUser);

        AuthRequest duplicateUser = new AuthRequest("existingUser", "password789");
        assertThrows(UserAlreadyExistsException.class, () -> userService.add(duplicateUser));
    }

    @Test
    void testAuthenticateUser() {
        AuthRequest authRequest = new AuthRequest("userToAuthenticate", "password123");
        userService.add(authRequest);

        ResponseEntity<String> response = userService.authenticate(authRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testAuthenticateNonexistentUser() {
        AuthRequest nonExistentUser = new AuthRequest("nonExistentUser", "password123");
        assertThrows(UserNotFoundException.class, () -> userService.authenticate(nonExistentUser));
    }

    @Test
    void testAuthenticateInvalidPassword() {
        AuthRequest authRequest = new AuthRequest("userWithInvalidPassword", "correctPassword");
        userService.add(authRequest);

        AuthRequest invalidPassword = new AuthRequest("userWithInvalidPassword", "wrongPassword");
        assertThrows(ForbiddenRequestException.class, () -> userService.authenticate(invalidPassword));
    }
}
