package org.wefresh.wefresh_server.user.dto.response;

import org.wefresh.wefresh_server.user.domain.User;

public record UserNicknameDto(
        String nickname
) {
    public static UserNicknameDto from(User user) {
        return new UserNicknameDto(user.getNickname());
    }
}
