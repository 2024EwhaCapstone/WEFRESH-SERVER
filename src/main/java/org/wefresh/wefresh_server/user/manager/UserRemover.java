package org.wefresh.wefresh_server.user.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.wefresh.wefresh_server.user.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class UserRemover {

    private final UserRepository userRepository;

    public void deleteById(final Long id) {
        userRepository.deleteById(id);
    }
}
