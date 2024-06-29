package moodbuddy.moodbuddy.domain.letter.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LetterResSaveDTO {
    @Schema(description = "사용자 고민 편지 letterId")
    private Long letterId;
    @Schema(description = "사용자의 email")
    private String userEmail;
    @Schema(description = "사용자가 고민 편지를 작성한 날짜")
    private LocalDateTime letterDate;
}
