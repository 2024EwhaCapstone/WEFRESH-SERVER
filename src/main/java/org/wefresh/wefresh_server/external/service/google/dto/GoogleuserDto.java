package org.wefresh.wefresh_server.external.service.google.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GoogleuserDto(
       String sub,
       String email,
       String picture
) {
}
