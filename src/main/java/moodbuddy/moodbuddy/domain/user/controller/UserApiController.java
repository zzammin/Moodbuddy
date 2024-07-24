package moodbuddy.moodbuddy.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moodbuddy.moodbuddy.domain.diary.dto.response.DiaryResDetailDTO;
import moodbuddy.moodbuddy.domain.user.dto.request.*;
import moodbuddy.moodbuddy.domain.user.dto.response.UserResCalendarMonthListDTO;
import moodbuddy.moodbuddy.domain.user.dto.response.UserResCalendarSummaryDTO;
import moodbuddy.moodbuddy.domain.user.dto.response.UserResMainPageDTO;
import moodbuddy.moodbuddy.domain.user.dto.response.*;
import moodbuddy.moodbuddy.domain.user.service.UserService;
import moodbuddy.moodbuddy.global.common.response.ApiResponse;
import moodbuddy.moodbuddy.global.common.util.JwtUtil;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
@Slf4j
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

    //월별 통계 보기
    @GetMapping("/main/month-static")
    @Operation(summary = "월별 통계 보기", description = "사용자가 선택한 월의 통계(감정 리스트, 짧은 한 마디) 를 보여줍니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "SUCCESS", content = @Content(schema = @Schema(implementation = UserResStatisticsMonthDTO.class)))
            // @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Parameters({
            @Parameter(name="month", description = "YYYY-MM-DD 형식으로 입력하세요"),
    })
    public ResponseEntity<?> getMonthStatic
    (@RequestParam("month") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate month) {
        return ResponseEntity.ok(userService.getMonthStatic(month));
    }

    // 다음 달 나에게 짧은 한 마디
    @PostMapping("/main/month-comment")
    @Operation(summary = "다음 달 나에게 짧은 한 마디")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "SUCCESS", content = @Content(schema = @Schema(implementation = UserResMonthCommentDTO.class)))
            // @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> monthComment(
            @Parameter(description = "월별 통계의 달과 한 마디를 받아오는 DTO")
            @RequestBody UserReqMonthCommentDTO userReqMonthCommentDTO
    ){
        return ResponseEntity.ok(userService.monthComment(userReqMonthCommentDTO));
    }

    // 다음 달 나에게 짧은 한 마디 수정
    @PostMapping("/main/month-comment-update")
    @Operation(summary = "다음 달 나에게 짧은 한 마디 수정")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "SUCCESS", content = @Content(schema = @Schema(implementation = UserResMonthCommentUpdateDTO.class)))
            // @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> monthCommentUpdate(
            @Parameter(description = "월별 통계의 달과 수정할 한 마디를 받아오는 DTO")
            @RequestBody UserReqMonthCommentUpdateDTO userReqMonthCommentUpdateDTO
    ){
        return ResponseEntity.ok(userService.monthCommentUpdate(userReqMonthCommentUpdateDTO));
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
    public ResponseEntity<?> updateProfile(@ModelAttribute UserProfileUpdateDto updateDto) throws IOException
    {
        Long kakaoId = JwtUtil.getUserId();
        UserProfileDto updateProfile = userService.updateProfile(updateDto);
        userService.scheduleUserMessage(kakaoId);
        return ResponseEntity.ok(updateProfile);
    }

    /** 테스트를 위한 임시 자체 로그인 **/
    @PostMapping("/test/login")
    @Operation(summary = "자체 로그인")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "SUCCESS", content = @Content(schema = @Schema(implementation = LoginResponseDto.class)))
            // @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> login(
            @Parameter(description = "자체 로그인 회원 정보를 받아오는 DTO")
            @RequestBody UserReqLoginDTO userReqLoginDTO
    ){
        log.info("[DiaryApiController] login");
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(HttpStatus.CREATED.value(), "[SUCCESS] UserApiController login", userService.login(userReqLoginDTO)));
    }

    @GetMapping("/checkTodayDiary")
    @Operation(summary = "오늘 일기 작성 가능 여부 확인")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "SUCCESS", content = @Content(schema = @Schema(implementation = UserResCheckTodayDiaryDTO.class)))
            // @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> checkTodayDiary(){
        log.info("[DiaryApiController] login");
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(HttpStatus.CREATED.value(), "[SUCCESS] UserApiController checkTodayDiary", userService.checkTodayDiary()));
    }
}
