package moodbuddy.moodbuddy.domain.bookMark.service;

import moodbuddy.moodbuddy.domain.bookMark.dto.response.BookMarkResToggleDTO;
import moodbuddy.moodbuddy.domain.diary.dto.response.DiaryResDetailDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookMarkService {
    BookMarkResToggleDTO toggle(Long bookId);
    Page<DiaryResDetailDTO> bookMarkFindAllByWithPageable(Pageable pageable);
    void deleteByDiaryId(Long diaryId);
}
