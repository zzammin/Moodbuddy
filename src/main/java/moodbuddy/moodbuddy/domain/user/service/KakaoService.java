package moodbuddy.moodbuddy.domain.user.service;

import moodbuddy.moodbuddy.domain.user.dto.KakaoProfile;
import moodbuddy.moodbuddy.domain.user.dto.KakaoTokenDto;
import moodbuddy.moodbuddy.domain.user.dto.response.LoginResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface KakaoService {
    public KakaoTokenDto getKakaoAccessToken(String code);

    public KakaoProfile getUserInfo(String kakaoAccessToken);

    public LoginResponseDto login(String kakaoAccessToken);
}

