package org.wefresh.wefresh_server.user.repository;

import org.springframework.data.repository.CrudRepository;
import org.wefresh.wefresh_server.user.domain.Token;

import java.util.Optional;

public interface TokenRepository extends CrudRepository<Token, Long> {

    Optional<Token> findByRefreshToken(String refreshToken);
}
