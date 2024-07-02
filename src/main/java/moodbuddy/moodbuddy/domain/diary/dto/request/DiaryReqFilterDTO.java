package moodbuddy.moodbuddy.domain.diary.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import moodbuddy.moodbuddy.domain.diary.entity.DiaryEmotion;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiaryReqFilterDTO {
    @Schema(description = "검색하고 싶은 키워드")
    private String keyWord;
    @Schema(description = "검색하고 싶은 년도")
    private Integer year;
    @Schema(description = "검색하고 싶은 월")
    private Integer month;
    @Schema(description = "검색하고 싶은 감정")
    private DiaryEmotion diaryEmotion;
}
