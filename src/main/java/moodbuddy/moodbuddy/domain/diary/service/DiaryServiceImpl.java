package moodbuddy.moodbuddy.domain.diary.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import moodbuddy.moodbuddy.domain.user.entity.User;
import moodbuddy.moodbuddy.domain.user.repository.UserRepository;
import moodbuddy.moodbuddy.global.common.util.JwtUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class DiaryServiceImpl implements DiaryService {
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final DiaryRepository diaryRepository;
    private final DiaryImageServiceImpl diaryImageService;
    private final WebClient naverWebClient; // 인스턴스 이름을 NaverCloudConfig의 WebClient의 빈 이름(naverWebClient)과 맞추고 DI 진행

    @Override
    @Transactional
    public DiaryResSaveDTO save(DiaryReqSaveDTO diaryReqSaveDTO) throws IOException {
        log.info("[DiaryServiceImpl] save");
        Long userId = JwtUtil.getMemberId();
        String userEmail = JwtUtil.getEmail();
        String summary = summarize(diaryReqSaveDTO.getDiaryContent()); // 일기 내용 요약 결과

        Diary diary = DiaryMapper.toDiaryEntity(diaryReqSaveDTO, userEmail, summary);
        diary = diaryRepository.save(diary);

        if (diaryReqSaveDTO.getDiaryImgList() != null) {
            diaryImageService.saveDiaryImages(diaryReqSaveDTO.getDiaryImgList(), diary);
        }

        // 일기 작성하면 편지지 개수 늘려주기
        letterNumPlus(userId);

        return DiaryMapper.toSaveDTO(diary);
    }

    @Override
    @Transactional
    public void letterNumPlus(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isPresent()){
            int letterNums = optionalUser.get().getUserLetterNums() + 1;
            userRepository.updateLetterNumsById(userId, letterNums);
        }
    }

    @Override
    @Transactional
    public DiaryResUpdateDTO update(DiaryReqUpdateDTO diaryReqUpdateDTO) throws IOException {
        log.info("[DiaryServiceImpl] update");

        Diary diary = diaryRepository.findById(diaryReqUpdateDTO.getDiaryId()).get(); // 예외 처리 로직 추가

        diary.updateDiary(diaryReqUpdateDTO.getDiaryTitle(), diaryReqUpdateDTO.getDiaryDate(), diaryReqUpdateDTO.getDiaryContent(), diaryReqUpdateDTO.getDiaryWeather());

        if (diaryReqUpdateDTO.getImagesToDelete() != null) {
            diaryImageService.deleteDiaryImages(diaryReqUpdateDTO.getImagesToDelete());
        }

        if (diaryReqUpdateDTO.getDiaryImgList() != null) {
            diaryImageService.saveDiaryImages(diaryReqUpdateDTO.getDiaryImgList(), diary);
        }

        return DiaryMapper.toUpdateDTO(diary);
    }

    @Override
    @Transactional
    public void delete(Long diaryId) {
        log.info("[DiaryServiceImpl] delete");

        Diary diary = diaryRepository.findById(diaryId).get(); // 예외 처리 로직 추가

        List<DiaryImage> images = diaryImageService.findImagesByDiary(diary);
        List<String> imageUrls = images.stream()
                .map(DiaryImage::getDiaryImgURL)
                .collect(Collectors.toList());

        if (!imageUrls.isEmpty()) {
            diaryImageService.deleteDiaryImages(imageUrls);
        }

        diaryRepository.delete(diary);
    }

    @Override
    @Transactional
    public DiaryResDraftSaveDTO draftSave(DiaryReqDraftSaveDTO diaryReqDraftSaveDTO) throws IOException {
        log.info("[DiaryServiceImpl] draftSave");
        String userEmail = JwtUtil.getEmail();

        Diary diary = DiaryMapper.toDraftDiaryEntity(diaryReqDraftSaveDTO, userEmail);
        diary = diaryRepository.save(diary);

        if (diaryReqDraftSaveDTO.getDiaryImgList() != null) {
            diaryImageService.saveDiaryImages(diaryReqDraftSaveDTO.getDiaryImgList(), diary);
        }

        return DiaryMapper.toDraftSaveDTO(diary);
    }

    @Override
    public DiaryResDraftFindAllDTO draftFindAll() {
        log.info("[DiaryServiceImpl] draftFindAll");
        String userEmail = JwtUtil.getEmail();
        return diaryRepository.draftFindAll(userEmail);
    }

    @Override
    @Transactional
    public void draftSelectDelete(DiaryReqDraftSelectDeleteDTO diaryReqDraftSelectDeleteDTO) {
        log.info("[DiaryServiceImpl] draftSelectDelete");
        String userEmail = JwtUtil.getEmail();

        List<Diary> diariesToDelete = diaryRepository.findAllById(diaryReqDraftSelectDeleteDTO.getDiaryIdList()).stream()
                .filter(diary -> diary.getUserEmail().equals(userEmail))
                .collect(Collectors.toList());

        diaryRepository.deleteAll(diariesToDelete);
    }


    @Override
    public DiaryResFindOneDTO findOne(Long diaryId) {
        log.info("[DiaryServiceImpl] findOne");
        String userEmail = JwtUtil.getEmail();

        Optional<Diary> optionalDiary = diaryRepository.findById(diaryId);

        if(!optionalDiary.isPresent()) {
            // 에러 반화
        }

        if(!optionalDiary.get().getUserEmail().equals(userEmail)) {
            // 에러 반환
        }
        return DiaryMapper.toFindOneDTO(optionalDiary.get());
    }

    @Override
    public DiaryResFindAllDTO findAll() {
        log.info("[DiaryServiceImpl] findAll");
        String userEmail = JwtUtil.getEmail();

        return new DiaryResFindAllDTO();
    }

    @Override
    public DiaryResSimilarFindAllDTO similarFindAll(DiaryEmotion diaryEmotion) {
        return new DiaryResSimilarFindAllDTO();
    }

    /** 추가 메서드 **/

    @Override
    @Transactional
    public DiaryResCalendarMonthListDTO monthlyCalendar(DiaryReqCalendarMonthDTO calendarMonthDTO){
        log.info("[DiaryService] monthlyCalendar");
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
            log.error("[DiaryService] monthlyCalendar", e);
            throw new RuntimeException("[DiaryService] monthlyCalendar error", e);
        }
    }

    @Override
    @Transactional
    public DiaryResCalendarSummaryDTO summary(DiaryReqCalendarSummaryDTO calendarSummaryDTO) {
        log.info("[DiaryService] summary");
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
            log.error("[DiaryService] summary", e);
            throw new RuntimeException("[DiaryService] summary error", e);
        }
    }

    @Override
    public Map<String,Object> getRequestBody(String content){
        Map<String, Object> documentObject = new HashMap<>(); // DocumentObject 를 위한 Map 생성
        documentObject.put("content", content); // 요약할 내용 (일기 내용)

        Map<String, Object> optionObject = new HashMap<>(); // OptionObject 를 위한 Map 생성
        optionObject.put("language", "ko"); // 한국어
        optionObject.put("summaryCount", 1); // 요약 줄 수 (1줄)

        Map<String, Object> requestBody = new HashMap<>(); // Request Body 를 위한 Map 생성
        requestBody.put("document", documentObject);
        requestBody.put("option", optionObject);
        return requestBody;
    }

    @Override
    public String summarize(String content){
        log.info("[DiaryService] summarize");
        try {
            // getRequestBody 메소드에 일기 내용을 전달하여, Request Body 를 위한 Map 생성
            Map<String,Object> requestBody = getRequestBody(content);

            // naverWebClient 를 사용하여 API 호출
            String response = naverWebClient.post()
                    .body(BodyInserters.fromValue(requestBody)) // 1. BodyInserters 가 무엇인가?
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response);
            return jsonNode.path("summary").asText(); // summary 결과
        } catch (Exception e){
            log.error("[DiaryService] summarize error",e);
            throw new RuntimeException("[DiaryService] monthlyCalendar error", e);
        }
    }
}
