package moodbuddy.moodbuddy.domain.diary.service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moodbuddy.moodbuddy.domain.bookMark.service.BookMarkService;
import moodbuddy.moodbuddy.domain.diary.dto.request.*;
import moodbuddy.moodbuddy.domain.diary.dto.response.*;
import moodbuddy.moodbuddy.domain.diary.entity.Diary;
import moodbuddy.moodbuddy.domain.diary.entity.DiaryEmotion;
import moodbuddy.moodbuddy.domain.diary.entity.DiaryStatus;
import moodbuddy.moodbuddy.domain.diary.entity.DiarySubject;
import moodbuddy.moodbuddy.domain.diary.mapper.DiaryMapper;
import moodbuddy.moodbuddy.domain.diary.repository.DiaryRepository;
import moodbuddy.moodbuddy.domain.diary.util.DiaryUtil;
import moodbuddy.moodbuddy.domain.diaryImage.service.DiaryImageService;
import moodbuddy.moodbuddy.domain.gpt.service.GptService;
import moodbuddy.moodbuddy.domain.user.service.UserService;
import moodbuddy.moodbuddy.global.common.util.JwtUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {
    private final DiaryRepository diaryRepository;
    private final DiaryImageService diaryImageService;
    private final DiarySummarizeService diarySummarizeService;
    private final DiaryFindService diaryFindService;
    private final BookMarkService bookMarkService;
    private final UserService userService;
    private final GptService gptService;

    /** =========================================================  정목  ========================================================= **/

    @Override
    @Transactional
    public DiaryResDetailDTO save(DiaryReqSaveDTO diaryReqSaveDTO) throws IOException {
        log.info("[DiaryServiceImpl] save");
        final Long kakaoId = JwtUtil.getUserId();

//        /** format **/
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
//        LocalDate parsedDiaryDate = LocalDate.parse(diaryReqSaveDTO.getDiaryDate(), formatter);
//
//        /** format **/

        DiaryUtil.validateExistingDiary(diaryRepository, diaryReqSaveDTO.getDiaryDate(), kakaoId);

        String summary = diarySummarizeService.summarize(diaryReqSaveDTO.getDiaryContent());
        DiarySubject diarySubject = classifyDiaryContent(diaryReqSaveDTO.getDiaryContent());

        Diary diary = DiaryMapper.toDiaryEntity(diaryReqSaveDTO, kakaoId, summary, diarySubject);
        diary = diaryRepository.save(diary);

        DiaryUtil.saveDiaryImages(diaryImageService, diaryReqSaveDTO.getDiaryImgList(), diary);

        checkTodayDiaryPlusLetter(diaryReqSaveDTO, kakaoId); // 오늘 날짜의 일기인 경우에만 편지지 개수를 늘려줍니다.
        userService.setUserCheckTodayDairy(kakaoId); // 일기 작성 불가

        return DiaryMapper.toDetailDTO(diary);
    }

    @Override
    @Transactional
    public DiaryResDetailDTO update(DiaryReqUpdateDTO diaryReqUpdateDTO) throws IOException {
        log.info("[DiaryServiceImpl] update");
        final Long kakaoId = JwtUtil.getUserId();

        if (isDraftToPublished(diaryReqUpdateDTO)) {
            DiaryUtil.validateExistingDiary(diaryRepository, diaryReqUpdateDTO.getDiaryDate(), kakaoId);
        }

        Diary findDiary = diaryFindService.findDiaryById(diaryReqUpdateDTO.getDiaryId());
        diaryFindService.validateDiaryAccess(findDiary, kakaoId);

        String summary = diarySummarizeService.summarize(diaryReqUpdateDTO.getDiaryContent());
        DiarySubject diarySubject = classifyDiaryContent(diaryReqUpdateDTO.getDiaryContent());

        findDiary.updateDiary(diaryReqUpdateDTO, summary, diarySubject);

        // 기존 이미지 삭제
//        DiaryUtil.deleteAllDiaryImages(diaryImageService, findDiary);
        DiaryUtil.deleteExcludingImages(diaryImageService, findDiary, diaryReqUpdateDTO.getExistingDiaryImgList());

        // 새로운 이미지 저장
        DiaryUtil.saveDiaryImages(diaryImageService, diaryReqUpdateDTO.getDiaryImgList(), findDiary);

        return DiaryMapper.toDetailDTO(findDiary);
    }

    @Override
    @Transactional
    public void delete(Long diaryId) throws IOException {
        log.info("[DiaryServiceImpl] delete");
        final Long kakaoId = JwtUtil.getUserId();
        final Diary findDiary = diaryFindService.findDiaryById(diaryId);
        diaryFindService.validateDiaryAccess(findDiary, kakaoId);

        bookMarkService.deleteByDiaryId(diaryId);
        DiaryUtil.deleteAllDiaryImages(diaryImageService, findDiary);
        diaryRepository.delete(findDiary);
    }

    @Override
    @Transactional
    public DiaryResDetailDTO draftSave(DiaryReqSaveDTO diaryReqSaveDTO) throws IOException {
        log.info("[DiaryServiceImpl] draftSave");
        final Long kakaoId = JwtUtil.getUserId();

//        /** format **/
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
//        LocalDateTime parsedDiaryDate = LocalDateTime.parse(diaryReqSaveDTO.getDiaryDate(), formatter);
//        /** format **/


        Diary diary = DiaryMapper.toDraftEntity(diaryReqSaveDTO, kakaoId);
        diary = diaryRepository.save(diary);

        DiaryUtil.saveDiaryImages(diaryImageService, diaryReqSaveDTO.getDiaryImgList(), diary);

        return DiaryMapper.toDetailDTO(diary);
    }

    @Override
    public DiaryResDraftFindAllDTO draftFindAll() {
        log.info("[DiaryServiceImpl] draftFindAll");
        final Long kakaoId = JwtUtil.getUserId();
        return diaryRepository.draftFindAllByKakaoId(kakaoId);
    }

    @Override
    @Transactional
    public void draftSelectDelete(DiaryReqDraftSelectDeleteDTO diaryReqDraftSelectDeleteDTO) {
        log.info("[DiaryServiceImpl] draftSelectDelete");
        final Long kakaoId = JwtUtil.getUserId();

        List<Diary> diariesToDelete = diaryRepository.findAllById(diaryReqDraftSelectDeleteDTO.getDiaryIdList()).stream()
                .peek(diary -> diaryFindService.validateDiaryAccess(diary, kakaoId)) // 접근 권한 확인
                .collect(Collectors.toList());

        diariesToDelete.forEach(diary -> {
            try {
                DiaryUtil.deleteAllDiaryImages(diaryImageService, diary);
            } catch (IOException e) {
                log.error("Error deleting diary images", e);
                throw new RuntimeException(e);  // 필요에 따라 적절한 예외 처리
            }
        });

        diaryRepository.deleteAll(diariesToDelete);
    }


    @Override
    public DiaryResDetailDTO findOneByDiaryId(Long diaryId) {
        log.info("[DiaryServiceImpl] findOneByDiaryId");
        final Long kakaoId = JwtUtil.getUserId();

        final Diary findDiary = diaryFindService.findDiaryById(diaryId);
        diaryFindService.validateDiaryAccess(findDiary, kakaoId);

        return diaryRepository.findOneByDiaryId(diaryId);
    }

    @Override
    public Page<DiaryResDetailDTO> findAll(Pageable pageable) {
        log.info("[DiaryServiceImpl] findAllWithPageable");
        final Long kakaoId = JwtUtil.getUserId();

        return diaryRepository.findAllByKakaoIdWithPageable(kakaoId, pageable);
    }

    @Override
    public Page<DiaryResDetailDTO> findAllByEmotion(DiaryEmotion diaryEmotion, Pageable pageable) {
        log.info("[DiaryServiceImpl] findAllByEmotionWithPageable");
        final Long kakaoId = JwtUtil.getUserId();

        return diaryRepository.findAllByEmotionWithPageable(diaryEmotion, kakaoId, pageable);
    }

    @Override
    public Page<DiaryResDetailDTO> findAllByFilter(DiaryReqFilterDTO diaryReqFilterDTO, Pageable pageable) {
        log.info("[DiaryServiceImpl] findAllByFilter");
        final Long kakaoId = JwtUtil.getUserId();

        return diaryRepository.findAllByFilterWithPageable(diaryReqFilterDTO, kakaoId, pageable);
    }

    private boolean isDraftToPublished(DiaryReqUpdateDTO diaryReqUpdateDTO) {
        return diaryReqUpdateDTO.getDiaryStatus().equals(DiaryStatus.DRAFT);
    }

    private DiarySubject classifyDiaryContent(String diaryContent) {
        Mono<String> subjectMono = gptService.classifyDiaryContent(diaryContent);
        String classifiedSubject = subjectMono.block();
        return DiarySubject.valueOf(classifiedSubject);
    }

    private void checkTodayDiaryPlusLetter(DiaryReqSaveDTO diaryReqSaveDTO, Long kakaoId) {
        LocalDate today = LocalDate.now();
        if (diaryReqSaveDTO.getDiaryDate().isEqual(today)) {
            userService.numPlus(kakaoId); // 일기 작성하면 편지지 개수 늘려주기
        }
    }

}
