package moodbuddy.moodbuddy.domain.quddyTI.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
//@Builder
public class QuddyTIResDetailDTO {
    @Schema(description = "사용자 고유 식별자(kakaoId)", example = "2")
    private Long kakaoId;
    @Schema(description = "Happiness 갯수", example = "2")
    private Integer happinessCount;
    @Schema(description = "Anger 갯수", example = "2")
    private Integer angerCount;
    @Schema(description = "Disgust 갯수", example = "2")
    private Integer disgustCount;
    @Schema(description = "Fear 갯수", example = "2")
    private Integer fearCount;
    @Schema(description = "Neutral 갯수", example = "2")
    private Integer neutralCount;
    @Schema(description = "Sadness 갯수", example = "2")
    private Integer sadnessCount;
    @Schema(description = "Surprise 갯수", example = "2")
    private Integer surpriseCount;
    @Schema(description = "Daily 갯수", example = "2")
    private Integer dailyCount;
    @Schema(description = "Growth 갯수", example = "2")
    private Integer growthCount;
    @Schema(description = "Emotion 갯수", example = "2")
    private Integer emotionCount;
    @Schema(description = "Travel 갯수", example = "2")
    private Integer travelCount;
    @Schema(description = "쿼디티아이", example = "PEH")
    private String quddyTIType ;
}
