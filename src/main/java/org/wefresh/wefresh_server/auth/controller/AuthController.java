package org.wefresh.wefresh_server.auth.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.wefresh.wefresh_server.auth.dto.request.UserLoginDto;
import org.wefresh.wefresh_server.auth.dto.response.JwtTokenDto;
import org.wefresh.wefresh_server.auth.service.AuthService;
import org.wefresh.wefresh_server.common.auth.annotation.UserId;
import org.wefresh.wefresh_server.common.auth.constant.AuthConstant;
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

    @PostMapping("/auth/signout")
    public ResponseEntity<Void> signout(
            @UserId final Long userId
    ) {
        authService.signout(userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/auth/reissue")
    public ResponseEntity<JwtTokenDto> reissue(
            @NotBlank @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) final String refreshToken
    ) {
        return ResponseEntity.ok(authService.reissue(refreshToken));
    }

}
