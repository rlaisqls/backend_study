package com.practice.board.service.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.oauth2.sdk.TokenResponse;
import com.practice.board.dto.response.TokensResponse;
import com.practice.board.entity.refeshToken.RefreshToken;
import com.practice.board.entity.refeshToken.RefreshTokenRepository;
import com.practice.board.entity.user.Authority;
import com.practice.board.entity.user.GoogleUser;
import com.practice.board.entity.user.User;
import com.practice.board.entity.user.UserRepository;
import com.practice.board.exception.UserAlreadyExistException;
import com.practice.board.exception.UserNotFoundException;
import com.practice.board.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuthUserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User>{

    private final UserRepository userRepository;
    private final LoginService loginService;

    //google 로그인
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        User user = GoogleUser.builder()
                .oauthId(String.valueOf(attributes.get("sub")))
                .username((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .authority(Authority.valueOf("ROLE_USER"))
                .activated(true)
                .build();

        if(userRepository.findByEmail(user.getEmail()).isPresent()) loginService.login(user);
        else userRepository.save(user);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getAuthority().toString())),
                attributes,
                userNameAttributeName);
    }
}