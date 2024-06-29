package moodbuddy.moodbuddy.domain.diary.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@Schema(description = "그 달의 일기 작성 날짜와 감정 리스트 DTO")
public class DiaryResCalendarMonthListDTO {
    @Schema(description = "그 달의 일기 작성 날짜와 감정 리스트")
    private List<DiaryResCalendarMonthDTO> diaryResCalendarMonthDTOList;
}
