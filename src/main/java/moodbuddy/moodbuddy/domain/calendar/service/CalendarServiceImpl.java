package moodbuddy.moodbuddy.domain.calendar.service;


import lombok.RequiredArgsConstructor;
import moodbuddy.moodbuddy.domain.calendar.dto.CalendarResponseDTO;
import moodbuddy.moodbuddy.domain.profile.repository.ProfileRepository;
import moodbuddy.moodbuddy.domain.profileImage.repository.ProfileImageRepository;
import moodbuddy.moodbuddy.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CalendarServiceImpl implements CalendarService{

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final ProfileImageRepository profileImageRepository;

//    private String userKakaoId;

    // 사용자의 세션에 kakaoId를 저장하고, 이를 필요할 때마다 가져와서 사용하는 방법이 있습니다. Spring Security와 같은 프레임워크를 사용하면 세션 관리가 더 쉬워집니다.
    // 이 방식에 대해서 얘기해보기

    /**
     * 메인 화면 이동
     * @param kakaoId
     * @return CalendarResponseDTO
     */
    @Override
    public CalendarResponseDTO mainPage(String kakaoId){
//        this.userKakaoId = kakaoId;
        // kakaoId를 통해 userRepository에서 유저 조회 (Optional 사용)
        // -> member_id, birthday, cur_diary_nums 가져와서 DTO에 넣기

        // 조회한 유저의 member_id를 통해 profileRepository에서 유저 프로필 조회 (Optional 사용)
        // -> profile_id, nickname, profile_comment 가져와서 DTO에 넣기

        // profile_id를 통해 profileImageRepository에서 유저 프로필 이미지 조회 (Optional 사용)
        // -> image_url 가져와서 DTO에 넣기

        // 조회한 유저의 member_id를 통해 diary에서 일기 리스트 조회 (List<Diary> 사용)
        // 이 때, 현재 달을 계산하여 현재 달의 일기 리스트를 전부 응답해줘야 함! (캘린더의 기본 처음 화면은 현재 달의 데이터가 필요)
        // 이 때, for-each 문을 통해 일기 리스트의 일기 하나하나의 emotion을 가져와서, 횟수 세어주기 필요!
        // (더 나은 알고리즘이 있을까? 고민해보자)
        // Map을 이용하여 emotion 당 횟수를 계산하고(ex. {1:1, 2:0, 3:0, ... }), 그 중 값이 max인 키와 그 값을 구하면, 그 것이 가장 많이 나온 emotion의 int 값과 그 횟수이다!
        // -> 가장 많이 나온 emotion의 int 값과 그 횟수를 DTO에 넣기
        return null;
    }

    /**
     * 캘린더 달 이동 (캘린더의 < , > 버튼)
     * @param month
     * @return CalendarResponseDTO
     */
    @Override
    public CalendarResponseDTO monthlyCalendar(String month){
        // kakaoId를 통해 userRepository에서 유저 조회 (Optional 사용)
        // -> member_id 가져오기

        // member_id에 맞는 List<Diary> 중에서, month에서 DateTimeFormatter를 이용해서 년, 월을 얻어오고, SQL의 LIKE 연산자를 이용해서 년 월에 맞는 List<Diary>를 얻어온다
        // (여기서 member_id에 맞는 리스트 전체 조회를 하지 말고, member_id와 년 월에 맞는 리스트만 조회하자)
        // -> 그 Diary 리스트를 그대로 DTO에 넣어서 반환해주면 될 것 같다.
        return null;
    }

    /**
     * 일기 한 줄 요약
     * @param day
     * @return CalendarResponseDTO
     */
    @Override
    public CalendarResponseDTO diarySummary(String day){
        // kakaoId를 통해 userRepository에서 유저 조회 (Optional 사용)
        // -> member_id 가져오기

        // member_id에 맞는 List<Diary> 에서 for-each 문을 사용하여 리스트 각각의 diary의 title을 DTO에 넣고,
        // content를 가져와서 문서 요약 api 사용하여 요약된 내용 응답도 DTO에 넣는다
        // -> Diary의 title, summary를 넣은 DTO 반환
        return null;
    }
}
