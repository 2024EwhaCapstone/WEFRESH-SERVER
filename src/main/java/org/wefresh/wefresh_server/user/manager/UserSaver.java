package org.wefresh.wefresh_server.user.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.wefresh.wefresh_server.user.domain.User;
import org.wefresh.wefresh_server.user.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class UserSaver {

    private final UserRepository userRepository;

    public User save(final User user){
        return userRepository.save(user);
    }

}
