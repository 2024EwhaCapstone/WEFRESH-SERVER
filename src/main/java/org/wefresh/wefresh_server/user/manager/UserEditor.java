package org.wefresh.wefresh_server.user.manager;

import org.springframework.stereotype.Component;
import org.wefresh.wefresh_server.user.domain.User;

@Component
public class UserEditor {

    public void updateNickname(
            final User user,
            final String nickname
    ) {
        user.updateNickname(nickname);
    }
}
