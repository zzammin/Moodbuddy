package moodbuddy.moodbuddy.domain.bookMark.service;

import moodbuddy.moodbuddy.domain.bookMark.dto.response.BookMarkResToggleDTO;

public interface BookMarkService {
    BookMarkResToggleDTO toggle(Long bookId);
}
