package moodbuddy.moodbuddy.domain.diary.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moodbuddy.moodbuddy.domain.diary.dto.response.DiaryResDTO;
import moodbuddy.moodbuddy.domain.diary.service.DiaryEmotionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/member/diary")
@Tag(name = "Diary", description = "일기 감정 관련 API")
@RequiredArgsConstructor
@Slf4j
public class DiaryEmotionApiController {
    private final DiaryEmotionService diaryEmotionService;
    //클라이언트가 일기 작성 -> 일기 요약본 flask서버로 전달 -> flask 서버에서는 모델을 통한 감정 분석 후 결과를 리턴
    @PostMapping("/description")
    @Operation(description = "일기 감정 분석")
    public ResponseEntity<DiaryResDTO> description() throws JsonProcessingException {
        DiaryResDTO result = diaryEmotionService.description();
        return ResponseEntity.ok(result);
    }
}
