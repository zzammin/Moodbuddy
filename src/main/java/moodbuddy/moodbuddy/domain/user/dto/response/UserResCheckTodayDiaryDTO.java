package moodbuddy.moodbuddy.domain.user.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class UserResCheckTodayDiaryDTO {
    private Long kakaoId;
    private Boolean checkTodayDairy;

    public UserResCheckTodayDiaryDTO(Long kakaoId, Boolean checkTodayDairy) {
        this.kakaoId = kakaoId;
        this.checkTodayDairy = checkTodayDairy;
    }
}
