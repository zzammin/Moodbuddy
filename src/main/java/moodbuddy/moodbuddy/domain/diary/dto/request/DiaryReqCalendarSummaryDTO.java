package moodbuddy.moodbuddy.domain.diary.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DiaryReqCalendarSummaryDTO {
    @Schema(description = "사용자가 캘린더에서 선택한 날짜", example = "2024-06-01")
    private String calendarDay;
}
