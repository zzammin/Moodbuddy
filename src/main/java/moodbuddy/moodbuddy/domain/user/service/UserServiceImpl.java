package moodbuddy.moodbuddy.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moodbuddy.moodbuddy.domain.diary.entity.Diary;
import moodbuddy.moodbuddy.domain.diary.entity.DiaryEmotion;
import moodbuddy.moodbuddy.domain.diary.repository.DiaryRepository;
import moodbuddy.moodbuddy.domain.diaryImage.service.DiaryImageServiceImpl;
import moodbuddy.moodbuddy.domain.monthcomment.entity.MonthComment;
import moodbuddy.moodbuddy.domain.monthcomment.repository.MonthCommentRepository;
import moodbuddy.moodbuddy.domain.profile.entity.Profile;
import moodbuddy.moodbuddy.domain.profile.repository.ProfileRepository;
import moodbuddy.moodbuddy.domain.profileImage.entity.ProfileImage;
import moodbuddy.moodbuddy.domain.profileImage.repository.ProfileImageRepository;
import moodbuddy.moodbuddy.domain.user.dto.request.*;
import moodbuddy.moodbuddy.domain.user.dto.response.UserResCalendarMonthDTO;
import moodbuddy.moodbuddy.domain.user.dto.response.UserResCalendarMonthListDTO;
import moodbuddy.moodbuddy.domain.user.dto.response.UserResCalendarSummaryDTO;
import moodbuddy.moodbuddy.domain.user.dto.response.UserResMainPageDTO;
import moodbuddy.moodbuddy.domain.user.dto.response.*;
import moodbuddy.moodbuddy.domain.user.entity.User;
import moodbuddy.moodbuddy.domain.user.repository.UserRepository;
import moodbuddy.moodbuddy.global.common.exception.ErrorCode;
import moodbuddy.moodbuddy.global.common.exception.member.MemberIdNotFoundException;
import moodbuddy.moodbuddy.global.common.exception.user.UserKakaoIdNotFoundException;
import moodbuddy.moodbuddy.global.common.util.JwtUtil;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static moodbuddy.moodbuddy.global.common.config.MapperConfig.modelMapper;

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
    private final MonthCommentRepository monthCommentRepository;
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

                log.info("diaryList : "+diaryList);

                // 감정 데이터 초기화
                DiaryEmotion diaryEmotion = null;
                int maxEmotionNum = 0;

                if (!diaryList.isEmpty()) {
                    // 횟수가 최댓값인 emotion과 그 값을 저장하기 위한 Map
                    Map<DiaryEmotion, Integer> emotionMap = emotionNum(diaryList);
                    log.info("emotionMap : "+emotionMap);

                    // Map에서 key와 value 가져오기
                    if (!emotionMap.isEmpty()) {
                        diaryEmotion = emotionMap.keySet().iterator().next(); // key가 하나밖에 없기 때문에 iterator().next() 사용
                        maxEmotionNum = emotionMap.get(diaryEmotion);
                    }
                }

                return UserResMainPageDTO.builder()
                        .nickname(optionalUser.get().getNickname())
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
                log.info("d.getDiaryEmotion() : "+d.getDiaryEmotion());
                emotionNum.merge(d.getDiaryEmotion(), 1, Integer::sum);
            }
            log.info("emotionNum : "+emotionNum);
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

            log.info("maxKey : "+maxKey);
            log.info("maxValue : "+maxValue);
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
                            .diaryId(diary.getId())
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

            // summaryDiary가 존재하면 그에 맞게 DTO를 build하여 반환하고, 그렇지 않으면 빈 DTO를 반환한다.
            return summaryDiary.map(diary -> UserResCalendarSummaryDTO.builder()
                            .diaryId(diary.getId())
                            .diaryTitle(diary.getDiaryTitle())
                            .diarySummary(diary.getDiarySummary())
                            .build())
                    .orElse(UserResCalendarSummaryDTO.builder().build());
        } catch(Exception e){
            log.error("[UserService] summary NoSuchElementException", e);
            throw new RuntimeException("[UserService] summary NoSuchElementException", e);
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
                userRepository.updateLastDiaryNumsById(user.getUserId(), user.getUserCurDiaryNums()); // 한 달이 지났으니 userCurDiaryNums를 userlastDiaryNums로 변경
                userRepository.updateCurDiaryNumsById(user.getUserId(), 0); // 새로운 달의 일기 개수를 위해 userCurDiaryNums 초기화
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
    public UserResStatisticsMonthDTO getMonthStatic(LocalDate month) {

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
            if (emotion != null && emotionCountMap.containsKey(emotion)) {
                emotionCountMap.put(emotion, emotionCountMap.get(emotion) + 1);
            }
        }

        // LocalDate를 문자열로 변환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        String formattedMonth = month.format(formatter);

//        // 다음 달 나에게 짧은 한 마디 내용
//        String monthComment = monthCommentRepository.findCommentByKakaoIdAndMonth(kakaoId,formattedMonth)
//                .map(MonthComment::getCommentContent)
//                .orElse(null);

        Optional<MonthComment> monthComment = monthCommentRepository.findCommentByKakaoIdAndMonth(kakaoId, formattedMonth);

        // Map을 EmotionStaticDto 리스트로 변환하고 nums 값으로 내림차순 정렬
        List<EmotionStaticDto> emotionStaticDtoList = emotionCountMap.entrySet().stream()
                .map(entry -> new EmotionStaticDto(entry.getKey(), entry.getValue()))
                .sorted((e1, e2) -> e2.getNums().compareTo(e1.getNums())) // nums 값으로 내림차순 정렬
                .collect(Collectors.toList());

        return monthComment.map(mc -> UserResStatisticsMonthDTO.builder()
                        .emotionStaticDtoList(emotionStaticDtoList)
                        .monthComment(mc.getCommentContent())
                        .commentCheck(true)
                        .build())
                .orElse(UserResStatisticsMonthDTO.builder()
                        .emotionStaticDtoList(emotionStaticDtoList)
                        .monthComment(null)
                        .commentCheck(false)
                        .build());
    }

    @Override
    @Transactional
    public UserResMonthCommentDTO monthComment(UserReqMonthCommentDTO userReqMonthCommentDTO){
        log.info("[UserService] monthComment");
        try {
            Long kakaoId = JwtUtil.getUserId();

            // 기존에 다음 달의 한 마디가 존재할 경우 예외 발생
            monthCommentRepository.findCommentByKakaoIdAndMonth(kakaoId, userReqMonthCommentDTO.getChooseMonth())
                    .ifPresent(monthComment -> { throw new RuntimeException("이미 다음 달의 한 마디가 존재합니다."); });

            MonthComment monthComment = MonthComment.builder()
                    .kakaoId(kakaoId)
                    .commentDate(userReqMonthCommentDTO.getChooseMonth())
                    .commentContent(userReqMonthCommentDTO.getMonthComment())
                    .build();
            monthCommentRepository.save(monthComment);

            return UserResMonthCommentDTO.builder()
                    .chooseMonth(userReqMonthCommentDTO.getChooseMonth())
                    .monthComment(userReqMonthCommentDTO.getMonthComment())
                    .build();
        } catch (Exception e){
            log.error("[UserService] monthComment error"+e);
            throw new RuntimeException(e);
        }
    }


    @Override
    @Transactional
    public UserResMonthCommentUpdateDTO monthCommentUpdate(UserReqMonthCommentUpdateDTO userReqMonthCommentUpdateDTO){
        log.info("[UserService] monthCommentUpdate");
        try{
            Long kakaoId = JwtUtil.getUserId();
            monthCommentRepository.updateCommentByKakaoIdAndMonth(kakaoId, userReqMonthCommentUpdateDTO.getChooseMonth(), userReqMonthCommentUpdateDTO.getMonthComment());
            MonthComment mc = monthCommentRepository.findCommentByKakaoIdAndMonth(kakaoId,userReqMonthCommentUpdateDTO.getChooseMonth())
                    .orElseThrow(()->new NoSuchElementException("그 달에 해당하는 한 마디가 없습니다."));
            return UserResMonthCommentUpdateDTO.builder()
                    .chooseMonth(mc.getCommentDate())
                    .monthComment(mc.getCommentContent())
                    .build();
        } catch (Exception e){
            log.error("[UserService] monthCommentUpdate error",e);
            throw new RuntimeException(e);
        }
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
                .build();

        return updateUserProfile;
    }


//    @Override
//    @Transactional
//    public List<User> getAllUsersWithAlarms() {
//        return userRepository.findAll().stream()
//                .filter(user -> user.getAlarm() != null && user.getAlarm())
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    @Transactional
//    public UserResUpdateTokenDTO updateToken(UserReqUpdateTokenDTO userReqTokenDTO){
//        try{
//            log.info("[UserService] updateToken");
//            Long kakaoId = JwtUtil.getUserId();
//            User user = userRepository.findByKakaoId(kakaoId)
//                            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
//            userRepository.updateFcmTokenByKakaoId(kakaoId, userReqTokenDTO.getFcmToken());
//            return UserResUpdateTokenDTO.builder()
//                    .nickname(user.getNickname())
//                    .fcmToken(userReqTokenDTO.getFcmToken())
//                    .build();
//        } catch (Exception e){
//            log.error("[UserService] updateToken error",e);
//            throw new RuntimeException(e);
//        }
//    }

    @Override
    @Transactional
    public void changeCount(Long kakaoId, boolean increment) {
        log.info("[DiaryServiceImpl] numPlus");
        try {
            User user = userRepository.findByKakaoId(kakaoId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
//            int curDiaryNums = user.getUserCurDiaryNums() == null ? 0 : user.getUserCurDiaryNums();
//            int letterNums = user.getUserLetterNums() == null ? 0 : user.getUserLetterNums();

            if (!increment) { // true
                user.plusUserNumCount();
            } else { // false
                user.minusUserNumCount();
            }

//            userRepository.updateCurDiaryNumsById(user.getUserId(), curDiaryNums);
//            userRepository.updateLetterNumsById(user.getUserId(), letterNums);
        } catch (Exception e) {
            log.error("[DiaryServiceImpl] numPlus error: " + e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public User findUserByKakaoId(Long kakaoId) {
        return userRepository.findByKakaoId(kakaoId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
    }

    /** 테스트를 위한 임시 자체 로그인 **/
    @Override
    public LoginResponseDto login(UserReqLoginDTO userReqLoginDTO) {
        User findUser = getUser_kakaoId(userReqLoginDTO.getKakaoId());
        return  modelMapper.map(findUser, LoginResponseDto.class);
    }

    @Override
    public UserResCheckTodayDiaryDTO checkTodayDiary() {
        Long kakaoId = JwtUtil.getUserId();
        return UserResCheckTodayDiaryDTO.builder()
                .kakaoId(kakaoId)
                .checkTodayDairy(getUser_kakaoId(kakaoId).getCheckTodayDairy())
                .build();
    }

    @Override
    public void setUserCheckTodayDairy(Long kakaoId, Boolean check) {
        User findUser = getUser_kakaoId(kakaoId);
        findUser.setCheckTodayDiary(check);
    }


    private User getUser_kakaoId(Long kakaoId) {
        final Optional<User> optionalUser = userRepository.findByKakaoId(kakaoId);
        if(!optionalUser.isPresent()) {
            throw new UserKakaoIdNotFoundException(ErrorCode.NOT_FOUND_USER);
        }
        return optionalUser.get();
    }
}