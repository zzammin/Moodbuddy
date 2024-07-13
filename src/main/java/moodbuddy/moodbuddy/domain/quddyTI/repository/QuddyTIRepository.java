package moodbuddy.moodbuddy.domain.quddyTI.repository;

import moodbuddy.moodbuddy.domain.quddyTI.entity.QuddyTI;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuddyTIRepository extends JpaRepository<QuddyTI, Long>, QuddyTIRepositoryCustom {
    Optional<QuddyTI> findByKakaoId(Long kakaoId);
}
