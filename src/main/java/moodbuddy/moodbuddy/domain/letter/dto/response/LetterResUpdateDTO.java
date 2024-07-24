package moodbuddy.moodbuddy.domain.letter.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class LetterResUpdateDTO {
    @Schema(description = "사용자의 닉네임")
    private String nickname;
    @Schema(description = "사용자의 쿼디 답장 알람 설정")
    private Boolean letterAlarm;
}
