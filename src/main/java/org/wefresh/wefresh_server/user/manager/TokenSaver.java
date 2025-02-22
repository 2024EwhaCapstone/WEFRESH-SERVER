package org.wefresh.wefresh_server.user.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.wefresh.wefresh_server.user.domain.Token;
import org.wefresh.wefresh_server.user.repository.TokenRepository;

@Component
@RequiredArgsConstructor
public class TokenSaver {

    private final TokenRepository tokenRepository;

    public void save(final Token token){
        tokenRepository.save(token);
    }

}
