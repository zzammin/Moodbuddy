package moodbuddy.moodbuddy.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moodbuddy.moodbuddy.domain.diary.entity.Diary;
import moodbuddy.moodbuddy.domain.diary.entity.DiaryEmotion;
import moodbuddy.moodbuddy.domain.diary.repository.DiaryRepository;
import moodbuddy.moodbuddy.domain.diaryImage.service.DiaryImageServiceImpl;
import moodbuddy.moodbuddy.domain.profile.entity.Profile;
import moodbuddy.moodbuddy.domain.profile.repository.ProfileRepository;
import moodbuddy.moodbuddy.domain.profileImage.entity.ProfileImage;
import moodbuddy.moodbuddy.domain.profileImage.repository.ProfileImageRepository;
import moodbuddy.moodbuddy.domain.user.dto.request.UserProfileUpdateDto;
import moodbuddy.moodbuddy.domain.user.dto.request.UserReqCalendarMonthDTO;
import moodbuddy.moodbuddy.domain.user.dto.request.UserReqCalendarSummaryDTO;
import moodbuddy.moodbuddy.domain.user.dto.request.UserReqMainPageDTO;
import moodbuddy.moodbuddy.domain.user.dto.response.UserResCalendarMonthDTO;
import moodbuddy.moodbuddy.domain.user.dto.response.UserResCalendarMonthListDTO;
import moodbuddy.moodbuddy.domain.user.dto.response.UserResCalendarSummaryDTO;
import moodbuddy.moodbuddy.domain.user.dto.response.UserResMainPageDTO;
import moodbuddy.moodbuddy.domain.user.dto.response.*;
import moodbuddy.moodbuddy.domain.user.entity.User;
import moodbuddy.moodbuddy.domain.user.repository.UserRepository;
import moodbuddy.moodbuddy.global.common.exception.member.MemberIdNotFoundException;
import moodbuddy.moodbuddy.global.common.util.JwtUtil;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static moodbuddy.moodbuddy.global.common.config.MapperConfig.modelMapper;

import java.io.IOException;
import java.time.LocalDate;
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
    private final DiaryImageServiceImpl diaryImageService;

    /** =========================================================  재민  ========================================================= **/

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
                            .diaryId(diary.getId())
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

    /** =========================================================  다연  ========================================================= **/

    //해당하는 월에 유저 아이디로 diary_emotion 조회 -> 감정별로 group by or 불러와서 리스트 또는 hashmap 형태로 가공 (감정(key), 횟수(value))
    @Override
    @Transactional(readOnly = true)
    public List<EmotionStaticDto> getEmotionStatic(LocalDate month) {

        Long kakaoId = JwtUtil.getUserId();

        int year = month.getYear();
        int monthValue = month.getMonthValue();

        List<Diary> diaries = diaryRepository.findDiaryEmotionByKakaoIdAndMonth(kakaoId, year, monthValue);

        // 감정별로 횟수를 세기 위한 Map 생성 및 초기화
        Map<DiaryEmotion, Integer> emotionCountMap = new HashMap<>();

        // 모든 가능한 감정에 대해 기본값 0 설정
        for (DiaryEmotion emotion : DiaryEmotion.values()) {
            emotionCountMap.put(emotion, 0);
        }

        // 일기 데이터를 이용하여 감정별로 횟수를 세기
        for (Diary diary : diaries) {
            DiaryEmotion emotion = diary.getDiaryEmotion();
            emotionCountMap.put(emotion, emotionCountMap.get(emotion) + 1);
        }

        // Map을 EmotionStaticDto 리스트로 변환하고 nums 값으로 내림차순 정렬
        return emotionCountMap.entrySet().stream()
                .map(entry -> new EmotionStaticDto(entry.getKey(), entry.getValue()))
                .sorted((e1, e2) -> e2.getNums().compareTo(e1.getNums())) // nums 값으로 내림차순 정렬
                .collect(Collectors.toList());

    }

    //일기 작성 횟수 조회
    //year parameter로 받아서 -> year에 해당하는 데이터 key,value <month, nums> 형태로 출력
    @Override
    @Transactional(readOnly = true)
    public List<DiaryNumsDto> getDiaryNums(LocalDate year) {

        Long kakaoId = JwtUtil.getUserId();
        int yearValue = year.getYear();

        List<Diary> diaries = diaryRepository.findAllByYear(kakaoId, yearValue);

        Map<Integer, Integer> yearCountMap = new HashMap<>();

        // 1월~12월에 대해 일기 작성 횟수 0으로 초기화
        for (int i = 1; i < 13; i++) {
            yearCountMap.put(i, 0);
        }

        // diary 객체를 이용하여 일기 작성 횟수 세기
        for (Diary diary : diaries) {
            int monthValue = diary.getDiaryDate().getMonthValue();
            yearCountMap.put(monthValue, yearCountMap.get(monthValue) + 1);
        }

        // 결과를 정렬하여 반환
        return yearCountMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey()) // 키 값으로 정렬
                .map(entry -> new DiaryNumsDto(entry.getKey() + "월", entry.getValue())) // String 형식으로 변환
                .collect(Collectors.toList());
    }

    //감정 횟수 조회(해당 년도)
    @Override
    @Transactional(readOnly = true)
    public List<EmotionStaticDto> getEmotionNums() {

        Long kakaoId = JwtUtil.getUserId();

        List<Diary> diaries = diaryRepository.findDiaryEmotionAllByKakaoId(kakaoId);

        log.info("일기들", diaries);

        Map<DiaryEmotion, Integer> emotionCountMap = new HashMap<>();

        for (DiaryEmotion emotion : DiaryEmotion.values()) {
            emotionCountMap.put(emotion, 0);
        }

        for (Diary diary : diaries) {
            DiaryEmotion emotion = diary.getDiaryEmotion();
            if (emotion != null && emotionCountMap.containsKey(emotion)) {
                emotionCountMap.put(emotion, emotionCountMap.get(emotion) + 1);
            }
        }

        return emotionCountMap.entrySet().stream()
                .map(entry -> new EmotionStaticDto(entry.getKey(), entry.getValue()))
                .sorted((e1, e2) -> e2.getNums().compareTo(e1.getNums())) // nums 값으로 내림차순 정렬
                .collect(Collectors.toList());


    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileDto getUserProfile() {
        Long kakaoId = JwtUtil.getUserId();

        User user = userRepository.findByKakaoId(kakaoId).orElseThrow(
                () -> new MemberIdNotFoundException(JwtUtil.getUserId())
        );
        Profile profile = profileRepository.findByKakaoId(kakaoId).orElseThrow(
                () -> new MemberIdNotFoundException(JwtUtil.getUserId())
        );
        ProfileImage profileImage = profileImageRepository.findByKakaoId(kakaoId).orElseThrow(
                () -> new MemberIdNotFoundException(JwtUtil.getUserId())
        );

        UserProfileDto profileDto = UserProfileDto.builder()
                .url(profileImage.getProfileImgURL())
                .profileComment(profile.getProfileComment())
                .nickname(user.getNickname())
                .alarm(user.getAlarm())
                .alarmTime(user.getAlarmTime())
                .gender(user.getGender())
                .birthday(user.getBirthday())
                .build();

        return profileDto;
    }

    //프로필 이미지 -> s3 사용
    //프로필 이미지 s3에 저장 -> url setter로 변경
    @Override
    @Transactional
    public UserProfileDto updateProfile(UserProfileUpdateDto dto) throws IOException {
        Long kakaoId = JwtUtil.getUserId();

        User user = userRepository.findByKakaoId(kakaoId).orElseThrow(
                () -> new MemberIdNotFoundException(JwtUtil.getUserId())
        );
        Profile profile = profileRepository.findByKakaoId(kakaoId).orElseThrow(
                () -> new MemberIdNotFoundException(JwtUtil.getUserId())
        );
        ProfileImage profileImage = profileImageRepository.findByKakaoId(kakaoId).orElseThrow(
                () -> new MemberIdNotFoundException(JwtUtil.getUserId())
        );

        profile.setProfileComment(dto.getProfileComment());
        profileRepository.save(profile);

        user.setAlarm(dto.getAlarm());
        user.setAlarmTime(dto.getAlarmTime());
        user.setNickname(dto.getNickname());
        user.setGender(dto.getGender());
        user.setBirthday(dto.getBirthday());
        user.setFcmToken(dto.getFcmToken());
        userRepository.save(user);

        if (dto.getNewProfileImg() != null) {
            String url = diaryImageService.saveProfileImages(dto.getNewProfileImg());
            profileImage.setProfileImgURL(url);
            profileImageRepository.save(profileImage);
        }


        // 업데이트된 정보를 기반으로 UserProfileDto 객체를 생성하여 반환
        UserProfileDto updateUserProfile = UserProfileDto.builder()
                .url(profileImage.getProfileImgURL())
                .profileComment(profile.getProfileComment())
                .alarm(user.getAlarm())
                .alarmTime(user.getAlarmTime())
                .nickname(user.getNickname())
                .gender(user.getGender())
                .birthday(user.getBirthday())
                .fcmToken(user.getFcmToken())
                .build();

        return updateUserProfile;

    }

    public User findUserByKakaoId(Long kakaoId) {
        return userRepository.findByKakaoId(kakaoId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
    }
}
