package moodbuddy.moodbuddy.domain.bookMark.repository;

import moodbuddy.moodbuddy.domain.bookMark.entity.BookMark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookMarkRepository extends JpaRepository<BookMark, Long> {
}
