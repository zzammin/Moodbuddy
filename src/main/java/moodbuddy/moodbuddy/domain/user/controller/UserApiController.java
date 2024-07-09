package moodbuddy.moodbuddy.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import moodbuddy.moodbuddy.domain.letter.dto.response.LetterResSaveDTO;
import moodbuddy.moodbuddy.domain.user.dto.request.UserReqCalendarMonthDTO;
import moodbuddy.moodbuddy.domain.user.dto.request.UserReqCalendarSummaryDTO;
import moodbuddy.moodbuddy.domain.user.dto.response.UserResCalendarMonthListDTO;
import moodbuddy.moodbuddy.domain.user.dto.response.UserResCalendarSummaryDTO;
import moodbuddy.moodbuddy.domain.user.dto.response.UserResMainPageDTO;
import moodbuddy.moodbuddy.domain.user.service.UserService;
import moodbuddy.moodbuddy.global.common.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class UserApiController {

    private final UserService userService;



    @GetMapping("/main")
    @Operation(summary = "메인 화면", description = "메인 화면으로 이동합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "SUCCESS", content = @Content(schema = @Schema(implementation = UserResMainPageDTO.class)))
            // @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> mainPage(){
        return ResponseEntity.ok(userService.mainPage());
    }

    @PostMapping("/main/month")
    @Operation(summary = "캘린더 달 이동", description = "캘린더의 달을 이동시킵니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "SUCCESS", content = @Content(schema = @Schema(implementation = UserResCalendarMonthListDTO.class)))
            // @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> monthlyCalendar(
            @Parameter(description = "캘린더에서 이동할 년, 월을 담고 있는 DTO")
            @RequestBody UserReqCalendarMonthDTO calendarMonthDTO
    ){
        return ResponseEntity.ok(userService.monthlyCalendar(calendarMonthDTO));
    }

    @PostMapping("/main/summary")
    @Operation(summary = "일기 한 줄 요약", description = "사용자가 선택한 날짜의 일기를 한 줄로 요약합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "SUCCESS", content = @Content(schema = @Schema(implementation = UserResCalendarSummaryDTO.class)))
            // @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> summary(
            @Parameter(description = "사용자가 선택한 날짜를 담고 있는 DTO")
            @RequestBody UserReqCalendarSummaryDTO calendarSummaryDTO
    ){
        return ResponseEntity.ok(userService.summary(calendarSummaryDTO));
    }
}
