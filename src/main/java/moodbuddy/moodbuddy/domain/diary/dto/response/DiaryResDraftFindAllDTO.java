package moodbuddy.moodbuddy.domain.diary.dto.response;

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
public class DiaryResDraftFindAllDTO {
    @Schema(description = "임시 저장 일기 DTO를 담고 있는 List")
    List<DiaryResDraftFindOneDTO> diaryResDraftFindOneList;
}
