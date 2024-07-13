package moodbuddy.moodbuddy.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import moodbuddy.moodbuddy.domain.letter.dto.response.LetterResSaveDTO;
import moodbuddy.moodbuddy.domain.user.dto.request.UserProfileUpdateDto;
import moodbuddy.moodbuddy.domain.user.dto.request.UserReqCalendarMonthDTO;
import moodbuddy.moodbuddy.domain.user.dto.request.UserReqCalendarSummaryDTO;
import moodbuddy.moodbuddy.domain.user.dto.request.UserReqMainPageDTO;
import moodbuddy.moodbuddy.domain.user.dto.response.UserResCalendarMonthListDTO;
import moodbuddy.moodbuddy.domain.user.dto.response.UserResCalendarSummaryDTO;
import moodbuddy.moodbuddy.domain.user.dto.response.UserResMainPageDTO;
import moodbuddy.moodbuddy.domain.user.dto.response.*;
import moodbuddy.moodbuddy.domain.user.service.UserService;
import moodbuddy.moodbuddy.global.common.response.ApiResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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
    @Operation(summary = "일기 한 줄 요약", description = "사용자가 선택한 날짜의 일기를 한 줄 요약한 결과를 보여줍니다.")
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

    //월별 통계 보기 _ 월별 감정 통계
    @GetMapping("/main/emotion-static")
    @Operation(summary = "월별 감정 통계 보기", description = "사용자가 선택한 월의 감정 통계를 보여줍니다.")
    @Parameters({
            @Parameter(name="month", description = "YYYY-MM-DD 형식으로 입력하세요"),
    })
    public ResponseEntity<?> getEmotionStatic
    (@RequestParam("month") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate month) {
        List<EmotionStaticDto> emotionStats = userService.getEmotionStatic(month);
        return ResponseEntity.ok(emotionStats);
    }


    //내 활동 _ 일기 횟수 조회 , 년 + 해당하는 월
    @GetMapping("/main/diary-nums")
    @Operation(summary = "현재까지 작성한 일기 횟수", description = "해당 년도의 월별로 작성한 일기 횟수를 보여줍니다.")
    @Parameters({
            @Parameter(name="year", description = "YYYY-MM-DD 형식으로 입력하세요"),
    })
    public ResponseEntity<?> getDiaryNums
    (@RequestParam("year") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate year) {
        List<DiaryNumsDto> diaryNumsDtos = userService.getDiaryNums(year);
        return ResponseEntity.ok(userService.getDiaryNums(year));
    }


    //감정 횟수 조회
    @GetMapping("/main/emotion-nums")
    @Operation(summary = "감정 횟수 조회", description = "감정 횟수를 보여줍니다")
    public ResponseEntity<?> getEmotionNums()
    {
        List<EmotionStaticDto> emotionNums = userService.getEmotionNums();
        return ResponseEntity.ok(emotionNums);
    }

    //프로필 조회
    @GetMapping("/main/profile")
    @Operation(summary = "프로필 조회")
    public ResponseEntity<?> getProfile()
    {
        UserProfileDto profile = userService.getUserProfile();
        return ResponseEntity.ok(profile);
    }


    //프로필 수정
    @PostMapping ("/main/profile-edit")
    @Operation(summary = "프로필 수정", description = "alarmTime(str) -> HH:mm 형식,birthday(str) -> YYYY-mm-dd 형식 ")
    public ResponseEntity<?> updateProfile(UserProfileUpdateDto updateDto)
    {
        UserProfileDto updateProfile = userService.updateProfile(updateDto);
        return ResponseEntity.ok(updateProfile);
    }



}
