package org.wefresh.wefresh_server.auth.dto;

public record SocialUserDto(
        String platformId,
        String email,
        String profileImg
) {

    public static SocialUserDto of(String platformId, String email, String profileImg) {
        return new SocialUserDto(platformId, email, profileImg);
    }
}
