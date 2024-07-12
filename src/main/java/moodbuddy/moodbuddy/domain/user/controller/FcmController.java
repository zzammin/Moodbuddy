//package moodbuddy.moodbuddy.domain.user.controller;
//
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.Parameter;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import moodbuddy.moodbuddy.domain.user.dto.fcm.FcmReqDTO;
//import moodbuddy.moodbuddy.domain.user.dto.fcm.FcmResDTO;
//import moodbuddy.moodbuddy.domain.user.service.FcmService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequiredArgsConstructor
//@Slf4j
//@RequestMapping("/api/v1/alarm")
//public class FcmController {
//
//    private final FcmService fcmService;
//
//    // 알림 보내기
//    @PostMapping("/send")
//    @Operation(description = "FCM 알림 전송")
//    @ApiResponses(value = {
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "SUCCESS", content = @Content(schema = @Schema(implementation = FcmResDTO.class)))
//            // @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
//    })
//    public ResponseEntity<?> pushAlarm(
//            @Parameter(description = "FCM Token과 메시지 title, body를 담고 있는 DTO")
//            @RequestBody FcmReqDTO fcmReqDTO
//    ){
//        return ResponseEntity.ok(fcmService.sendMessage(fcmReqDTO));
//    }
//}
