package moodbuddy.moodbuddy.domain.diary.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import moodbuddy.moodbuddy.domain.diary.dto.DiaryRequestDTO;
import moodbuddy.moodbuddy.domain.diary.dto.DiaryResponseDTO;
import moodbuddy.moodbuddy.domain.diary.service.DiaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "diary-controller" , description = "Diary API")
public class DiaryApiController {

    private final DiaryService diaryService;

    @PostMapping("/main/month")
    @Operation(summary = "캘린더 달 이동", description = "캘린더의 달을 이동시킵니다.")
    public ResponseEntity<DiaryResponseDTO> monthlyCalendar(
            @Parameter(description = "사용자의 userId")
            @RequestParam Long userId,
            @Parameter(description = "캘린더에서 이동할 년, 월을 담고 있는 DTO")
            DiaryRequestDTO.CalendarMonthDTO calendarMonthDTO
    ){
        return ResponseEntity.ok(diaryService.monthlyCalendar(userId, calendarMonthDTO));
    }

    @PostMapping("/main/summary")
    @Operation(summary = "일기 한 줄 요약", description = "사용자가 선택한 날짜의 일기를 한 줄로 요약합니다.")
    public ResponseEntity<DiaryResponseDTO> summary(
            @Parameter(description = "사용자의 userId")
            @RequestParam Long userId,
            @Parameter(description = "사용자가 선택한 날짜를 담고 있는 DTO")
            DiaryRequestDTO.CalendarSummaryDTO calendarSummaryDTO
    ){
        return ResponseEntity.ok(diaryService.summary(userId, calendarSummaryDTO));
    }
}
