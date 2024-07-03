package moodbuddy.moodbuddy.domain.bookMark.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookMarkResToggleDTO {
    @Schema(description = "북마크 성공(true) / 북마크 취소(false)", example = "true")
    private boolean bookmarked;
}
