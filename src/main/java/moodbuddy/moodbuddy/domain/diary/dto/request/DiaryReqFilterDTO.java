package moodbuddy.moodbuddy.domain.diary.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import moodbuddy.moodbuddy.domain.diary.entity.DiaryEmotion;
import moodbuddy.moodbuddy.domain.diary.entity.DiarySubject;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiaryReqFilterDTO {
    @Schema(description = "검색하고 싶은 키워드", example = "쿼카")
    private String keyWord;
    @Schema(description = "검색하고 싶은 년도", example = "2024")
    private Integer year;
    @Schema(description = "검색하고 싶은 월", example = "7")
    private Integer month;
    @Schema(description = "검색하고 싶은 감정(HAPPY, ANGRY, AVERSION, SURPRISED, CALMNESS, DEPRESSION, FEAR)", example = "HAPPY")
    private DiaryEmotion diaryEmotion;
    @Schema(description = "검색하고 싶은 주제(DAILY, GROWTH, EMOTION, TRAVEL)", example = "DAILY")
    private DiarySubject diarySubject;
}
