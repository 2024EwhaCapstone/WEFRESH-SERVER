package org.wefresh.wefresh_server.user.dto.response;

import org.wefresh.wefresh_server.auth.dto.response.JwtTokenDto;
import org.wefresh.wefresh_server.user.domain.User;

public record UserTokenDto(
        String name,
        JwtTokenDto jwtTokenDto
) {
    public static UserTokenDto of(User user, JwtTokenDto tokens) {
        return new UserTokenDto(
                user.getNickname(),
                tokens
        );
    }
}
