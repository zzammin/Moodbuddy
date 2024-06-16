package moodbuddy.moodbuddy.domain.calender.repository;

import moodbuddy.moodbuddy.domain.calender.entity.Calender;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalenderRepository extends JpaRepository<Calender, Long> {
}
