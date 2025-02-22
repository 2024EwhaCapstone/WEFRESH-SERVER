package org.wefresh.wefresh_server.user.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.wefresh.wefresh_server.common.exception.BusinessException;
import org.wefresh.wefresh_server.common.exception.code.UserErrorCode;
import org.wefresh.wefresh_server.user.domain.Provider;
import org.wefresh.wefresh_server.user.domain.User;
import org.wefresh.wefresh_server.user.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class UserRetriever {

    private final UserRepository userRepository;

    public boolean existsByProviderIdAndProvider(final String providerId, final Provider provider) {
        return userRepository.existsByProviderIdAndProvider(providerId, provider);
    }

    public User findByProviderIdAndProvider(final String providerId, Provider provider) {
        return userRepository.findByProviderIdAndProvider(providerId, provider)
                .orElseThrow(() -> new BusinessException(UserErrorCode.NOT_FOUND_USER));
    }
}
