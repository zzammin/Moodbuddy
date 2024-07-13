package moodbuddy.moodbuddy.domain.letter.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LetterResPageDTO {
    @Schema(description = "프로필 닉네임")
    private String profileNickname;
    @Schema(description = "유저 생일")
    private String userBirth;
    @Schema(description = "프로필 코멘트")
    private String profileComment;
    @Schema(description = "프로필 이미지")
    private String profileImageUrl;
    @Schema(description = "유저의 편지 개수")
    private Integer userLetterNums;
    @Schema(description = "답장 도착 확인용 리스트")
    private List<LetterResPageAnswerDTO> letterResPageAnswerDTOList;
}
