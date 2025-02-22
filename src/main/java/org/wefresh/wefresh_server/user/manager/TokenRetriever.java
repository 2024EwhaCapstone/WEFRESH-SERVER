package org.wefresh.wefresh_server.user.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.wefresh.wefresh_server.common.exception.BusinessException;
import org.wefresh.wefresh_server.common.exception.code.AuthErrorCode;
import org.wefresh.wefresh_server.user.domain.Token;
import org.wefresh.wefresh_server.user.repository.TokenRepository;

@Component
@RequiredArgsConstructor
public class TokenRetriever {

    private final TokenRepository tokenRepository;

    public Token findByRefreshToken(final String refreshToken) {
        return tokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new BusinessException(AuthErrorCode.NOT_FOUND_REFRESH_TOKEN));
    }
}
