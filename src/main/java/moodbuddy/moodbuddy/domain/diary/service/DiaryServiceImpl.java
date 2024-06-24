package moodbuddy.moodbuddy.domain.diary.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moodbuddy.moodbuddy.domain.diary.dto.request.*;
import moodbuddy.moodbuddy.domain.diary.dto.response.*;
import moodbuddy.moodbuddy.domain.diary.entity.Diary;
import moodbuddy.moodbuddy.domain.diary.entity.DiaryEmotion;
import moodbuddy.moodbuddy.domain.diary.mapper.DiaryMapper;
import moodbuddy.moodbuddy.domain.diary.repository.DiaryRepository;
import moodbuddy.moodbuddy.domain.diaryImage.entity.DiaryImage;
import moodbuddy.moodbuddy.domain.diaryImage.service.DiaryImageServiceImpl;
import moodbuddy.moodbuddy.global.common.util.JwtUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class DiaryServiceImpl implements DiaryService {
    private final ModelMapper modelMapper;
    private final DiaryRepository diaryRepository;
    private final DiaryImageServiceImpl diaryImageService;

    @Override
    @Transactional
    public DiaryResSaveDTO save(DiaryReqSaveDTO diaryReqSaveDTO) throws IOException {
        log.info("[DiaryService] save");

        String userEmail = JwtUtil.getEmail();
        Diary diary = DiaryMapper.toEntity(diaryReqSaveDTO, userEmail);
        diary = diaryRepository.save(diary);

        if (diaryReqSaveDTO.getDiaryImgList() != null) {
            diaryImageService.saveDiaryImages(diaryReqSaveDTO.getDiaryImgList(), diary);
        }

        return DiaryMapper.toSaveDTO(diary);
    }

    @Override
    @Transactional
    public DiaryResUpdateDTO update(DiaryReqUpdateDTO diaryReqUpdateDTO) throws IOException {
        log.info("[DiaryService] update");

        Diary diary = diaryRepository.findById(diaryReqUpdateDTO.getDiaryId()).get(); // 예외 처리 로직 추가

        diary.updateDiary(diaryReqUpdateDTO.getDiaryTitle(), diaryReqUpdateDTO.getDiaryDate(), diaryReqUpdateDTO.getDiaryContent(), diaryReqUpdateDTO.getDiaryWeather());

        if (diaryReqUpdateDTO.getImagesToDelete() != null) {
            diaryImageService.deleteDiaryImages(diaryReqUpdateDTO.getImagesToDelete());
        }

        if (diaryReqUpdateDTO.getDiaryImgList() != null) {
            diaryImageService.saveDiaryImages(diaryReqUpdateDTO.getDiaryImgList(), diary);
        }

        diary = diaryRepository.save(diary);
        return DiaryMapper.toUpdateDTO(diary);
    }

    @Override
    @Transactional
    public DiaryResDeleteDTO delete(Long diaryId) {
        log.info("[DiaryService] delete");

        Diary diary = diaryRepository.findById(diaryId).get(); // 예외 처리 로직 추가

        List<DiaryImage> images = diaryImageService.findImagesByDiary(diary);
        List<String> imageUrls = images.stream()
                .map(DiaryImage::getDiaryImgURL)
                .collect(Collectors.toList());

        if (!imageUrls.isEmpty()) {
            diaryImageService.deleteDiaryImages(imageUrls);
        }

        diaryRepository.delete(diary);

        return DiaryMapper.toDeleteDTO(diary);
    }

    @Override
    @Transactional
    public DiaryResDraftSaveDTO draftSave(DiaryReqDraftSaveDTO diaryResDraftDTO) {
        return new DiaryResDraftSaveDTO();
    }

    @Override
    public DiaryResDraftFindAllDTO draftFindAll() {
        return new DiaryResDraftFindAllDTO();
    }

    @Override
    public DiaryResDraftDeleteDTO draftDelete(Long diaryId) {
        return null;
    }

    @Override
    public DiaryResDraftDeleteAllDTO draftDeleteAll() {
        return null;
    }

    @Override
    public DiaryResFindOneDTO findOne(Long diaryId) {
        return new DiaryResFindOneDTO();
    }

    @Override
    public DiaryResFindAllDTO findAll() {
        return new DiaryResFindAllDTO();
    }

    @Override
    public DiaryResSimilarFindAllDTO similarFindAll(DiaryEmotion diaryEmotion) {
        return new DiaryResSimilarFindAllDTO();
    }

    /** 추가 메서드 **/

    /**
     * 캘린더 달 이동 (캘린더의 < , > 버튼)
     * @param calendarMonthDTO
     * @return
     */

    // try-catch 문 쓰자!
    @Override
    public DiaryResCalendarMonthListDTO monthlyCalendar(DiaryReqCalendarMonthDTO calendarMonthDTO){
        try{
            // -> userEmail 가져오기
            String userEmail = JwtUtil.getEmail();

            // calendarMonthDTO에서 month 가져오기
            // user_id에 맞는 List<Diary> 중에서, month에서 DateTimeFormatter의 ofPattern을 이용한 LocalDateTime 파싱을 통해 년, 월을 얻어오고,
            // repository 에서는 LIKE 연산자를 이용해서 그 년, 월에 맞는 List<Diary>를 얻어온다
            // (여기서 user_id에 맞는 리스트 전체 조회를 하지 말고, user_id와 년 월에 맞는 리스트만 조회하자)
            // -> 그 Diary 리스트를 그대로 DTO에 넣어서 반환해주면 될 것 같다.
            List<Diary> monthlyDiaryList = diaryRepository.findByUserEmailAndMonth(userEmail, calendarMonthDTO.getCalendarMonth());

            List<DiaryResCalendarMonthDTO> diaryResCalendarMonthDTOList = monthlyDiaryList.stream()
                    .map(diary -> DiaryResCalendarMonthDTO.builder()
                            .diaryCreatedTime(diary.getCreatedTime())
                            .diaryEmotion(diary.getDiaryEmotion())
                            .build())
                    .collect(Collectors.toList());

            return DiaryResCalendarMonthListDTO.builder()
                    .diaryResCalendarMonthDTOList(diaryResCalendarMonthDTOList)
                    .build();
        } catch (Exception e) {
            log.error("[DiaryServiceImpl] monthlyCalendar", e);
            throw new RuntimeException("월간 캘린더 데이터를 가져오는 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 일기 한 줄 요약 보여주기
     * @param calendarSummaryDTO
     * @return
     */
    // "일기 작성할 때" , 그 일기 내용을 Diary 테이블의 content 컬럼에 저장하고, 문서 요약 API에 보내서 요약된 내용을 summary 컬럼에 저장한다.
    @Override
    public DiaryResCalendarSummaryDTO summary(DiaryReqCalendarSummaryDTO calendarSummaryDTO) {
        try {
            // userEmail 가져오기
            String userEmail = JwtUtil.getEmail();

            // userEmail와 calendarSummaryDTO에서 가져온 day와 일치하는 Diary 하나를 가져온다.
            Optional<Diary> summaryDiary = diaryRepository.findByUserEmailAndDay(userEmail, calendarSummaryDTO.getCalendarDay());

            // summaryDiary가 존재하면 DTO를 반환하고, 그렇지 않으면 NoSuchElementException 예외 처리
            return summaryDiary.map(diary -> DiaryResCalendarSummaryDTO.builder()
                            .diaryTitle(diary.getDiaryTitle())
                            .diarySummary(diary.getDiarySummary())
                            .build())
                    .orElseThrow(() -> new NoSuchElementException("해당 날짜에 대한 일기를 찾을 수 없습니다."));
        } catch(Exception e){
            log.error("[DiaryServiceImpl] summary", e);
            throw new RuntimeException("캘린더 요약 데이터를 가져오는 중 오류가 발생했습니다.", e);
        }
    }
}
