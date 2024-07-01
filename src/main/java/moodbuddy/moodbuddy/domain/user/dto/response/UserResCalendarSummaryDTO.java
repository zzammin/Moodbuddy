package moodbuddy.moodbuddy.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserResCalendarSummaryDTO {
    @Schema(description = "그 날의 일기 제목")
    private String diaryTitle;
    @Schema(description = "그 날의 일기 요약 내용")
    private String diarySummary;
}
