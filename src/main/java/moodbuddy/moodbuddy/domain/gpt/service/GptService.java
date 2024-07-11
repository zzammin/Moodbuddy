package moodbuddy.moodbuddy.domain.gpt.service;

import reactor.core.publisher.Mono;

public interface GptService {
    Mono<String> classifyDiaryContent(String content);
}
