package moodbuddy.moodbuddy.domain.gpt.service;

import lombok.extern.slf4j.Slf4j;
import moodbuddy.moodbuddy.domain.gpt.dto.GPTRequestDTO;
import moodbuddy.moodbuddy.domain.gpt.dto.GPTResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class GptServiceImpl implements GptService{

    private final WebClient gptWebClient;

    @Value("${gpt.model}")
    private String model;

    @Value("${gpt.api.url}")
    private String apiUrl;

    public GptServiceImpl(@Qualifier("gptWebClient") WebClient gptWebClient) {
        this.gptWebClient = gptWebClient;
    }

    @Override
    public Mono<String> classifyDiaryContent(String content) {
        String prompt = "DiaryContent 내용을 보고 \"일상\", \"성장\", \"감정\", \"여행\" 중 어떤 주제에 해당하는 지 주제 값만 \"DAILY\", \"GROWTH\", \"EMOTION\", \"TRAVEL\" 로 출력해줘.\nContent: " + content + "\nCategory:";

        GPTRequestDTO gptRequestDTO = new GPTRequestDTO(model, prompt);

        return gptWebClient.post()
                .uri(apiUrl)
                .bodyValue(gptRequestDTO)
                .retrieve()
                .bodyToMono(GPTResponseDTO.class)
                .map(response -> response.getChoices().get(0).getMessage().getContent().trim().toUpperCase());
    }
}