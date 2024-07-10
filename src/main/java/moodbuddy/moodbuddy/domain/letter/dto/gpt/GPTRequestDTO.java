package moodbuddy.moodbuddy.domain.letter.dto.gpt;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
// swagger 작성 필요 x
public class GPTRequestDTO {
    private String model;
    private List<GPTMessageDTO> messages;

    public GPTRequestDTO(String model, String prompt) {
        this.model = model;
        this.messages = new ArrayList<>();
        this.messages.add(new GPTMessageDTO("user", prompt));
    }
}
