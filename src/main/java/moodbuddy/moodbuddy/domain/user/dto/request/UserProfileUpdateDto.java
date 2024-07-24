package moodbuddy.moodbuddy.domain.user.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserProfileUpdateDto {
    String profileComment;
    Boolean alarm;
    @Schema(description = "알림, HH:mm 형식")
    String alarmTime;
    @Schema(description = "사용자 전화번호, -을 제외한 01012345678 형식")
    String phoneNumber;
    @Schema(description = "수정할 프로필 이미지", example = "[\"image1.png\", \"image2.png\"]")
    MultipartFile newProfileImg;
    String nickname;
    Boolean gender;
    @Schema(description = "생일, YYYY-mm-dd 형식")
    String birthday;
}
