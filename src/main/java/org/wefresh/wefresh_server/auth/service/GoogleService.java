package org.wefresh.wefresh_server.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.wefresh.wefresh_server.auth.dto.SocialUserDto;
import org.wefresh.wefresh_server.common.auth.constant.AuthConstant;
import org.wefresh.wefresh_server.external.service.google.GoogleInfoClient;
import org.wefresh.wefresh_server.external.service.google.GoogleRevokeClient;
import org.wefresh.wefresh_server.external.service.google.GoogleTokenClient;
import org.wefresh.wefresh_server.external.service.google.dto.GoogleTokenDto;
import org.wefresh.wefresh_server.external.service.google.dto.GoogleuserDto;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleService {

    private final GoogleTokenClient googleTokenClient;
    private final GoogleInfoClient googleInfoClient;
    private final GoogleRevokeClient googleRevokeClient;

    @Value("${oauth.google.client-id}")
    private String clientId;

    @Value("${oauth.google.client-secret}")
    private String clientSecret;

    @Value("${oauth.google.redirect-uri}")
    private String redirectUri;

    public GoogleTokenDto getSocialToken(final String code) {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("code", URLDecoder.decode(code, StandardCharsets.UTF_8));
        requestBody.add("client_id", clientId);
        requestBody.add("client_secret", clientSecret);
        requestBody.add("redirect_uri", redirectUri);
        requestBody.add("grant_type", "authorization_code");

        return googleTokenClient.getToken(requestBody);
    }

    public SocialUserDto getSocialUserInfo(final String providerToken) {
        GoogleuserDto googleUserDto = googleInfoClient.getUserInformation(AuthConstant.BEARER_TOKEN_PREFIX + providerToken);
        return SocialUserDto.of(
                googleUserDto.sub(),
                googleUserDto.email(),
                googleUserDto.picture()
        );
    }

    public void revoke(final String accessToken) {
        googleRevokeClient.revokeToken("token=" + accessToken);
    }
}

