package moodbuddy.moodbuddy.domain.letter.dto.gpt;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
// swagger 작성 필요 x
public class GPTMessageDTO {
    private String role;
    private String content;
}
