package moodbuddy.moodbuddy.domain.diary.service;

import lombok.RequiredArgsConstructor;
import moodbuddy.moodbuddy.domain.diary.dto.DiaryRequestDTO;
import moodbuddy.moodbuddy.domain.diary.dto.DiaryResponseDTO;
import moodbuddy.moodbuddy.domain.diary.repository.DiaryRepository;
import moodbuddy.moodbuddy.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService{

    private final UserRepository userRepository;
    private final DiaryRepository diaryRepository;

    /**
     * 캘린더 달 이동 (캘린더의 < , > 버튼)
     * @param userId
     * @param calendarMonthDTO
     * @return
     */
    @Override
    public DiaryResponseDTO monthlyCalendar(Long userId, DiaryRequestDTO.CalendarMonthDTO calendarMonthDTO){
        // userId를 통해 userRepository에서 유저 조회 (Optional 사용)
        // -> member_id 가져오기

        // calendarMonthDTO에서 month 가져오기
        // user_id에 맞는 List<Diary> 중에서, month에서 DateTimeFormatter의 ofPattern을 이용한 LocalDateTime 파싱을 통해 년, 월을 얻어오고,
        // SQL의 LIKE 연산자를 이용해서 그 년, 월에 맞는 List<Diary>를 얻어온다
        // (여기서 member_id에 맞는 리스트 전체 조회를 하지 말고, member_id와 년 월에 맞는 리스트만 조회하자)
        // -> 그 Diary 리스트를 그대로 DTO에 넣어서 반환해주면 될 것 같다.
        return null;
    }

    /**
     * 일기 한 줄 요약 보여주기
     * @param userId
     * @param calendarSummaryDTO
     * @return
     */
    // @Builder 애노테이션을 통한 빌더 형식 사용하기
    // "일기 작성할 때" , 그 일기 내용을 Diary 테이블의 content 컬럼에 저장하고, 문서 요약 API에 보내서 요약된 내용을 summary 컬럼에 저장한다.
    @Override
    public DiaryResponseDTO summary(Long userId, DiaryRequestDTO.CalendarSummaryDTO calendarSummaryDTO){
        // userId를 통해 userRepository에서 유저 조회 (Optional 사용)
        // -> member_id 가져오기

        // user_id에 맞는 List<Diary> 에서 calendarSummaryDTO에서 가져온 day와 일치하는 Diary를 가져온다.
        // -> 가져온 Diary의 title, summary를 넣은 DTO 반환
        return null;
    }
}
