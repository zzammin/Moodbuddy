package moodbuddy.moodbuddy.domain.diary.dto.request;

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
public class DiaryReqDraftSelectDeleteDTO {
    @Schema(description = "삭제하고 싶은 임시 저장 일기 고유 식별자(diaryId)를 담는 List", example = "[1, 2]")
    private List<Long> diaryIdList;
}
