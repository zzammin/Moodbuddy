package moodbuddy.moodbuddy.domain.user.service;

import lombok.RequiredArgsConstructor;
import moodbuddy.moodbuddy.domain.profile.repository.ProfileRepository;
import moodbuddy.moodbuddy.domain.profileImage.repository.ProfileImageRepository;
import moodbuddy.moodbuddy.domain.user.dto.UserResponseDTO;
import moodbuddy.moodbuddy.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final ProfileImageRepository profileImageRepository;

    /**
     * 메인 화면 이동
     * @param userId
     * @return
     */
    @Override
    public UserResponseDTO mainPage(Long userId){
        // userId를 통해 userRepository에서 유저 조회 (Optional 사용)
        // -> user_id, birthday, cur_diary_nums 가져와서 DTO에 넣기

        // 조회한 유저의 user_id를 통해 profileRepository에서 유저 프로필 조회 (Optional 사용)
        // -> profile_id, nickname, profile_comment 가져와서 DTO에 넣기

        // profile_id를 통해 profileImageRepository에서 유저 프로필 이미지 조회 (Optional 사용)
        // -> image_url 가져와서 DTO에 넣기

        // 조회한 유저의 user_id를 통해 diary에서 일기 리스트 조회 (List<Diary> 사용)
        // 이 때, 현재 달을 계산하여 현재 달의 일기 리스트를 전부 응답해줘야 함! (캘린더의 기본 처음 화면은 현재 달의 데이터가 필요)
        // 이 때, for-each 문을 통해 일기 리스트의 일기 하나하나의 emotion을 가져와서, 횟수 세어주기 필요!
        // (더 나은 알고리즘이 있을까? 고민해보자)
        // Map을 이용하여 emotion 당 횟수를 계산하고(ex. {1:1, 2:0, 3:0, ... }), 그 중 값이 max인 키와 그 값을 구하면, 그 것이 가장 많이 나온 emotion의 int 값과 그 횟수이다!
        // -> 가장 많이 나온 emotion의 int 값과 그 횟수를 DTO에 넣기
        return null;
    }
}
