package moodbuddy.moodbuddy.domain.letter.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class LetterReqUpdateDTO {
    @Schema(description = "사용자의 쿼디 답장 알람 설정")
    private Boolean letterAlarm;
}
