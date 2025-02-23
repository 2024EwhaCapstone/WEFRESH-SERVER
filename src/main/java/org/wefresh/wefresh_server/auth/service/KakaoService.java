package org.wefresh.wefresh_server.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.wefresh.wefresh_server.auth.dto.SocialUserDto;
import org.wefresh.wefresh_server.common.auth.constant.AuthConstant;
import org.wefresh.wefresh_server.external.service.kakao.KakaoFeignClient;
import org.wefresh.wefresh_server.external.service.kakao.KakaoTokenClient;
import org.wefresh.wefresh_server.external.service.kakao.dto.KakaoTokenDto;
import org.wefresh.wefresh_server.external.service.kakao.dto.KakaoUserDto;

@Service
@RequiredArgsConstructor
public class KakaoService {

    private final KakaoTokenClient kakaoTokenClient;
    private final KakaoFeignClient kakaoFeignClient;

    @Value("${oauth.kakao.client-id}")
    private String clientId;

    @Value("${oauth.kakao.redirect-uri}")
    private String redirectUri;

    @Value("${oauth.kakao.admin-key}")
    private String adminKey;

    public KakaoTokenDto getSocialToken(
            final String code
    ) {
        return kakaoTokenClient.getToken(
                code,
                clientId,
                redirectUri,
                "authorization_code"
        );
    }

    public SocialUserDto getSocialUserInfo(final String providerToken) {
        KakaoUserDto kakaoUserDto = kakaoFeignClient.getUserInformation(AuthConstant.BEARER_TOKEN_PREFIX + providerToken);
        return SocialUserDto.of(
                kakaoUserDto.id().toString(),
                kakaoUserDto.kakaoAccount().email(),
                kakaoUserDto.kakaoAccount().profile().profileImageUrl());
    }

    public void unlinkKakaoUser(final String providerId) {
        kakaoFeignClient.unlinkUser(
                AuthConstant.GRANT_TYPE + adminKey,
                AuthConstant.TARGET_ID_TYPE,
                Long.valueOf(providerId)
        );
    }
}
