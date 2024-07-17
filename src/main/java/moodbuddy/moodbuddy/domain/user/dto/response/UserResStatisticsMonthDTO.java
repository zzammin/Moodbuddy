package moodbuddy.moodbuddy.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResStatisticsMonthDTO {
    @Schema(description = "월별 통계 감정 리스트")
    private List<EmotionStaticDto> emotionStaticDtoList;
    @Schema(description = "다음 달 나에게 짧은 한 마디")
    private String monthComment;
}
