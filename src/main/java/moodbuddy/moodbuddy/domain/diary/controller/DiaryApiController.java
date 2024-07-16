package moodbuddy.moodbuddy.domain.diary.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moodbuddy.moodbuddy.domain.diary.dto.request.*;
import moodbuddy.moodbuddy.domain.diary.dto.response.*;
import moodbuddy.moodbuddy.domain.diary.entity.DiaryEmotion;
import moodbuddy.moodbuddy.domain.diary.entity.DiarySubject;
import moodbuddy.moodbuddy.domain.diary.service.DiaryService;
import moodbuddy.moodbuddy.global.common.response.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/member/diary")
@Tag(name = "Diary", description = "일기 관련 API")
@RequiredArgsConstructor
@Slf4j
public class DiaryApiController {
    private final DiaryService diaryService;

    /** 구현 완료 **/
    @PostMapping("/save")
    @Operation(summary = "일기 작성", description = "새로운 일기를 작성합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "SUCCESS", content = @Content(schema = @Schema(implementation = DiaryResDetailDTO.class)))
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> save(@Parameter(description = "일기 정보를 담고 있는 DTO")
                                      @ModelAttribute DiaryReqSaveDTO diaryReqSaveDTO) throws IOException {
        log.info("[DiaryApiController] save");
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(HttpStatus.CREATED.value(), "[SUCCESS] DiaryApiController save", diaryService.save(diaryReqSaveDTO)));
    }
    /** 구현 완료 **/
    @PostMapping("/update")
    @Operation(summary = "일기 수정", description = "기존 일기를 수정합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "SUCCESS", content = @Content(schema = @Schema(implementation = DiaryResDetailDTO.class)))
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> update(@Parameter(description = "수정된 일기 정보를 담고 있는 DTO")
                                        @ModelAttribute DiaryReqUpdateDTO diaryReqUpdateDTO) throws IOException {
        log.info("[DiaryApiController] update");
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(HttpStatus.CREATED.value(), "[SUCCESS] DiaryApiController update", diaryService.update(diaryReqUpdateDTO)));
    }
    /** 구현 완료 **/
    @DeleteMapping("/delete/{diaryId}")
    @Operation(summary = "일기 삭제", description = "기존 일기를 삭제합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "SUCCESS", content = @Content(schema = @Schema(implementation = DiaryResDetailDTO.class)))
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> delete(@Parameter(description = "일기 고유 식별자")
                                        @PathVariable("diaryId") Long diaryId) throws IOException {
        log.info("[DiaryApiController] delete");
        diaryService.delete(diaryId);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(HttpStatus.CREATED.value(), "[SUCCESS] DiaryApiController delete"));
    }
    /** 구현 완료 **/
    @PostMapping("/draftSave")
    @Operation(summary = "일기 임시 저장", description = "일기를 임시 저장합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "SUCCESS", content = @Content(schema = @Schema(implementation = DiaryResDetailDTO.class)))
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> draftSave(@Parameter(description = "임시 저장 일기 정보를 담고 있는 DTO")
                                           @ModelAttribute DiaryReqSaveDTO diaryReqSaveDTO) throws IOException {
        log.info("[DiaryApiController] draftSave");
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(HttpStatus.CREATED.value(), "[SUCCESS] DiaryApiController draftSave", diaryService.draftSave(diaryReqSaveDTO)));
    }
    /** 구현 완료 **/
    @GetMapping("/draftFindAll")
    @Operation(summary = "임시 저장 일기 목록 조회", description = "임시 저장 일기를 모두 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "SUCCESS", content = @Content(schema = @Schema(implementation = DiaryResDraftFindAllDTO.class)))
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> draftFindAll() {
        log.info("[DiaryApiController] draftFindAll");
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(HttpStatus.CREATED.value(), "[SUCCESS] DiaryApiController draftFindAll", diaryService.draftFindAll()));
    }
    /** 구현 완료 **/
    @DeleteMapping("/draftSelectDelete")
    @Operation(summary = "임시 저장 일기 선택 삭제", description = "임시 저장 일기를 선택해서 삭제합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "SUCCESS", content = @Content(schema = @Schema(implementation = DiaryResDraftFindAllDTO.class)))
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> draftSelectDelete(@Parameter(description = "삭제할 임시 저장 일기 고유 식별자를 담고 있는 DTO")
                                                   @RequestBody DiaryReqDraftSelectDeleteDTO diaryReqDraftSelectDeleteDTO) throws IOException {
        log.info("[DiaryApiController] draftSelectDelete");
        diaryService.draftSelectDelete(diaryReqDraftSelectDeleteDTO);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(HttpStatus.CREATED.value(), "[SUCCESS] DiaryApiController draftSelectDelete"));
    }
    /** 구현 완료 **/
    @GetMapping("/findOne/{diaryId}")
    @Operation(summary = "일기 하나 조회", description = "일기 하나를 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "SUCCESS", content = @Content(schema = @Schema(implementation = DiaryResDetailDTO.class)))
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> findOneByDiaryId(@Parameter(description = "일기 고유 식별자")
                                                  @PathVariable("diaryId") Long diaryId) {
        log.info("[DiaryApiController] findOne");
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(HttpStatus.CREATED.value(), "[SUCCESS] DiaryApiController findOne", diaryService.findOneByDiaryId(diaryId)));
    }
    /** 구현 완료 **/
    @GetMapping("/findAllPageable")
    @Operation(summary = "일기 전체 조회", description = "일기를 모두 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "SUCCESS", content = @Content(schema = @Schema(implementation = DiaryResDetailDTO.class)))
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> findAll(Pageable pageable) {
        log.info("[DiaryApiController] findAllPageable");
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(HttpStatus.CREATED.value(), "[SUCCESS] DiaryApiController findAllPageable", diaryService.findAll(pageable)));
    }
    /** 구현 완료 **/
    @GetMapping("/findAllByEmotionWithPageable")
    @Operation(summary = "일기 감정으로 일기 전체 조회", description = "감정이 똑같은 일기를 모두 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "SUCCESS", content = @Content(schema = @Schema(implementation = DiaryResDetailDTO.class))),
            // @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> findAllByEmotion(
            @Parameter(description = "검색하고 싶은 감정(HAPPY, ANGRY, AVERSION, SURPRISED, CALMNESS, DEPRESSION, FEAR)", example = "HAPPY")
            @RequestParam("diaryEmotion") DiaryEmotion diaryEmotion, Pageable pageable) {
        log.info("[DiaryApiController] findAllByEmotionWithPageable");
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(HttpStatus.CREATED.value(), "[SUCCESS] DiaryApiController findAllByEmotionWithPageable", diaryService.findAllByEmotion(diaryEmotion, pageable)));
    }
    /** 구현 완료(키워드 제외) **/
    @GetMapping("/findAllByFilter")
    @Operation(summary = "일기 필터링으로 전체 조회", description = "여러 필터링을 선택하여 일기를 모두 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "SUCCESS", content = @Content(schema = @Schema(implementation = DiaryResDetailDTO.class)))
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> findAllByFilter(@Parameter(description = "필터링 데이터를 담고 있는 DTO")
                                                 @RequestParam(value = "keyWord", required = false) String keyWord,
                                                 @RequestParam(value = "year", required = false) Integer year,
                                                 @RequestParam(value = "month", required = false) Integer month,
                                                 @RequestParam(value = "diaryEmotion", required = false) DiaryEmotion diaryEmotion,
                                                 @RequestParam(value = "diarySubject", required = false) DiarySubject diarySubject, Pageable pageable) {
        log.info("[DiaryApiController] findAllByFilter");
        DiaryReqFilterDTO diaryReqFilterDTO = getDiaryReqFilterDTO(keyWord, year, month, diaryEmotion, diarySubject);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(HttpStatus.CREATED.value(), "[SUCCESS] DiaryApiController findAllByFilter", diaryService.findAllByFilter(diaryReqFilterDTO, pageable)));
    }

    private static DiaryReqFilterDTO getDiaryReqFilterDTO(String keyWord, Integer year, Integer month, DiaryEmotion diaryEmotion, DiarySubject diarySubject) {
        DiaryReqFilterDTO diaryReqFilterDTO = DiaryReqFilterDTO.builder()
                .keyWord(keyWord)
                .year(year)
                .month(month)
                .diaryEmotion(diaryEmotion)
                .diarySubject(diarySubject)
                .build();
        return diaryReqFilterDTO;
    }
}
