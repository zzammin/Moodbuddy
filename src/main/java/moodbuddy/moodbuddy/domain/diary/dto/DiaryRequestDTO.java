package moodbuddy.moodbuddy.domain.diary.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.AllArgsConstructor;

public class DiaryRequestDTO {

    @Data
    @AllArgsConstructor
    public static class CalendarMonthDTO{
        @Schema(description = "캘린더에서 이동할 년, 월", example = "202406")
        private String calendarMonth;
    }

    @Data
    @AllArgsConstructor
    public static class CalendarSummaryDTO{
        @Schema(description = "캘린더에서 선택한 날짜", example = "20240601")
        private String calendarDay;
    }
}
