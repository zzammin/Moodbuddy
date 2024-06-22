package moodbuddy.moodbuddy.domain.letter.service;

import lombok.RequiredArgsConstructor;
import moodbuddy.moodbuddy.domain.letter.dto.LetterRequestDTO;
import moodbuddy.moodbuddy.domain.letter.dto.LetterResponseDTO;
import moodbuddy.moodbuddy.domain.letter.repository.LetterRepository;
import moodbuddy.moodbuddy.domain.profile.repository.ProfileRepository;
import moodbuddy.moodbuddy.domain.profileImage.repository.ProfileImageRepository;
import moodbuddy.moodbuddy.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LetterServiceImpl implements LetterService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final ProfileImageRepository profileImageRepository;
    private final LetterRepository letterRepository;

    // try-catch 문 쓰자!

    /**
     * 고민상담소 첫 페이지
     * @param userId
     * @return
     */
    @Override
    public LetterResponseDTO letterPage(Long userId){
        // userId를 통해 userRepository에서 유저 조회 (Optional 사용)
        // -> user_id, birthday, letter_nums 가져와서 DTO에 넣기

        // user_id를 통해 profileRepository에서 유저 프로필 조회 (Optional 사용)
        // -> profile_id, nickname, profile_comment 가져와서 DTO에 넣기

        // 조회한 유저 프로필의 profile_id를 통해 profileImageRepository에서 유저 프로필 이미지 조회 (Optional 사용)
        // -> image_url 가져와서 DTO에 넣기

        // user_id를 통해 letterRepository에서 편지 리스트 조회 (List<Letter>)
        // for-each 문을 통해 Letter 각각의 createdTime을 가져오고, Map을 이용해서 answer_content 가 있으면 그 createdTime : 1, 없으면 createdTime : 0으로 설정
        // 그렇게 만든 Map을 List<Map<LocalDateTime, Integer>> 이 형태로 createdTime : 답장 도착 유무 를 리스트에 넣기
        // -> 이 리스트도 DTO에 넣기 (편지 작성 날짜와 답장 도착 유무)
        return null;
    }

    /**
     * 고민 편지 작성
     * @param userId
     * @param letterRequestDTO
     * @return
     */
    // writeLetter가 호출되면, 12시간 타이머를 설정해둬야 하는데, 이걸 어떻게 할 수 있을까?
    // 이후에 12시간 타이머가 끝나면, 카카오톡 알림톡 전송 (쏘다 API 이용)
    @Override
    public LetterResponseDTO writeLetter(Long userId, LetterRequestDTO letterRequestDTO){
        // @Builder 애노테이션을 통한 빌더 형식으로 Letter 저장하기!
        // letter_id : Letter의 user_id 컬럼 값으로 userId가 없다면 letter_id를 1로 설정하고, userId가 있다면 userId를 가진 Letter row의 letter_id의 max 값 + 1로 설정
        // user_id : userId를 Letter의 user_id로 저장
        // format, worry_content : letterRequestDTO에서 worry_content와 format 가져와서 저장
        return null;
    }

    /**
     * 고민 편지 내용
     * @param userId
     * @param letterId
     * @return
     */
    @Override
    public LetterResponseDTO details(Long userId, Long letterId){
        // letterId와 userId가 동시에 매핑되는 Letter를 letterRepository에서 조회 (Optional 사용)
        // -> 이 Letter의 worry_content, answer_content를 DTO에 넣기
        return null;
    }
}
