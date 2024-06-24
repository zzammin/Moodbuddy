package moodbuddy.moodbuddy.domain.letter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import moodbuddy.moodbuddy.domain.letter.dto.LetterReqDTO;
import moodbuddy.moodbuddy.domain.letter.service.LetterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/letter")
@Tag(name = "letter-controller" , description = "Letter API")
public class LetterApiController {

    private final LetterService letterService;

    @GetMapping("")
    @Operation(summary = "고민상담소 첫 페이지", description = "고민상담소의 첫 페이지로 이동합니다.")
    public ResponseEntity<?> letterPage(){
        return ResponseEntity.ok(letterService.letterPage());
    }

    @PostMapping("/write")
    @Operation(summary = "고민상담소 편지 등록", description = "사용자가 작성한 고민 편지를 등록합니다.")
    public ResponseEntity<?> write(
            @Parameter(description = "사용자가 작성한 고민 편지와 답장 형식을 담고 있는 DTO")
            LetterReqDTO letterRequestDTO
    ){
        return null;
    }

    @GetMapping("/details")
    @Operation(summary = "고민상담소 편지 내용", description = "사용자가 작성한 편지 내용과 그에 대한 쿼디의 답변 내용을 보여줍니다.")
    public ResponseEntity<?> details(
            @Parameter(description = "편지의 letterId")
            @RequestParam Long letterId
    ){
        return ResponseEntity.ok(letterService.details(letterId));
    }
}
