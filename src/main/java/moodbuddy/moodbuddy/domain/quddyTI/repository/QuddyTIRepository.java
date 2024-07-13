package moodbuddy.moodbuddy.domain.quddyTI.repository;

import moodbuddy.moodbuddy.domain.quddyTI.entity.QuddyTI;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuddyTIRepository extends JpaRepository<QuddyTI, Long>, QuddyTIRepositoryCustom {
}
