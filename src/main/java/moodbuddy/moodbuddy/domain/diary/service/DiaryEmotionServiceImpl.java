package moodbuddy.moodbuddy.domain.diary.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moodbuddy.moodbuddy.domain.diary.dto.response.DiaryResDTO;
import moodbuddy.moodbuddy.domain.diary.entity.Diary;
import moodbuddy.moodbuddy.domain.diary.entity.DiaryEmotion;
import moodbuddy.moodbuddy.domain.diary.repository.DiaryRepository;
import moodbuddy.moodbuddy.domain.gpt.service.GptService;
import moodbuddy.moodbuddy.global.common.exception.database.DatabaseNullOrEmptyException;
import moodbuddy.moodbuddy.global.common.util.JwtUtil;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class DiaryEmotionServiceImpl implements DiaryEmotionService {
    private final DiaryRepository diaryRepository;
    private final ObjectMapper objectMapper;
    private final GptService gptService;
    @Override
    @Transactional
    public DiaryResDTO description() throws JsonProcessingException {

        RestTemplate restTemplate = new RestTemplate();

        //헤더를 JSON으로 설정함
        HttpHeaders headers = new HttpHeaders();

        //파라미터로 들어온 dto를 JSON 객체로 변환
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 쿼리 결과를 JSON 객체로 변환
        Diary diary = diaryRepository.findDiarySummaryById(JwtUtil.getUserId())
                .orElseThrow(() -> new DatabaseNullOrEmptyException("Diary Summary data not found for kakaoId: " + JwtUtil.getUserId()));

        // 'diarySummary' key와 diary.getDiarySummary() 값을 포함하는 Map 생성
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("diarySummary", diary.getDiarySummary());

        // Map을 JSON 문자열로 변환
        String param = objectMapper.writeValueAsString(paramMap);
        log.info(param+"서버로 전송");

        HttpEntity<String> entity = new HttpEntity<String>(param , headers);

        //실제 Flask 서버랑 연결하기 위한 URL
        String url = "https://clean-brave-eel.ngrok-free.app/model";

        // Flask 서버로 데이터를 전송하고 받은 응답 값을 처리
        String response = restTemplate.postForObject(url, entity, String.class);

        // 받은 응답 값을 DiaryDesResponseDto로 변환

        Mono<String> monoComment = gptService.emotionComment(response);
        String comment = monoComment.block();
        DiaryResDTO responseDto = DiaryResDTO.builder()
                .emotion(response)
                .diaryDate(diary.getDiaryDate())
                .comment(comment)
                .build();

        String emotion = responseDto.getEmotion();

        try {
            // 문자열을 DiaryEmotion enum 값으로 변환
            DiaryEmotion diaryEmotion = DiaryEmotion.valueOf(emotion.toUpperCase());

            diary.setDiaryEmotion(diaryEmotion);
            // diary 엔티티를 저장
            diaryRepository.save(diary);
        } catch (IllegalArgumentException e) {
            // 유효하지 않은 emotion 값이 들어왔을 때의 처리
            System.err.println("Invalid emotion value: " + emotion);
        }


        return responseDto;
    }
}
