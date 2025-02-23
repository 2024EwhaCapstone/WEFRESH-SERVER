package org.wefresh.wefresh_server.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NicknameUpdateDto(
        @NotBlank @Size(max = 10)
        String nickname
) {
}
