package moodbuddy.moodbuddy.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserReqCalendarMonthDTO {
//    @Schema(description = "캘린더의 연도", example = "2024")
//    private String calendarYear;
//    @Schema(description = "캘린더의 월", example = "06")
//    private String calendarMonth;
    @Schema(description = "캘린더의 년, 월", example = "2024-06")
    private String calendarMonth;
}