package moodbuddy.moodbuddy.domain.profile.repository;

import moodbuddy.moodbuddy.domain.profile.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
