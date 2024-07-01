package moodbuddy.moodbuddy.domain.diary.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moodbuddy.moodbuddy.domain.diary.dto.request.*;
import moodbuddy.moodbuddy.domain.diary.dto.response.*;
import moodbuddy.moodbuddy.domain.diary.service.DiaryServiceImpl;
import moodbuddy.moodbuddy.global.common.response.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/member/diary")
@RequiredArgsConstructor
@Slf4j
public class DiaryApiController {
    private final DiaryServiceImpl diaryService;
    @PostMapping("/save")
    @Operation(summary = "일기 작성")
    public ResponseEntity<?> save(@ModelAttribute DiaryReqSaveDTO diaryReqSaveDTO) throws IOException {
        log.info("[DiaryApiController] save");
        DiaryResDetailDTO result = diaryService.save(diaryReqSaveDTO);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(HttpStatus.CREATED.value(), "[SUCCESS] DiaryApiController save", result));
    }

    @PatchMapping("/update")
    @Operation(summary = "일기 수정")
    public ResponseEntity<?> update(@ModelAttribute DiaryReqUpdateDTO diaryReqUpdateDTO) throws IOException {
        log.info("[DiaryApiController] update");
        DiaryResDetailDTO result = diaryService.update(diaryReqUpdateDTO);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(HttpStatus.CREATED.value(), "[SUCCESS] DiaryApiController update", result));
    }

    @DeleteMapping("/delete/{diaryId}")
    @Operation(summary = "일기 삭제")
    public ResponseEntity<?> delete(@PathVariable("diaryId") Long diaryId) {
        log.info("[DiaryApiController] delete");
        diaryService.delete(diaryId);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(HttpStatus.CREATED.value(), "[SUCCESS] DiaryApiController delete"));
    }

    @PostMapping("/draftSave")
    @Operation(summary = "일기 임시 저장")
    public ResponseEntity<?> draftSave(@RequestBody DiaryReqSaveDTO diaryReqSaveDTO) throws IOException {
        log.info("[DiaryApiController] draftSave");
        DiaryResDetailDTO result = diaryService.draftSave(diaryReqSaveDTO);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(HttpStatus.CREATED.value(), "[SUCCESS] DiaryApiController draftSave", result));
    }

    @GetMapping("/draftFindAll")
    @Operation(summary = "임시 저장 일기 목록 조회")
    public ResponseEntity<?> draftFindAll() {
        log.info("[DiaryApiController] draftFindAll");
        DiaryResDraftFindAllDTO result = diaryService.draftFindAll();
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(HttpStatus.CREATED.value(), "[SUCCESS] DiaryApiController draftFindAll", result));
    }

    @DeleteMapping("/draftSelectDelete")
    @Operation(summary = "임시 저장 일기 선택 삭제")
    public ResponseEntity<?> draftSelectDelete(@RequestBody DiaryReqDraftSelectDeleteDTO diaryReqDraftSelectDeleteDTO) {
        log.info("[DiaryApiController] draftSelectDelete");
        diaryService.draftSelectDelete(diaryReqDraftSelectDeleteDTO);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(HttpStatus.CREATED.value(), "[SUCCESS] DiaryApiController draftSelectDelete"));
    }

    @GetMapping("/findOne/{diaryId}")
    @Operation(summary = "일기 하나 조회")
    public ResponseEntity<?> findOneByDiaryId(@PathVariable("diaryId") Long diaryId) {
        log.info("[DiaryApiController] findOne");
        DiaryResDetailDTO result = diaryService.findOneByDiaryId(diaryId);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(HttpStatus.CREATED.value(), "[SUCCESS] DiaryApiController findOne", result));
    }

    @GetMapping("/findAllPageable")
    @Operation(summary = "일기 전체 조회")
    public ResponseEntity<?> findAllWithPageable(Pageable pageable) {
        log.info("[DiaryApiController] findAllPageable");
        Page<DiaryResDetailDTO> result = diaryService.findAllWithPageable(pageable);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(HttpStatus.CREATED.value(), "[SUCCESS] DiaryApiController findAllPageable", result));
    }
    @GetMapping("/findAllByEmotionWithPageable")
    @Operation(summary = "일기 비슷한 감정으로 전체 조회")
    public ResponseEntity<?> findAllByEmotionWithPageable(@RequestBody DiaryReqEmotionDTO diaryReqEmotionDTO, Pageable pageable) {
        log.info("[DiaryApiController] findAllByEmotionWithPageable");
        Page<DiaryResDetailDTO> result = diaryService.findAllByEmotionWithPageable(diaryReqEmotionDTO, pageable);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(HttpStatus.CREATED.value(), "[SUCCESS] DiaryApiController findAllByEmotionWithPageable", result));
    }

    @GetMapping("/findAllByFilter")
    @Operation(summary = "일기 필터링으로 전체 조회")
    public ResponseEntity<?> findAllByFilter(@RequestBody DiaryReqFilterDTO diaryReqFilterDTO, Pageable pageable) {
        log.info("[DiaryApiController] findAllByFilter");
        Page<DiaryResDetailDTO> result = diaryService.findAllByFilter(diaryReqFilterDTO, pageable);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(HttpStatus.CREATED.value(), "[SUCCESS] DiaryApiController findAllByFilter", result));
    }

    /** =========================================================  위 정목 아래 재민  ========================================================= **/
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
