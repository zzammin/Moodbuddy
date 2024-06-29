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
import moodbuddy.moodbuddy.domain.user.dto.response.UserResMainPageDTO;
import moodbuddy.moodbuddy.domain.user.entity.User;
import moodbuddy.moodbuddy.domain.user.repository.UserRepository;
import moodbuddy.moodbuddy.global.common.util.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
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
            // userId를 통해 userRepository에서 유저 조회 (Optional 사용)
            Long userId = JwtUtil.getUserId();
            Optional<User> optionalUser = userRepository.findById(userId);

            // 조회한 유저의 user_id를 통해 profileRepository에서 유저 프로필 조회 (Optional 사용)
            Optional<Profile> optionalProfile = profileRepository.findByUserId(userId);

            if (optionalUser.isPresent() && optionalProfile.isPresent()) {
                // profile_id를 통해 profileImageRepository에서 유저 프로필 이미지 조회 (Optional 사용)
                Optional<ProfileImage> optionalProfileImage = profileImageRepository.findByProfileId(optionalProfile.get().getId());
                String profileImgURL = optionalProfileImage.map(ProfileImage::getProfileImgURL).orElse("");

                // 현재 달 계산
                YearMonth yearMonth = YearMonth.now();
                String currentYearMonth = yearMonth.toString();

                // 현재 달의 일기 리스트
                List<Diary> diaryList = diaryRepository.findByUserIdAndMonth(userId, currentYearMonth);

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
}
