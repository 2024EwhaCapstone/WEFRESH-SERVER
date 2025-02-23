package org.wefresh.wefresh_server.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wefresh.wefresh_server.user.domain.User;
import org.wefresh.wefresh_server.user.dto.request.NicknameUpdateDto;
import org.wefresh.wefresh_server.user.dto.response.UserDto;
import org.wefresh.wefresh_server.user.dto.response.UserNicknameDto;
import org.wefresh.wefresh_server.user.manager.UserEditor;
import org.wefresh.wefresh_server.user.manager.UserRetriever;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRetriever userRetriever;
    private final UserEditor userEditor;

    @Transactional(readOnly = true)
    public UserDto getUserInfo(final Long userId) {
        User user = userRetriever.findById(userId);
        return UserDto.from(user);
    }

    @Transactional
    public UserNicknameDto updateNickname(
            final Long userId,
            final NicknameUpdateDto nicknameUpdateDto
    ) {
        User user = userRetriever.findById(userId);
        userEditor.updateNickname(user, nicknameUpdateDto.nickname());

        return UserNicknameDto.from(user);
    }
}
