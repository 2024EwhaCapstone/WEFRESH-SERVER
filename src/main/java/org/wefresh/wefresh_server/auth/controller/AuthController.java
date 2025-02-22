package org.wefresh.wefresh_server.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.wefresh.wefresh_server.auth.dto.request.UserLoginDto;
import org.wefresh.wefresh_server.auth.service.AuthService;
import org.wefresh.wefresh_server.user.dto.response.UserTokenDto;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/signin")
    public ResponseEntity<UserTokenDto> signin(
            @Valid @RequestBody final UserLoginDto userLoginDto
    ) {
        return ResponseEntity.ok(authService.signin(userLoginDto));
    }

}
