package moodbuddy.moodbuddy.domain.diary.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@Slf4j
public class DiarySummarizeServiceImpl implements DiarySummarizeService {
    private final WebClient naverWebClient;

    public DiarySummarizeServiceImpl(@Qualifier("naverWebClient") WebClient naverWebClient) {
        this.naverWebClient = naverWebClient;
    }

    @Override
    public String summarize(String content) {
        log.info("[DiaryService] summarize");
        try {
            log.info("content : " + content);

            // getRequestBody 메소드에 일기 내용을 전달하여, Request Body 를 위한 Map 생성
            Map<String, Object> requestBody = getRequestBody(content);
            log.info("requestBody : " + requestBody);

            // naverWebClient 를 사용하여 API 호출
            String response = naverWebClient.post()
                    .body(BodyInserters.fromValue(requestBody))
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), clientResponse -> {
                        return clientResponse.bodyToMono(String.class).flatMap(errorBody -> {
                            log.error("API 요청 실패 - 상태 코드: " + clientResponse.statusCode());
                            log.error("오류 본문: " + errorBody);
                            return Mono.error(new RuntimeException("API 요청 실패 - 상태 코드: " + clientResponse.statusCode() + ", 오류 본문: " + errorBody));
                        });
                    })
                    .bodyToMono(String.class)
                    .block();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response);
            return jsonNode.path("summary").asText(); // summary 결과
        } catch (Exception e) {
            log.error("[DiaryService] summarize error", e);
            throw new RuntimeException("[DiaryService] summarize error", e);
        }
    }

    @Override
    public Map<String, Object> getRequestBody(String content) {
        log.info("[DiaryService] getRequestBody");
        try {
            Map<String, Object> documentObject = new HashMap<>(); // DocumentObject 를 위한 Map 생성
            documentObject.put("content", content); // 요약할 내용 (일기 내용)

            Map<String, Object> optionObject = new HashMap<>(); // OptionObject 를 위한 Map 생성
            optionObject.put("language", "ko"); // 한국어
            optionObject.put("summaryCount", 1); // 요약 줄 수 (1줄)

            Map<String, Object> requestBody = new HashMap<>(); // Request Body 를 위한 Map 생성
            requestBody.put("document", documentObject);
            requestBody.put("option", optionObject);
            return requestBody;
        } catch (Exception e) {
            log.error("[DiaryService] getRequestBody error", e);
            throw new RuntimeException("[DiaryService] getRequestBody error", e);
        }
    }
}
