package moodbuddy.moodbuddy.domain.bookMark.repository;

import moodbuddy.moodbuddy.domain.diary.dto.response.DiaryResDetailDTO;
import moodbuddy.moodbuddy.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookMarkRepositoryCustom {
    Page<DiaryResDetailDTO> bookMarkFindAllWithPageable(User user, Pageable pageable);
}
