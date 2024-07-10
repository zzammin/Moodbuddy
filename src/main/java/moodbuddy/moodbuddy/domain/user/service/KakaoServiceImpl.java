package moodbuddy.moodbuddy.domain.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import moodbuddy.moodbuddy.domain.profile.entity.Profile;
import moodbuddy.moodbuddy.domain.profile.repository.ProfileRepository;
import moodbuddy.moodbuddy.domain.profileImage.entity.ProfileImage;
import moodbuddy.moodbuddy.domain.profileImage.repository.ProfileImageRepository;
import moodbuddy.moodbuddy.domain.user.dto.response.LoginResponseDto;
import moodbuddy.moodbuddy.domain.user.entity.User;
import moodbuddy.moodbuddy.domain.user.repository.UserRepository;
import moodbuddy.moodbuddy.global.common.util.JwtUtil;
import moodbuddy.moodbuddy.global.properties.KakaoProperties;
import moodbuddy.moodbuddy.domain.user.dto.KakaoProfile;
import moodbuddy.moodbuddy.domain.user.dto.KakaoTokenDto;
import org.springframework.http.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;

import java.time.LocalDate;
import java.util.Optional;
import static moodbuddy.moodbuddy.global.common.config.MapperConfig.modelMapper;

@Service
@Slf4j
public class KakaoServiceImpl implements KakaoService{
    private final ProfileImageRepository profileImageRepository;
    private final ProfileRepository profileRepository;

    private final KakaoProperties kakaoProperties;
    private final UserRepository userRepository;

    public KakaoServiceImpl
            (final KakaoProperties kakaoProperties, final UserRepository userRepository,
             ProfileRepository profileRepository,
             ProfileImageRepository profileImageRepository)
    {
        this.kakaoProperties = kakaoProperties;
        this.userRepository = userRepository;

        this.profileRepository = profileRepository;
        this.profileImageRepository = profileImageRepository;
    }

    @Override
    @Transactional
    //client에서 제공한 code를 이용하여 KakaoAccessToken 생성
    public KakaoTokenDto getKakaoAccessToken(String code) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");

        // Http Response Body 객체 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code"); //카카오 공식문서 기준 authorization_code 로 고정
        params.add("client_id", kakaoProperties.getApiKey()); // 카카오 Dev 앱 REST API 키
        params.add("redirect_uri", kakaoProperties.getRedirectUrl()); // 카카오 Dev redirect uri
        params.add("code", code); // 프론트에서 인가 코드 요청시 받은 인가 코드값
        params.add("client_secret", kakaoProperties.getClientSecret());

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        log.info("Request Headers: {}", headers);
        log.info("Request Body: {}", params);

        // 카카오로부터 Access token 받아오기
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> accessTokenResponse = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // JSON Parsing (-> KakaoTokenDto)
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        KakaoTokenDto kakaoTokenDto = null;
        try {
            kakaoTokenDto = objectMapper.readValue(accessTokenResponse.getBody(), KakaoTokenDto.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return kakaoTokenDto;
    }

    @Override
    @Transactional
    //KakaoAccessToken으로 user 정보 불러오기
    public KakaoProfile getUserInfo(String kakaoAccessToken) {

        log.info("kakao Access Token 받아오기 성공 " + kakaoAccessToken);

        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + kakaoAccessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> accountInfoRequest = new HttpEntity<>(headers);

        // POST 방식으로 API 서버에 요청 후 response 받아옴
        ResponseEntity<String> accountInfoResponse = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                accountInfoRequest,
                String.class
        );

        log.info("kakao 서버에 요청 성공");

        // JSON Parsing (-> kakaoAccountDto)
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        KakaoProfile profile = null;
        try {
            profile = objectMapper.readValue(accountInfoResponse.getBody(), KakaoProfile.class);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        log.info("json parsing 성공");

        return profile;
    }

    @Override
    @Transactional
    public LoginResponseDto login(String kakaoAccessToken) {

        log.info("kakao Access Token 받아오기 성공 " + kakaoAccessToken);

        KakaoProfile kakaoProfile = getUserInfo(kakaoAccessToken);
        final Optional<User> byKakaoId = userRepository.findByKakaoId(kakaoProfile.getId());

        //kakaoId가 존재한다면 login, 존재하지 않는다면 signup
        if (byKakaoId.isEmpty()) {
            final User save = userRepository.save(
                    User.builder()
                            .userRole("ROLE_USER")
                            .nickname(kakaoProfile.getProperties().getNickname())
                            .kakaoId(kakaoProfile.getId())
                            .alarm(kakaoProfile.getKakaoAccount().isTalkMessage())
                            .userCurDiaryNums(0)
                            .deleted(false)
                            .accessToken(JwtUtil.createJwt(kakaoProfile.getId()))
                            .accessTokenExpiredAt(LocalDate.now().plusYears(1L))
                            .refreshToken(JwtUtil.createRefreshToken(kakaoProfile.getId()))
                            .refreshTokenExpiredAt(LocalDate.now().plusYears(1L))
                            .build()
            );

            profileRepository.save(
                    Profile.builder()
                            .kakaoId(kakaoProfile.getId())
                            .build()
            );

            profileImageRepository.save(
                    ProfileImage.builder()
                            .kakaoId(kakaoProfile.getId())
                            .profileImgURL(kakaoProfile.getProperties().getProfileImage())
                            .build()
            );


            log.info("sign up is success" +kakaoProfile.getProperties().getNickname());

            return modelMapper.map(save, LoginResponseDto.class);

        }else{
            User loginUser = byKakaoId.get();

            log.info("login is success" + kakaoProfile.getProperties().getNickname());

            return  modelMapper.map(loginUser, LoginResponseDto.class);
        }

    }
}
