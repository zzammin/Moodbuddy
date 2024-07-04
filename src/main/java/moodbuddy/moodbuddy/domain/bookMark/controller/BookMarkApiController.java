package moodbuddy.moodbuddy.domain.bookMark.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moodbuddy.moodbuddy.domain.bookMark.dto.response.BookMarkResToggleDTO;
import moodbuddy.moodbuddy.domain.bookMark.service.BookMarkServiceImpl;
import moodbuddy.moodbuddy.domain.diary.dto.response.DiaryResDetailDTO;
import moodbuddy.moodbuddy.global.common.response.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/member/bookMark")
@Tag(name = "BookMark", description = "북마크 관련 API")
@RequiredArgsConstructor
@Slf4j
public class BookMarkApiController {
    private final BookMarkServiceImpl bookMarkService;
    /** 구현 완료 **/
    @PostMapping("/toggle/{diaryId}")
    @Operation(summary = "북마크 토글", description = "북마크 토글 북마크 성공(true) / 북마크 취소(false)")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "SUCCESS", content = @Content(schema = @Schema(implementation = BookMarkResToggleDTO.class)))
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> toggle(@Parameter(description = "일기 고유 식별자")
                                  @PathVariable("diaryId") Long diaryId) throws IOException {
        log.info("[BookMarkApiController] toggle");
        BookMarkResToggleDTO result = bookMarkService.toggle(diaryId);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(HttpStatus.CREATED.value(), "[SUCCESS] BookMarkApiController toggle", result));
    }

    @GetMapping("/findAll")
    @Operation(summary = "북마크 전체 조회", description = "북마크 전체 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "SUCCESS", content = @Content(schema = @Schema(implementation = DiaryResDetailDTO.class)))
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> findAll(Pageable pageable) throws IOException {
        log.info("[BookMarkApiController] findAll");
        Page<DiaryResDetailDTO> result = bookMarkService.bookMarkFindAllByWithPageable(pageable);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(HttpStatus.CREATED.value(), "[SUCCESS] BookMarkApiController findAll", result));
    }
}
