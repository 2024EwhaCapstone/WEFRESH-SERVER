package org.wefresh.wefresh_server.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wefresh.wefresh_server.user.domain.Provider;
import org.wefresh.wefresh_server.user.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByProviderIdAndProvider(String serialId, Provider provider);

    Optional<User> findByProviderIdAndProvider(String providerId, Provider provider);
}
