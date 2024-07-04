package moodbuddy.moodbuddy.domain.letter.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LetterResPageAnswerDTO {
    @Schema(description = "고민 편지 작성 날짜")
    private LocalDateTime letterCreatedTime;
    @Schema(description = "답장 도착 유무(답장 도착 O : 1, 답장 도착 X : 0")
    private Integer answerCheck;
}
