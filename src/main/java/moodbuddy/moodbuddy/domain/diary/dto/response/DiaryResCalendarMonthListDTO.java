package moodbuddy.moodbuddy.domain.diary.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class DiaryResCalendarMonthListDTO {
    private List<DiaryResCalendarMonthDTO> diaryResCalendarMonthDTOList;
}
