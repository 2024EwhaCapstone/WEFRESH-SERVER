package org.wefresh.wefresh_server.auth.controller;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wefresh.wefresh_server.auth.dto.request.UserLoginDto;
import org.wefresh.wefresh_server.auth.dto.response.JwtTokenDto;
import org.wefresh.wefresh_server.auth.service.AuthService;
import org.wefresh.wefresh_server.common.auth.annotation.UserId;
import org.wefresh.wefresh_server.common.auth.constant.AuthConstant;
import org.wefresh.wefresh_server.common.dto.ResponseDto;
import org.wefresh.wefresh_server.user.dto.response.UserTokenDto;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/signin")
    public ResponseEntity<ResponseDto<UserTokenDto>> signin(
            @NotBlank @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) final String providerToken,
            @Valid @RequestBody final UserLoginDto userLoginDto
    ) {
        return ResponseEntity.ok().body(ResponseDto.success(authService.signin(providerToken, userLoginDto)));
    }

//    @PostMapping("/auth/signin")
//    public ResponseEntity<ResponseDto<UserTokenDto>> signin(
//            @Valid @RequestBody final UserLoginDto userLoginDto
//    ) {
//        return ResponseEntity.ok().body(ResponseDto.success(authService.signin(userLoginDto)));
//    }

    @PostMapping("/auth/signout")
    public ResponseEntity<ResponseDto<Void>> signout(
            @UserId final Long userId
    ) {
        authService.signout(userId);
        return ResponseEntity.ok().body(ResponseDto.success());
    }

    @PostMapping("/auth/reissue")
    public ResponseEntity<ResponseDto<JwtTokenDto>> reissue(
            @NotBlank @RequestHeader(AuthConstant.AUTHORIZATION_HEADER) final String refreshToken
    ) {
        return ResponseEntity.ok().body(ResponseDto.success(authService.reissue(refreshToken)));
    }

    @DeleteMapping("/auth/withdrawal")
    public ResponseEntity<ResponseDto<Void>> withdraw(
            @UserId final Long userId,
            @Nullable @RequestHeader(value = AuthConstant.GOOGLE_WITHDRAW_HEADER, required = false) final String accessToken
    ) {
        authService.withdrawal(userId, accessToken);
        return ResponseEntity.ok().body(ResponseDto.success());
    }

}
