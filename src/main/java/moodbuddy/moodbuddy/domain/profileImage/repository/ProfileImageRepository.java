package moodbuddy.moodbuddy.domain.profileImage.repository;

import moodbuddy.moodbuddy.domain.profileImage.entity.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {
}
