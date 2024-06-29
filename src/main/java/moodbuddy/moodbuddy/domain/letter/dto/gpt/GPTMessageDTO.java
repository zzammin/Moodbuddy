package moodbuddy.moodbuddy.domain.letter.dto.gpt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GPTMessageDTO {
    private String role;
    private String content;
}
