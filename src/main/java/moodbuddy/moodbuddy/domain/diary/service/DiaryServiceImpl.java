package moodbuddy.moodbuddy.domain.diary.service;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import moodbuddy.moodbuddy.domain.profile.repository.ProfileRepository;
import moodbuddy.moodbuddy.domain.profileImage.repository.ProfileImageRepository;
import moodbuddy.moodbuddy.domain.user.entity.User;
import moodbuddy.moodbuddy.domain.user.repository.UserRepository;
import moodbuddy.moodbuddy.global.common.exception.database.DatabaseNullOrEmptyException;
import moodbuddy.moodbuddy.global.common.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
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
    private final ObjectMapper objectMapper;

    public DiaryServiceImpl(UserRepository userRepository, DiaryRepository diaryRepository,
                            DiaryImageServiceImpl diaryImageService,
                           @Qualifier("naverWebClient") WebClient naverWebClient,
                            ObjectMapper objectMapper){
        this.userRepository = userRepository;
        this.diaryRepository = diaryRepository;
        this.diaryImageService = diaryImageService;
        this.naverWebClient = naverWebClient;
        this.objectMapper = objectMapper;
    }


    @Override
    @Transactional
    public DiaryResDetailDTO save(DiaryReqSaveDTO diaryReqSaveDTO) throws IOException {
        log.info("[DiaryServiceImpl] save");
        Long kakaoId = JwtUtil.getUserId();

        String summary = summarize(diaryReqSaveDTO.getDiaryContent()); // 일기 내용 요약 결과
        Diary diary = DiaryMapper.toDiaryEntity(diaryReqSaveDTO, kakaoId, summary);

        diary = diaryRepository.save(diary);

//        saveDocument(diary);

        if (diaryReqSaveDTO.getDiaryImgList() != null) {
            diaryImageService.saveDiaryImages(diaryReqSaveDTO.getDiaryImgList(), diary);
        }

        // 일기 작성하면 편지지 개수 늘려주기
        numPlus(kakaoId);

        return DiaryMapper.toDetailDTO(diary);
    }

    @Override
    @Transactional
    public void numPlus(Long kakaoId) {
        log.info("[DiaryServiceImpl] numPlus");
        try{
            User user = userRepository.findByKakaoId(kakaoId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
            int curDiaryNums = user.getUserCurDiaryNums() == null ? 1 :user.getUserCurDiaryNums() + 1;
            int letterNums = user.getUserLetterNums() == null ? 1 : user.getUserLetterNums() + 1;
            userRepository.updateCurDiaryNumsByKakaoId(kakaoId,curDiaryNums);
            userRepository.updateLetterNumsByKakaoId(kakaoId,letterNums);
        } catch (Exception e){
            log.error("[DiaryServiceImpl] numPlus" + e);
            throw new RuntimeException(e);
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
        Long kakaoId = JwtUtil.getUserId();

        Diary diary = DiaryMapper.toDraftEntity(diaryReqSaveDTO, kakaoId);
        diary = diaryRepository.save(diary);

        if (diaryReqSaveDTO.getDiaryImgList() != null) {
            diaryImageService.saveDiaryImages(diaryReqSaveDTO.getDiaryImgList(), diary);
        }

        return DiaryMapper.toDetailDTO(diary);
    }

    @Override
    public DiaryResDraftFindAllDTO draftFindAll() {
        log.info("[DiaryServiceImpl] draftFindAll");
        Long kakaoId = JwtUtil.getUserId();
        return diaryRepository.draftFindAllByKakaoId(kakaoId);
    }

    @Override
    @Transactional
    public void draftSelectDelete(DiaryReqDraftSelectDeleteDTO diaryReqDraftSelectDeleteDTO) {
        log.info("[DiaryServiceImpl] draftSelectDelete");
        Long kakaoId = JwtUtil.getUserId();

        List<Diary> diariesToDelete = diaryRepository.findAllById(diaryReqDraftSelectDeleteDTO.getDiaryIdList()).stream()
                .filter(diary -> diary.getKakaoId().equals(kakaoId))
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
        Long kakaoId = JwtUtil.getUserId();

        // [추가해야 할 내]
        // diaryId가 존재하는 Id 값인지 확인하는 예외처리
        // userEmail하고 Diary의 userEmail하고 같은 지 확인하는 예외처리

        return diaryRepository.findOneByDiaryId(diaryId);
    }

    @Override
    public Page<DiaryResDetailDTO> findAllWithPageable(Pageable pageable) {
        log.info("[DiaryServiceImpl] findAllWithPageable");
        Long kakaoId = JwtUtil.getUserId();

        return diaryRepository.findAllByKakaoIdWithPageable(kakaoId, pageable);
    }

    @Override
    public Page<DiaryResDetailDTO> findAllByEmotionWithPageable(DiaryEmotion diaryEmotion, Pageable pageable) {
        log.info("[DiaryServiceImpl] findAllByEmotionWithPageable");
        Long kakaoId = JwtUtil.getUserId();

        return diaryRepository.findAllByEmotionWithPageable(diaryEmotion, kakaoId, pageable);
    }

    @Override
    public Page<DiaryResDetailDTO> findAllByFilter(DiaryReqFilterDTO diaryReqFilterDTO, Pageable pageable) {
        log.info("[DiaryServiceImpl] findAllByFilter");
        Long kakaoId = JwtUtil.getUserId();

        return diaryRepository.findAllByFilterWithPageable(diaryReqFilterDTO, kakaoId, pageable);
    }

    public Diary findDiaryById(Long diaryId) {
        return diaryRepository.findById(diaryId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 일기입니다."));
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

    /** =========================================================  위 재민 아래 다연  ========================================================= **/

    @Override
    @Transactional
    public DiaryDesResponseDto description() throws JsonProcessingException {

        RestTemplate restTemplate = new RestTemplate();

        //헤더를 JSON으로 설정함
        HttpHeaders headers = new HttpHeaders();

        //파라미터로 들어온 dto를 JSON 객체로 변환
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 쿼리 결과를 JSON 객체로 변환
        Diary diary = diaryRepository.findDiarySummaryById(JwtUtil.getUserId())
                .orElseThrow(() -> new DatabaseNullOrEmptyException("Diary Summary data not found for kakaoId: " + JwtUtil.getUserId()));

        // 'diarySummary' key와 diary.getDiarySummary() 값을 포함하는 Map 생성
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("diarySummary", diary.getDiarySummary());

        // Map을 JSON 문자열로 변환
        String param = objectMapper.writeValueAsString(paramMap);
        log.info(param+"서버로 전송");

        HttpEntity<String> entity = new HttpEntity<String>(param , headers);

        //실제 Flask 서버랑 연결하기 위한 URL
        String url = "http://127.0.0.1:8099/model";

        // Flask 서버로 데이터를 전송하고 받은 응답 값을 처리
        String response = restTemplate.postForObject(url, entity, String.class);

        // 받은 응답 값을 DiaryDesResponseDto로 변환
        DiaryDesResponseDto responseDto = objectMapper.readValue(response, DiaryDesResponseDto.class);

        String emotion = responseDto.getEmotion();

        try {
            // 문자열을 DiaryEmotion enum 값으로 변환
            DiaryEmotion diaryEmotion = DiaryEmotion.valueOf(emotion.toUpperCase());

            diary.setDiaryEmotion(diaryEmotion);
            // diary 엔티티를 저장
            diaryRepository.save(diary);
        } catch (IllegalArgumentException e) {
            // 유효하지 않은 emotion 값이 들어왔을 때의 처리
            System.err.println("Invalid emotion value: " + emotion);
        }

        return responseDto;
    }
}
