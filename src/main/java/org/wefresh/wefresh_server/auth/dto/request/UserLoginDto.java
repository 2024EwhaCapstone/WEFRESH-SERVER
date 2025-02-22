package org.wefresh.wefresh_server.auth.dto.request;

import jakarta.validation.constraints.NotNull;
import org.wefresh.wefresh_server.user.domain.Provider;

public record UserLoginDto(
        @NotNull
        Provider provider,
        @NotNull
        String code
) {
}
