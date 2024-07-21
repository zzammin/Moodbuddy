package moodbuddy.moodbuddy.domain.user.dto.fcm;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FcmResDTO {
//    private String response;
    @Schema(description = "FCM 푸시 알림 메시지의 제목")
    private String title;
    @Schema(description = "FCM 푸시 알림 메시지의 내용")
    private String body;
}