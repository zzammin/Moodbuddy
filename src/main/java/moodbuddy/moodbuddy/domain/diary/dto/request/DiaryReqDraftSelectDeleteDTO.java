package moodbuddy.moodbuddy.domain.diary.dto.request;

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
    private List<Long> diaryIdList;
}
