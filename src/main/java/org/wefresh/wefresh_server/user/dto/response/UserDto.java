package org.wefresh.wefresh_server.user.dto.response;

import org.wefresh.wefresh_server.user.domain.User;

public record UserDto(
        Long userId,
        String nickname,
        String profileImg
) {
    public static UserDto from(User user) {
        return new UserDto(
                user.getId(),
                user.getNickname(),
                user.getProfileImg()
        );
    }
}
