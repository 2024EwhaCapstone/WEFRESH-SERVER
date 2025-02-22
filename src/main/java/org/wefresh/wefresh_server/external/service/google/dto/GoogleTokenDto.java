package org.wefresh.wefresh_server.external.service.google.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GoogleTokenDto(
        String accessToken,
        String refreshToken
) {
    public static GoogleTokenDto of(String accessToken, String refreshToken) {
        return new GoogleTokenDto(accessToken, refreshToken);
    }

}
