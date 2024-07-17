package moodbuddy.moodbuddy.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResMonthCommentDTO {
    @Schema(description = "월별 통계에서 선택한 달", example = "2024-06")
    private String chooseMonth;
    @Schema(description = "다음 달 나에게 짧은 한 마디")
    private String monthComment;
}
