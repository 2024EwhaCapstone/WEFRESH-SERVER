package org.wefresh.wefresh_server.external.service.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Profile(
        @JsonProperty("profile_image_url")
        String profileImageUrl
) {
}
