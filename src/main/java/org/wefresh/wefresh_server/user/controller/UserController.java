package org.wefresh.wefresh_server.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.wefresh.wefresh_server.common.auth.annotation.UserId;
import org.wefresh.wefresh_server.common.dto.ResponseDto;
import org.wefresh.wefresh_server.user.dto.request.NicknameUpdateDto;
import org.wefresh.wefresh_server.user.dto.response.UserDto;
import org.wefresh.wefresh_server.user.dto.response.UserNicknameDto;
import org.wefresh.wefresh_server.user.service.UserService;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users/me")
    public ResponseEntity<ResponseDto<UserDto>> getUserInfo(
            @UserId final Long userId
    ) {
        return ResponseEntity.ok().body(ResponseDto.success(userService.getUserInfo(userId)));
    }

    @PatchMapping("/users/me/nickname")
    public ResponseEntity<ResponseDto<UserNicknameDto>> updateName(
            @UserId final Long userId,
            @Valid @RequestBody final NicknameUpdateDto nicknameUpdateDto
    ) {
        return ResponseEntity.ok().body(ResponseDto.success(userService.updateNickname(userId, nicknameUpdateDto)));
    }
}
