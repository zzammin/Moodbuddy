package moodbuddy.moodbuddy.domain.diary.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moodbuddy.moodbuddy.domain.diary.dto.request.DiaryReqSaveDTO;
import moodbuddy.moodbuddy.domain.diary.dto.response.DiaryResSaveDTO;
import moodbuddy.moodbuddy.domain.diary.service.DiaryServiceImpl;
import moodbuddy.moodbuddy.global.common.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/member/diary")
@RequiredArgsConstructor
@Slf4j
public class DiaryApiController {
    private final DiaryServiceImpl diaryService;
    @PostMapping("/save")
    public ResponseEntity<?> save(@ModelAttribute DiaryReqSaveDTO diaryReqSaveDTO) {
        log.info("[DiaryApiController] save");
        try {
            DiaryResSaveDTO result = diaryService.save(diaryReqSaveDTO);
            return ResponseEntity.ok().body(ApiResponse.SUCCESS(HttpStatus.CREATED.value(), "[SUCCESS] DiaryApiController save", result));
        } catch (Exception e) {
            log.error("[DiaryApiController] error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }
}
