package moodbuddy.moodbuddy.domain.profileImage.repository;

import moodbuddy.moodbuddy.domain.profileImage.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
