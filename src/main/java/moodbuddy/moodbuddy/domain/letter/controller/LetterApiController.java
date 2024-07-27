package moodbuddy.moodbuddy.domain.letter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import moodbuddy.moodbuddy.domain.letter.dto.request.LetterReqDTO;
import moodbuddy.moodbuddy.domain.letter.dto.request.LetterReqUpdateDTO;
import moodbuddy.moodbuddy.domain.letter.dto.response.LetterResDetailsDTO;
import moodbuddy.moodbuddy.domain.letter.dto.response.LetterResPageDTO;
import moodbuddy.moodbuddy.domain.letter.dto.response.LetterResSaveDTO;
import moodbuddy.moodbuddy.domain.letter.service.LetterService;
import moodbuddy.moodbuddy.global.common.response.ApiPageResponse;
import moodbuddy.moodbuddy.global.common.response.ApiResponse;
import moodbuddy.moodbuddy.global.common.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Timer;
import java.util.TimerTask;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member/letter")
@Tag(name = "letter-controller", description = "Letter API")
public class LetterApiController {

    private final LetterService letterService;

    @GetMapping("")
    @Operation(summary = "고민상담소 첫 페이지", description = "고민상담소의 첫 페이지로 이동합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "SUCCESS", content = @Content(schema = @Schema(implementation = LetterResPageDTO.class))),
    })
    public ResponseEntity<?> letterPage(){
        return ResponseEntity.ok(letterService.letterPage());
    }

    @PostMapping("/alarm")
    @Operation(summary = "고민상담소 알람 설정", description = "사용자가 작성한 고민 편지에 대한 답장의 알람을 설정합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "SUCCESS", content = @Content(schema = @Schema(implementation = LetterResSaveDTO.class)))
    })
    public ResponseEntity<?> updateLetterAlarm(
            @Parameter(description = "편지의 letterId")
            @RequestBody LetterReqUpdateDTO letterReqUpdateDTO
    ){
        return ResponseEntity.ok(letterService.updateLetterAlarm(letterReqUpdateDTO));
    }

    @PostMapping("/write")
    @Operation(summary = "고민상담소 편지 등록", description = "사용자가 작성한 고민 편지를 등록합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "SUCCESS", content = @Content(schema = @Schema(implementation = LetterResSaveDTO.class))),
    })
    public ResponseEntity<LetterResSaveDTO> write(
            @Parameter(description = "사용자가 작성한 고민 편지와 답장 형식을 담고 있는 DTO")
            @RequestBody LetterReqDTO letterReqDTO
    ) {
        Long kakaoId = JwtUtil.getUserId();
        LetterResSaveDTO letterResSaveDTO = letterService.letterSave(kakaoId, letterReqDTO);

        // 30초 후에 letterAnswerSave 호출 예약
        scheduleLetterAnswerSave(kakaoId, letterResSaveDTO);

        return ResponseEntity.ok(letterResSaveDTO);
    }

    private void scheduleLetterAnswerSave(Long kakaoId, LetterResSaveDTO letterResSaveDTO) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // 30초 후에 실행할 작업
                letterService.letterAnswerSave(kakaoId, letterResSaveDTO);
            }
        }, 25000); // 25초 지연
    }

    @GetMapping("/details/{letterId}")
    @Operation(summary = "고민상담소 편지 내용", description = "사용자가 작성한 편지 내용과 그에 대한 쿼디의 답변 내용을 보여줍니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "SUCCESS", content = @Content(schema = @Schema(implementation = LetterResDetailsDTO.class)))
    })
    public ResponseEntity<?> details(
            @Parameter(description = "편지의 letterId")
            @PathVariable("letterId") Long letterId
    ){
        return ResponseEntity.ok(letterService.letterDetails(letterId));
    }
}
