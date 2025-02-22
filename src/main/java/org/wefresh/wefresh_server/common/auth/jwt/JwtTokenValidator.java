package org.wefresh.wefresh_server.common.auth.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.wefresh.wefresh_server.common.exception.BusinessException;
import org.wefresh.wefresh_server.common.exception.code.AuthErrorCode;

@Component
@RequiredArgsConstructor
public class JwtTokenValidator {

    private final JwtTokenProvider jwtTokenProvider;

    public void validateAccessToken(String accessToken) {
        try {
            jwtTokenProvider.getBody(accessToken);
        } catch (MalformedJwtException ex) {
            throw new BusinessException(AuthErrorCode.INVALID_TOKEN);
        } catch (ExpiredJwtException ex) {
            throw new BusinessException(AuthErrorCode.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException ex) {
            throw new BusinessException(AuthErrorCode.UNSUPPORTED_TOKEN);
        } catch (IllegalArgumentException ex) {
            throw new BusinessException(AuthErrorCode.EMPTY_TOKEN);
        }
    }

    public void equalsRefreshToken(String refreshToken, String storedRefreshToken) {
        if (!refreshToken.equals(storedRefreshToken)) {
            throw new BusinessException(AuthErrorCode.MISMATCH_REFRESH_TOKEN);
        }
    }
}

