package moodbuddy.moodbuddy.domain.letter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moodbuddy.moodbuddy.domain.letter.dto.request.LetterReqDTO;
import moodbuddy.moodbuddy.domain.letter.dto.response.LetterResDetailsDTO;
import moodbuddy.moodbuddy.domain.letter.dto.response.LetterResPageAnswerDTO;
import moodbuddy.moodbuddy.domain.letter.dto.response.LetterResPageDTO;
import moodbuddy.moodbuddy.domain.letter.dto.response.LetterResSaveDTO;
import moodbuddy.moodbuddy.domain.letter.entity.Letter;
import moodbuddy.moodbuddy.domain.letter.mapper.LetterMapper;
import moodbuddy.moodbuddy.domain.letter.repository.LetterRepository;
import moodbuddy.moodbuddy.domain.profile.entity.Profile;
import moodbuddy.moodbuddy.domain.profile.repository.ProfileRepository;
import moodbuddy.moodbuddy.domain.profileImage.entity.ProfileImage;
import moodbuddy.moodbuddy.domain.profileImage.repository.ProfileImageRepository;
import moodbuddy.moodbuddy.domain.user.entity.User;
import moodbuddy.moodbuddy.domain.user.repository.UserRepository;
import moodbuddy.moodbuddy.global.common.util.JwtUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class LetterServiceImpl implements LetterService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final ProfileImageRepository profileImageRepository;
    private final LetterRepository letterRepository;

    @Override
    @Transactional
    public LetterResPageDTO letterPage(){
        log.info("[LetterService] letterPage");
        try{
            // userId를 통해 userRepository에서 유저 조회 (Optional 사용)
            Long userId = JwtUtil.getMemberId();
            Optional<User> optionalUser = userRepository.findById(userId);
            // user_id를 통해 profileRepository에서 유저 프로필 조회 (Optional 사용)
            Optional<Profile> optionalProfile = profileRepository.findByUserId(userId);
            if(optionalUser.isPresent() && optionalProfile.isPresent()){
                // 조회한 유저 프로필의 profile_id를 통해 profileImageRepository에서 유저 프로필 이미지 조회 (Optional 사용)
                Optional<ProfileImage> optionalProfileImage = profileImageRepository.findByProfileId(optionalProfile.get().getId());
                String profileImageURL = optionalProfileImage.map(ProfileImage::getProfileImgURL).orElse("");

                // user_id를 통해 letterRepository에서 편지 리스트 조회 (List<Letter>)
                List<Letter> letters = letterRepository.findByUserId(userId);

                // 편지 리스트에서 답장이 있으면 answerCheck 를 1로, 없으면 0으로 설정하여 구성한 리스트
                List<LetterResPageAnswerDTO> letterResPageAnswerDTOList = letters.stream()
                        .map(letter -> LetterResPageAnswerDTO.builder()
                                        .letterCreatedTime(letter.getCreatedTime())
                                        .answerCheck(letter.getLetterAnswerContent()!=null?1:0)
                                        .build())
                        .collect(Collectors.toList());

                return LetterResPageDTO.builder()
                        .profileNickname(optionalProfile.get().getProfileNickName())
                        .userBirth(optionalUser.get().getUserBirth())
                        .profileComment(optionalProfile.get().getProfileComment())
                        .profileImageUrl(profileImageURL)
                        .userLetterNums(optionalUser.get().getUserLetterNums())
                        .letterResPageAnswerDTOList(letterResPageAnswerDTOList)
                        .build();
            }
            throw new NoSuchElementException("유저 또는 프로필을 찾을 수 없습니다. userId: " + userId);
        } catch (Exception e){
            log.error("[LetterService] letterPage", e);
            throw new RuntimeException("[LetterService] letterPage error", e);
        }
    }

    // writeLetter가 호출되면, 12시간 타이머를 설정해둬야 하는데, 이걸 어떻게 할 수 있을까?
    // 이후에 12시간 타이머가 끝나면, 카카오톡 알림톡 전송 (쏘다 API 이용)
    @Override
    @Transactional
    public LetterResSaveDTO save(LetterReqDTO letterReqDTO){
        log.info("[LetterService] writeLetter");
        try{
            Long userId = JwtUtil.getMemberId();
            Optional<User> optionalUser = userRepository.findById(userId);
            // user_id : userId를 Letter의 user_id로 저장
            // format, worry_content : letterRequestDTO에서 worry_content와 format 가져와서 저장
            if(optionalUser.isPresent()){
                Letter letter = LetterMapper.toLetterEntity(letterReqDTO, optionalUser.get());
                letter = letterRepository.save(letter);
                return LetterMapper.toLetterSaveDTO(letter);
            } else {
                throw new NoSuchElementException("userId를 가지는 사용자가 없습니다. userId : "+userId);
            }
        } catch (Exception e){
            log.error("[LetterService] writeLetter", e);
            throw new RuntimeException("[LetterService] writeLetter error", e);
        }
    }

    @Override
    @Transactional
    public LetterResDetailsDTO details(Long letterId){
        log.info("[LetterService] details");
        try{
            Long userId = JwtUtil.getMemberId();
            // letterId와 userId가 동시에 매핑되는 Letter를 letterRepository에서 조회 (Optional 사용)
            // -> 이 Letter의 worry_content, answer_content를 DTO에 매핑
            Optional<Letter> optionalLetter = letterRepository.findByIdAndUserId(letterId, userId);
            return optionalLetter.map(LetterMapper::toLetterDetailsDTO)
                    .orElseThrow(() -> new NoSuchElementException("letterId에 매핑되는 편지가 없습니다."));
        } catch (Exception e){
            log.error("[LetterService] details", e);
            throw new RuntimeException("[LetterService] details error", e);
        }
    }
}
