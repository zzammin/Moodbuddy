package moodbuddy.moodbuddy.domain.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moodbuddy.moodbuddy.domain.user.service.KakaoService;
import moodbuddy.moodbuddy.global.common.response.ApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moodbuddy.moodbuddy.domain.user.dto.response.LoginResponseDto;
import moodbuddy.moodbuddy.domain.user.service.KakaoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/user")
public class OAuthController {

    private final KakaoService kakaoService;

    @Value("${kakao.api_key}") String kakaoApiKey;

    @Value("${kakao.redirect_url}") String kakaoRedirectUrl;

    @GetMapping("/login")
    public RedirectView loginRedirect() {
        String kakaoLoginUrl = String.format(
                "https://kauth.kakao.com/oauth/authorize?client_id=%s&redirect_uri=%s&response_type=code",
                kakaoApiKey,
                kakaoRedirectUrl
        );

        return new RedirectView(kakaoLoginUrl);
    }

//http://localhost:8080/api/v1/user/login/oauth2/code/kakao
    //kakao id가 있다면 -> login
    //kakao id가 없다면 -> signup
    @GetMapping("/login/oauth2/code/kakao")
    public ResponseEntity<LoginResponseDto> kakaoLogin(HttpServletRequest request) {

        String code = request.getParameter("code");

        String accessToken = kakaoService.getKakaoAccessToken(code).getAccess_token();
        return ResponseEntity.ok(kakaoService.login(accessToken));
    }
}