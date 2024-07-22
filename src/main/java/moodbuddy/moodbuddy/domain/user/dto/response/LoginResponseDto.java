package moodbuddy.moodbuddy.domain.user.dto.response;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class LoginResponseDto {
//    private Long userId;
    private String accessToken;
    private String refreshToken;
//    private String nickname;
//    private LocalDate accessTokenExpiredAt;
//    private LocalDate refreshTokenExpiredAt;
}