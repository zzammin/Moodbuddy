package moodbuddy.moodbuddy.domain.diary.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DiaryReqCalendarMonthDTO {
    @Schema(description = "캘린더의 년, 월", example = "2024-06")
    private String calendarMonth;
}
