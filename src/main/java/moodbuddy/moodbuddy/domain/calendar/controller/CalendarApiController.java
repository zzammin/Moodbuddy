package moodbuddy.moodbuddy.domain.calendar.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import moodbuddy.moodbuddy.domain.calendar.dto.CalendarResponseDTO;
import moodbuddy.moodbuddy.domain.calendar.service.CalendarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/main")
@Tag(name = "Main" , description = "Main(Calendar) API")
public class CalendarApiController {

    private final CalendarService calendarService;

    @GetMapping("")
    @Operation(summary = "메인 화면", description = "메인 화면으로 이동합니다.")
    public ResponseEntity<CalendarResponseDTO> mainPage(
            @Parameter(description = "유저 카카오 아이디")
            @RequestParam String kakaoId
    ){
        return ResponseEntity.ok(calendarService.mainPage(kakaoId));
    }

    @GetMapping("/month")
    @Operation(summary = "캘린더 달 이동", description = "캘린더의 달을 이동시킵니다.")
    public ResponseEntity<CalendarResponseDTO> monthlyCalendar(
            @Parameter(description = "이동할 캘린더의 달")
            @RequestParam String month
    ){
        return ResponseEntity.ok(calendarService.monthlyCalendar(month));
    }

    @GetMapping("/summary")
    @Operation(summary = "일기 한 줄 요약", description = "사용자가 선택한 날짜의 일기를 한 줄로 요약해줍니다.")
    public ResponseEntity<CalendarResponseDTO> diarySummary(
            @Parameter(description = "사용자가 선택한 날짜")
            @RequestParam String day
    ){
        return ResponseEntity.ok(calendarService.diarySummary(day));
    }
}
