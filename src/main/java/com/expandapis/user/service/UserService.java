package com.expandapis.user.service;

import com.expandapis.handler.exceptions.BadRequestException;
import com.expandapis.handler.exceptions.ForbiddenRequestException;
import com.expandapis.handler.exceptions.UserAlreadyExistsException;
import com.expandapis.handler.exceptions.UserNotFoundException;
import com.expandapis.user.model.entity.User;
import com.expandapis.user.model.request.AuthRequest;
import com.expandapis.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@Transactional
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtEncoder jwtEncoder;


    public ResponseEntity<String> add (AuthRequest authRequest){
        validateAuthRequest(authRequest);
        if (userRepository.existsByUsername(authRequest.username())) {
            throw new UserAlreadyExistsException();
        }

        User newUser = userRepository.save(User.builder()
                .username(authRequest.username())
                .password(passwordEncoder.encode(authRequest.password()))
                .build());

        return ResponseEntity.ok(generateJwtToken(newUser.getId(), newUser.getUsername()));
    }

    public ResponseEntity<String> authenticate(AuthRequest authRequest){
        validateAuthRequest(authRequest);
        return userRepository.findByUsername(authRequest.username())
                .map(user -> {
                    if (passwordEncoder.matches(authRequest.password(), user.getPassword())) {
                        return ResponseEntity.ok(generateJwtToken(user.getId(), user.getUsername()));
                    } else {
                        throw new ForbiddenRequestException();
                    }
                })
                .orElseThrow(UserNotFoundException::new);
    }


    private String generateJwtToken(Long id, String username){
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(now.plus(45, ChronoUnit.MINUTES))
                .subject(username)
                .claim("id", id)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    private void validateAuthRequest(AuthRequest  authRequest){
        if (authRequest == null ||  !StringUtils.hasText(authRequest.username()) || !StringUtils.hasText(authRequest.password())){
            throw new BadRequestException("Oops! Something went wrong!");
        }
    }
}
