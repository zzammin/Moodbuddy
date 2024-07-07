package moodbuddy.moodbuddy.domain.letter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moodbuddy.moodbuddy.domain.letter.dto.gpt.GPTMessageDTO;
import moodbuddy.moodbuddy.domain.letter.dto.gpt.GPTRequestDTO;
import moodbuddy.moodbuddy.domain.letter.dto.gpt.GPTResponseDTO;
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
import moodbuddy.moodbuddy.domain.user.entity.User;
import moodbuddy.moodbuddy.domain.user.repository.UserRepository;
import moodbuddy.moodbuddy.global.common.util.JwtUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class LetterServiceImpl implements LetterService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final ProfileImageRepository profileImageRepository;
    private final LetterRepository letterRepository;
    private final WebClient gptWebClient;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4); // 4개의 쓰레드를 가진 풀 생성

    @Value("${gpt.model}")
    private String model;
    @Value("${gpt.api.url}")
    private String apiUrl;

    // 생성자 주입을 통한 의존성 주입
    public LetterServiceImpl(UserRepository userRepository,
                             ProfileRepository profileRepository,
                             ProfileImageRepository profileImageRepository,
                             LetterRepository letterRepository,
                             @Qualifier("gptWebClient") WebClient gptWebClient) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.profileImageRepository = profileImageRepository;
        this.letterRepository = letterRepository;
        this.gptWebClient = gptWebClient;
    }

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
                                .letterCreatedTime(letter.getCreatedTime())
                                .answerCheck(letter.getLetterAnswerContent() != null ? 1 : 0)
                                .build())
                        .collect(Collectors.toList());

                return LetterResPageDTO.builder()
                        .profileNickname(optionalProfile.get().getProfileNickName())
                        .userBirth(optionalUser.get().getBirthday())
                        .profileComment(optionalProfile.get().getProfileComment())
                        .profileImageUrl(profileImageURL)
                        .userLetterNums(optionalUser.get().getUserLetterNums())
                        .letterResPageAnswerDTOList(letterResPageAnswerDTOList)
                        .build();
            }
            throw new NoSuchElementException("유저 또는 프로필을 찾을 수 없습니다. kakaoId: " + kakaoId);
        } catch (Exception e) {
            log.error("[LetterService] letterPage", e);
            throw new RuntimeException("[LetterService] letterPage error", e);
        }
    }

    @Override
    @Transactional
    public LetterResSaveDTO save(LetterReqDTO letterReqDTO) {
        log.info("[LetterService] save");
        try {
            Long kakaoId = JwtUtil.getUserId();
            Optional<User> optionalUser = userRepository.findByKakaoId(kakaoId);
            if (optionalUser.isPresent()) {
                Letter letter = Letter.builder()
                        .user(optionalUser.get())
                        .letterFormat(letterReqDTO.getLetterFormat())
                        .letterWorryContent(letterReqDTO.getLetterWorryContent())
                        .letterDate(letterReqDTO.getLetterDate())
                        .build();
                letterRepository.save(letter);

                // ScheduledExecutorService를 사용하여 작업 예약, 지금은 임시로 5초 뒤에 작업을 실행하는 것으로 설정해 둠
                scheduler.schedule(new ContextAwareRunnable(() -> {
                    answerSave(optionalUser.get().getUserId(), letterReqDTO.getLetterWorryContent(), letterReqDTO.getLetterFormat(), letterReqDTO.getLetterDate());
                    // alarmTalk();
                }), 5, TimeUnit.SECONDS);

                return LetterResSaveDTO.builder()
                        .letterId(letter.getId())
                        .userNickname(optionalUser.get().getNickname())
                        .letterDate(letter.getLetterDate())
                        .build();
            } else {
                throw new NoSuchElementException("kakaoId를 가지는 사용자가 없습니다. kakaoId : " + kakaoId);
            }
        } catch (Exception e) {
            log.error("[LetterService] save error", e);
            throw new RuntimeException("[LetterService] save error", e);
        }
    }

    @Override
    @Transactional
    public void answerSave(Long userId, String worryContent, Integer format, LocalDateTime letterDate) {
        log.info("[LetterService] answerSave");
        try {
            String prompt = worryContent + (format == 1 ? " 이 내용에 대해 존댓말로 따뜻한 위로의 말을 해주세요" : " 이 내용에 대해 존댓말로 따끔한 해결의 말을 해주세요");
            log.info("prompt : "+prompt);
            GPTRequestDTO gptrequestDTO = new GPTRequestDTO(model, prompt);

            GPTResponseDTO response = gptWebClient.post()
                    .uri(apiUrl)
                    .bodyValue(gptrequestDTO)
                    .retrieve()
                    .bodyToMono(GPTResponseDTO.class)
                    .block();

            if (response != null && response.getChoices() != null) {
                for (GPTResponseDTO.Choice choice : response.getChoices()) {
                    GPTMessageDTO message = choice.getMessage();
                    if (message != null) {
                        String answer = message.getContent();
                        log.info("answer : "+answer);
                        Optional<Letter> optionalLetter = letterRepository.findByUserIdAndDate(userId, letterDate);
                        if (optionalLetter.isPresent()) {
                            letterRepository.updateAnswerByUserId(userId, answer);
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

    @Override
    public void alarmTalk(String fcmRegistration) {
        log.info("[LetterService] alarmTalk");
        try {
            Long kakaoId = JwtUtil.getUserId();
        } catch (Exception e) {
            log.error("[LetterService] alarmTalk error", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public LetterResDetailsDTO details(Long letterId) {
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
                    .letterWorryContent(letter.getLetterWorryContent())
                    .letterAnswerContent(letter.getLetterAnswerContent())
                    .letterDate(letter.getLetterDate())
                    .build();

        } catch (Exception e) {
            log.error("[LetterService] details", e);
            throw new RuntimeException("[LetterService] details error", e);
        }
    }


    // 비동기적으로 실행되는 작업이 현재 요청의 데이터를 정확하게 액세스하고 처리하게 하는 클래스
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
