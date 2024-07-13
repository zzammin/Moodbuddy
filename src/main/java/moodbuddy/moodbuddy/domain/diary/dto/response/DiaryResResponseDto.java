package moodbuddy.moodbuddy.domain.diary.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class DiaryResResponseDto {
    private String emotion;
    private LocalDateTime diaryDate;
    private String comment;
}