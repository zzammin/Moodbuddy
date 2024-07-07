package moodbuddy.moodbuddy.domain.letter.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LetterResDetailsDTO {
    @Schema(description = "사용자 고민 편지 letterId")
    private Long letterId;
    @Schema(description = "사용자의 nickname")
    private String userNickname;
    @Schema(description = "사용자의 고민 편지 답장 형식")
    private Integer letterFormat;
    @Schema(description = "사용자가 작성한 고민 편지 내용")
    private String letterWorryContent;
    @Schema(description = "사용자가 작성한 고민 편지 내용에 대한 답장")
    private String letterAnswerContent;
    @Schema(description = "사용자가 고민 편지를 작성한 날짜")
    private LocalDateTime letterDate;
}
