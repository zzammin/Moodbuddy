package moodbuddy.moodbuddy.domain.user.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UserReqCalendarSummaryDTO {
    @Schema(description = "사용자가 캘린더에서 선택한 날짜", example = "2024-06-01")
    private LocalDateTime calendarDay;
}
