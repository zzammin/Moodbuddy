package moodbuddy.moodbuddy.domain.diary.service;

import java.util.Map;

public interface DiarySummarizeService {
    // 일기 한 줄 요약
    String summarize(String content);

    // 네이버 클라우드 API 연동을 위한 Request Body 생성
    Map<String,Object> getRequestBody(String content);
}
