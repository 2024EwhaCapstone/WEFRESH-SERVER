package org.wefresh.wefresh_server.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wefresh.wefresh_server.auth.dto.SocialUserDto;
import org.wefresh.wefresh_server.auth.dto.request.UserLoginDto;
import org.wefresh.wefresh_server.auth.dto.response.JwtTokenDto;
import org.wefresh.wefresh_server.bookmark.manager.BookmarkRemover;
import org.wefresh.wefresh_server.common.auth.jwt.JwtTokenProvider;
import org.wefresh.wefresh_server.common.exception.BusinessException;
import org.wefresh.wefresh_server.common.exception.code.AuthErrorCode;
import org.wefresh.wefresh_server.external.service.google.dto.GoogleTokenDto;
import org.wefresh.wefresh_server.external.service.kakao.dto.KakaoTokenDto;
import org.wefresh.wefresh_server.food.manager.FoodRemover;
import org.wefresh.wefresh_server.todayRecipe.domain.TodayRecipe;
import org.wefresh.wefresh_server.todayRecipe.manager.TodayRecipeRemover;
import org.wefresh.wefresh_server.user.domain.Provider;
import org.wefresh.wefresh_server.user.domain.Token;
import org.wefresh.wefresh_server.user.domain.User;
import org.wefresh.wefresh_server.user.dto.response.UserTokenDto;
import org.wefresh.wefresh_server.user.manager.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final KakaoService kakaoService;
    private final GoogleService googleService;
    private final JwtTokenProvider jwtTokenProvider;

    private final UserSaver userSaver;
    private final UserRetriever userRetriever;

    private final TokenSaver tokenSaver;
    private final TokenRetriever tokenRetriever;
    private final TokenRemover tokenRemover;

    private final BookmarkRemover bookmarkRemover;
    private final FoodRemover foodRemover;
    private final TodayRecipeRemover todayRecipeRemover;
    private final UserRemover userRemover;

    @Transactional
    public UserTokenDto signin(final UserLoginDto userLoginDto) {
        SocialUserDto socialUserDto = getSocialInfo(userLoginDto);
        User user = loadOrCreateUser(userLoginDto.provider(), socialUserDto);
        JwtTokenDto tokens = jwtTokenProvider.issueTokens(user.getId());
        saveToken(user.getId(), tokens);
        return UserTokenDto.of(user, tokens);
    }

    @Transactional
    public void signout(final Long userId) {
        tokenRemover.removeById(userId);
    }

    @Transactional
    public JwtTokenDto reissue(final String refreshToken) {
        Long userId;
        try{
            userId = jwtTokenProvider.getUserIdFromJwt(refreshToken);
        } catch (Exception e) {
            throw new BusinessException(AuthErrorCode.INVALID_TOKEN);
        }
        Token token = tokenRetriever.findByRefreshToken(refreshToken);

        if(!userId.equals(token.getId())) {
            throw new BusinessException(AuthErrorCode.INVALID_TOKEN);
        }

        JwtTokenDto tokens = jwtTokenProvider.issueTokens(userId);
        saveToken(userId, tokens);
        return tokens;
    }

    @Transactional
    public void withdrawal(final Long userId, final String accessToken) {
        User user = userRetriever.findById(userId);

        if(user.getProvider() == Provider.KAKAO) {
            kakaoService.unlinkKakaoUser(user.getProviderId());
        } else if(user.getProvider() == Provider.GOOGLE) {
            googleService.revoke(accessToken);
        } else {
            throw new BusinessException(AuthErrorCode.INVALID_PROVIDER);
        }
        deleteUser(user);
    }

    private SocialUserDto getSocialInfo(final UserLoginDto userLoginDto) {
        if (userLoginDto.provider().toString().equals("KAKAO")){
            KakaoTokenDto kakaoTokenDto = kakaoService.getSocialToken(userLoginDto.code());
            System.out.println("kakaoTokenDto.accessToken() = " + kakaoTokenDto.accessToken());
            return kakaoService.getSocialUserInfo(kakaoTokenDto.accessToken());
        } else if (userLoginDto.provider().toString().equals("GOOGLE")){
            GoogleTokenDto googleTokenDto = googleService.getSocialToken(userLoginDto.code());
            System.out.println("googleTokenDto.accessToken() = " + googleTokenDto.accessToken());
            return googleService.getSocialUserInfo(googleTokenDto.accessToken());
        } else {
            throw new BusinessException(AuthErrorCode.INVALID_PROVIDER);
        }
    }

    private User loadOrCreateUser(final Provider provider, final SocialUserDto socialUserDto){
        boolean isRegistered = userRetriever.existsByProviderIdAndProvider(socialUserDto.platformId(), provider);

        if (!isRegistered){
            User newUser = User.builder()
                    .provider(provider)
                    .providerId(socialUserDto.platformId())
                    .email(socialUserDto.email())
                    .profileImg(socialUserDto.profileImg())
                    .build();

            userSaver.save(newUser);
        }

        return userRetriever.findByProviderIdAndProvider(socialUserDto.platformId(), provider);
    }

    private void saveToken(final Long userId, final JwtTokenDto tokens) {
        tokenSaver.save(
                Token.builder()
                        .id(userId)
                        .refreshToken(tokens.refreshToken())
                        .build()
        );
    }

    private void deleteUser(final User user) {
        bookmarkRemover.deleteByUserId(user.getId());
        foodRemover.deleteByUserId(user.getId());
        todayRecipeRemover.deleteByUserId(user.getId());

        userRemover.deleteById(user.getId());
    }


}
