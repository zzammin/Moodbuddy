package moodbuddy.moodbuddy.domain.profileImage.repository;

import moodbuddy.moodbuddy.domain.profileImage.entity.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {

    @Query("select pi from ProfileImage pi where pi.profile.id = :id")
    Optional<ProfileImage> findByProfileId(@Param("id") Long id);
}
