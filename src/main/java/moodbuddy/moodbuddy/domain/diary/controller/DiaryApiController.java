package moodbuddy.moodbuddy.domain.diary.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moodbuddy.moodbuddy.domain.diary.dto.request.*;
import moodbuddy.moodbuddy.domain.diary.dto.response.*;
import moodbuddy.moodbuddy.domain.diary.service.DiaryServiceImpl;
import moodbuddy.moodbuddy.global.common.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/member/diary")
@RequiredArgsConstructor
@Slf4j
public class DiaryApiController {
    private final DiaryServiceImpl diaryService;
    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody DiaryReqSaveDTO diaryReqSaveDTO) {
        log.info("[DiaryApiController] save");
        try {
            DiaryResSaveDTO result = diaryService.save(diaryReqSaveDTO);
            return ResponseEntity.ok().body(ApiResponse.SUCCESS(HttpStatus.CREATED.value(), "[SUCCESS] DiaryApiController save", result));
        } catch (Exception e) {
            log.error("[DiaryApiController] save", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }

    @PatchMapping("/update")
    public ResponseEntity<?> update(@RequestBody DiaryReqUpdateDTO diaryReqUpdateDTO) {
        log.info("[DiaryApiController] update");
        try {
            DiaryResUpdateDTO result = diaryService.update(diaryReqUpdateDTO);
            return ResponseEntity.ok().body(ApiResponse.SUCCESS(HttpStatus.CREATED.value(), "[SUCCESS] DiaryApiController update", result));
        } catch (Exception e) {
            log.error("[DiaryApiController] update", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{diaryId}")
    public ResponseEntity<?> delete(@PathVariable("diaryId") Long diaryId) {
        log.info("[DiaryApiController] delete");
        try {
            DiaryResDeleteDTO result = diaryService.delete(diaryId);
            return ResponseEntity.ok().body(ApiResponse.SUCCESS(HttpStatus.CREATED.value(), "[SUCCESS] DiaryApiController delete", result));
        } catch (Exception e) {
            log.error("[DiaryApiController] delete", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }

    @PostMapping("/draftSave")
    public ResponseEntity<?> draftSave(@RequestBody DiaryReqDraftSaveDTO diaryReqDraftSaveDTO) {
        log.info("[DiaryApiController] draftSave");
        try {
            DiaryResDraftSaveDTO result = diaryService.draftSave(diaryReqDraftSaveDTO);
            return ResponseEntity.ok().body(ApiResponse.SUCCESS(HttpStatus.CREATED.value(), "[SUCCESS] DiaryApiController draftSave", result));
        } catch (Exception e) {
            log.error("[DiaryApiController] draftSave", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }

    @GetMapping("/draftFindAll")
    public ResponseEntity<?> draftFindAll() {
        log.info("[DiaryApiController] draftFindAll");
        try {
            DiaryResDraftFindAllDTO result = diaryService.draftFindAll();
            return ResponseEntity.ok().body(ApiResponse.SUCCESS(HttpStatus.CREATED.value(), "[SUCCESS] DiaryApiController draftFindAll", result));
        } catch (Exception e) {
            log.error("[DiaryApiController] draftFindAll", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }

    @DeleteMapping("/draftSelectDelete")
    public ResponseEntity<?> draftSelectDelete(@RequestBody DiaryReqDraftSelectDeleteDTO diaryReqDraftSelectDeleteDTO) {
        log.info("[DiaryApiController] draftSelectDelete");
        try {
            diaryService.draftSelectDelete(diaryReqDraftSelectDeleteDTO);
            return ResponseEntity.ok().body(ApiResponse.SUCCESS(HttpStatus.CREATED.value(), "[SUCCESS] DiaryApiController draftSelectDelete"));
        } catch (Exception e) {
            log.error("[DiaryApiController] draftSelectDelete", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }

    @GetMapping("/findOne/{diaryId}")
    public ResponseEntity<?> findOne(@PathVariable("diaryId") Long diaryId) {
        log.info("[DiaryApiController] findOne");
        try {
            DiaryResFindOneDTO result = diaryService.findOne(diaryId);
            return ResponseEntity.ok().body(ApiResponse.SUCCESS(HttpStatus.CREATED.value(), "[SUCCESS] DiaryApiController findOne", result));
        } catch (Exception e) {
            log.error("[DiaryApiController] findOne", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }

    @PostMapping("/main/month")
    @Operation(summary = "캘린더 달 이동", description = "캘린더의 달을 이동시킵니다.")
    public ResponseEntity<?> monthlyCalendar(
            @Parameter(description = "캘린더에서 이동할 년, 월을 담고 있는 DTO")
            @RequestBody DiaryReqCalendarMonthDTO calendarMonthDTO
    ){
        return ResponseEntity.ok(diaryService.monthlyCalendar(calendarMonthDTO));
    }

    @PostMapping("/main/summary")
    @Operation(summary = "일기 한 줄 요약", description = "사용자가 선택한 날짜의 일기를 한 줄로 요약합니다.")
    public ResponseEntity<?> summary(
            @Parameter(description = "사용자가 선택한 날짜를 담고 있는 DTO")
            @RequestBody DiaryReqCalendarSummaryDTO calendarSummaryDTO
    ){
        return ResponseEntity.ok(diaryService.summary(calendarSummaryDTO));
    }
}
