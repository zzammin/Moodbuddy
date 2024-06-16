package moodbuddy.moodbuddy.domain.monthlyStatictics.repository;

import moodbuddy.moodbuddy.domain.monthlyStatictics.entity.MonthlyStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonthlyStatisticsRepository extends JpaRepository<MonthlyStatistics, Long> {
}
