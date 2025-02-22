package org.wefresh.wefresh_server.common.auth.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthConstant {

    public static final String USER_ID = "userId";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_TOKEN_PREFIX = "Bearer ";
    public static final String CHARACTER_ENCODING_UTF8 = "utf-8";

    // KAKAO
    public static final String KAKAO_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    public static final String KAKAO_URL = "https://kapi.kakao.com";

}
