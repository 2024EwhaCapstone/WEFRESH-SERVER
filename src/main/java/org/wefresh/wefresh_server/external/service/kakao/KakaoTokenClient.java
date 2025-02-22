package org.wefresh.wefresh_server.external.service.kakao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.wefresh.wefresh_server.common.auth.constant.AuthConstant;
import org.wefresh.wefresh_server.external.service.kakao.dto.KakaoTokenDto;

@FeignClient(name = "kakaoTokenClient", url = AuthConstant.KAKAO_TOKEN_URL)
public interface KakaoTokenClient {
    @PostMapping
    KakaoTokenDto getToken(
            @RequestParam("code") final String code,
            @RequestParam("client_id") final String clientId,
            @RequestParam("redirect_uri") final String redirectUri,
            @RequestParam("grant_type") final String grantType);
}
