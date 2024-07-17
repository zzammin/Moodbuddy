package moodbuddy.moodbuddy.domain.letter.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moodbuddy.moodbuddy.domain.gpt.dto.GPTMessageDTO;
import moodbuddy.moodbuddy.domain.gpt.dto.GPTRequestDTO;
import moodbuddy.moodbuddy.domain.gpt.dto.GPTResponseDTO;
import moodbuddy.moodbuddy.domain.gpt.service.GptService;
import moodbuddy.moodbuddy.domain.letter.dto.request.LetterReqDTO;
import moodbuddy.moodbuddy.domain.letter.dto.response.LetterResDetailsDTO;
import moodbuddy.moodbuddy.domain.letter.dto.response.LetterResPageAnswerDTO;
import moodbuddy.moodbuddy.domain.letter.dto.response.LetterResPageDTO;
import moodbuddy.moodbuddy.domain.letter.dto.response.LetterResSaveDTO;
import moodbuddy.moodbuddy.domain.letter.entity.Letter;
import moodbuddy.moodbuddy.domain.letter.repository.LetterRepository;
import moodbuddy.moodbuddy.domain.profile.entity.Profile;
import moodbuddy.moodbuddy.domain.profile.repository.ProfileRepository;
import moodbuddy.moodbuddy.domain.profileImage.entity.ProfileImage;
import moodbuddy.moodbuddy.domain.profileImage.repository.ProfileImageRepository;
import moodbuddy.moodbuddy.domain.user.dto.fcm.FcmReqDTO;
import moodbuddy.moodbuddy.domain.user.entity.User;
import moodbuddy.moodbuddy.domain.user.repository.UserRepository;
import moodbuddy.moodbuddy.domain.user.service.FcmService;
import moodbuddy.moodbuddy.global.common.exception.member.MemberIdNotFoundException;
import moodbuddy.moodbuddy.global.common.util.JwtUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class LetterServiceImpl implements LetterService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final ProfileImageRepository profileImageRepository;
    private final LetterRepository letterRepository;
    private final GptService gptService;
    private final FcmService fcmService;
    @PersistenceContext
    private EntityManager entityManager;
//    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4); // 4개의 쓰레드를 가진 풀 생성

    @Override
    @Transactional(readOnly = true)
    public LetterResPageDTO letterPage() {
        log.info("[LetterService] letterPage");
        try {
            Long kakaoId = JwtUtil.getUserId();
            Optional<User> optionalUser = userRepository.findByKakaoId(kakaoId);
            Optional<Profile> optionalProfile = profileRepository.findByKakaoId(kakaoId);
            if (optionalUser.isPresent() && optionalProfile.isPresent()) {
                Optional<ProfileImage> optionalProfileImage = profileImageRepository.findByKakaoId(kakaoId);
                String profileImageURL = optionalProfileImage.map(ProfileImage::getProfileImgURL).orElse("");

                List<Letter> letters = letterRepository.findByUserId(optionalUser.get().getUserId());
                List<LetterResPageAnswerDTO> letterResPageAnswerDTOList = letters.stream()
                        .map(letter -> LetterResPageAnswerDTO.builder()
                                .letterId(letter.getId())
                                .letterDate(letter.getLetterDate())
                                .answerCheck(letter.getLetterAnswerContent() != null ? 1 : 0)
                                .build())
                        .collect(Collectors.toList());

                return LetterResPageDTO.builder()
                        .nickname(optionalUser.get().getNickname())
                        .userBirth(optionalUser.get().getBirthday())
                        .profileComment(optionalProfile.get().getProfileComment())
                        .profileImageUrl(profileImageURL)
                        .userLetterNums(optionalUser.get().getUserLetterNums())
                        .letterResPageAnswerDTOList(letterResPageAnswerDTOList)
                        .build();
            }
            throw new NoSuchElementException("유저 또는 프로필을 찾을 수 없습니다. kakaoId: " + kakaoId);
        } catch (Exception e) {
            log.error("[LetterService] letterPage error", e);
            throw new RuntimeException("[LetterService] letterPage error", e);
        }
    }

    //            // FCM
//            log.info("사용자 FcmToken : " + user.getFcmToken());
//            // 이 FCM 작업을 12시간 뒤에 실행하도록 스케쥴링하기
//            if(user.getFcmToken()!=null){
//                fcmService.sendMessageTo(FcmReqDTO.builder()
//                        .token(user.getFcmToken())
//                        .title("moodbuddy : 고민 답장이 도착하였습니다.")
//                        .body("고민 편지에 대한 쿼디의 답장이 도착하였습니다! 어서 확인해보세요 :)")
//                        .build());
//            }

    @Override
    @Transactional(timeout = 10)
    public LetterResSaveDTO letterSave(LetterReqDTO letterReqDTO) {
        log.info("[LetterService] save");
        try {
            Long kakaoId = JwtUtil.getUserId();
            User user = userRepository.findByKakaoIdWithPessimisticLock(kakaoId).orElseThrow(
                    () -> new MemberIdNotFoundException(JwtUtil.getUserId())
            );

            // LockTimeout 설정
            entityManager.lock(user, LockModeType.PESSIMISTIC_WRITE, Collections.singletonMap("javax.persistence.lock.timeout", 10000));

            log.info("user.getUserLetterNums() : "+user.getUserLetterNums());
            // 편지지가 없을 경우 예외 처리
            if(user.getUserLetterNums() == null || user.getUserLetterNums() <= 0){
                throw new IllegalArgumentException("편지지가 없습니다.");
            }

            // 편지지 개수 업데이트
            user.setUserLetterNums(user.getUserLetterNums() - 1);
            userRepository.save(user);

            Letter letter = Letter.builder()
                    .user(user)
                    .letterFormat(letterReqDTO.getLetterFormat())
                    .letterWorryContent(letterReqDTO.getLetterWorryContent())
                    .letterDate(letterReqDTO.getLetterDate())
                    .build();
            letterRepository.save(letter);

            letterAnswerSave(letterReqDTO.getLetterWorryContent(), letterReqDTO.getLetterFormat(), letter.getId());
//            // ScheduledExecutorService를 사용하여 작업 예약, 지금은 임시로 5초 뒤에 작업을 실행하는 것으로 설정해 둠
//            scheduler.schedule(new ContextAwareRunnable(() -> {
////                letterAlarm(user.getUserId(), user.getFcmToken());
//            }), 5, TimeUnit.SECONDS);

            return LetterResSaveDTO.builder()
                    .letterId(letter.getId())
                    .userNickname(user.getNickname())
                    .letterDate(letter.getLetterDate())
                    .build();
        } catch (Exception e) {
            log.error("[LetterService] save error", e);
            throw new RuntimeException("[LetterService] save error", e);
        }
    }

    @Override
    @Transactional(timeout = 10)
    public void letterAnswerSave(String worryContent, Integer format, Long letterId) {
        log.info("[LetterService] answerSave");
        try {
            GPTResponseDTO response = gptService.letterAnswerSave(worryContent, format).block();

            log.info("letterId : "+letterId);
            if (response != null && response.getChoices() != null) {
                for (GPTResponseDTO.Choice choice : response.getChoices()) {
                    GPTMessageDTO message = choice.getMessage();
                    if (message != null) {
                        String answer = message.getContent();
                        Optional<Letter> optionalLetter = letterRepository.findById(letterId);
                        if (optionalLetter.isPresent()) {
                            letterRepository.updateAnswerByLetterId(letterId, answer);
                        }
                    }
                }
            } else {
                log.error("GPT 응답 오류");
            }
        } catch (Exception e) {
            log.error("[LetterService] answerSave error", e);
        }
    }

//    @Override
//    public void letterAlarm(Long userId, String fcmToken) {
//        log.info("[LetterService] alarmTalk");
//        try {
//            // alarmTalk 메소드에서)
//            // 4. com.google.firebase.messaging.Message 패키지의 Message를 이용해서 빌더 형식의 Message 생성
//            // ex.  Message message = Message.builder()
//            //        .setToken(token)
//            //        .putData("title", title)
//            //        .putData("body", body)
//            //        .build();
//            // 5. 이후에 예외 처리와 디버깅을 위해 FCM 응답값을 받아옴
//            // ex. String response = FirebaseMessaging.getInstance().send(message);
//            //     log.info("Successfully sent message: " + response);
//            Message message = Message.builder()
//                    .setToken(fcmToken)
//                    .putData("title", "moodbuddy : 고민 답장이 도착하였습니다.")
//                    .putData("body", "고민 편지에 대한 쿼디의 답장이 도착하였습니다! 어서 확인해보세요 :)")
//                    .build();
//
//            String response = FirebaseMessaging.getInstance().send(message);
//            log.info("Successfully sent message: " + response);
//        } catch (Exception e) {
//            log.error("[LetterService] alarmTalk error", e);
//        }
//    }

    @Override
    @Transactional(readOnly = true)
    public LetterResDetailsDTO letterDetails(Long letterId) {
        log.info("[LetterService] details");
        try {
            Long kakaoId = JwtUtil.getUserId();
            User user = userRepository.findByKakaoId(kakaoId)
                    .orElseThrow(() -> new NoSuchElementException("kakaoId에 해당되는 User가 없습니다"));

            Letter letter = letterRepository.findByIdAndUserId(letterId, user.getUserId())
                    .orElseThrow(() -> new NoSuchElementException("letterId에 매핑되는 편지가 없습니다"));

            return LetterResDetailsDTO.builder()
                    .letterId(letter.getId())
                    .userNickname(user.getNickname())
                    .letterFormat(letter.getLetterFormat())
                    .letterWorryContent(letter.getLetterWorryContent())
                    .letterAnswerContent(letter.getLetterAnswerContent())
                    .letterDate(letter.getLetterDate())
                    .build();

        } catch (Exception e) {
            log.error("[LetterService] details error", e);
            throw new RuntimeException("[LetterService] details error", e);
        }
    }


    // 비동기적으로 실행되는 작업이 현재 요청의 데이터를 액세스하고 처리하게 하는 클래스
    public static class ContextAwareRunnable implements Runnable {

        private final Runnable task;
        private final RequestAttributes context;

        public ContextAwareRunnable(Runnable task) {
            this.task = task;
            this.context = RequestContextHolder.currentRequestAttributes();
        }

        @Override
        public void run() {
            try {
                RequestContextHolder.setRequestAttributes(context);
                task.run();
            } finally {
                RequestContextHolder.resetRequestAttributes();
            }
        }
    }
}
