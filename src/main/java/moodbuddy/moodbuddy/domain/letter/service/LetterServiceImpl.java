package moodbuddy.moodbuddy.domain.letter.service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moodbuddy.moodbuddy.domain.gpt.dto.GPTMessageDTO;
import moodbuddy.moodbuddy.domain.gpt.dto.GPTResponseDTO;
import moodbuddy.moodbuddy.domain.gpt.service.GptService;
import moodbuddy.moodbuddy.domain.letter.dto.request.LetterReqDTO;
import moodbuddy.moodbuddy.domain.letter.dto.request.LetterReqUpdateDTO;
import moodbuddy.moodbuddy.domain.letter.dto.response.*;
import moodbuddy.moodbuddy.domain.letter.entity.Letter;
import moodbuddy.moodbuddy.domain.letter.repository.LetterRepository;
import moodbuddy.moodbuddy.domain.profile.entity.Profile;
import moodbuddy.moodbuddy.domain.profile.repository.ProfileRepository;
import moodbuddy.moodbuddy.domain.profileImage.entity.ProfileImage;
import moodbuddy.moodbuddy.domain.profileImage.repository.ProfileImageRepository;
import moodbuddy.moodbuddy.domain.user.entity.User;
import moodbuddy.moodbuddy.domain.user.repository.UserRepository;
import moodbuddy.moodbuddy.global.common.exception.member.MemberIdNotFoundException;
import moodbuddy.moodbuddy.global.common.util.JwtUtil;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
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

    @Value("${coolsms.api-key}")
    private String smsApiKey;
    @Value("${coolsms.api-secret}")
    private String smsApiSecretKey;
    @Value("${coolsms.sender-phone}")
    private String senderPhone;

    @Override
    @Transactional(timeout = 30)
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

                if(optionalUser.get().getLetterAlarm()==null){
                    userRepository.updateLetterAlarmByKakaoId(kakaoId, false);
                }

                return LetterResPageDTO.builder()
                        .nickname(optionalUser.get().getNickname())
                        .userBirth(optionalUser.get().getBirthday())
                        .profileComment(optionalProfile.get().getProfileComment())
                        .profileImageUrl(profileImageURL)
                        .userLetterNums(optionalUser.get().getUserLetterNums())
                        .letterAlarm(optionalUser.get().getLetterAlarm())
                        .letterResPageAnswerDTOList(letterResPageAnswerDTOList)
                        .build();
            }
            throw new NoSuchElementException("유저 또는 프로필을 찾을 수 없습니다. kakaoId: " + kakaoId);
        } catch (Exception e) {
            log.error("[LetterService] letterPage error", e);
            throw new RuntimeException("[LetterService] letterPage error", e);
        }
    }

    @Override
    @Transactional
    public LetterResUpdateDTO updateLetterAlarm(LetterReqUpdateDTO letterReqUpdateDTO){
        log.info("[LetterService] updateLetterAlarm");
        try{
            Long kakaoId = JwtUtil.getUserId();
            User user = userRepository.findByKakaoIdWithPessimisticLock(kakaoId).orElseThrow(
                    () -> new MemberIdNotFoundException(JwtUtil.getUserId())
            );
            user.setLetterAlarm(letterReqUpdateDTO.getLetterAlarm());
            return LetterResUpdateDTO.builder()
                    .nickname(user.getNickname())
                    .letterAlarm(user.getLetterAlarm())
                    .build();
        } catch (Exception e){
            log.error("[LetterService] updateLetterAlarm error", e);
            throw new RuntimeException("[LetterService] updateLetterAlarm error", e);
        }
    }

    @Override
    @Transactional
    public LetterResSaveDTO letterSave(Long kakaoId, LetterReqDTO letterReqDTO) {
        log.info("[LetterService] save");
        try {
            User user = userRepository.findByKakaoIdWithPessimisticLock(kakaoId).orElseThrow(
                    () -> new MemberIdNotFoundException(JwtUtil.getUserId())
            );

            log.info("user.getUserLetterNums() : "+user.getUserLetterNums());
            // 편지지가 없을 경우 예외 처리
            if (user.getUserLetterNums() == null || user.getUserLetterNums() <= 0) {
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
    @Transactional(timeout = 30)
    public void letterAnswerSave(Long kakaoId, LetterResSaveDTO letterResSaveDTO) {
        log.info("[LetterService] answerSave");
        try {
            User user = userRepository.findByKakaoIdWithPessimisticLock(kakaoId).orElseThrow(
                    () -> new MemberIdNotFoundException(JwtUtil.getUserId())
            );

            Letter letter = letterRepository.findById(letterResSaveDTO.getLetterId())
                    .orElseThrow(()->new IllegalArgumentException("letterId에 해당하는 편지가 없습니다"));
            GPTResponseDTO response = gptService.letterAnswerSave(letter.getLetterWorryContent(), letter.getLetterFormat()).block();

            if(user.getLetterAlarm() && !user.getPhoneNumber().isEmpty()){
                letterMessage(user.getPhoneNumber());
            }

            if (response != null && response.getChoices() != null) {
                for (GPTResponseDTO.Choice choice : response.getChoices()) {
                    GPTMessageDTO message = choice.getMessage();
                    if (message != null) {
                        String answer = message.getContent();
                        letterRepository.updateAnswerByLetterId(letterResSaveDTO.getLetterId(), answer);
                    }
                }
            } else {
                log.error("GPT 응답 오류");
            }
        } catch (Exception e) {
            log.error("[LetterService] answerSave error", e);
        }
    }

    public void letterMessage(String to){
        DefaultMessageService messageService =  NurigoApp.INSTANCE.initialize(smsApiKey, smsApiSecretKey, "https://api.coolsms.co.kr");

        Message message = new Message();
        message.setFrom(senderPhone);
        message.setTo(to);
        message.setText("[moodbuddy] 쿼디의 고민 편지 답장이 도착했어요! 어서 확인해보세요 :)");

        try {
            messageService.send(message); // 컨트롤러에서 30초 이후에 자동으로 호출하니까 sms는 따로 지연 시간 설정할 필요 X
        } catch (NurigoMessageNotReceivedException exception) {
            // 발송에 실패한 메시지 목록을 확인
            log.error("exception.getFailedMessageList() : "+exception.getFailedMessageList());
            log.error("exception.getMessage() : "+exception.getMessage());
        } catch (Exception exception) {
            log.error("exception.getMessage() : "+exception.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true, timeout = 30)
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
