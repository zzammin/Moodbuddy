package moodbuddy.moodbuddy.domain.diary.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import moodbuddy.moodbuddy.domain.diary.dto.request.*;
import moodbuddy.moodbuddy.domain.diary.dto.response.*;
import moodbuddy.moodbuddy.domain.diary.entity.Diary;
import moodbuddy.moodbuddy.domain.diary.mapper.DiaryMapper;
import moodbuddy.moodbuddy.domain.diary.repository.DiaryRepository;
import moodbuddy.moodbuddy.domain.diaryImage.entity.DiaryImage;
import moodbuddy.moodbuddy.domain.diaryImage.service.DiaryImageServiceImpl;
import moodbuddy.moodbuddy.domain.user.entity.User;
import moodbuddy.moodbuddy.domain.user.repository.UserRepository;
import moodbuddy.moodbuddy.global.common.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@Slf4j
public class DiaryServiceImpl implements DiaryService {

    private final UserRepository userRepository;
    private final DiaryRepository diaryRepository;
    private final DiaryImageServiceImpl diaryImageService;
    private final WebClient naverWebClient;

    @Autowired
    public DiaryServiceImpl(UserRepository userRepository,
                            DiaryRepository diaryRepository,
                            DiaryImageServiceImpl diaryImageService,
                            @Qualifier("naverWebClient") WebClient naverWebClient) {
        this.userRepository = userRepository;
        this.diaryRepository = diaryRepository;
        this.diaryImageService = diaryImageService;
        this.naverWebClient = naverWebClient;
    }

    @Override
    @Transactional
    public DiaryResDetailDTO save(DiaryReqSaveDTO diaryReqSaveDTO) throws IOException {
        log.info("[DiaryServiceImpl] save");
        Long userId = JwtUtil.getUserId();

//        String summary = summarize(diaryReqSaveDTO.getDiaryContent()); // 일기 내용 요약 결과
        String summary = "dasdasd"; // 일기 내용 요약 결과
        Diary diary = DiaryMapper.toEntity(diaryReqSaveDTO, userId, summary);

        diary = diaryRepository.save(diary);

//        saveDocument(diary);

        if (diaryReqSaveDTO.getDiaryImgList() != null) {
            diaryImageService.saveDiaryImages(diaryReqSaveDTO.getDiaryImgList(), diary);
        }

        // 일기 작성하면 편지지 개수 늘려주기
        letterNumPlus(userId);

        return DiaryMapper.toDetailDTO(diary);
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
    public DiaryResDetailDTO update(DiaryReqUpdateDTO diaryReqUpdateDTO) throws IOException {
        log.info("[DiaryServiceImpl] update");

        Optional<Diary> optionalDiary = diaryRepository.findById(diaryReqUpdateDTO.getDiaryId());// 예외 처리 로직 추가
        if(!optionalDiary.isPresent()) {
            // 에러 추가
        }

        Diary findDiary = optionalDiary.get();
        findDiary.updateDiary(diaryReqUpdateDTO.getDiaryTitle(), diaryReqUpdateDTO.getDiaryDate(), diaryReqUpdateDTO.getDiaryContent(), diaryReqUpdateDTO.getDiaryWeather());

        if (diaryReqUpdateDTO.getImagesToDelete() != null) {
            diaryImageService.deleteDiaryImages(diaryReqUpdateDTO.getImagesToDelete());
        }

        if (diaryReqUpdateDTO.getDiaryImgList() != null) {
            diaryImageService.saveDiaryImages(diaryReqUpdateDTO.getDiaryImgList(), findDiary);
        }

//        saveDocument(diary);

        return DiaryMapper.toDetailDTO(findDiary);
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
//        diaryElasticsearchRepository.deleteById(diaryId);
    }

    @Override
    @Transactional
    public DiaryResDetailDTO draftSave(DiaryReqDraftSaveDTO diaryReqDraftSaveDTO) throws IOException {
        log.info("[DiaryServiceImpl] draftSave");
        Long userId = JwtUtil.getUserId();

        Diary diary = DiaryMapper.toDraftDiaryEntity(diaryReqDraftSaveDTO, userId);
        diary = diaryRepository.save(diary);

        if (diaryReqDraftSaveDTO.getDiaryImgList() != null) {
            diaryImageService.saveDiaryImages(diaryReqDraftSaveDTO.getDiaryImgList(), diary);
        }

        return DiaryMapper.toDetailDTO(diary);
    }

    @Override
    public DiaryResDraftFindAllDTO draftFindAll() {
        log.info("[DiaryServiceImpl] draftFindAll");
        Long userId = JwtUtil.getUserId();
        return diaryRepository.draftFindAllByUserId(userId);
    }

    @Override
    @Transactional
    public void draftSelectDelete(DiaryReqDraftSelectDeleteDTO diaryReqDraftSelectDeleteDTO) {
        log.info("[DiaryServiceImpl] draftSelectDelete");
        Long userId = JwtUtil.getUserId();

        List<Diary> diariesToDelete = diaryRepository.findAllById(diaryReqDraftSelectDeleteDTO.getDiaryIdList()).stream()
                .filter(diary -> diary.getUserId().equals(userId))
                .collect(Collectors.toList());

        diaryRepository.deleteAll(diariesToDelete);
    }


    @Override
    public DiaryResFindOneDTO findOneByDiaryId(Long diaryId) {
        log.info("[DiaryServiceImpl] findOneByDiaryId");
        Long userId = JwtUtil.getUserId();

        // [추가해야 할 내]
        // diaryId가 존재하는 Id 값인지 확인하는 예외처리
        // userEmail하고 Diary의 userEmail하고 같은 지 확인하는 예외처리

        return diaryRepository.findOneByDiaryId(diaryId);
    }

    @Override
    public Page<DiaryResFindOneDTO> findAllWithPageable(Pageable pageable) {
        log.info("[DiaryServiceImpl] findAllWithPageable");
        Long userId = JwtUtil.getUserId();

        return diaryRepository.findAllByUserIdWithPageable(userId, pageable);
    }

    @Override
    public Page<DiaryResFindOneDTO> findAllByEmotionWithPageable(DiaryReqEmotionDTO diaryReqEmotionDTO, Pageable pageable) {
        log.info("[DiaryServiceImpl] findAllByEmotionWithPageable");
        Long userId = JwtUtil.getUserId();

        return diaryRepository.findAllByEmotionWithPageable(diaryReqEmotionDTO.getDiaryEmotion(), userId, pageable);
    }

    @Override
    public Page<DiaryResFindOneDTO> findAllByFilter(DiaryReqFilterDTO diaryReqFilterDTO, Pageable pageable) {
        log.info("[DiaryServiceImpl] findAllByFilter");
        Long userId = JwtUtil.getUserId();

        return diaryRepository.findAllByFilterWithPageable(diaryReqFilterDTO, userId, pageable);
    }

    /** 추가 메서드 **/
//    private void saveDocument(Diary diary) {
//        DiaryDocument diaryDocument = convertToDocument(diary);
//        diaryElasticsearchRepository.save(diaryDocument);
//    }
//
//    private DiaryDocument convertToDocument(Diary diary) {
//        return DiaryDocument.builder()
//                .id(diary.getId())
//                .diaryTitle(diary.getDiaryTitle())
//                .diaryDate(diary.getDiaryDate())
//                .diaryContent(diary.getDiaryContent())
//                .diaryWeather(diary.getDiaryWeather())
//                .diaryEmotion(diary.getDiaryEmotion())
//                .diaryStatus(diary.getDiaryStatus())
//                .userId(diary.getUserId())
//                .build();
//    }


    /** =========================================================  위 정목 아래 재민  ========================================================= **/

    @Override
    @Transactional
    public DiaryResCalendarMonthListDTO monthlyCalendar(DiaryReqCalendarMonthDTO calendarMonthDTO){
        log.info("[DiaryService] monthlyCalendar");
        try{
            // -> userID 가져오기
            Long userId = JwtUtil.getUserId();

            // calendarMonthDTO에서 month 가져오기
            // user_id에 맞는 List<Diary> 중에서, month에서 DateTimeFormatter의 ofPattern을 이용한 LocalDateTime 파싱을 통해 년, 월을 얻어오고,
            // repository 에서는 LIKE 연산자를 이용해서 그 년, 월에 맞는 List<Diary>를 얻어온다
            // (여기서 user_id에 맞는 리스트 전체 조회를 하지 말고, user_id와 년 월에 맞는 리스트만 조회하자)
            // -> 그 Diary 리스트를 그대로 DTO에 넣어서 반환해주면 될 것 같다.
            List<Diary> monthlyDiaryList = diaryRepository.findByUserIdAndMonth(userId, calendarMonthDTO.getCalendarMonth());

            List<DiaryResCalendarMonthDTO> diaryResCalendarMonthDTOList = monthlyDiaryList.stream()
                    .map(diary -> DiaryResCalendarMonthDTO.builder()
                            .diaryDate(diary.getDiaryDate())
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
            Long userId = JwtUtil.getUserId();

            // userEmail와 calendarSummaryDTO에서 가져온 day와 일치하는 Diary 하나를 가져온다.
            Optional<Diary> summaryDiary = diaryRepository.findByUserIdAndDay(userId, calendarSummaryDTO.getCalendarDay());

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
