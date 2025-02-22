package org.wefresh.wefresh_server.user.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.wefresh.wefresh_server.user.repository.TokenRepository;

@Component
@RequiredArgsConstructor
public class TokenRemover {

    private final TokenRepository tokenRepository;

    public void removeById(final Long id) {
        tokenRepository.deleteById(id);
    }
}
