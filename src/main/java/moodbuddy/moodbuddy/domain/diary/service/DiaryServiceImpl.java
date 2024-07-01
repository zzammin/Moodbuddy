package moodbuddy.moodbuddy.domain.diary.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moodbuddy.moodbuddy.domain.diary.dto.request.*;
import moodbuddy.moodbuddy.domain.diary.dto.response.*;
import moodbuddy.moodbuddy.domain.diary.entity.Diary;
import moodbuddy.moodbuddy.domain.diary.mapper.DiaryMapper;
import moodbuddy.moodbuddy.domain.diary.repository.DiaryRepository;
import moodbuddy.moodbuddy.domain.diaryImage.entity.DiaryImage;
import moodbuddy.moodbuddy.domain.diaryImage.service.DiaryImageServiceImpl;
import moodbuddy.moodbuddy.domain.profile.repository.ProfileRepository;
import moodbuddy.moodbuddy.domain.profileImage.repository.ProfileImageRepository;
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
import reactor.core.publisher.Mono;

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

    public DiaryServiceImpl(UserRepository userRepository, DiaryRepository diaryRepository,
                            DiaryImageServiceImpl diaryImageService,
                           @Qualifier("naverWebClient") WebClient naverWebClient){
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

        String summary = summarize(diaryReqSaveDTO.getDiaryContent()); // 일기 내용 요약 결과
        Diary diary = DiaryMapper.toDiaryEntity(diaryReqSaveDTO, userId, summary);

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
    public void letterNumPlus(Long kakaoId) {
        log.info("kakaoId : "+kakaoId);
        Optional<User> optionalUser = userRepository.findByKakaoId(kakaoId);
        optionalUser.ifPresent(user -> {
            log.info("user.getUserLetterNums() : "+user.getUserLetterNums());
            int letterNums = user.getUserLetterNums() == null ? 1 : user.getUserLetterNums() + 1;
            log.info("letterNums : "+letterNums);
            userRepository.updateLetterNumsByKakaoId(kakaoId,letterNums);
        });
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
        String summary = summarize(diaryReqUpdateDTO.getDiaryContent()); // 일기 내용 요약 결과

        // 감정 분석 로직 추가 필요

        findDiary.updateDiary(diaryReqUpdateDTO.getDiaryTitle(), diaryReqUpdateDTO.getDiaryDate(), diaryReqUpdateDTO.getDiaryContent(), diaryReqUpdateDTO.getDiaryWeather(), summary);

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
    public DiaryResDetailDTO draftSave(DiaryReqSaveDTO diaryReqSaveDTO) throws IOException {
        log.info("[DiaryServiceImpl] draftSave");
        Long userId = JwtUtil.getUserId();

        Diary diary = DiaryMapper.toDraftEntity(diaryReqSaveDTO, userId);
        diary = diaryRepository.save(diary);

        if (diaryReqSaveDTO.getDiaryImgList() != null) {
            diaryImageService.saveDiaryImages(diaryReqSaveDTO.getDiaryImgList(), diary);
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

        for(int i=0; i<diariesToDelete.size(); i++) {
            System.out.println(diariesToDelete.get(i).getId());
        }

        // 관련된 이미지 삭제
        for (Diary diary : diariesToDelete) {
            List<DiaryImage> images = diaryImageService.findImagesByDiary(diary);

            for(int i=0; i<images.size(); i++) {
                System.out.println(images.get(i).getId());
            }

            List<String> imageUrls = images.stream()
                    .map(DiaryImage::getDiaryImgURL)
                    .collect(Collectors.toList());

            for(int i=0; i<imageUrls.size(); i++) {
                System.out.println(imageUrls.get(i));
            }

            if (!imageUrls.isEmpty()) {
                diaryImageService.deleteDiaryImages(imageUrls);
            }
        }

        // 일기 삭제
        diaryRepository.deleteAll(diariesToDelete);
    }


    @Override
    public DiaryResDetailDTO findOneByDiaryId(Long diaryId) {
        log.info("[DiaryServiceImpl] findOneByDiaryId");
        Long userId = JwtUtil.getUserId();

        // [추가해야 할 내]
        // diaryId가 존재하는 Id 값인지 확인하는 예외처리
        // userEmail하고 Diary의 userEmail하고 같은 지 확인하는 예외처리

        return diaryRepository.findOneByDiaryId(diaryId);
    }

    @Override
    public Page<DiaryResDetailDTO> findAllWithPageable(Pageable pageable) {
        log.info("[DiaryServiceImpl] findAllWithPageable");
        Long userId = JwtUtil.getUserId();

        return diaryRepository.findAllByUserIdWithPageable(userId, pageable);
    }

    @Override
    public Page<DiaryResDetailDTO> findAllByEmotionWithPageable(DiaryReqEmotionDTO diaryReqEmotionDTO, Pageable pageable) {
        log.info("[DiaryServiceImpl] findAllByEmotionWithPageable");
        Long userId = JwtUtil.getUserId();

        return diaryRepository.findAllByEmotionWithPageable(diaryReqEmotionDTO.getDiaryEmotion(), userId, pageable);
    }

    @Override
    public Page<DiaryResDetailDTO> findAllByFilter(DiaryReqFilterDTO diaryReqFilterDTO, Pageable pageable) {
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
    public String summarize(String content) {
        log.info("[DiaryService] summarize");
        try {
            log.info("content : " + content);

            // getRequestBody 메소드에 일기 내용을 전달하여, Request Body 를 위한 Map 생성
            Map<String, Object> requestBody = getRequestBody(content);
            log.info("requestBody : " + requestBody);

            // naverWebClient 를 사용하여 API 호출
            String response = naverWebClient.post()
                    .body(BodyInserters.fromValue(requestBody))
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), clientResponse -> {
                        return clientResponse.bodyToMono(String.class).flatMap(errorBody -> {
                            log.error("API 요청 실패 - 상태 코드: " + clientResponse.statusCode());
                            log.error("오류 본문: " + errorBody);
                            return Mono.error(new RuntimeException("API 요청 실패 - 상태 코드: " + clientResponse.statusCode() + ", 오류 본문: " + errorBody));
                        });
                    })
                    .bodyToMono(String.class)
                    .block();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response);
            return jsonNode.path("summary").asText(); // summary 결과
        } catch (Exception e) {
            log.error("[DiaryService] summarize error", e);
            throw new RuntimeException("[DiaryService] summarize error", e);
        }
    }

    @Override
    public Map<String, Object> getRequestBody(String content) {
        log.info("[DiaryService] getRequestBody");
        try {
            Map<String, Object> documentObject = new HashMap<>(); // DocumentObject 를 위한 Map 생성
            documentObject.put("content", content); // 요약할 내용 (일기 내용)

            Map<String, Object> optionObject = new HashMap<>(); // OptionObject 를 위한 Map 생성
            optionObject.put("language", "ko"); // 한국어
            optionObject.put("summaryCount", 1); // 요약 줄 수 (1줄)

            Map<String, Object> requestBody = new HashMap<>(); // Request Body 를 위한 Map 생성
            requestBody.put("document", documentObject);
            requestBody.put("option", optionObject);
            return requestBody;
        } catch (Exception e) {
            log.error("[DiaryService] getRequestBody error", e);
            throw new RuntimeException("[DiaryService] getRequestBody error", e);
        }
    }
}
