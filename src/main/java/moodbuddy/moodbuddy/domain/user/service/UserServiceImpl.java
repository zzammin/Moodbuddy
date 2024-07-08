package moodbuddy.moodbuddy.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moodbuddy.moodbuddy.domain.diary.entity.Diary;
import moodbuddy.moodbuddy.domain.diary.entity.DiaryEmotion;
import moodbuddy.moodbuddy.domain.diary.repository.DiaryRepository;
import moodbuddy.moodbuddy.domain.profile.entity.Profile;
import moodbuddy.moodbuddy.domain.profile.repository.ProfileRepository;
import moodbuddy.moodbuddy.domain.profileImage.entity.ProfileImage;
import moodbuddy.moodbuddy.domain.profileImage.repository.ProfileImageRepository;
import moodbuddy.moodbuddy.domain.user.dto.request.UserReqCalendarMonthDTO;
import moodbuddy.moodbuddy.domain.user.dto.request.UserReqCalendarSummaryDTO;
import moodbuddy.moodbuddy.domain.user.dto.response.UserResCalendarMonthDTO;
import moodbuddy.moodbuddy.domain.user.dto.response.UserResCalendarMonthListDTO;
import moodbuddy.moodbuddy.domain.user.dto.response.UserResCalendarSummaryDTO;
import moodbuddy.moodbuddy.domain.user.dto.response.UserResMainPageDTO;
import moodbuddy.moodbuddy.domain.user.entity.User;
import moodbuddy.moodbuddy.domain.user.repository.UserRepository;
import moodbuddy.moodbuddy.global.common.util.JwtUtil;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@EnableScheduling
@Slf4j
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final ProfileImageRepository profileImageRepository;
    private final DiaryRepository diaryRepository;


    @Override
    @Transactional
    public UserResMainPageDTO mainPage(){
        log.info("[UserService] mainPage");
        try {
            // kakaoId를 통해 userRepository에서 유저 조회 (Optional 사용)
            Long kakaoId = JwtUtil.getUserId();
            Optional<User> optionalUser = userRepository.findByKakaoId(kakaoId);

            // 조회한 유저의 user_id를 통해 profileRepository에서 유저 프로필 조회 (Optional 사용)
            Optional<Profile> optionalProfile = profileRepository.findByKakaoId(kakaoId);

            if (optionalUser.isPresent() && optionalProfile.isPresent()) {
                // profile_id를 통해 profileImageRepository에서 유저 프로필 이미지 조회 (Optional 사용)
                Optional<ProfileImage> optionalProfileImage = profileImageRepository.findByKakaoId(kakaoId);
                String profileImgURL = optionalProfileImage.map(ProfileImage::getProfileImgURL).orElse("");

                // 현재 달 계산
                YearMonth yearMonth = YearMonth.now();
                String currentYearMonth = yearMonth.toString();

                // 현재 달의 일기 리스트
                List<Diary> diaryList = diaryRepository.findByKakaoIdAndMonth(kakaoId, currentYearMonth);

                // 횟수가 최댓값인 emotion과 그 값을 저장하기 위한 Map
                Map<DiaryEmotion, Integer> emotionMap = emotionNum(diaryList);

                // Map에서 key와 value 가져오기
                DiaryEmotion diaryEmotion = emotionMap.keySet().iterator().next(); // key가 하나밖에 없기 때문에 iterator().next() 사용
                int maxEmotionNum = emotionMap.get(diaryEmotion);

                return UserResMainPageDTO.builder()
                        .profileNickName(optionalProfile.get().getProfileNickName())
                        .userBirth(optionalUser.get().getBirthday())
                        .profileComment(optionalProfile.get().getProfileComment())
                        .profileImgURL(profileImgURL)
                        .userCurDiaryNums(optionalUser.get().getUserCurDiaryNums())
                        .diaryEmotion(diaryEmotion)
                        .maxEmotionNum(maxEmotionNum)
                        .build();
            } else {
                throw new RuntimeException("유저와 프로필이 없습니다.");
            }
        } catch (Exception e){
            log.error("[UserService] mainPage error",e);
            throw new RuntimeException("[UserService] mainPage error",e);
        }
    }

    @Override
    public Map<DiaryEmotion,Integer> emotionNum(List<Diary> diaryList){
        log.info("[UserService] emotionNum");
        try{
            // 각 Diary의 emotion을 통해 한 달의 emotion 횟수를 세기 위한 Map
            Map<DiaryEmotion,Integer> emotionNum = new HashMap<>();
            for(Diary d : diaryList){
                // emotionNum에 현재 Diary의 key가 없다면, key의 value를 1로 설정
                // 이미 현재 Diary의 key가 있다면, 그 key의 value를 + 1
                emotionNum.merge(d.getDiaryEmotion(), 1, Integer::sum);
            }
            // emotion 개수 중 최댓값 찾기
            return getMaxEmotion(emotionNum);
        } catch (Exception e){
            log.error("[UserService] emotionNum error",e);
            throw new RuntimeException("[UserService] emotionNum error",e);
        }
    }

    @Override
    public Map<DiaryEmotion, Integer> getMaxEmotion(Map<DiaryEmotion, Integer> emotionNum) {
        log.info("[UserService] getMaxEmotion");
        try{
            int maxValue = 0;
            DiaryEmotion maxKey = null;
            for(Map.Entry<DiaryEmotion,Integer> entry : emotionNum.entrySet()){
                if(entry.getValue() > maxValue){
                    maxKey = entry.getKey();
                    maxValue = entry.getValue();
                }
            }

            // emotion 개수 중 최댓값의 emotion과 그 값을 저장할 Map
            Map<DiaryEmotion,Integer> maxEmotion = new HashMap<>();
            if(maxKey != null){
                maxEmotion.put(maxKey, maxValue);
            }
            return maxEmotion;
        } catch (Exception e){
            log.error("[UserService] getMaxEmotion error",e);
            throw new RuntimeException("[UserService] getMaxEmotion error",e);
        }
    }

    @Override
    @Transactional
    public UserResCalendarMonthListDTO monthlyCalendar(UserReqCalendarMonthDTO calendarMonthDTO){
        log.info("[UserService] monthlyCalendar");
        try{
            // -> userID 가져오기
            Long kakaoId = JwtUtil.getUserId();

            // calendarMonthDTO에서 month 가져오기
            // user_id에 맞는 List<Diary> 중에서, month에서 DateTimeFormatter의 ofPattern을 이용한 LocalDateTime 파싱을 통해 년, 월을 얻어오고,
            // repository 에서는 LIKE 연산자를 이용해서 그 년, 월에 맞는 List<Diary>를 얻어온다
            // (여기서 user_id에 맞는 리스트 전체 조회를 하지 말고, user_id와 년 월에 맞는 리스트만 조회하자)
            // -> 그 Diary 리스트를 그대로 DTO에 넣어서 반환해주면 될 것 같다.
            List<Diary> monthlyDiaryList = diaryRepository.findByKakaoIdAndMonth(kakaoId, calendarMonthDTO.getCalendarMonth());

            List<UserResCalendarMonthDTO> diaryResCalendarMonthDTOList = monthlyDiaryList.stream()
                    .map(diary -> UserResCalendarMonthDTO.builder()
                            .diaryDate(diary.getDiaryDate())
                            .diaryEmotion(diary.getDiaryEmotion())
                            .build())
                    .collect(Collectors.toList());

            return UserResCalendarMonthListDTO.builder()
                    .diaryResCalendarMonthDTOList(diaryResCalendarMonthDTOList)
                    .build();
        } catch (Exception e) {
            log.error("[UserService] monthlyCalendar error", e);
            throw new RuntimeException("[UserService] monthlyCalendar error", e);
        }
    }

    @Override
    @Transactional
    public UserResCalendarSummaryDTO summary(UserReqCalendarSummaryDTO calendarSummaryDTO) {
        log.info("[UserService] summary");
        try {
            Long kakaoId = JwtUtil.getUserId();

            // userEmail와 calendarSummaryDTO에서 가져온 day와 일치하는 Diary 하나를 가져온다.
            Optional<Diary> summaryDiary = diaryRepository.findByKakaoIdAndDay(kakaoId, calendarSummaryDTO.getCalendarDay());

            // summaryDiary가 존재하면 DTO를 반환하고, 그렇지 않으면 NoSuchElementException 예외 처리
            return summaryDiary.map(diary -> UserResCalendarSummaryDTO.builder()
                            .diaryTitle(diary.getDiaryTitle())
                            .diarySummary(diary.getDiarySummary())
                            .build())
                    .orElseThrow(() -> new NoSuchElementException("해당 날짜에 대한 일기를 찾을 수 없습니다."));
        } catch(Exception e){
            log.error("[UserService] summary error", e);
            throw new RuntimeException("[UserService] summary error", e);
        }
    }

    @Override
    @Transactional
    @Scheduled(cron = "0 0 0 1 * ?") // 매월 1일 자정에 자동으로 실행
    public void changeDiaryNums(){
        log.info("[UserService] changeDiaryNums");
        try{
            List<User> users = userRepository.findAll();
            for(User user : users){
                userRepository.updateLastDiaryNumsByKakaoId(user.getKakaoId(), user.getUserCurDiaryNums()); // 한 달이 지났으니 userCurDiaryNums를 userlastDiaryNums로 변경
                userRepository.updateCurDiaryNumsByKakaoId(user.getKakaoId(), 0); // 새로운 달의 일기 개수를 위해 userCurDiaryNums 초기화
            }
        } catch (Exception e) {
            log.error("[UserService] changeDiaryNums error"+ e);
            throw new RuntimeException(e);
        }
    }

    public User findUserByKakaoId(Long kakaoId) {
        return userRepository.findByKakaoId(kakaoId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
    }
}
